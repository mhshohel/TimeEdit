/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.services
 *
 * @FileName AlarmScheduleDetails.java
 * 
 * @FileCreated Oct 27, 2011
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
package time.edit.lnu.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import time.edit.lnu.datatype.CourseOrTeacher;
import time.edit.lnu.datatype.Event;
import time.edit.lnu.helper.DataHelper;
import time.edit.lnu.schedule.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * AlarmScheduleDetails Class to Show Details Information of Alarm Task
 * 
 */
public class AlarmScheduleDetails extends Activity {
    private Bundle extras;
    private DataHelper databaseHelper;
    private SimpleDateFormat sdf;
    private SimpleDateFormat formatter;
    private String titleString;
    private MediaPlayer mediaPlayer;
    private Resources resource;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.alarm_details_screen);
	resource = getResources();
	databaseHelper = new DataHelper(AlarmScheduleDetails.this);

	sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	formatter = new SimpleDateFormat("EEEE, d MMMM yyyy");

	extras = getIntent().getExtras();

	titleString = "";
	int courseTeacherId = extras.getInt("courseTeacherId");
	long eventPK = extras.getLong("eventPK");

	ArrayList<CourseOrTeacher> coursesOrTeacher = databaseHelper
		.getAllCoursesOrTeachers();
	Event event = databaseHelper.getEventByEventPK(eventPK);
	CourseOrTeacher courseTeacher = null;

	for (CourseOrTeacher c : coursesOrTeacher) {
	    if (courseTeacherId == c.getCourseTeacherId()) {
		courseTeacher = c;
		titleString = c.getCourseTeacherName();
		break;
	    }
	}

	TextView title = (TextView) findViewById(R.id.title);
	title.setText(titleString);

	String start = event.getStart();
	String stop = event.getStop();

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
	    Log.w("Date Format", "Exception in Alarm Schedule Details");
	}
	date.setText("Date: " + dateString);

	TextView courseId = (TextView) findViewById(R.id.course_id);
	courseId.setText("Course ID: " + courseTeacher.getCourseTeacherId());

	TextView room = (TextView) findViewById(R.id.room);
	room.setText("Room No: "
		+ (!event.getRoom().trim().equalsIgnoreCase("") ? event
			.getRoom() : "n/a"));

	TextView personnel = (TextView) findViewById(R.id.personnel);
	personnel.setText("Personnel: "
		+ (!event.getPersonnel().trim().equalsIgnoreCase("") ? event
			.getPersonnel() : "n/a"));

	TextView moment = (TextView) findViewById(R.id.moment);
	moment.setText("Moment: "
		+ (!event.getMoment().trim().equalsIgnoreCase("") ? event
			.getMoment() : "n/a"));

	TextView remark = (TextView) findViewById(R.id.remark);
	remark.setText("Remark: "
		+ (!event.getRemark().trim().equalsIgnoreCase("") ? event
			.getRemark() : "n/a"));

	ImageButton closeButton = (ImageButton) findViewById(R.id.close_button);
	closeButton.setBackgroundColor(Color.TRANSPARENT);

	closeButton.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		finish();
	    }
	});

	databaseHelper.close();

	playAlarm();

	showAlert(resource.getString(R.string.alarm_wake_up_title),
		resource.getString(R.string.alarm_wake_up_message));
    }

    /**
     * Play Alarm, Default Alarm, Alert, Ringtone
     */
    private void playAlarm() {
	Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	if (alert == null) {
	    alert = RingtoneManager
		    .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
	    if (alert == null) {
		alert = RingtoneManager
			.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	    }
	}

	mediaPlayer = new MediaPlayer();
	try {
	    mediaPlayer.setDataSource(this, alert);
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (SecurityException e) {
	    e.printStackTrace();
	} catch (IllegalStateException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
	    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
	    mediaPlayer.setLooping(true);
	    try {
		mediaPlayer.prepare();
	    } catch (IllegalStateException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    mediaPlayer.start();
	}
    }

    /**
     * Stop Alarm
     */
    private void stopAlarm() {
	mediaPlayer.stop();
    }

    /**
     * Show Alert Dialog
     * 
     * @param title
     *            String
     * @param message
     *            String
     */
    private void showAlert(String title, String message) {
	AlertDialog.Builder builder = new AlertDialog.Builder(
		AlarmScheduleDetails.this);
	builder.setIcon(R.drawable.warning);
	builder.setTitle(title);
	builder.setMessage(message)
		.setCancelable(true)
		.setPositiveButton(R.string.dialog_button_ok,
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				stopAlarm();
				dialog.dismiss();
			    }
			});
	AlertDialog alert = builder.create();
	alert.show();
    }
}