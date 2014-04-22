/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.schedule
 *
 * @FileName DetailsInformation.java
 * 
 * @FileCreated Oct 21, 2011
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import time.edit.lnu.datatype.CourseOrTeacher;
import time.edit.lnu.datatype.MyAlarmList;
import time.edit.lnu.helper.DataHelper;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * DetailsInformation: used to view details class information
 * 
 */
public class DetailsInformation extends Activity {
    private Bundle extras;
    private DataHelper databaseHelper;
    private SimpleDateFormat sdf;
    private SimpleDateFormat formatter;
    private String titleString;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.details_screen);

	databaseHelper = new DataHelper(DetailsInformation.this);

	sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	formatter = new SimpleDateFormat("EEEE, d MMMM yyyy");

	extras = getIntent().getExtras();

	titleString = "";

	ArrayList<CourseOrTeacher> coursesOrTeacher = databaseHelper
		.getAllCoursesOrTeachers();

	for (CourseOrTeacher c : coursesOrTeacher) {
	    if (extras.getInt("id") == c.getCourseTeacherId()) {
		titleString = c.getCourseTeacherName();
		break;
	    }
	}

	TextView title = (TextView) findViewById(R.id.title);
	title.setText(titleString);

	String start = extras.getString("start");
	String stop = extras.getString("stop");

	TextView time = (TextView) findViewById(R.id.time);
	time.setText("Time: " + start.substring(8, 10) + ":"
		+ start.substring(10, 12) + "-" + stop.substring(8, 10) + ":"
		+ stop.substring(10, 12));

	TextView date = (TextView) findViewById(R.id.date);
	String dateString = "";
	try {
	    dateString = formatter.format(sdf.parse(start));
	    dateString = String.valueOf(
		    Character.toUpperCase(dateString.charAt(0))).concat(
		    dateString.substring(1, dateString.length()));
	} catch (Exception e) {
	}
	date.setText("Date: " + dateString);

	TextView courseId = (TextView) findViewById(R.id.course_id);
	courseId.setText("Course ID: " + extras.getString("courseId"));

	TextView room = (TextView) findViewById(R.id.room);
	room.setText("Room No: " + extras.getString("room"));

	TextView personnel = (TextView) findViewById(R.id.personnel);
	personnel.setText("Personnel: " + extras.getString("personnel"));

	TextView moment = (TextView) findViewById(R.id.moment);
	moment.setText("Moment: " + extras.getString("moment"));

	TextView remark = (TextView) findViewById(R.id.remark);
	remark.setText("Remark: " + extras.getString("remark"));

	TextView courseEvent = (TextView) findViewById(R.id.course_event);
	courseEvent.setText("Course Event: " + extras.getString("courseEvent"));

	TextView color = (TextView) findViewById(R.id.color);
	color.setBackgroundColor(Color.WHITE);

	long eventPK = extras.getLong("eventPK");
	ArrayList<MyAlarmList> alarmList = databaseHelper
		.getAlarmByEventPK(eventPK);
	TextView alarm = (TextView) findViewById(R.id.alarm);

	System.out.println(alarmList.size());
	if (alarmList.size() == 1) {
	    long timeStamp = alarmList.get(0).getAlarmStartTimeStamp();
	    Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(timeStamp);

	    Date dt = cal.getTime();
	    SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    alarm.setText("Alarm: " + smd.format(dt).toString());
	} else {
	    alarm.setText("Alarm: " + "Disabled");
	}

	databaseHelper.close();
    }
}
