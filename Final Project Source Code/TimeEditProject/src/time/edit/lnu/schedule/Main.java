/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.schedule
 *
 * @FileName Main.java
 * 
 * @FileCreated Oct 22, 2011
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
import java.util.Collections;
import java.util.Date;

import time.edit.lnu.datatype.AlarmOption;
import time.edit.lnu.datatype.CourseOrTeacher;
import time.edit.lnu.datatype.Event;
import time.edit.lnu.datatype.MyAlarmList;
import time.edit.lnu.datatype.MyList;
import time.edit.lnu.helper.DataHelper;
import time.edit.lnu.services.AlarmMessageReceiver;
import time.edit.lnu.services.AutoUpdateService;
import time.edit.lnu.services.TimeEditPreference;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * Main Class: Project runs from here
 * 
 */
public class Main extends Activity {
    private DataHelper databaseHelper;
    private ArrayList<CourseOrTeacher> courcesOrTeachers;
    private ArrayList<MyList> myList;
    private CoursesOrTeacherListAdapter adapter;
    private ListView listView;
    private Resources resource;
    private SharedPreferences prefs;
    private int autoUpdateTime;
    private Context context;

    private final int ADD_NEW_COURSE_TEACHER = 1;
    private final int CLOSE_APPLICATION = 2;
    private final int SETTINGS = 3;
    private final int ABOUT = 4;

    private final int EDIT = 1;
    private final int DELETE = 2;
    private final int DELETE_ALL = 3;
    private final int ACTIVATE_ALARM = 4;
    private final int DEACTIVATE_ALARM = 5;

    private ProgressDialog progressDialog;
    private DeleteThread deleteThread;
    private ProgressDialog alarmActiveDialog;
    private AlarmActiveThread alarmActiveThread;
    private ProgressDialog alarmDeActiveDialog;
    private AlarmDeActiveThread alarmDeActiveThread;

    private int alarmTime = 0;
    private static boolean isAutoUpdateEnable = false;
    private static int previousTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	listView = (ListView) findViewById(R.id.course_list_view);
	initComponents();

