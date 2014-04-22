package time.edit.lnu.schedule;

import time.edit.lnu.helper.DataHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * AddCourseScreen is an activity shown when the user wants to add a course.
 */
public class AddCourseTeacherScreen extends Activity {

    private Bundle extras;
    private DataHelper databaseHelper;
    private EditText nameEntry;
    private Resources resource;
    private int pickedColor;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_course_screen);

	initComponents();

	Button addButton = (Button) findViewById(R.id.add_course_button);
	addButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View view) {
		databaseHelper = new DataHelper(AddCourseTeacherScreen.this);
		int id = extras.getInt("id");
		String signature = extras.getString("signature").trim();
		String name = nameEntry.getText().toString().trim();
		String type = extras.getString("type").trim();
		String alarmOption = extras.getString("alarm");

		if (!databaseHelper.isFoundCourseTeacher(id)) {
		    databaseHelper.insertCourseOrTeacher(id, signature, name,
			    pickedColor, type, alarmOption);
		} else {
		    showMessage(name + " is already in your list");
		}
		databaseHelper.close();
		startActivity(new Intent(AddCourseTeacherScreen.this,
			Main.class));
		finish();
	    }
	});

	Button cancelButton = (Button) findViewById(R.id.cancel_adding_course_button);
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
	pickedColor = Color.WHITE;
	getWindow().setSoftInputMode(
		WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
    }

    /**
     * Show message
     * 
     * @param String
     *            msg
     */
    private void showMessage(String msg) {
	Toast.makeText(AddCourseTeacherScreen.this, msg, Toast.LENGTH_SHORT)
		.show();
    }
}