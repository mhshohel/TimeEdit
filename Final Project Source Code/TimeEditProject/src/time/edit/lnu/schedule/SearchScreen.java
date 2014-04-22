/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.schedule
 *
 * @FileName SearchScreen.java
 * 
 * @FileCreated Oct 22, 2011
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

import java.util.ArrayList;
import java.util.Collections;

import time.edit.lnu.datatype.AlarmOption;
import time.edit.lnu.datatype.CourseOrTeacher;
import time.edit.lnu.helper.TimeEdit;
import time.edit.lnu.helper.TimeEdit.ObjectSearchResponse;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * SearchScreen lets the user search for courses or teachers.
 */
public class SearchScreen extends Activity {

    private Button searchButton;
    private EditText edit;
    private ArrayAdapter<CourseOrTeacher> adapter;
    private ArrayList<CourseOrTeacher> courseOrTeacherList;
    private ListView listView;
    private Spinner typeSpinner;
    private Resources resources;
    private ProgressDialog progressDialog;
    private SearchThread searchThread;
    private String type;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.search_screen);

	initComponents();

	listView.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		Intent intent = new Intent(SearchScreen.this,
			AddCourseTeacherScreen.class);

		int ids = courseOrTeacherList.get(position)
			.getCourseTeacherId();
		String signature = courseOrTeacherList.get(position)
			.getCourseTeacherSignature();
		String name = courseOrTeacherList.get(position)
			.getCourseTeacherName();
		int color = courseOrTeacherList.get(position)
			.getCourseTeacherColor();
		String type = courseOrTeacherList.get(position).getType();
		String alarmOption = courseOrTeacherList.get(position)
			.getAlarmOption();

		intent.putExtra("id", ids);
		intent.putExtra("signature", signature);
		intent.putExtra("name", name);
		intent.putExtra("color", color);
		intent.putExtra("type", type);
		intent.putExtra("alarm", alarmOption);
		searchThread = null;
		startActivity(intent);
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
	courseOrTeacherList = new ArrayList<CourseOrTeacher>();
	resources = getResources();

	listView = (ListView) findViewById(R.id.resultlist);
	adapter = new ArrayAdapter<CourseOrTeacher>(SearchScreen.this,
		R.layout.list_item, courseOrTeacherList);
	listView.setAdapter(adapter);
	listView.setTextFilterEnabled(true);

	edit = (EditText) findViewById(R.id.entry);

	String[] types = { resources.getString(R.string.search_spinner_course),
		resources.getString(R.string.search_spinner_teacher) };
	typeSpinner = (Spinner) findViewById(R.id.type_spinner);
	ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, types);
	typeAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	typeSpinner.setAdapter(typeAdapter);

	searchButton = (Button) findViewById(R.id.search_screen_button);
	searchButton.setText(resources.getString(R.string.search_button_text));
	searchButton.setOnClickListener(new SearchClickListener());

	getWindow().setSoftInputMode(
		WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Action of Search Button
     */
    private class SearchClickListener implements OnClickListener {
	@Override
	public void onClick(View view) {
	    String text = edit.getText().toString().trim();
	    if (!text.equals("")) {
		progressDialog = ProgressDialog.show(view.getContext(), null,
			resources.getString(R.string.search_message), true,
			false);

		searchThread = new SearchThread(handler);
		searchThread.start();
	    }
	}
    }

    /**
     * Thread for Search Action
     * 
     */
    private class SearchThread extends Thread {
	Handler handler;

	SearchThread(Handler handler) {
	    this.handler = handler;
	}

	public synchronized void run() {
	    try {
		String SessionId = TimeEdit.TE_Session_open("iPhone", "schema");
		// typeSpinner.getSelectedItemPosition() = 0 - Course
		// and 1 - Teacher
		ObjectSearchResponse osr;
		if (typeSpinner.getSelectedItemPosition() == 0) {
		    osr = TimeEdit.TE_Object_search(SessionId, 2, "", edit
			    .getText().toString());
		} else {
		    osr = TimeEdit.TE_Object_searchExtended(SessionId, 6, edit
			    .getText().toString());
		}

		TimeEdit.TE_Session_close(SessionId);
		courseOrTeacherList.clear();
		if (osr.ObjectIds.length != 0) {
		    for (int i = 0; i < osr.ObjectIds.length; i++) {
			String courseName;
			if (typeSpinner.getSelectedItemPosition() == 0) {
			    type = typeSpinner.getSelectedItem().toString();
			    courseName = osr.ObjectNames[i];
			} else {
			    type = typeSpinner.getSelectedItem().toString();
			    courseName = osr.ObjectNames[i] + " ("
				    + osr.ObjectSignatures[i] + ")";
			}

			CourseOrTeacher courseOrTeacher = new CourseOrTeacher(
				osr.ObjectIds[i], osr.ObjectSignatures[i],
				courseName, Color.WHITE, type,
				AlarmOption.DISABLED.toString());
			courseOrTeacherList.add(courseOrTeacher);
		    }
		    Collections.sort(courseOrTeacherList);
		    handler.sendMessage(handler.obtainMessage(1));
		} else {
		    handler.sendMessage(handler.obtainMessage(0));
		}
	    } catch (Exception e) {
		handler.sendMessage(handler.obtainMessage(-1));
	    }
	}
    }

    /**
     * Handler for Search Thread
     */
    private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    if (msg.what == 1) {
		adapter.notifyDataSetChanged();
		if (listView == null) {
		    listView = (ListView) findViewById(R.id.resultlist);
		}
		if (adapter == null) {
		    adapter = new ArrayAdapter<CourseOrTeacher>(
			    SearchScreen.this, R.layout.list_item,
			    courseOrTeacherList);
		}
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
	    } else if (msg.what == 0) {
		String message = resources
			.getString(R.string.search_notfound_message);
		String title = resources
			.getString(R.string.search_notfound_title);
		showAlert(title, message);
	    } else {
		String title = resources
			.getString(R.string.search_failed_title);
		String message = resources
			.getString(R.string.search_failed_message);
		showAlert(title, message);
	    }
	    progressDialog.dismiss();
	}
    };

    /**
     * Show Alert Dialog
     * 
     * @param title
     *            String
     * @param message
     *            String
     */
    private void showAlert(String title, String message) {
	AlertDialog.Builder builder = new AlertDialog.Builder(SearchScreen.this);
	builder.setIcon(R.drawable.warning);
	builder.setTitle(title);
	builder.setMessage(message)
		.setCancelable(true)
		.setPositiveButton(R.string.dialog_button_ok,
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			    }
			});
	AlertDialog alert = builder.create();
	alert.show();
    }
}