	listView.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		Intent intent = new Intent(Main.this, Schedule.class);
		intent.putExtra("id", myList.get(position).getId());
		intent.putExtra("signature", myList.get(position)
			.getSignature());
		intent.putExtra("name", myList.get(position).getName());
		intent.putExtra("color", myList.get(position).getColor());
		intent.putExtra("type", myList.get(position).getType());
		intent.putExtra("alarm", myList.get(position).getAlarmOption());
		startActivity(intent);
	    }
	});
    }

    @Override
    public void onResume() {
	super.onResume();
	initComponents();
    }

    @Override
    public void onStop() {
	super.onStop();
    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	this.finish();
    }

    /**
     * Get Preference
     */
    private void getPreference() {
	prefs = PreferenceManager.getDefaultSharedPreferences(this);
	autoUpdateTime = Integer.parseInt(prefs.getString(
		"LnuAutoUpdatePreference", "0"));
    }

    /**
     * Initialize basic components
     */
    private void initComponents() {
	context = Main.this;
	getPreference();
	autoUpdate();
	resource = getResources();
	if (listView == null) {
	    listView = (ListView) findViewById(R.id.course_list_view);
	}
	update();
    }

    /**
     * Check whether course is blank or not, if blank redirect to search screen
     */
    private void ifBlankDatabase() {
	databaseHelper = new DataHelper(Main.this);
	if (databaseHelper.countCourseOrTeacher() == 0) {
	    databaseHelper.close();
	    startActivity(new Intent(Main.this, SearchScreen.class));
	}
	databaseHelper.close();
    }

    /**
     * Update List of Course
     */
    public void update() {
	ifBlankDatabase();
	databaseHelper = new DataHelper(Main.this);
	courcesOrTeachers = databaseHelper.getAllCoursesOrTeachers();
	databaseHelper.close();

	myList = new ArrayList<MyList>();
	myList.clear();
	for (int i = 0; i < courcesOrTeachers.size(); i++) {
	    myList.add(new MyList(
		    courcesOrTeachers.get(i).getCourseTeacherId(),
		    courcesOrTeachers.get(i).getCourseTeacherSignature(),
		    courcesOrTeachers.get(i).getCourseTeacherName(),
		    courcesOrTeachers.get(i).getCourseTeacherColor(),
		    courcesOrTeachers.get(i).getType(), courcesOrTeachers
			    .get(i).getAlarmOption()));
	}

	Collections.sort(myList);

	if (myList.size() == 1) {
	    myList.add(new MyList(-1, "-1", resource
		    .getString(R.string.main_menu_list_todays), Color.WHITE,
		    "OTHERS", ""));
	} else if (myList.size() > 1) {
	    myList.add(new MyList(-1, "-1", resource
		    .getString(R.string.main_menu_list_todays), Color.WHITE,
		    "OTHERS", ""));
	    myList.add(new MyList(-2, "-2", resource
		    .getString(R.string.main_menu_list_show_all), Color.WHITE,
		    "OTHERS", ""));
	}

	adapter = new CoursesOrTeacherListAdapter(this, myList);

	listView.setAdapter(adapter);
	registerForContextMenu(listView);
    }

    /**
     * 
     * CoursesOrTeacherListAdapter: Generate List of Courses
     * 
     */
    public class CoursesOrTeacherListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	ArrayList<MyList> myList;

	public CoursesOrTeacherListAdapter(Context context,
		ArrayList<MyList> myList) {
	    this.myList = myList;
	    inflater = (LayoutInflater) context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    ImageView alarmImageView = null;
	    TextView coursesListItemTitle = null;
	    View coursesSideline = null;

	    if (convertView == null) {
		convertView = inflater
			.inflate(R.layout.courses_list_item, null);

		holder = new ViewHolder(convertView);
		convertView.setTag(holder);
	    }
	    holder = (ViewHolder) convertView.getTag();

	    coursesListItemTitle = holder.getTitle();
	    coursesSideline = holder.getSideline();
	    coursesListItemTitle.setText(myList.get(position).getName());
	    coursesSideline.setBackgroundColor(Color.WHITE);

	    String alarmOption = myList.get(position).getAlarmOption();
	    alarmImageView = holder.getImage();

	    // --------------------------- Alarm Icon ----------------------
	    if (alarmOption.equalsIgnoreCase(AlarmOption.ENABLED.toString())) {
		alarmImageView.setImageResource(R.drawable.alarm_active);
	    } else if (alarmOption.equalsIgnoreCase(AlarmOption.DISABLED
		    .toString())) {
		alarmImageView.setImageResource(R.drawable.alarm_custom);
	    } else if (alarmOption.equalsIgnoreCase(AlarmOption.CUSTOMIZED
		    .toString())) {
		alarmImageView.setImageResource(R.drawable.alarm_active);
	    }

	    databaseHelper = new DataHelper(Main.this);
	    int alarmCount = databaseHelper.countEventsByAlarmOption(myList
		    .get(position).getId());

	    if (alarmCount == 0) {
		databaseHelper.updateCourseOrTeacher(myList.get(position)
			.getId(), AlarmOption.DISABLED);
	    }
	    // ------------------------------------------------------------
	    databaseHelper.close();

	    return convertView;
	}

	/**
	 * 
	 * Holder class to hold layout resources
	 * 
	 */
	private class ViewHolder {
	    private View row;
	    private TextView courseTitle = null;
	    private View sideline = null;
	    private ImageView alarmImageView = null;

	    public ViewHolder(View row) {
		this.row = row;
	    }

	    public TextView getTitle() {
		if (courseTitle == null) {
		    courseTitle = (TextView) row
			    .findViewById(R.id.courses_list_item_title);
		}
		return courseTitle;
	    }

	    public View getSideline() {
		if (sideline == null) {
		    sideline = (View) row.findViewById(R.id.courses_sideline);
		}
		return sideline;
	    }

	    public ImageView getImage() {
		if (alarmImageView == null) {
		    alarmImageView = (ImageView) row
			    .findViewById(R.id.course_list_alarm_icon);
		}
		return alarmImageView;
	    }
	}

	@Override
	public int getCount() {
	    return myList.size();
	}

	@Override
	public Object getItem(int arg0) {
	    return null;
	}

	@Override
	public long getItemId(int arg0) {
	    return 0;
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	boolean result = super.onCreateOptionsMenu(menu);
	MenuItem m1 = menu.add(Menu.NONE, ADD_NEW_COURSE_TEACHER, Menu.NONE,
		resource.getString(R.string.main_add_new_course_teacher));
	MenuItem m2 = menu.add(Menu.NONE, CLOSE_APPLICATION, Menu.NONE,
		resource.getString(R.string.main_close_application));
	MenuItem m3 = menu.add(Menu.NONE, SETTINGS, Menu.NONE,
		resource.getString(R.string.main_option_menu_setting));
	MenuItem m4 = menu.add(Menu.NONE, ABOUT, Menu.NONE,
		resource.getString(R.string.main_option_menu_about));
	m1.setIcon(android.R.drawable.ic_menu_add);
	m2.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
	m3.setIcon(android.R.drawable.ic_menu_preferences);
	m4.setIcon(android.R.drawable.ic_menu_info_details);

	return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case ADD_NEW_COURSE_TEACHER:
	    startActivity(new Intent(Main.this, SearchScreen.class));
	    return true;
	case CLOSE_APPLICATION:
	    this.finish();
	    return true;
	case SETTINGS:
	    startActivity(new Intent(Main.this, TimeEditPreference.class));
	    return true;
	case ABOUT:
	    about();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
	menu.setHeaderTitle(resource
		.getString(R.string.main_contetext_menu_title));
	menu.setHeaderIcon(R.drawable.options);
	menu.add(Menu.NONE, EDIT, Menu.NONE,
		resource.getString(R.string.main_edit_course_teacher));
	menu.add(Menu.NONE, DELETE, Menu.NONE,
		resource.getString(R.string.main_delete_course_teacher));
	menu.add(Menu.NONE, DELETE_ALL, Menu.NONE,
		resource.getString(R.string.main_delete_all_course_teacher));
	menu.add(Menu.NONE, ACTIVATE_ALARM, Menu.NONE,
		resource.getString(R.string.main_activate_alarm_course_teacher));
	menu.add(Menu.NONE, DEACTIVATE_ALARM, Menu.NONE, resource
		.getString(R.string.main_deactivate_alarm_course_teacher));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
		.getMenuInfo();
	int courseTeacherId = myList.get(info.position).getId();

	switch (item.getItemId()) {
	case EDIT:
	    if (courseTeacherId != -1 && courseTeacherId != -2) {
		Intent intent = new Intent(Main.this,
			EditCourseTeacherScreen.class);
		CourseOrTeacher courseOrTeacher = courcesOrTeachers
			.get(info.position);
		intent.putExtra("id", courseOrTeacher.getCourseTeacherId());
		intent.putExtra("signature",
			courseOrTeacher.getCourseTeacherSignature());
		intent.putExtra("name", courseOrTeacher.getCourseTeacherName());
		intent.putExtra("color",
			courseOrTeacher.getCourseTeacherColor());
		intent.putExtra("type", courseOrTeacher.getType());
		intent.putExtra("alarm", courseOrTeacher.getAlarmOption());
		startActivity(intent);
	    }
	    return true;
	case DELETE:
	    if (courseTeacherId != -1 && courseTeacherId != -2) {
		progressDialog = ProgressDialog.show(Main.this, null,
			resource.getString(R.string.main_delete_progress),
			true, false);
		deleteThread = new DeleteThread(handler, courseTeacherId);
		deleteThread.start();
	    }
	    return true;
	case DELETE_ALL:
	    progressDialog = ProgressDialog.show(Main.this, null,
		    resource.getString(R.string.main_delete_progress), true,
		    false);
	    deleteThread = new DeleteThread(handler, 0);// courseTeacherId =
							// 0 means delete
							// all
	    deleteThread.start();
	    return true;
	case ACTIVATE_ALARM:
	    if (courseTeacherId != -1 && courseTeacherId != -2) {
		setAlarmTime(courseTeacherId);
	    } else {
		showAlert(
			resource.getString(R.string.main_alarm_cannot_set_from_today_title),
			resource.getString(R.string.main_alarm_cannot_set_from_today_message),
			false);
	    }
	    return true;
	case DEACTIVATE_ALARM:
	    if (courseTeacherId != -1 && courseTeacherId != -2) {
		alarmDeActiveDialog = ProgressDialog
			.show(Main.this,
				null,
				resource.getString(R.string.main_deactivate_alarm_progress),
				true, false);
		alarmDeActiveThread = new AlarmDeActiveThread(handler,
			courseTeacherId);
		alarmDeActiveThread.start();
	    } else {
		showAlert(
			resource.getString(R.string.main_alarm_cannot_deactive_from_today_title),
			resource.getString(R.string.main_alarm_cannot_deactive_from_today_message),
			false);
	    }
	    return true;
	default:
	    return super.onContextItemSelected(item);
	}
    }

    /**
     * 
     * Delete Thread To Delete CourseTeacher List
     * 
     */
    private class DeleteThread extends Thread {
	Handler handler;
	int courseTeacherId;

	// courseTeacherId = 0 means delete all
	public DeleteThread(Handler handler, int courseTeacherId) {
	    this.handler = handler;
	    this.courseTeacherId = courseTeacherId;
	}

	public synchronized void run() {
	    boolean result = false;
	    try {
		databaseHelper = new DataHelper(Main.this);
		if (courseTeacherId == 0) {
		    ArrayList<MyAlarmList> alarmList = databaseHelper
			    .getAllAlarmTime();
		    if (alarmList.size() != 0) {
			for (MyAlarmList alarm : alarmList) {
			    alarmDeActive(alarm.getEventId());
			}
		    }
		    result = deleteAllCourseEvent();
		} else {
		    ArrayList<MyAlarmList> alarmList = databaseHelper
			    .getAllAlarmTimeByCourseId(courseTeacherId);
		    if (alarmList.size() != 0) {
			for (MyAlarmList alarm : alarmList) {
			    alarmDeActive(alarm.getEventId());
			}
		    }

		    result = deleteCourseEvent(courseTeacherId);
		}
		databaseHelper.close();

		Log.v("TAG", "Course Deleted");
		handler.sendMessage(handler.obtainMessage(1));

	    } catch (Exception e) {
		Log.v("TAG", "Course Delete Exception");
		handler.sendMessage(handler.obtainMessage(-1));
		e.printStackTrace();
	    }
	}
    }

    /**
     * Handle Threads
     */
    private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    if (msg.what == 1) {
		deleteThread = null;
		progressDialog.dismiss();
		update();
	    } else if (msg.what == -1) {
		deleteThread = null;
		progressDialog.dismiss();
		showAlert(
			resource.getString(R.string.main_delete_course_teacher_title),
			resource.getString(R.string.main_delete_course_teacher_message),
			false);
	    } else if (msg.what == 2) {
		alarmActiveThread = null;
		alarmActiveDialog.dismiss();
		update();
	    } else if (msg.what == -2) {
		alarmActiveThread = null;
		alarmActiveDialog.dismiss();
		showAlert(
			resource.getString(R.string.main_alarm_set_fail_title),
			resource.getString(R.string.main_alarm_set_fail_message),
			false);
	    } else if (msg.what == 3) {
		alarmDeActiveThread = null;
		alarmDeActiveDialog.dismiss();
		update();
	    } else if (msg.what == -3) {
		alarmDeActiveThread = null;
		alarmDeActiveDialog.dismiss();
		showAlert(
			resource.getString(R.string.main_alarm_unset_fail_title),
			resource.getString(R.string.main_alarm_unset_fail_message),
			false);
	    }
	}
    };

    /**
     * Return True or False and Delete course event by courseTeacherId
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>Boolean</b> deleteCourseEvent
     */
    private boolean deleteCourseEvent(int courseTeacherId) {
	return databaseHelper.deleteCourseOrTeacher(courseTeacherId);
    }

    /**
     * Return True or False and Delete all course or teacher
     * 
     * @return <b>Boolean</b> deleteAllCourseEvent
     */
    private boolean deleteAllCourseEvent() {
	return databaseHelper.deleteAllCoursesOrTeachers();
    }

    /**
     * 
     * AlarmActiveThread to activate alarm
     * 
     */
    private class AlarmActiveThread extends Thread {
	Handler handler;
	int courseTeacherId;
	int alarm;

	public AlarmActiveThread(Handler handler, int courseTeacherId,
		int alarmTime) {
	    this.handler = handler;
	    this.courseTeacherId = courseTeacherId;
	    this.alarm = alarmTime;
	}

	public synchronized void run() {
	    try {
		boolean result = activateAlarm(courseTeacherId, alarm);
		handler.sendMessage(handler.obtainMessage(2));

	    } catch (Exception e) {
		handler.sendMessage(handler.obtainMessage(-2));
		e.printStackTrace();
	    }
	}
    }

    /**
     * 
     * AlarmDeActiveThread to deactivate alarm
     * 
     */
    private class AlarmDeActiveThread extends Thread {
	Handler handler;
	int courseTeacherId;

	public AlarmDeActiveThread(Handler handler, int courseTeacherId) {
	    this.handler = handler;
	    this.courseTeacherId = courseTeacherId;
	}

	public synchronized void run() {
	    try {
		boolean result = deActivateAlarm(courseTeacherId);

		if (result == true) {
		    handler.sendMessage(handler.obtainMessage(3));
		} else {
		    handler.sendMessage(handler.obtainMessage(-3));
		}
	    } catch (Exception e) {
		handler.sendMessage(handler.obtainMessage(-3));
		e.printStackTrace();
	    }
	}
    }

    /**
     * Set Alarm for Course or Teacher
     * 
     * @param Int
     *            courseTeacherId
     */
    private void setAlarmTime(int courseTeacherId) {
	final String[] time = resource
		.getStringArray(R.array.before_lecture_time_text);
	final int[] timeValue = resource
		.getIntArray(R.array.before_lecture_time_value);
	final int id = courseTeacherId;

	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle(resource.getString(R.string.main_set_alarm_title));
	builder.setIcon(R.drawable.alarm_enabled);
	builder.setItems(time, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int item) {
		alarmTime = timeValue[item];
		dialog.dismiss();
		alarmActiveDialog = ProgressDialog.show(Main.this, null,
			"Setting Alarm...", true, false);
		alarmActiveThread = new AlarmActiveThread(handler, id,
			alarmTime);
		alarmActiveThread.start();
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    /**
     * Process of Activate Alarm
     * 
     * @param Int
     *            courseTeacherId
     * @param Int
     *            alarmTime
     * @return <b>Boolen</b> activateAlarm
     */
    private boolean activateAlarm(int courseTeacherId, int alarmTime) {
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String today = sdf.format(date) + "000000";

	databaseHelper = new DataHelper(Main.this);
	Event[] events = databaseHelper.getAllEvents(today, courseTeacherId);

	ArrayList<MyAlarmList> alarmList = new ArrayList<MyAlarmList>();
	MyAlarmList myAlarm = null;
	Calendar cal = Calendar.getInstance();
	AlarmOption option = AlarmOption.DISABLED;
	long currentTime = 0;
	long setAlarmTime = 0;

	if (events.length != 0) {
	    databaseHelper.updateCourseOrTeacher(courseTeacherId,
		    AlarmOption.ENABLED);

	    for (Event event : events) {
		String lectureTime = event.getStart();
		long timeStamp = getTimeStamp(lectureTime);
		long eventPK = event.getPrimaryKey();

		cal.clear();
		cal.setTimeInMillis(timeStamp);
		cal.add(Calendar.MINUTE, -alarmTime);

		currentTime = System.currentTimeMillis();
		setAlarmTime = cal.getTimeInMillis();

		if (currentTime < setAlarmTime) {
		    option = AlarmOption.ENABLED;
		    myAlarm = new MyAlarmList(courseTeacherId,
			    event.getPrimaryKey(), setAlarmTime, option);

		    alarmList.add(myAlarm);
		    alarmActive(eventPK, courseTeacherId, setAlarmTime);
		} else {
		    option = AlarmOption.DISABLED;
		}

		databaseHelper.updateEventAlarmOption(eventPK, option);
	    }
	    databaseHelper.insertAlarm(courseTeacherId, alarmList);
	    databaseHelper.close();
	} else {
	    databaseHelper.updateCourseOrTeacher(courseTeacherId,
		    AlarmOption.DISABLED);
	    databaseHelper.close();
	    showAlert(
		    resource.getString(R.string.main_alarm_set_fail_no_class_title),
		    resource.getString(R.string.main_alarm_set_fail_no_class_message),
		    false);
	}

	return true;
    }

    /**
     * Active Alarm
     * 
     * @param Long
     *            eventPK
     * @param Int
     *            courseTeacherId
     * @param Long
     *            alarmTimestamp
     */
    private void alarmActive(long eventPK, int courseTeacherId,
	    long alarmTimestamp) {
	int id = (int) eventPK;
	Intent intent = new Intent(Main.this, AlarmMessageReceiver.class);
	intent.putExtra("eventPK", eventPK);
	intent.putExtra("courseTeacherId", courseTeacherId);
	PendingIntent sender = PendingIntent.getBroadcast(Main.this, id,
		intent, 0);

	long startTime = alarmTimestamp;
	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
	am.set(AlarmManager.RTC_WAKEUP, startTime, sender);
    }

    /**
     * Process of Deactivate
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>Boolean</b> deActivateAlarm
     */
    private boolean deActivateAlarm(int courseTeacherId) {
	databaseHelper = new DataHelper(Main.this);
	ArrayList<MyAlarmList> alarmList = databaseHelper
		.getAllAlarmTime(courseTeacherId);

	int size = alarmList.size();

	if (size != 0) {
	    for (MyAlarmList myEvent : alarmList) {
		long eventPK = myEvent.getEventId();
		alarmDeActive(eventPK);
		databaseHelper.deleteAlarmByEvent(courseTeacherId, eventPK);
		databaseHelper.updateEventAlarmOption(eventPK,
			AlarmOption.DISABLED);
	    }
	    databaseHelper.updateCourseOrTeacher(courseTeacherId,
		    AlarmOption.DISABLED);
	    databaseHelper.close();
	} else {
	    databaseHelper.close();
	    return false;
	}

	return true;
    }

    /**
     * Deactivate Alarm
     * 
     * @param eventPK
     */
    private void alarmDeActive(long eventPK) {
	int id = (int) eventPK;
	Intent intent = new Intent(Main.this, AlarmMessageReceiver.class);
	PendingIntent sender = PendingIntent.getBroadcast(Main.this, id,
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
     * @return <b>getTimeStamp</b>
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
     * Show Alert Dialog
     * 
     * @param title
     *            String
     * @param message
     *            String
     */
    private void showAlert(String title, String message, boolean isNull) {
	final boolean check = isNull;
	AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
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
     * Auto Update Settings
     */
    private void autoUpdate() {
	long startTime = 10 * 1000 + System.currentTimeMillis();// default, not
								// used
	long interval = autoUpdateTime + 3600000; // 3600000 for 1 Hour

	Date date = new Date();
	int dateVal = date.getHours();
	if (autoUpdateTime != 0) {
	    dateVal = dateVal / autoUpdateTime;
	    dateVal = ((dateVal * autoUpdateTime) + autoUpdateTime) * 60;

	    int hour = date.getHours();
	    int min = date.getMinutes();
	    min = ((hour) * 60) + min;
	    dateVal = dateVal - min + 1;

	    Calendar cals = Calendar.getInstance();
	    cals.add(Calendar.MINUTE, dateVal);
	    startTime = cals.getTimeInMillis();
	}

	Intent intent = new Intent(Main.this, AutoUpdateService.class);
	PendingIntent pendingIntent = PendingIntent.getService(
		context.getApplicationContext(), 0, intent,
		PendingIntent.FLAG_UPDATE_CURRENT);

	AlarmManager am = (AlarmManager) context
		.getSystemService(Context.ALARM_SERVICE);

	if (isAutoUpdateEnable == false) {
	    if (autoUpdateTime != 0) {
		previousTime = autoUpdateTime;
		isAutoUpdateEnable = true;
		am.setRepeating(AlarmManager.RTC, startTime, interval,
			pendingIntent);
	    }
	} else {
	    if (autoUpdateTime == 0) {
		isAutoUpdateEnable = false;
		am.cancel(pendingIntent);
		this.stopService(intent);
	    } else {
		if (previousTime != autoUpdateTime) {
		    previousTime = autoUpdateTime;
		    isAutoUpdateEnable = true;
		    am.setRepeating(AlarmManager.RTC, startTime, interval,
			    pendingIntent);

		    this.startService(intent);
		}
	    }
	}
    }

    /**
     * About: Information
     */
    private void about() {
	AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
	builder.setIcon(R.drawable.warning);
	builder.setTitle("About");
	StringBuilder message = new StringBuilder();
	message.append("Time Edit Application").append("\n");
	message.append("Md. Shohel Shamim").append("\n");
	message.append("ms222ij@student.lnu.se").append("\n");
	message.append("Linnaeus University").append("\n");
	message.append("Icons downloaded from").append("\n")
		.append("www.findicons.com");
	builder.setMessage(message.toString())
		.setCancelable(true)
		.setPositiveButton(R.string.dialog_button_ok,
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			    }
			});
	AlertDialog alert = builder.create();
	alert.show();
    }
}