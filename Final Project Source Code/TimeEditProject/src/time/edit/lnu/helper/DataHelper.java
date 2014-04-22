/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.datatype
 *
 * @FileName DataHelper.java
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
package time.edit.lnu.helper;

import java.util.ArrayList;

import time.edit.lnu.datatype.AlarmOption;
import time.edit.lnu.datatype.CourseOrTeacher;
import time.edit.lnu.datatype.Event;
import time.edit.lnu.datatype.MyAlarmList;
import time.edit.lnu.datatype.MyEvents;
import time.edit.lnu.datatype.MyNotificationList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DateHelper handles all database queries.
 */
public class DataHelper {

    private static final String DATABASE_NAME = "lnuTimeEditDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String EVENT_TABLE = "event_table";
    private static final String COURSE_TEACHER_TABLE = "course_teacher_table";
    private static final String ALARM_TABLE = "alarm_table";
    private static final String TAG = "TimeEditDataHelper";

    private static final String COURSE_TEACHER_ID = "courseTeacherId";
    private static final String COURSE_TEACHER_SIGNATURE = "courseTeacherSignature";
    private static final String COURSE_TEACHER_NAME = "courseTeacherName";
    private static final String COURSE_TEACHER_COLOR = "courseTeacherColor";
    private static final String COURSE_TEACHER_TYPE = "type";
    private static final String COURSE_TEACHER_ALARM_OPTION = "alarmOption";
    private static final String EVENT_PRIMARY_KEY = "_id";
    private static final String EVENT_START = "start";
    private static final String EVENT_STOP = "stop";
    private static final String EVENT_ROOM = "room";
    private static final String EVENT_PERSONNEL = "personnel";
    private static final String EVENT_MOMENT = "moment";
    private static final String EVENT_REMARK = "remark";
    private static final String EVENT_SUMMARY = "summary";
    private static final String EVENT_ALARM_OPTION = "alarmOption";
    private static final String ALARM_PRIMARY_KEY = "_id";
    private static final String ALARM_COURSE_TEACHER_ID = "courseTeacherId";
    private static final String ALARM_EVENT_ID = "eventId";
    private static final String ALARM_START = "alarmTimeStart";
    private static final String ALARM_OPTION = "alarmOption";

    private Context context;
    private SQLiteDatabase db;
    private OpenHelper openHelper;

    /**
     * Constructor of DataHelper Class
     * 
     * @param Context
     *            context
     */
    public DataHelper(Context context) {
	this.context = context;
	openHelper = new OpenHelper(this.context);
	this.db = openHelper.getWritableDatabase();
    }

    /**
     * Close database connection
     */
    public void close() {
	openHelper.close();
	db.close();
    }

    /**
     * Insert Course or Teacher Information
     * 
     * @param Int
     *            courseTeacherId
     * @param String
     *            courseTeacherSignature
     * @param String
     *            courseTeacherName
     * @param Int
     *            courseTeacherColor
     * @param String
     *            type
     * @param String
     *            alarmOption
     */
    public void insertCourseOrTeacher(int courseTeacherId,
	    String courseTeacherSignature, String courseTeacherName,
	    int courseTeacherColor, String type, String alarmOption) {
	ContentValues contentValue = new ContentValues();
	contentValue.put(COURSE_TEACHER_ID, courseTeacherId);
	contentValue.put(COURSE_TEACHER_SIGNATURE, courseTeacherSignature);
	contentValue.put(COURSE_TEACHER_NAME, courseTeacherName);
	contentValue.put(COURSE_TEACHER_COLOR, courseTeacherColor);
	contentValue.put(COURSE_TEACHER_TYPE, type);
	contentValue.put(COURSE_TEACHER_ALARM_OPTION, alarmOption);
	db.insert(COURSE_TEACHER_TABLE, null, contentValue);
    }

    /**
     * Update Course or Teacher Information with all Course Teacher information
     * 
     * @param Int
     *            courseTeacherId
     * @param String
     *            courseTeacherSignature
     * @param String
     *            courseTeacherName
     * @param Int
     *            courseTeacherColor
     * @param String
     *            type
     * @param String
     *            alarmOption
     */
    public void updateCourseOrTeacher(int courseTeacherId,
	    String courseTeacherSignature, String courseTeacherName,
	    int courseTeacherColor, String type, String alarmOption) {
	ContentValues contentValue = new ContentValues();
	contentValue.put(COURSE_TEACHER_ID, courseTeacherId);
	contentValue.put(COURSE_TEACHER_SIGNATURE, courseTeacherSignature);
	contentValue.put(COURSE_TEACHER_NAME, courseTeacherName);
	contentValue.put(COURSE_TEACHER_COLOR, courseTeacherColor);
	contentValue.put(COURSE_TEACHER_TYPE, type);
	contentValue.put(COURSE_TEACHER_ALARM_OPTION, alarmOption);
	db.update(COURSE_TEACHER_TABLE, contentValue, COURSE_TEACHER_ID + "="
		+ courseTeacherId, null);
    }

