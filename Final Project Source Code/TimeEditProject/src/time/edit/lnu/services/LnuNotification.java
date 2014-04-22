/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.services
 *
 * @FileName LnuNotification.java
 * 
 * @FileCreated Oct 19, 2011
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

import time.edit.lnu.schedule.R;
import time.edit.lnu.schedule.Schedule;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * LnuNotification: this class created to collect and format notification text
 * to notify user
 * 
 */
public class LnuNotification extends Activity {
    private static String oldDataList = "";
    private static String newDataList = "";

    private ArrayList<String> myList;
    private CoursesOrTeacherListAdapter adapter;
    private TextView notificatinHeader;
    private TextView notificationBody;
    private ListView listView;
    private Resources resource;
    private final int CLOSE_APPLICATION = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.custom_notification);
	resource = getResources();

	((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
		.cancel(Schedule.LNU_NOTIFICATION_ID);

	myList = new ArrayList<String>();

	myList.add(oldDataList);
	myList.add(newDataList);

	listView = (ListView) findViewById(R.id.notification_list_view);
	adapter = new CoursesOrTeacherListAdapter(this, myList);
	listView.setAdapter(adapter);

    }

    /**
     * 
     * Show list of Notification
     * 
     */
    public class CoursesOrTeacherListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	ArrayList<String> myList;

	public CoursesOrTeacherListAdapter(Context context,
		ArrayList<String> myList) {
	    this.myList = myList;
	    inflater = (LayoutInflater) context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    if (convertView == null) {
		convertView = inflater.inflate(R.layout.notification_list_item,
			null);

		notificatinHeader = (TextView) convertView
			.findViewById(R.id.notificatin_header);
		notificationBody = (TextView) convertView
			.findViewById(R.id.notification_body);
	    }

	    if (position == 0) {
		notificatinHeader.setText(resource
			.getString(R.string.notification_previous_class));
	    } else {
		notificatinHeader.setText(resource
			.getString(R.string.notification_updated_class));
	    }

	    notificationBody.setText(myList.get(position).toString());

	    return convertView;
	}

	@Override
	public int getCount() {
	    return myList.size();
	}

	@Override
	public Object getItem(int arg0) {
	    return null;
	}

	@Override
	public long getItemId(int arg0) {
	    return 0;
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	boolean result = super.onCreateOptionsMenu(menu);
	MenuItem m1 = menu.add(Menu.NONE, CLOSE_APPLICATION, Menu.NONE,
		getResources().getString(R.string.main_close_application));
	m1.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
	return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case CLOSE_APPLICATION:
	    finish();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
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
     * Set Notification List
     * 
     * @param String
     *            oldData
     * @param String
     *            newData
     */
    public static void setNotificationList(String oldData, String newData) {
	oldDataList = "";
	oldDataList = oldData;
	newDataList = "";
	newDataList = newData;
    }
}