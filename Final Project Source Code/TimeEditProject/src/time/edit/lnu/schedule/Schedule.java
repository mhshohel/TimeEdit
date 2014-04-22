/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.schedule
 *
 * @FileName Schedule.java
 * 
 * @FileCreated Oct 16, 2011
 *
 * @Author MD. SHOHEL SHAMIM
 *
 * @CivicRegistration 19841201-R119
 *
 * MSc. in Software Technology
 *
 * Linnaeus University, Växjö, Sweden
 *
 */
package time.edit.lnu.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import time.edit.lnu.datatype.AlarmOption;
import time.edit.lnu.datatype.CourseOrTeacher;
import time.edit.lnu.datatype.Event;
import time.edit.lnu.datatype.MyAlarmList;
import time.edit.lnu.datatype.MyEvents;
import time.edit.lnu.datatype.MyNotificationList;
import time.edit.lnu.helper.DataHelper;
import time.edit.lnu.helper.ICalDownloader;
import time.edit.lnu.services.AlarmMessageReceiver;
import time.edit.lnu.services.LnuNotification;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * TODO
 * 
 */
public class Schedule extends Activity {
    private Bundle extras;
    private ListView listView;
    private SeparatedListAdapter adapter;
    private DataHelper databaseHelper;
    private Event[] events;
    private ProgressDialog progressDialog;
    private DownloadThread progressThread;
    private int id = 0;
    private Resources resource;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private SimpleDateFormat formatter = new SimpleDateFormat(
	    "EEEE, d MMMM yyyy");
    private Calendar cal = GregorianCalendar.getInstance();
    private Date todayDate = cal.getTime();
    private String today = sdf.format(todayDate).substring(0, 8);
    private String tomorrow;

    private ArrayList<MyEvents> updatedListEvents = new ArrayList<MyEvents>();
    private ArrayList<MyNotificationList> myNotifications;

    private final int ACTIVATE_ALARM = 1;
    private final int DEACTIVATE_ALARM = 2;
    private final int SHOW_PREVIOUS_CLASSES = 3;

    private int alarmTime = 0;
    public static int LNU_NOTIFICATION_ID = 841201119;

    private boolean isPrevious = false;