    /**
     * Update Course or Teacher Information by courseTeacherId and AlarmOption
     * 
     * @param Int
     *            courseTeacherId
     * @param AlarmOption
     *            alarmOption
     */
    public void updateCourseOrTeacher(int courseTeacherId,
	    AlarmOption alarmOption) {
	ContentValues contentValue = new ContentValues();
	contentValue.put(COURSE_TEACHER_ALARM_OPTION, alarmOption.toString());
	db.update(COURSE_TEACHER_TABLE, contentValue, COURSE_TEACHER_ID + "="
		+ courseTeacherId, null);
    }

    /**
     * Update Event's Alarm Option
     * 
     * @param Long
     *            eventPK
     * @param AlarmOption
     *            alarmOption
     */
    public void updateEventAlarmOption(long eventPK, AlarmOption alarmOption) {
	ContentValues contentValue = new ContentValues();
	contentValue.put(EVENT_ALARM_OPTION, alarmOption.toString());
	db.update(EVENT_TABLE, contentValue, EVENT_PRIMARY_KEY + "=" + eventPK,
		null);
    }

    /**
     * Return True or False if courseTeacherId is in Database or Not
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>Boolean</b> isFoundCourseTeacher
     */
    public boolean isFoundCourseTeacher(int courseTeacherId) {
	Cursor cursor = this.db.query(COURSE_TEACHER_TABLE, new String[] {
		COURSE_TEACHER_ID, COURSE_TEACHER_SIGNATURE,
		COURSE_TEACHER_NAME, COURSE_TEACHER_COLOR, COURSE_TEACHER_TYPE,
		COURSE_TEACHER_ALARM_OPTION }, COURSE_TEACHER_ID + "="
		+ courseTeacherId, null, null, null, null);

	if (cursor.moveToFirst()) {
	    do {
		cursor.close();
		return true;
	    } while (cursor.moveToNext());
	}

	cursor.close();
	return false;
    }

    /**
     * Return Total Number of Course or Teacher
     * 
     * @return <b>Long</b> countCourseOrTeacher
     */
    public long countCourseOrTeacher() {
	return DatabaseUtils.queryNumEntries(db, COURSE_TEACHER_TABLE);
    }

    /**
     * Return Total Number of Events by Alarm Option
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>Int</b> countEventsByAlarmOption
     */
    public int countEventsByAlarmOption(int courseTeacherId) {
	int count = 0;
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, COURSE_TEACHER_ID + " = "
		+ courseTeacherId, null, null, null, null);

	if (cursor.moveToFirst()) {
	    do {
		if (cursor.getString(9).equalsIgnoreCase(
			AlarmOption.ENABLED.toString())) {
		    count++;
		}
	    } while (cursor.moveToNext());
	}
	cursor.close();

