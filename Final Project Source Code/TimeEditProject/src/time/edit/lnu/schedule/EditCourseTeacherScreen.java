/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.schedule
 *
 * @FileName EditCourseScreen.java
 * 
 * @FileCreated Oct 17, 2011
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

import time.edit.lnu.helper.DataHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * EditCourseTeacherScreen: used to edit course information
 * 
 */
public class EditCourseTeacherScreen extends Activity {
    private Bundle extras;
    private DataHelper databaseHelper;
    private EditText nameEntry;
    private Resources resource;
    private int pickedColor;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.edit_course_screen);

	getWindow().setSoftInputMode(
		WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	initComponents();

	Button editButton = (Button) findViewById(R.id.edit_course_button);
	editButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View view) {
		databaseHelper = new DataHelper(EditCourseTeacherScreen.this);
		int id = extras.getInt("id");
		String signature = extras.getString("signature").trim();
		String name = nameEntry.getText().toString().trim();
		String type = extras.getString("type").trim();
		String alarm = extras.getString("alarm");

		databaseHelper.updateCourseOrTeacher(id, signature,
			name.toString(), pickedColor, type, alarm);
		databaseHelper.close();
		startActivity(new Intent(EditCourseTeacherScreen.this,
			Main.class));
		finish();
	    }
	});

	Button cancelButton = (Button) findViewById(R.id.cancel_editing_course_button);
	cancelButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
		finish();
	    }
	});
    }

    @Override
    public void onResume() {
	super.onResume();
    }

    @Override
    public void onStop() {
	super.onStop();
    }

    /**
     * Initialize basic components
     */
    private void initComponents() {
	extras = getIntent().getExtras();

	resource = getResources();

	((TextView) findViewById(R.id.course_screen_name)).setText(resource
		.getString(R.string.course_screen_name));
	((TextView) findViewById(R.id.course_screen_signature))
		.setText(resource.getString(R.string.course_screen_signature));

	nameEntry = (EditText) findViewById(R.id.courseNameEntry);
	nameEntry.setText(extras.getString("name"));

	TextView signatureEntry = (TextView) findViewById(R.id.courseSignatureEntry);
	signatureEntry.setText(extras.getString("signature"));

	pickedColor = extras.getInt("color");
    }
}
