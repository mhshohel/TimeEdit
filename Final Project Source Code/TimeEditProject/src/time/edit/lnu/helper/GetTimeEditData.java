/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.helper
 *
 * @FileName GetTimeEditData.java
 * 
 * @FileCreated Oct 29, 2011
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
package time.edit.lnu.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import time.edit.lnu.datatype.CourseOrTeacher;
import time.edit.lnu.datatype.Event;
import time.edit.lnu.datatype.MyEvents;
import time.edit.lnu.datatype.MyNotificationList;
import time.edit.lnu.schedule.R;
import time.edit.lnu.schedule.Schedule;
import time.edit.lnu.services.LnuNotification;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * GetTimeEditData class created for Auto Update and Widget Service to get
 * updated data from Internet
 * 
 */
public class GetTimeEditData {
    private DownloadThread downloadThread;
    private Context context;
    private DataHelper databaseHelper;
    private ArrayList<MyEvents> updatedListEvents = new ArrayList<MyEvents>();
    private Calendar cal = GregorianCalendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private ArrayList<MyNotificationList> myNotifications;
    private Date todayDate = cal.getTime();
    private String today = sdf.format(todayDate).substring(0, 8);
    private SimpleDateFormat formatter = new SimpleDateFormat(
	    "EEEE, d MMMM yyyy");
    private static int LNU_NOTIFICATION_ID = Schedule.LNU_NOTIFICATION_ID;

    /**
     * Constructor of GetTimeEditData
     * 
     * @param Context
     *            context
     */
    public GetTimeEditData(Context context) {
	this.context = context;
    }

    /**
     * Update Database start download Thread to get data from Internet
     */
    public void updateData() {
	downloadThread = new DownloadThread(handler);
	downloadThread.start();
    }

    /**
     * 
     * DownloadThread Class to Separate download task from main task and to
     * download data from Internet
     * 
     */
    private class DownloadThread extends Thread {
	Handler handler;
	ArrayList<Integer> allCourseTeacherIds;

	/**
	 * DownloadThread Constructor
	 * 
	 * @param Handler
	 *            handler
	 */
	public DownloadThread(Handler handler) {
	    this.handler = handler;
	}

	/**
	 * Run The Task
	 */
	public synchronized void run() {
	    databaseHelper = new DataHelper(context);
	    this.allCourseTeacherIds = databaseHelper
		    .getAllCourseOrTeacherIds();

	    int language = 2; // 2 is to get English Version; 1 is for Swedish
			      // Version
	    updatedListEvents.clear();

	    try {
		for (int courseTeacherId : allCourseTeacherIds) {
		    Event[] events;

		    events = ICalDownloader.downloadICal(courseTeacherId,
			    language);

		    Log.v("TAG", "iCal downloaded");
		    updatedListEvents
			    .add(new MyEvents(events, courseTeacherId));
		}
		databaseHelper.close();
		handler.sendMessage(handler.obtainMessage(1));

	    } catch (Exception e) {
		Log.v("TAG",
			"Exception Downloading Data from Net, DownloadThread of GetTimeEditData");
		handler.sendMessage(handler.obtainMessage(-1));
		e.printStackTrace();
	    }
	}
    }

    /**
     * Handler to Control the Threads
     */
    private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    if (msg.what == 1) {
		getData(true);
	    } else if (msg.what == -1) {
		getData(false);
	    }

	    downloadThread = null;
	}
    };

    /**
     * To insert updated data from Internet or get data from database
     * 
     * @param Boolean
     *            isDownloaded
     */
    private void getData(boolean isDownloaded) {
	databaseHelper = new DataHelper(context);
	String startDay = sdf.format(todayDate).substring(0, 8) + "000000";

	if (isDownloaded == true) {
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
	databaseHelper.close();
    }

    /**
     * Return formated Information of Event's Start time, course signature by
     * courseTeacher and event
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
     * Return Formated Date
     * 
     * @param String
     *            start
     * @return <b>String</b> getDateString
     */
    private String getDateString(String start) {
	cal.roll(GregorianCalendar.DAY_OF_MONTH, 1);
	Date tomorrowDate = cal.getTime();
	String tomorrow = sdf.format(tomorrowDate).substring(0, 8);

	if (start.substring(0, 8).equals(today)) {
	    return context.getResources().getString(R.string.today);
	} else if (start.substring(0, 8).equals(tomorrow)) {
	    return context.getResources().getString(R.string.tomorrow);
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

    /**
     * Notify User if Schedule Change
     * 
     * @param String
     *            oldData
     * @param String
     *            newData
     */
    private void notifyUser(String oldData, String newData) {
	LnuNotification.setNotificationList(oldData, newData);

	String notificationService = Context.NOTIFICATION_SERVICE;
	NotificationManager notificationManager = (NotificationManager) context
		.getSystemService(notificationService);

	int icon = R.drawable.icon_old;
	CharSequence tickerText = context.getResources().getString(
		R.string.schedule_notification_ticker_title);
	long when = System.currentTimeMillis();
	CharSequence contentTitle = context.getResources().getString(
		R.string.app_name);
	CharSequence contentText = context.getResources().getString(
		R.string.schedule_notification_ticker_message);

	Notification notification = new Notification(icon, tickerText, when);
	notification.defaults |= Notification.DEFAULT_ALL;

	Intent notificationIntent = new Intent(context, LnuNotification.class);
	PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
		notificationIntent, 0);
	notification.setLatestEventInfo(context, contentTitle, contentText,
		contentIntent);
	notificationManager.notify(LNU_NOTIFICATION_ID, notification);
    }
}