	return count;
    }

    /**
     * Return True and Delete all Course or Teacher Information
     * 
     * @return <b>Boolean</b> deleteAllCoursesOrTeachers
     */
    public boolean deleteAllCoursesOrTeachers() {
	this.db.delete(COURSE_TEACHER_TABLE, null, null);
	deleteAllEvents();
	deleteAllAlarm();

	return true;
    }

    /**
     * Return True and Delete Course or Teacher by courseTeacherId
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>Boolean</b> deleteCourseOrTeacher
     */
    public boolean deleteCourseOrTeacher(int courseTeacherId) {
	this.db.delete(COURSE_TEACHER_TABLE, COURSE_TEACHER_ID + "="
		+ courseTeacherId, null);
	this.db.delete(EVENT_TABLE, COURSE_TEACHER_ID + "=" + courseTeacherId,
		null);
	deleteAlarmByCourseTeacher(courseTeacherId);

	return true;
    }

    /**
     * Get Course or Teacher by courseTeacherId
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>CourseOrTeacher</b> getCourseOrTeacher
     */
    public CourseOrTeacher getCourseOrTeacher(int courseTeacherId) {
	Cursor cursor = this.db.query(COURSE_TEACHER_TABLE, new String[] {
		COURSE_TEACHER_ID, COURSE_TEACHER_SIGNATURE,
		COURSE_TEACHER_NAME, COURSE_TEACHER_COLOR, COURSE_TEACHER_TYPE,
		COURSE_TEACHER_ALARM_OPTION }, COURSE_TEACHER_ID + "="
		+ courseTeacherId, null, null, null, null);

	CourseOrTeacher courseTeacher = null;
	if (cursor.moveToFirst()) {
	    do {
		courseTeacher = new CourseOrTeacher(cursor.getInt(0),
			cursor.getString(1), cursor.getString(2),
			cursor.getInt(3), cursor.getString(4),
			cursor.getString(5));
	    } while (cursor.moveToNext());
	}

	cursor.close();

	return courseTeacher;
    }

    /**
     * Return All Course or Teacher Information
     * 
     * @return <b>ArrayList<CourseOrTeacher</b> getAllCoursesOrTeachers
     */
    public ArrayList<CourseOrTeacher> getAllCoursesOrTeachers() {
	ArrayList<CourseOrTeacher> list = new ArrayList<CourseOrTeacher>();
	Cursor cursor = this.db.query(COURSE_TEACHER_TABLE, new String[] {
		COURSE_TEACHER_ID, COURSE_TEACHER_SIGNATURE,
		COURSE_TEACHER_NAME, COURSE_TEACHER_COLOR, COURSE_TEACHER_TYPE,
		COURSE_TEACHER_ALARM_OPTION }, null, null, null, null,
		COURSE_TEACHER_NAME + " asc");
	if (cursor.moveToFirst()) {
	    do {
		list.add(new CourseOrTeacher(cursor.getInt(0), cursor
			.getString(1), cursor.getString(2), cursor.getInt(3),
			cursor.getString(4), cursor.getString(5)));
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return list;
    }

    /**
     * Return All Course or Teacher Information by courseTeacherId
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>ArrayList<CourseOrteacher></b> getAllCoursesOrTeachers
     */
    public ArrayList<CourseOrTeacher> getAllCoursesOrTeachers(
	    int courseTeacherId) {
	ArrayList<CourseOrTeacher> list = new ArrayList<CourseOrTeacher>();
	Cursor cursor = this.db.query(COURSE_TEACHER_TABLE, new String[] {
		COURSE_TEACHER_ID, COURSE_TEACHER_SIGNATURE,
		COURSE_TEACHER_NAME, COURSE_TEACHER_COLOR, COURSE_TEACHER_TYPE,
		COURSE_TEACHER_ALARM_OPTION }, COURSE_TEACHER_ID + "="
		+ courseTeacherId, null, null, null, null);
	if (cursor.moveToFirst()) {
	    do {
		list.add(new CourseOrTeacher(cursor.getInt(0), cursor
			.getString(1), cursor.getString(2), cursor.getInt(3),
			cursor.getString(4), cursor.getString(5)));
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return list;
    }

    /**
     * Return All Course or Teacher Id
     * 
     * @return <b>ArrayList<Integer></b> getAllCourseOrTeacherIds
     */
    public ArrayList<Integer> getAllCourseOrTeacherIds() {
	ArrayList<Integer> list = new ArrayList<Integer>();
	Cursor cursor = this.db.query(COURSE_TEACHER_TABLE,
		new String[] { COURSE_TEACHER_ID }, null, null, null, null,
		null);
	if (cursor.moveToFirst()) {
	    do {
		list.add(cursor.getInt(0));
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return list;
    }

    /**
     * Insert Events Information by Events array
     * 
     * @param Event
     *            [] events
     */
    public void insertEvents(Event[] events) {
	for (Event event : events) {
	    ContentValues contentValue = new ContentValues();
	    contentValue.put(COURSE_TEACHER_ID, event.getCourseTeacherId());
	    contentValue.put(EVENT_START, event.getStart());
	    contentValue.put(EVENT_STOP, event.getStop());
	    contentValue.put(EVENT_ROOM, event.getRoom());
	    contentValue.put(EVENT_PERSONNEL, event.getPersonnel());
	    contentValue.put(EVENT_MOMENT, event.getMoment());
	    contentValue.put(EVENT_REMARK, event.getRemark());
	    contentValue.put(EVENT_SUMMARY, event.getSummary());
	    contentValue.put(EVENT_ALARM_OPTION, event.getAlarmOption());
	    db.insert(EVENT_TABLE, null, contentValue);
	}
    }

    /**
     * Insert Single Event
     * 
     * @param Event
     *            event
     */
    public void insertEvents(Event event) {
	ContentValues contentValue = new ContentValues();
	contentValue.put(COURSE_TEACHER_ID, event.getCourseTeacherId());
	contentValue.put(EVENT_START, event.getStart());
	contentValue.put(EVENT_STOP, event.getStop());
	contentValue.put(EVENT_ROOM, event.getRoom());
	contentValue.put(EVENT_PERSONNEL, event.getPersonnel());
	contentValue.put(EVENT_MOMENT, event.getMoment());
	contentValue.put(EVENT_REMARK, event.getRemark());
	contentValue.put(EVENT_SUMMARY, event.getSummary());
	contentValue.put(EVENT_ALARM_OPTION, event.getAlarmOption());
	db.insert(EVENT_TABLE, null, contentValue);
    }

    /**
     * Insert Events by Events List and Return Notification List if Schedule
     * Change
     * 
     * @param ArrayList
     *            <MyEvents> myEvents
     * @param String
     *            today
     * @return <b>ArrayList<MyNotificationList></b> insertEvents
     */
    public ArrayList<MyNotificationList> insertEvents(
	    ArrayList<MyEvents> myEvents, String today) {
	int formatToday = Integer.parseInt(today.substring(0, 8));
	ArrayList<MyNotificationList> myNotificationList = new ArrayList<MyNotificationList>();

	for (MyEvents myEvent : myEvents) {
	    int courseTeacherId = myEvent.getCourseTeacherId();
	    boolean isEmpty = isFoundEventByCourseTeacherId(courseTeacherId);

	    if (!isEmpty) {
		insertEvents(myEvent.getEvents());
		myNotificationList.add(new MyNotificationList());
	    } else {
		MyNotificationList myNotification = new MyNotificationList();
		CourseOrTeacher courseOrTeacher = this
			.getCourseOrTeacher(courseTeacherId);
		ArrayList<Event> previousdEvents = new ArrayList<Event>();
		previousdEvents = this.getAllEventsList(today, courseTeacherId);
		for (Event event : myEvent.getEvents()) {
		    String start = event.getStart().substring(0, 8);
		    int currentDate = Integer.parseInt(start);

		    if (currentDate >= formatToday) {
			if (previousdEvents.contains(event)) {
			    previousdEvents.remove(event);
			} else {
			    // If updated events found then insert into database
			    myNotification.setCourseOrTeacher(courseOrTeacher);
			    myNotification.addUpdated(event);
			    insertEvents(event);

			    if (courseOrTeacher.getAlarmOption().equals(
				    AlarmOption.ENABLED)) {
				updateCourseOrTeacher(courseTeacherId,
					AlarmOption.CUSTOMIZED);
			    }
			}
		    }
		}

		if (previousdEvents.size() != 0) {
		    myNotification.setCourseOrTeacher(courseOrTeacher);
		    for (Event event : previousdEvents) {
			// If updated events found then delete previous event
			// from database
			myNotification.addOldest(event);
			deleteEvents(event.getPrimaryKey());
			deleteAlarmByEvent(event.getCourseTeacherId(),
				event.getPrimaryKey());
		    }
		}

		myNotificationList.add(myNotification);
	    }
	}
	return myNotificationList;
    }

    /**
     * Return True or False if Events found by CourseTeacherId
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>Boolean</b> isFoundEventByCourseTeacherId
     */
    public boolean isFoundEventByCourseTeacherId(int courseTeacherId) {
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY }, COURSE_TEACHER_ID + "=" + courseTeacherId,
		null, null, null, EVENT_START + " asc");

	if (cursor.moveToFirst()) {
	    do {
		cursor.close();
		return true;
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return false;
    }

    /**
     * Return True or False if Event found by Event Primary Key
     * 
     * @param Long
     *            eventPK
     * @return <b>Boolean</b> isFoundEventByEventPK
     */
    public boolean isFoundEventByEventPK(long eventPK) {
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY }, EVENT_PRIMARY_KEY + "=" + eventPK, null, null,
		null, null);

	if (cursor.moveToFirst()) {
	    do {
		cursor.close();
		return true;
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return false;
    }

    /**
     * Return True or False if Alarm found By Event Primary Key
     * 
     * @param Long
     *            eventPK
     * @return <b>Boolean</b> isFoundAlarmByEventPK
     */
    public boolean isFoundAlarmByEventPK(long eventPK) {
	Cursor cursor = this.db.query(ALARM_TABLE, new String[] {
		ALARM_PRIMARY_KEY, ALARM_COURSE_TEACHER_ID, ALARM_EVENT_ID,
		ALARM_START, ALARM_OPTION }, ALARM_EVENT_ID + " = " + eventPK,
		null, null, null, ALARM_START + " ASC");

	if (cursor.moveToFirst()) {
	    do {
		cursor.close();
		return true;
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return false;
    }

    /**
     * Return True or False if Alarm Time Expired or less than current TimeStamp
     * 
     * @param Long
     *            eventPK
     * @param Long
     *            timeStamp
     * @return <b>Boolean</b> isAlarmExpiredByEventPK
     */
    public boolean isAlarmExpiredByEventPK(long eventPK, long timeStamp) {
	Cursor cursor = this.db.query(ALARM_TABLE, new String[] {
		ALARM_PRIMARY_KEY, ALARM_COURSE_TEACHER_ID, ALARM_EVENT_ID,
		ALARM_START, ALARM_OPTION }, ALARM_EVENT_ID + " = " + eventPK
		+ " AND " + ALARM_START + " < " + timeStamp, null, null, null,
		null);

	if (cursor.moveToFirst()) {
	    do {
		cursor.close();
		return true;
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return false;
    }

    /**
     * Delete All Events
     */
    public void deleteAllEvents() {
	this.db.delete(EVENT_TABLE, null, null);
    }

    /**
     * Delete Events by Event's Primary Key
     * 
     * @param Long
     *            primaryKey
     */
    public void deleteEvents(long eventPK) {
	this.db.delete(EVENT_TABLE, EVENT_PRIMARY_KEY + "=" + eventPK, null);
    }

    /**
     * Return All Events List
     * 
     * @return <b>ArrayList<Event></b> getAllEvents
     */
    public ArrayList<Event> getAllEvents() {
	ArrayList<Event> list = new ArrayList<Event>();
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, null, null, null, null,
		EVENT_START + " asc");
	if (cursor.moveToFirst()) {
	    do {
		Event event = new Event(cursor.getInt(1), cursor.getString(2),
			cursor.getString(3), cursor.getString(4),
			cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8),
			cursor.getString(9));
		event.setPrimaryKey(cursor.getInt(0));
		list.add(event);
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return list;
    }

    /**
     * Return All Events list by courseTeacherId
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>ArrayList<Event></b> getAllEvents
     */
    public ArrayList<Event> getAllEvents(int courseTeacherId) {
	ArrayList<Event> list = new ArrayList<Event>();
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, COURSE_TEACHER_ID + "="
		+ courseTeacherId, null, null, null, EVENT_START + " asc");
	if (cursor.moveToFirst()) {
	    do {
		Event event = new Event(cursor.getInt(1), cursor.getString(2),
			cursor.getString(3), cursor.getString(4),
			cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8),
			cursor.getString(9));
		event.setPrimaryKey(cursor.getInt(0));
		list.add(event);
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return list;
    }

    /**
     * Return All Events Array by courseTeacherId
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>Event[]</b> getAllEventsArray
     */
    public Event[] getAllEventsArray(int courseTeacherId) {
	ArrayList<Event> list = new ArrayList<Event>();
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, COURSE_TEACHER_ID + "="
		+ courseTeacherId, null, null, null, EVENT_START + " asc");
	if (cursor.moveToFirst()) {
	    do {
		Event event = new Event(cursor.getInt(1), cursor.getString(2),
			cursor.getString(3), cursor.getString(4),
			cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8),
			cursor.getString(9));
		event.setPrimaryKey(cursor.getInt(0));
		list.add(event);
	    } while (cursor.moveToNext());
	}
	cursor.close();
	Event[] events = new Event[list.size()];
	list.toArray(events);

	return events;
    }

    /**
     * Return Single Event by Event Primary Key
     * 
     * @param Long
     *            eventPK
     * @return <b>Event</b> getEventByEventPK
     */
    public Event getEventByEventPK(long eventPK) {
	Event event = null;
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, EVENT_PRIMARY_KEY + "="
		+ eventPK, null, null, null, null);
	if (cursor.moveToFirst()) {
	    do {
		event = new Event(cursor.getInt(1), cursor.getString(2),
			cursor.getString(3), cursor.getString(4),
			cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8),
			cursor.getString(9));
		event.setPrimaryKey(cursor.getInt(0));
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return event;
    }

    /**
     * Return All Events converting to Array
     * 
     * @return <b>Event[]</b> getAllEventsArray
     */
    public Event[] getAllEventsArray() {
	ArrayList<Event> list = new ArrayList<Event>();
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, null, null, null, null,
		EVENT_START + " asc");
	if (cursor.moveToFirst()) {
	    do {
		Event event = new Event(cursor.getInt(1), cursor.getString(2),
			cursor.getString(3), cursor.getString(4),
			cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8),
			cursor.getString(9));
		event.setPrimaryKey(cursor.getInt(0));
		list.add(event);
	    } while (cursor.moveToNext());
	}
	cursor.close();
	Event[] events = new Event[list.size()];
	list.toArray(events);

	return events;
    }

    /**
     * Return All Event of Today
     * 
     * @param String
     *            today
     * @return <b>Events[]</b> getAllEvents
     */
    public Event[] getAllEvents(String today) {
	ArrayList<Event> list = new ArrayList<Event>();
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, EVENT_START + " > "
		+ today, null, null, null, EVENT_START + " asc");
	if (cursor.moveToFirst()) {
	    do {
		Event event = new Event(cursor.getInt(1), cursor.getString(2),
			cursor.getString(3), cursor.getString(4),
			cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8),
			cursor.getString(9));
		event.setPrimaryKey(cursor.getInt(0));
		list.add(event);
	    } while (cursor.moveToNext());
	}
	cursor.close();
	Event[] events = new Event[list.size()];
	list.toArray(events);

	return events;
    }

    /**
     * Return All Events Between Today And Tomorrow
     * 
     * @param String
     *            today
     * @param String
     *            tomorrow
     * @return <b>Event[]</b> getAllTodaysEvents
     */
    public Event[] getAllTodaysEvents(String today, String tomorrow) {
	ArrayList<Event> list = new ArrayList<Event>();
	String selection = EVENT_START + " BETWEEN " + today + " AND "
		+ tomorrow;
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, selection, null, null,
		null, EVENT_START + " asc");
	if (cursor.moveToFirst()) {
	    do {
		Event event = new Event(cursor.getInt(1), cursor.getString(2),
			cursor.getString(3), cursor.getString(4),
			cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8),
			cursor.getString(9));
		event.setPrimaryKey(cursor.getInt(0));
		list.add(event);
	    } while (cursor.moveToNext());
	}
	cursor.close();
	Event[] events = new Event[list.size()];
	list.toArray(events);

	return events;
    }

    /**
     * Return All Events List By CourseTeacherId of Today
     * 
     * @param String
     *            today
     * @param Int
     *            courseTeacherId
     * @return <b>ArrayList<Event><b/> getAllEventsList
     */
    public ArrayList<Event> getAllEventsList(String today, int courseTeacherId) {
	ArrayList<Event> list = new ArrayList<Event>();
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, EVENT_START + " > "
		+ today + " AND " + COURSE_TEACHER_ID + "=" + courseTeacherId,
		null, null, null, EVENT_START + " asc");
	if (cursor.moveToFirst()) {
	    do {
		Event event = new Event(cursor.getInt(1), cursor.getString(2),
			cursor.getString(3), cursor.getString(4),
			cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8),
			cursor.getString(9));
		event.setPrimaryKey(cursor.getInt(0));
		list.add(event);
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return list;
    }

    /**
     * Return All Events by CourseTeacherId of Today
     * 
     * @param String
     *            today
     * @param Int
     *            courseTeacherId
     * @return <b>Event[]</b> getAllEvents
     */
    public Event[] getAllEvents(String today, int courseTeacherId) {
	ArrayList<Event> list = new ArrayList<Event>();
	Cursor cursor = this.db.query(EVENT_TABLE, new String[] {
		EVENT_PRIMARY_KEY, COURSE_TEACHER_ID, EVENT_START, EVENT_STOP,
		EVENT_ROOM, EVENT_PERSONNEL, EVENT_MOMENT, EVENT_REMARK,
		EVENT_SUMMARY, EVENT_ALARM_OPTION }, EVENT_START + " > "
		+ today + " AND " + COURSE_TEACHER_ID + "=" + courseTeacherId,
		null, null, null, EVENT_START + " asc");
	if (cursor.moveToFirst()) {
	    do {
		Event event = new Event(cursor.getInt(1), cursor.getString(2),
			cursor.getString(3), cursor.getString(4),
			cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8),
			cursor.getString(9));
		event.setPrimaryKey(cursor.getInt(0));
		list.add(event);
	    } while (cursor.moveToNext());
	}
	cursor.close();
	Event[] events = new Event[list.size()];
	list.toArray(events);

	return events;
    }

    /**
     * Insert Alarm
     * 
     * @param Int
     *            courseTeacherId
     * @param Long
     *            eventId
     * @param Long
     *            alarmStartTime
     * @param AlarmOption
     *            alarmOption
     */
    public void insertAlarm(int courseTeacherId, long eventId,
	    long alarmStartTime, AlarmOption alarmOption) {
	ContentValues contentValue = new ContentValues();
	contentValue.put(ALARM_COURSE_TEACHER_ID, courseTeacherId);
	contentValue.put(ALARM_EVENT_ID, eventId);
	contentValue.put(ALARM_START, alarmStartTime);
	contentValue.put(ALARM_OPTION, alarmOption.toString());
	db.insert(ALARM_TABLE, null, contentValue);
    }

    /**
     * Insert list of Alarm by courseTeacherId
     * 
     * @param Int
     *            courseTeacherId
     * @param ArrayList
     *            <MyAlarmList> alarmList
     */
    public void insertAlarm(int courseTeacherId,
	    ArrayList<MyAlarmList> alarmList) {
	deleteAlarmByCourseTeacher(courseTeacherId);
	ContentValues contentValue = new ContentValues();
	for (MyAlarmList alarm : alarmList) {
	    contentValue.put(ALARM_COURSE_TEACHER_ID,
		    alarm.getCourseTeacherId());
	    contentValue.put(ALARM_EVENT_ID, alarm.getEventId());
	    contentValue.put(ALARM_START, alarm.getAlarmStartTimeStamp());
	    contentValue.put(ALARM_OPTION, alarm.getAlarmOption().toString());
	    db.insert(ALARM_TABLE, null, contentValue);
	}
    }

    /**
     * Delete Alarm By Course or Teacher
     * 
     * @param Int
     *            courseTeacherId
     */
    public void deleteAlarmByCourseTeacher(int courseTeacherId) {
	this.db.delete(ALARM_TABLE, ALARM_COURSE_TEACHER_ID + "="
		+ courseTeacherId, null);
    }

    /**
     * Delete Alarm By Event
     * 
     * @param Int
     *            courseTeacherId
     * @param Long
     *            eventPK
     */
    public void deleteAlarmByEvent(int courseTeacherId, long eventPK) {
	this.db.delete(ALARM_TABLE, ALARM_COURSE_TEACHER_ID + "="
		+ courseTeacherId + " AND " + ALARM_EVENT_ID + " = " + eventPK,
		null);
    }

    /**
     * Delete All Alarm By Event's Primary Key
     * 
     * @param Long
     *            eventPK
     */
    public void deleteAlarmByEventPK(long eventPK) {
	this.db.delete(ALARM_TABLE, ALARM_EVENT_ID + " = " + eventPK, null);
    }

    /**
     * Delete All Alarm
     */
    public void deleteAllAlarm() {
	this.db.delete(ALARM_TABLE, null, null);
    }

    /**
     * Return List of Alarm Time by courseTeacherId from current time
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>ArrayList<MyAlarmList></b> getAllAlarmTime
     */
    public ArrayList<MyAlarmList> getAllAlarmTime(int courseTeacherId) {
	long time = System.currentTimeMillis();
	ArrayList<MyAlarmList> alarmContainer = new ArrayList<MyAlarmList>();

	Cursor cursor = this.db.query(ALARM_TABLE, new String[] {
		ALARM_PRIMARY_KEY, ALARM_COURSE_TEACHER_ID, ALARM_EVENT_ID,
		ALARM_START, ALARM_OPTION }, ALARM_START + ">" + time + " AND "
		+ ALARM_COURSE_TEACHER_ID + " = " + courseTeacherId, null,
		null, null, ALARM_START + " ASC");

	if (cursor.moveToFirst()) {
	    do {
		MyAlarmList alarmList = new MyAlarmList(cursor.getInt(1),
			cursor.getInt(2), cursor.getLong(3),
			AlarmOption.ENABLED);
		alarmList.setAlarmPrimaryKey(cursor.getInt(0));
		alarmContainer.add(alarmList);
	    } while (cursor.moveToNext());
	}
	cursor.close();

	return alarmContainer;
    }

    /**
     * Get List of Alarm Time by courseTeacherId
     * 
     * @param Int
     *            courseTeacherId
     * @return <b>ArrayList<MyAlarmList></b> getAllAlarmTimeByCourseId
     */
    public ArrayList<MyAlarmList> getAllAlarmTimeByCourseId(int courseTeacherId) {
	ArrayList<MyAlarmList> alarmContainer = new ArrayList<MyAlarmList>();

	Cursor cursor = this.db.query(ALARM_TABLE, new String[] {
		ALARM_PRIMARY_KEY, ALARM_COURSE_TEACHER_ID, ALARM_EVENT_ID,
		ALARM_START, ALARM_OPTION }, ALARM_COURSE_TEACHER_ID + " = "
		+ courseTeacherId, null, null, null, null);

	AlarmOption option;
	if (cursor.moveToFirst()) {
	    do {
		if (cursor.getString(4).equalsIgnoreCase(
			AlarmOption.ENABLED.toString())) {
		    option = AlarmOption.ENABLED;
		} else if (cursor.getString(4).equalsIgnoreCase(
			AlarmOption.DISABLED.toString())) {
		    option = AlarmOption.DISABLED;
		} else {
		    option = AlarmOption.CUSTOMIZED;
		}

		MyAlarmList alarmList = new MyAlarmList(cursor.getInt(1),
			cursor.getInt(2), cursor.getLong(3), option);
		alarmList.setAlarmPrimaryKey(cursor.getInt(0));
		alarmContainer.add(alarmList);
	    } while (cursor.moveToNext());
	}
	cursor.close();

	return alarmContainer;
    }

    /**
     * Return list of Alarm Time
     * 
     * @return <b>ArrayList<MyAlarmList></b> getAllAlarmTime
     */
    public ArrayList<MyAlarmList> getAllAlarmTime() {
	ArrayList<MyAlarmList> alarmContainer = new ArrayList<MyAlarmList>();

	Cursor cursor = this.db.query(ALARM_TABLE, new String[] {
		ALARM_PRIMARY_KEY, ALARM_COURSE_TEACHER_ID, ALARM_EVENT_ID,
		ALARM_START, ALARM_OPTION }, null, null, null, null, null);

	AlarmOption option;
	if (cursor.moveToFirst()) {
	    do {
		if (cursor.getString(4).equalsIgnoreCase(
			AlarmOption.ENABLED.toString())) {
		    option = AlarmOption.ENABLED;
		} else if (cursor.getString(4).equalsIgnoreCase(
			AlarmOption.DISABLED.toString())) {
		    option = AlarmOption.DISABLED;
		} else {
		    option = AlarmOption.CUSTOMIZED;
		}

		MyAlarmList alarmList = new MyAlarmList(cursor.getInt(1),
			cursor.getInt(2), cursor.getLong(3), option);
		alarmList.setAlarmPrimaryKey(cursor.getInt(0));
		alarmContainer.add(alarmList);
	    } while (cursor.moveToNext());
	}
	cursor.close();

	return alarmContainer;
    }

    /**
     * Return list of Alarm Time By Event's Primary Key
     * 
     * @param Long
     *            eventPK
     * @return <b>ArrayList<MyAlarmList></b> getAlarmByEventPK
     */
    public ArrayList<MyAlarmList> getAlarmByEventPK(long eventPK) {
	ArrayList<MyAlarmList> alarmContainer = new ArrayList<MyAlarmList>();

	Cursor cursor = this.db.query(ALARM_TABLE, new String[] {
		ALARM_PRIMARY_KEY, ALARM_COURSE_TEACHER_ID, ALARM_EVENT_ID,
		ALARM_START, ALARM_OPTION }, ALARM_EVENT_ID + " = " + eventPK,
		null, null, null, null);

	AlarmOption option;
	if (cursor.moveToFirst()) {
	    do {
		if (cursor.getString(4).equalsIgnoreCase(
			AlarmOption.ENABLED.toString())) {
		    option = AlarmOption.ENABLED;
		} else if (cursor.getString(4).equalsIgnoreCase(
			AlarmOption.DISABLED.toString())) {
		    option = AlarmOption.DISABLED;
		} else {
		    option = AlarmOption.CUSTOMIZED;
		}

		MyAlarmList alarmList = new MyAlarmList(cursor.getInt(1),
			cursor.getInt(2), cursor.getLong(3), option);
		alarmList.setAlarmPrimaryKey(cursor.getInt(0));
		alarmContainer.add(alarmList);
	    } while (cursor.moveToNext());
	}
	cursor.close();

	return alarmContainer;
    }

    /**
     * 
     * Create Database Table or Update or Drop Table
     * 
     */
    private static class OpenHelper extends SQLiteOpenHelper {

	OpenHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL("CREATE TABLE " + COURSE_TEACHER_TABLE + "("
		    + COURSE_TEACHER_ID + " INTEGER PRIMARY KEY, "
		    + COURSE_TEACHER_SIGNATURE + " TEXT, "
		    + COURSE_TEACHER_NAME + " TEXT, " + COURSE_TEACHER_COLOR
		    + " INTEGER, " + COURSE_TEACHER_TYPE + " TEXT, "
		    + COURSE_TEACHER_ALARM_OPTION + " TEXT)");

	    db.execSQL("CREATE TABLE " + EVENT_TABLE + "(" + EVENT_PRIMARY_KEY
		    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COURSE_TEACHER_ID
		    + " INTEGER, " + EVENT_START + " INTEGER" + ", "
		    + EVENT_STOP + " INTEGER, " + EVENT_ROOM + " TEXT, "
		    + EVENT_PERSONNEL + " TEXT, " + EVENT_MOMENT + " TEXT, "
		    + EVENT_REMARK + " TEXT, " + EVENT_SUMMARY + " TEXT, "
		    + EVENT_ALARM_OPTION + " TEXT)");

	    db.execSQL("CREATE TABLE " + ALARM_TABLE + "(" + ALARM_PRIMARY_KEY
		    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		    + ALARM_COURSE_TEACHER_ID + " INTEGER, " + ALARM_EVENT_ID
		    + " INTEGER, " + ALARM_START + " LONG, " + ALARM_OPTION
		    + " TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.v(TAG, "Upgrading database from version " + oldVersion + " to "
		    + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + COURSE_TEACHER_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
	    onCreate(db);
	}
    }
}