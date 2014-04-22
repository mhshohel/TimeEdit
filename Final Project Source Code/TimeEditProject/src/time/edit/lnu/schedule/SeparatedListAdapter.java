/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.schedule
 *
 * @FileName SeparatedListAdapter.java
 * 
 * @FileCreated Oct 16, 2011
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import time.edit.lnu.datatype.AlarmOption;
import time.edit.lnu.helper.DataHelper;
import time.edit.lnu.services.AlarmMessageReceiver;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * SeparatedListAdapter: to get customized events list
 * 
 */
public class SeparatedListAdapter extends BaseAdapter {
    private ArrayList<String> flags;
    private ArrayList<Object> objects;
    private LayoutInflater inflater;
    private long currentTimeStamp = System.currentTimeMillis();
    private Context context;
    private DataHelper databaseHelper;

    /**
     * Constructor of SeparatedListAdapter
     * 
     * @param Context
     *            context
     * @param ArrayList
     *            <String> flags
     * @param ArrayList
     *            <Object> objects
     */
    public SeparatedListAdapter(Context context, ArrayList<String> flags,
	    ArrayList<Object> objects) {
	this.context = context;
	this.flags = flags;
	this.objects = objects;
	inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
	return objects.size();
    }

    @Override
    public Object getItem(int position) {
	return null;
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    /**
     * Return position of List View
     * 
     * @param Int
     *            position
     * @return <b>Int</b> getPosition
     */
    public int getPosition(int position) {
	if (flags.get(position).equals("EVENT")) {
	    int realPosition = position;
	    for (int i = 0; i < position; i++)
		if (flags.get(i).equals("HEADER"))
		    realPosition--;
	    return realPosition;
	} else
	    return -1;
    }

    @Override
    public int getItemViewType(int position) {
	if (flags.get(position).equals("HEADER"))
	    return 0;
	else
	    return 1;
    }

    @Override
    public int getViewTypeCount() {
	return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	// Header
	if (flags.get(position).equals("HEADER")) {
	    if (convertView == null) {
		convertView = inflater.inflate(R.layout.schedule_list_header,
			null);
		TextView tv = (TextView) convertView;
		tv.setText(objects.get(position).toString());
		tv.setTextColor(Color.BLACK);
		convertView.setTag(tv);
	    } else {
		((TextView) convertView.getTag()).setText(objects.get(position)
			.toString());
	    }
	    return convertView;
	}
	// Event
	else if (flags.get(position).equals("EVENT")) {
	    if (convertView == null) {
		convertView = inflater.inflate(R.layout.schedule_list_item,
			null);

		convertView = inflater.inflate(R.layout.schedule_list_item,
			null);
		EventHolder eh = new EventHolder();
		eh.sideline = (View) convertView.findViewById(R.id.sideline);
		eh.list_complex_title = (TextView) convertView
			.findViewById(R.id.list_complex_title);
		eh.list_complex_title.setTextColor(Color.BLACK);
		eh.list_complex_caption = (TextView) convertView
			.findViewById(R.id.list_complex_caption);
		eh.list_complex_caption.setTextColor(Color.BLACK);
		eh.alarmImage = (ImageView) convertView
			.findViewById(R.id.schedule_list_alarm_icon);

		convertView.setTag(eh);
	    }

	    EventHolder eh = (EventHolder) convertView.getTag();
	    Object[] os = (Object[]) objects.get(position);
	    eh.list_complex_title.setText((String) os[0]);
	    eh.sideline.setBackgroundColor(Color.WHITE);
	    eh.list_complex_caption.setText((String) os[2]);

	    String alarmOption = (String) os[3];
	    long eventPK = (Long) os[4];
	    String alarmStartTime = (String) os[5];
	    long savedAlarmTimeStamp = getTimeStamp(alarmStartTime);

	    databaseHelper = new DataHelper(context);
	    if (alarmOption.equalsIgnoreCase(AlarmOption.ENABLED.toString())) {
		if (databaseHelper.isFoundAlarmByEventPK(eventPK) == true) {
		    if (savedAlarmTimeStamp > currentTimeStamp) {
			eh.alarmImage
				.setImageResource(R.drawable.event_alarm_activate);
		    } else {
			eh.alarmImage
				.setImageResource(R.drawable.event_alarm_deactivate);
			databaseHelper.updateEventAlarmOption(eventPK,
				AlarmOption.DISABLED);
			databaseHelper.deleteAlarmByEventPK(eventPK);
			alarmDeActive(eventPK);
		    }
		} else {
		    eh.alarmImage
			    .setImageResource(R.drawable.event_alarm_deactivate);
		    databaseHelper.updateEventAlarmOption(eventPK,
			    AlarmOption.DISABLED);
		}
	    } else if (alarmOption.equalsIgnoreCase(AlarmOption.DISABLED
		    .toString())) {
		eh.alarmImage
			.setImageResource(R.drawable.event_alarm_deactivate);
	    }
	    databaseHelper.close();

	    return convertView;
	}
	return convertView;
    }

    /**
     * 
     * Event Holder
     * 
     */
    private static class EventHolder {
	View sideline;
	TextView list_complex_title;
	TextView list_complex_caption;
	ImageView alarmImage;
    }

    /**
     * Return TimeStamp of a Date
     * 
     * @param String
     *            date
     * @return <b>Long</b> getTimeStamp
     */
    private long getTimeStamp(String date) {
	long timeStamp = 0;
	try {
	    timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").parse(date)
		    .getTime();
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	return timeStamp;
    }

    /**
     * Alarm Deactivate by Event's Primary Key
     * 
     * @param Long
     *            eventPK
     */
    private void alarmDeActive(long eventPK) {
	int id = (int) eventPK;
	Intent intent = new Intent(context, AlarmMessageReceiver.class);
	PendingIntent sender = PendingIntent.getBroadcast(context, id, intent,
		0);

	sender.cancel();
	AlarmManager am = (AlarmManager) context
		.getSystemService(Context.ALARM_SERVICE);
	am.cancel(sender);
    }
}