    private SharedPreferences prefs;
    private boolean isOfflineMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.schedule_main);

	listView = (ListView) findViewById(R.id.schedule_list);
	initComponents();
	update();

	listView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		if (adapter.getPosition(position) != -1) {
		    Intent intent = new Intent(Schedule.this,
			    DetailsInformation.class);

		    String summary = events[adapter.getPosition(position)]
			    .getSummary();
		    String[] summarySplit = null;
		    summarySplit = summary.split("\n");

		    String courseId = "n/a";
		    String courseEvent = "n/a";

		    if (summarySplit.length > 2) {
			courseId = summarySplit[0];
			courseEvent = summarySplit[1];
		    }

		    long eventPK = events[adapter.getPosition(position)]
			    .getPrimaryKey();
		    int courseTeacherid = events[adapter.getPosition(position)]
			    .getCourseTeacherId();
		    String start = events[adapter.getPosition(position)]
			    .getStart();
		    String stop = events[adapter.getPosition(position)]
			    .getStop();
		    String room = events[adapter.getPosition(position)]
			    .getRoom();
		    String personnel = events[adapter.getPosition(position)]
			    .getPersonnel();
		    String moment = events[adapter.getPosition(position)]
			    .getMoment();
		    moment = (!moment.trim().equalsIgnoreCase("")) ? moment
			    : "n/a";
		    String remark = events[adapter.getPosition(position)]
			    .getRemark();
		    remark = (!remark.trim().equalsIgnoreCase("")) ? remark
			    : "n/a";

		    intent.putExtra("id", courseTeacherid);
		    intent.putExtra("courseId", courseId);
		    intent.putExtra("courseEvent", courseEvent);
		    intent.putExtra("start", start);
		    intent.putExtra("stop", stop);
		    intent.putExtra("room", room);
		    intent.putExtra("personnel", personnel);
		    intent.putExtra("moment", moment);
		    intent.putExtra("remark", remark);
		    intent.putExtra("eventPK", eventPK);

		    startActivity(intent);
		}
	    }
	});
    }

    @Override
    public void onResume() {
	super.onResume();
    }

    @Override
    public void onStop() {
	super.onStop();
    }

    /**
     * Initialize basic components
     */
    private void initComponents() {
	getPreference();
	extras = getIntent().getExtras();
	id = extras.getInt("id");
	resource = getResources();

	if (listView == null) {
	    listView = (ListView) findViewById(R.id.schedule_list);
	}
	registerForContextMenu(listView);
    }

    /**
     * Get Preference
     */
    private void getPreference() {
	prefs = PreferenceManager.getDefaultSharedPreferences(Schedule.this);

	isOfflineMode = Boolean.parseBoolean(Boolean.toString(prefs.getBoolean(
		"LnuOfflinePref", false)));
    }

    /**
     * Download new data for each course or teacher, updates database
     */
    public void update() {
	if (listView == null) {
	    listView = (ListView) findViewById(R.id.schedule_list);
	}
	databaseHelper = new DataHelper(Schedule.this);

	ArrayList<Integer> coursesTeachersIds = new ArrayList<Integer>();

	if (id == -1 || id == -2) {
	    coursesTeachersIds = databaseHelper.getAllCourseOrTeacherIds();
	} else {
	    coursesTeachersIds.add(id);
	}

	databaseHelper.close();

	progressDialog = ProgressDialog.show(Schedule.this, null,
		getResources().getString(R.string.update_message), true, false);
	progressThread = new DownloadThread(handler, coursesTeachersIds);
	progressThread.start();
    }

    /**
     * 
     * DownloadThread used to separately download data from Internet
     * 
     */
    private class DownloadThread extends Thread {
	Handler handler;
	ArrayList<Integer> allCourseTeacherIds;

	public DownloadThread(Handler handler,
		ArrayList<Integer> courseTeacherIds) {
	    this.handler = handler;
	    this.allCourseTeacherIds = courseTeacherIds;
	}

	public synchronized void run() {
	    databaseHelper = new DataHelper(Schedule.this);

	    int language = 2; // English version, 1 for Swedish version
	    updatedListEvents.clear();

	    try {
		if (isPrevious == false) {
		    if (isOfflineMode == false) {
			for (int courseTeacherId : allCourseTeacherIds) {

			    Event[] events;

			    events = ICalDownloader.downloadICal(
				    courseTeacherId, language);

			    Log.v("TAG", "iCal downloaded");

			    updatedListEvents.add(new MyEvents(events,
				    courseTeacherId));
			}
		    } else {
			Thread.sleep(500);
		    }
		}
		databaseHelper.close();
		handler.sendMessage(handler.obtainMessage(1));
	    } catch (Exception e) {
		Log.v("TAG", "Exception");
		handler.sendMessage(handler.obtainMessage(-1));
		e.printStackTrace();
	    }
	}
    }

    /**
     * Handler to handle Threads
     */
    private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    if (msg.what == 1) {
		if (isPrevious == false) {
		    getData(true);
		} else {
		    getData(false);
		}
		progressThread = null;
		progressDialog.dismiss();
	    } else if (msg.what == -1) {
		getData(false);
		progressThread = null;
		progressDialog.dismiss();
		String message = resource
			.getString(R.string.update_failed_message);
		String title = resource
			.getString(R.string.update_failed_message_title);
		showAlert(title, message, false);
	    }

	}
    };

    /**
     * Return Formated Message for Schedule
     * 
     * @param CourseOrTeacher
     *            courseTeacher
     * @param Event
     *            event
     * @return <b>String</b> getFormatedMessage
     */
    private String getFormatedMessage(CourseOrTeacher courseTeacher, Event event) {
	StringBuilder message = new StringBuilder();
	message.append(getDateString(event.getStart())).append("\n");
	message.append(courseTeacher.getCourseTeacherSignature()).append(" - ")
		.append(event.getMoment()).append("\n");
	message.append(event.getCaption()).append("\n");
	message.append(event.getPersonnel()).append("\n");

	return message.toString();
    }

    /**
     * Populated list with data from database
     * 
     * @param Boolean
     *            isDownloaded
     */
    private void getData(boolean isDownloaded) {
	cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH, 1);
	Date tomorrowDate = cal.getTime();

	tomorrow = sdf.format(tomorrowDate).substring(0, 8);

	databaseHelper = new DataHelper(Schedule.this);

	String startDay = sdf.format(todayDate).substring(0, 8) + "000000";
	String endDay = tomorrow + "000000";

	if (isDownloaded == true && isOfflineMode == false) {
	    myNotifications = databaseHelper.insertEvents(updatedListEvents,
		    startDay);

	    StringBuilder updatedData = new StringBuilder();// updated
	    StringBuilder oldestData = new StringBuilder();// Oldest

	    for (MyNotificationList notification : myNotifications) {
		ArrayList<Event> oldest = notification.getOldest();
		ArrayList<Event> updated = notification.getUpdated();

		if (oldest.size() != 0) {
		    for (Event event : oldest) {
			String message = getFormatedMessage(
				notification.getCourseOrTeacher(), event);
			oldestData.append(message).append("\n");
		    }
		}

		if (updated.size() != 0) {
		    for (Event event : updated) {
			String message = getFormatedMessage(
				notification.getCourseOrTeacher(), event);
			updatedData.append(message).append("\n");
		    }
		}

		String oldData = oldestData.toString().trim();
		String newData = updatedData.toString().trim();

		if (oldData != "" || newData != "") {
		    notifyUser(oldData, newData);
		}
	    }
	}

	ArrayList<CourseOrTeacher> coursesTeachers = new ArrayList<CourseOrTeacher>();

	if (id == -1 || id == -2) {
	    coursesTeachers = databaseHelper.getAllCoursesOrTeachers();
	    if (id == -1) {
		events = databaseHelper.getAllTodaysEvents(startDay, endDay);
	    } else if (id == -2) {
		if (isPrevious == true) {
		    isPrevious = false;
		    events = (Event[]) databaseHelper.getAllEventsArray();
		} else {
		    events = databaseHelper.getAllEvents(startDay);
		}
	    }
	} else {
	    coursesTeachers = databaseHelper.getAllCoursesOrTeachers(id);
	    if (isPrevious == true) {
		isPrevious = false;
		events = databaseHelper.getAllEventsArray(id);
	    } else {
		events = databaseHelper.getAllEvents(startDay, id);
	    }
	}

	databaseHelper.close();

	if (events.length == 0) {
	    String title = "";
	    if (isOfflineMode == true) {
		title = resource.getString(R.string.schedule_no_class_title1);
	    } else {
		title = resource.getString(R.string.schedule_no_class_title);
	    }

	    String message = resource
		    .getString(R.string.schedule_no_class_message);

	    showAlert(title, message, true);
	} else {
	    databaseHelper.close();

	    ArrayList<String> flags = new ArrayList<String>();
	    ArrayList<Object> objects = new ArrayList<Object>();

	    String date = "";

	    for (Event e : events) {
		if (!e.getStart().substring(0, 8).equals(date)) {
		    flags.add("HEADER");
		    objects.add(getDateString(e.getStart()));
		    date = e.getStart().substring(0, 8);
		}
		flags.add("EVENT");
		for (CourseOrTeacher c : coursesTeachers) {
		    if (e.getCourseTeacherId() == c.getCourseTeacherId()) {
			objects.add(new Object[] {
				c.getCourseTeacherSignature() + " "
					+ e.getMoment(),
				c.getCourseTeacherColor(), e.getCaption(),
				e.getAlarmOption(), e.getPrimaryKey(),
				e.getStart() });
		    }
		}
	    }

	    adapter = new SeparatedListAdapter(getApplicationContext(), flags,
		    objects);

	    if (listView == null) {
		listView = (ListView) findViewById(R.id.schedule_list);
	    }
	    listView.setAdapter(adapter);
	}
    }

    /**
     * Return Formated Date for Schedule
     * 
     * @param String
     *            start
     * @return <b>String</b> getDateString
     */
    private String getDateString(String start) {
	if (start.substring(0, 8).equals(today)) {
	    return getResources().getString(R.string.today);
	} else if (start.substring(0, 8).equals(tomorrow)) {
	    return getResources().getString(R.string.tomorrow);
	} else {
	    String dateString = "";
	    try {
		dateString = formatter.format(sdf.parse(start));
		dateString = String.valueOf(
			Character.toUpperCase(dateString.charAt(0))).concat(
			dateString.substring(1, dateString.length()));
	    } catch (Exception e) {
	    }
	    return dateString;
	}
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
	menu.setHeaderTitle(resource
		.getString(R.string.schedule_contetext_menu_title));
	menu.setHeaderIcon(R.drawable.options);
	menu.add(Menu.NONE, ACTIVATE_ALARM, Menu.NONE,
		resource.getString(R.string.schedule_active_alarm));
	menu.add(Menu.NONE, DEACTIVATE_ALARM, Menu.NONE,
		resource.getString(R.string.schedule_deactive_alarm));
	menu.add(Menu.NONE, SHOW_PREVIOUS_CLASSES, Menu.NONE,
		resource.getString(R.string.schedule_show_previous_class));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
		.getMenuInfo();

	Event adapterEvent = null;
	int adpterPosVal = adapter.getPosition(info.position);
	String text = resource
		.getString(R.string.schedul_toast_for_header_context_menu);
	if (adpterPosVal != -1) {
	    adapterEvent = events[adapter.getPosition(info.position)];
	}

	switch (item.getItemId()) {
	case ACTIVATE_ALARM:
	    if (adapterEvent != null) {
		setAlarmTime(adapterEvent);
	    } else {
		contextMenuFailedMessage(getApplicationContext(), text);
	    }
	    return true;
	case DEACTIVATE_ALARM:
	    if (adapterEvent != null) {
		deActivateAlarm(adapterEvent);
	    } else {
		contextMenuFailedMessage(getApplicationContext(), text);
	    }
	    return true;
	case SHOW_PREVIOUS_CLASSES:
	    if (id != -1) {
		isPrevious = true;
		update();
	    } else {
		showAlert(
			resource.getString(R.string.schedul_previous_class_cannot_show_title),
			resource.getString(R.string.schedul_previous_class_cannot_show_message),
			false);
	    }
	    return true;
	default:
	    return super.onContextItemSelected(item);
	}
    }

    /**
     * Process of Alarm Activation
     * 
     * @param Event
     *            event
     * @param Int
     *            alarmTime
     */
    private void activateAlarm(Event event, int alarmTime) {
	databaseHelper = new DataHelper(Schedule.this);
	long eventPK = event.getPrimaryKey();
	ArrayList<MyAlarmList> alarmList = databaseHelper
		.getAlarmByEventPK(eventPK);

	Calendar cal = Calendar.getInstance();
	long currentTime = 0;
	long setAlarmTime = 0;

	String lectureTime = event.getStart();
	long timeStamp = getTimeStamp(lectureTime);

	cal.clear();
	cal.setTimeInMillis(timeStamp);
	cal.add(Calendar.MINUTE, -alarmTime);

	currentTime = System.currentTimeMillis();
	setAlarmTime = cal.getTimeInMillis();

	int size = alarmList.size();

	if (size != 0) {
	    if (currentTime < setAlarmTime) {
		int courseTeacherId = event.getCourseTeacherId();
		alarmDeActive(eventPK);
		databaseHelper.deleteAlarmByEvent(courseTeacherId, eventPK);

		updateDatabseForAlarm(eventPK, event, setAlarmTime);

		databaseHelper.insertAlarm(event.getCourseTeacherId(), eventPK,
			setAlarmTime, AlarmOption.ENABLED);

		databaseHelper.close();
		update();
	    } else {
		databaseHelper.close();
		showAlert(
			resource.getString(R.string.schedule_alarm_set_failed_title),
			resource.getString(R.string.schedule_alarm_set_failed_message),
			false);
	    }

	    if (event.getAlarmOption().toString()
		    .equalsIgnoreCase(AlarmOption.DISABLED.toString())) {
		update();
	    }
	} else {
	    if (currentTime < setAlarmTime) {
		updateDatabseForAlarm(eventPK, event, setAlarmTime);

		databaseHelper.insertAlarm(event.getCourseTeacherId(), eventPK,
			setAlarmTime, AlarmOption.ENABLED);

		databaseHelper.close();
		update();
	    } else {
		databaseHelper.close();
		showAlert(
			resource.getString(R.string.schedule_alarm_set_failed_title),
			resource.getString(R.string.schedule_alarm_set_failed_message),
			false);
	    }
	}
    }

    /**
     * Update Alarm Time in Database
     * 
     * @param Long
     *            eventPK
     * @param Event
     *            event
     * @param Long
     *            setAlarmTime
     */
    private void updateDatabseForAlarm(long eventPK, Event event,
	    long setAlarmTime) {
	databaseHelper.updateEventAlarmOption(eventPK, AlarmOption.ENABLED);

	databaseHelper.updateCourseOrTeacher(event.getCourseTeacherId(),
		AlarmOption.CUSTOMIZED);

	alarmActive(eventPK, event.getCourseTeacherId(), setAlarmTime);
    }

    /**
     * Active Alarm
     * 
     * @param Event
     *            eventPK
     * @param Int
     *            courseTeacherId
     * @param Long
     *            alarmTimestamp
     */
    private void alarmActive(long eventPK, int courseTeacherId,
	    long alarmTimestamp) {
	int id = (int) eventPK;
	Intent intent = new Intent(Schedule.this, AlarmMessageReceiver.class);
	intent.putExtra("eventPK", eventPK);
	intent.putExtra("courseTeacherId", courseTeacherId);
	PendingIntent sender = PendingIntent.getBroadcast(Schedule.this, id,
		intent, 0);

	long startTime = alarmTimestamp;
	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
	am.set(AlarmManager.RTC_WAKEUP, startTime, sender);
    }

    /**
     * Process of Alarm Deactivation
     * 
     * @param Event
     *            event
     */
    private void deActivateAlarm(Event event) {
	databaseHelper = new DataHelper(Schedule.this);
	long eventPK = event.getPrimaryKey();
	ArrayList<MyAlarmList> alarmList = databaseHelper
		.getAlarmByEventPK(eventPK);

	int size = alarmList.size();

	if (size != 0) {
	    if (event.getAlarmOption().toString()
		    .equalsIgnoreCase(AlarmOption.DISABLED.toString())) {
		databaseHelper.close();
		showAlert(
			resource.getString(R.string.schedule_alarm_deactivate_failed_title),
			resource.getString(R.string.schedule_alarm_deactivate_failed_message),
			false);
	    } else {
		int courseTeacherId = event.getCourseTeacherId();
		alarmDeActive(eventPK);
		databaseHelper.deleteAlarmByEvent(courseTeacherId, eventPK);
		databaseHelper.updateCourseOrTeacher(courseTeacherId,
			AlarmOption.CUSTOMIZED);
		databaseHelper.updateEventAlarmOption(eventPK,
			AlarmOption.DISABLED);
		databaseHelper.close();
		update();
	    }
	} else {
	    databaseHelper.close();
	    showAlert(
		    resource.getString(R.string.schedule_alarm_deactivate_failed_title),
		    resource.getString(R.string.schedule_alarm_deactivate_failed_message),
		    false);
	}
    }

    /**
     * Deactivate Alarm
     * 
     * @param Long
     *            eventPK
     */
    private void alarmDeActive(long eventPK) {
	int id = (int) eventPK;
	Intent intent = new Intent(Schedule.this, AlarmMessageReceiver.class);
	PendingIntent sender = PendingIntent.getBroadcast(Schedule.this, id,
		intent, 0);

	sender.cancel();
	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
	am.cancel(sender);
    }

    /**
     * Return TimeStamp of a Date
     * 
     * @param String
     *            date
     * @return <b>Long</b>
     */
    private long getTimeStamp(String date) {
	long timeStamp = 0;
	try {
	    timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").parse(date)
		    .getTime();
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	return timeStamp;
    }

    /**
     * Alarm Setting Dialog
     * 
     * @param Event
     *            event
     */
    private void setAlarmTime(final Event event) {
	final String[] time = resource
		.getStringArray(R.array.before_lecture_time_text);
	final int[] timeValue = resource
		.getIntArray(R.array.before_lecture_time_value);

	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle(resource
		.getString(R.string.schedule_alarm_dialog_title));
	builder.setIcon(R.drawable.alarm_enabled);
	builder.setItems(time, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int item) {
		alarmTime = timeValue[item];
		dialog.dismiss();
		activateAlarm(event, alarmTime);
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    /**
     * Show Alert Dialog
     * 
     * @param title
     *            String
     * @param message
     *            String
     */
    private void showAlert(String title, String message, boolean isNull) {
	final boolean check = isNull;
	AlertDialog.Builder builder = new AlertDialog.Builder(Schedule.this);
	builder.setIcon(R.drawable.warning);
	builder.setTitle(title);
	builder.setMessage(message)
		.setCancelable(true)
		.setPositiveButton(R.string.dialog_button_ok,
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				if (check) {
				    finish();
				}
			    }
			});
	AlertDialog alert = builder.create();
	alert.show();
    }

    /**
     * Show Toast message if user try to active, deactive alarm by press in the
     * header of ListView
     */
    private void contextMenuFailedMessage(Context context, String text) {
	Toast.makeText(context, text, 5000).show();
    }

    /**
     * Notify user if change in Schedule
     * 
     * @param String
     *            oldData
     * @param String
     *            newData
     */
    private void notifyUser(String oldData, String newData) {
	LnuNotification.setNotificationList(oldData, newData);

	String notificationService = Context.NOTIFICATION_SERVICE;
	NotificationManager notificationManager = (NotificationManager) getSystemService(notificationService);

	int icon = R.drawable.icon_old;
	CharSequence tickerText = resource
		.getString(R.string.schedule_notification_ticker_title);
	long when = System.currentTimeMillis();
	Context context = getApplicationContext();
	CharSequence contentTitle = getResources().getString(R.string.app_name);
	CharSequence contentText = resource
		.getString(R.string.schedule_notification_ticker_message);

	Notification notification = new Notification(icon, tickerText, when);
	notification.defaults |= Notification.DEFAULT_ALL;

	Intent notificationIntent = new Intent(Schedule.this,
		LnuNotification.class);
	PendingIntent contentIntent = PendingIntent.getActivity(Schedule.this,
		0, notificationIntent, 0);
	notification.setLatestEventInfo(context, contentTitle, contentText,
		contentIntent);
	notificationManager.notify(LNU_NOTIFICATION_ID, notification);
    }
}