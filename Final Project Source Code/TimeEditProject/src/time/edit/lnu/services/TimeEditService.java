/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.services
 *
 * @FileName TimeEditService.java
 * 
 * @FileCreated Oct 28, 2011
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import time.edit.lnu.datatype.Event;
import time.edit.lnu.helper.DataHelper;
import time.edit.lnu.helper.GetTimeEditData;
import time.edit.lnu.schedule.R;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

/**
 * TimeEditService: for Widget to get data from Internet or Database
 * 
 */
public class TimeEditService extends Service {
    private SharedPreferences prefs;
    private int autoUpdateTime;

    @Override
    public void onStart(Intent intent, int startId) {
	if (intent == null) {
	    return;
	}

	getPreference();

	if (autoUpdateTime == 0) {
	    GetTimeEditData ged = new GetTimeEditData(getApplicationContext());
	    ged.updateData();
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	String today = sdf.format(new Date()).substring(0, 8) + "000000";

	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH, 1);
	Date tomorrowDate = cal.getTime();
	String tomorrow = sdf.format(tomorrowDate).substring(0, 8) + "000000";

	DataHelper databaseHelper = new DataHelper(getApplicationContext());
	Event[] events = databaseHelper.getAllTodaysEvents(today, tomorrow);
	databaseHelper.close();

	int size = events.length;

	StringBuilder sb = new StringBuilder();
	if (size == 0) {
	    sb.append("You have no classes/meetings today.");
	} else {
	    for (Event event : events) {
		String summary = event.getSummary();
		String[] summarySplit = null;
		summarySplit = summary.split("\n");

		String courseId = "n/a";

		if (summarySplit.length > 2) {
		    courseId = summarySplit[0];
		}

		sb.append(courseId).append(" ").append(event.getCaption())
			.append("\n");
	    }
	}

	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
		.getApplicationContext());

	int[] appWidgetIds = intent
		.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

	SimpleDateFormat topDateFormat = new SimpleDateFormat(
		"EEE dd, MMM, yyyy");

	Bundle extras = intent.getExtras();
	ArrayList<Integer> array = null;
	array = extras.getIntegerArrayList("arrayList");

	if (appWidgetIds.length > 0) {
	    if (array != null) {
		for (int i = 0; i < array.size(); i++) {
		    RemoteViews remoteViews = new RemoteViews(getPackageName(),
			    R.layout.lnu_widget_layout);
		    remoteViews.setTextViewText(R.id.widget_date,
			    topDateFormat.format(new Date()));
		    remoteViews.setTextViewText(R.id.widget_schedule_list,
			    sb.toString());

		    appWidgetManager.updateAppWidget(array.get(i), remoteViews);
		}
	    }
	    stopSelf();
	}

	super.onStart(intent, startId);
    }

    public void onCreate() {
	super.onCreate();
    }

    public void onDestroy() {
	super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
	return null;
    }

    /**
     * Get Preference
     */
    private void getPreference() {
	prefs = PreferenceManager.getDefaultSharedPreferences(this);
	autoUpdateTime = Integer.parseInt(prefs.getString(
		"LnuAutoUpdatePreference", "0"));
    }
}