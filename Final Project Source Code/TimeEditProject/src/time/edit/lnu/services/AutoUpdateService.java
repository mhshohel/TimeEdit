/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.services
 *
 * @FileName AutoUpdateService.java
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
package time.edit.lnu.services;

import time.edit.lnu.helper.GetTimeEditData;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * AutoUpdateService: to Update data according to users choice
 * 
 */
public class AutoUpdateService extends Service {

    @Override
    public void onStart(Intent intent, int startId) {
	GetTimeEditData ged = new GetTimeEditData(getApplicationContext());
	ged.updateData();
    }

    /**
     * OnCreate
     */
    public void onCreate() {
	super.onCreate();
    }

    /**
     * onDestroy
     */
    public void onDestroy() {
	super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
	return null;
    }
}