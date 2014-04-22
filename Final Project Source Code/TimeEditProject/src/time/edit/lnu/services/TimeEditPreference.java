/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.services
 *
 * @FileName TimeEditPreference.java
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

import time.edit.lnu.schedule.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * TimeEditPreference for Preference Screen
 * 
 */
public class TimeEditPreference extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	addPreferencesFromResource(R.xml.timeedit_preference);
    }
}