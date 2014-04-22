/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.splash
 *
 * @FileName SplashScreen.java
 * 
 * @FileCreated Oct 30, 2011
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
package time.edit.lnu.splash;

import time.edit.lnu.schedule.Main;
import time.edit.lnu.schedule.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

/**
 * SplashScreen Class: Got Help from Internet named Splash Screen Basic Tutorial
 * and Modified code to Use in Time Edit Application
 * 
 */
public class SplashScreen extends Activity {
    protected boolean active = true;
    protected int splashTime = 5000;
    private Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.splash_screen_layout);

	splashTread = new Thread() {
	    public void stopThread() {
		splashTread = null;
	    }

	    @Override
	    public void run() {
		try {
		    int waited = 0;
		    while (active && (waited < splashTime)) {
			sleep(100);
			if (active) {
			    waited += 100;
			}
		    }
		} catch (InterruptedException e) {
		    Log.w("Splash Screen", "Splash Screen Thread Problem");
		} finally {
		    finish();
		    startActivity(new Intent(SplashScreen.this, Main.class));
		    stopThread();
		}
	    }
	};
	splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    active = false;
	}
	return true;
    }
}