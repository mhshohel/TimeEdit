/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.services
 *
 * @FileName AlarmMessageReceiver.java
 * 
 * @FileCreated Oct 25, 2011
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

import time.edit.lnu.datatype.AlarmOption;
import time.edit.lnu.helper.DataHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * AlarmMessageReceiver Class to Active or Deactive Alarm
 * 
 */
public class AlarmMessageReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
	this.context = context;
	DataHelper databaseHelper = new DataHelper(this.context);

	Bundle extras = intent.getExtras();
	long eventPK = extras.getLong("eventPK");
	int courseTeacherId = extras.getInt("courseTeacherId");

	if (databaseHelper.isFoundAlarmByEventPK(eventPK)) {
	    if (databaseHelper.isFoundEventByEventPK(eventPK)) {
		databaseHelper.updateEventAlarmOption(eventPK,
			AlarmOption.DISABLED);
		databaseHelper.deleteAlarmByEventPK(eventPK);

		int alarmCount = databaseHelper
			.countEventsByAlarmOption(courseTeacherId);

		if (alarmCount == 0) {
		    databaseHelper.updateCourseOrTeacher(courseTeacherId,
			    AlarmOption.DISABLED);
		}

		databaseHelper.close();
		Intent newIntent = new Intent(context,
			AlarmScheduleDetails.class);
		newIntent.putExtra("eventPK", eventPK);
		newIntent.putExtra("courseTeacherId", courseTeacherId);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent);
	    }
	}
	databaseHelper.close();
    }
}