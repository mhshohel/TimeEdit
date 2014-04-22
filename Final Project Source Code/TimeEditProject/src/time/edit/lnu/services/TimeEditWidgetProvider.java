/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.services
 *
 * @FileName TimeEditWidgetProvider.java
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import time.edit.lnu.schedule.R;
import time.edit.lnu.splash.SplashScreen;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Time Edit Widget provider Class
 * 
 */
public class TimeEditWidgetProvider extends AppWidgetProvider {
    Context context;

    private static ArrayList<Integer> intArrayList = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	    int[] appWidgetIds) {

	this.context = context;
	if (intArrayList == null) {
	    intArrayList = new ArrayList<Integer>();
	}

	Bundle bundle = new Bundle();

	Intent intent = new Intent(context.getApplicationContext(),
		TimeEditService.class);
	intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
	intent.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);
	intArrayList.add(appWidgetIds[0]);

	bundle.putIntegerArrayList("arrayList", intArrayList);

	intent.putExtras(bundle);

	PendingIntent pendingIntentUpdate = PendingIntent.getService(context,
		0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	// ---------------Activity---------------
	for (int i = 0; i < appWidgetIds.length; i++) {
	    Intent intentTimeEditActivity = new Intent(
		    context.getApplicationContext(), SplashScreen.class);
	    RemoteViews remoteViewsActivity = clickToViewFullReport(context,
		    intentTimeEditActivity, appWidgetIds[i]);

	    appWidgetManager.updateAppWidget(appWidgetIds[i],
		    remoteViewsActivity);
	}
	context.startService(intent);

	// ---------------Alarm Update TimeEdit---------------
	int everyDayUpdateFromNetHours = 2; // Every 2 hour update
	long startTime;
	long interval = everyDayUpdateFromNetHours + 3600000; // 3600000 = 1
	// hour

	Date date = new Date();
	int dateVal = date.getHours();

	dateVal = dateVal / everyDayUpdateFromNetHours;
	dateVal = ((dateVal * everyDayUpdateFromNetHours) + everyDayUpdateFromNetHours) * 60;

	int hour = date.getHours();
	int min = date.getMinutes();
	min = ((hour) * 60) + min;
	dateVal = dateVal - min + 3;// Update at 00:03, 01:03, 02:03...

	Calendar cals = Calendar.getInstance();
	cals.add(Calendar.MINUTE, dateVal);
	startTime = cals.getTimeInMillis();

	AlarmManager am = (AlarmManager) context
		.getSystemService(Context.ALARM_SERVICE);
	am.setRepeating(AlarmManager.RTC, startTime, interval,
		pendingIntentUpdate);

	super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * Return RemoteViews for Click to Open Main Activity
     * 
     * @param Context
     *            context
     * @param Intent
     *            intent
     * @return RemoteViews clickToViewFullReport
     */
    private RemoteViews clickToViewFullReport(Context context, Intent intent,
	    int id) {
	RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
		R.layout.lnu_widget_layout);
	PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
		intent, Intent.FLAG_ACTIVITY_NEW_TASK);

	remoteViews
		.setOnClickPendingIntent(R.id.widget_lnu_icon, pendingIntent);

	return remoteViews;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
	final int nWidgets = appWidgetIds.length;

	for (int i = 0; i < nWidgets; i++) {
	    int appWidgetId = appWidgetIds[i];

	    for (int j = 0; j < intArrayList.size(); j++) {
		if (intArrayList.get(j) == appWidgetId) {
		    intArrayList.remove(j);
		    break;
		}
	    }

	    if (intArrayList.size() == 0) {
		Intent intent = new Intent(context.getApplicationContext(),
			TimeEditService.class);
		PendingIntent pendingIntentUpdateEveryHour = PendingIntent
			.getService(context.getApplicationContext(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) context
			.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pendingIntentUpdateEveryHour);
		context.stopService(intent);
	    }
	}

	super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
	context.stopService(new Intent(context, TimeEditService.class));
	super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
	super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
	super.onReceive(context, intent);
    }
}