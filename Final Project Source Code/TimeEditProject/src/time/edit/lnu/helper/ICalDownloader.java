package time.edit.lnu.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import time.edit.lnu.datatype.Event;

/**
 * ICalDownloader Provided by Linnaeus University downloads ical-files from
 * timeedit.lnu.se and converts ical events to Event objects.
 */
public class ICalDownloader {

    public static final int TIMEOUT = 10000;

    /**
     * Return Events Array by courseId and language
     * 
     * @param Int
     *            courseId
     * @param Int
     *            lang
     * @return <b>Event[]</b> downloadICal
     * @throws Exception
     */
    public static Event[] downloadICal(int courseId, int lang) throws Exception {
	// http://timeedit.lnu.se/4DACTION/iCal_downloadReservations/timeedit.ics?from=1014&to=1022&id1=4561000&branch=1&lang=1
	// http://timeedit.lnu.se/4DACTION/iCal_downloadReservations/timeedit.ics?from=1036&to=1102&id1=13468000&branch=2&lang=1
	InputStream in = null;
	ArrayList<Event> eventList = new ArrayList<Event>();
	boolean event = false;

	int index = 0;

	String urlString = "http://timeedit.lnu.se/4DACTION/iCal_downloadReservations/timeedit.ics?"
		// + "from=" + startWeek + "&to=" + endWeek + "&"
		+ "id1=" + courseId + "&branch=2&lang=" + lang;

	int response = -1;

	URL url = new URL(urlString);
	URLConnection conn = url.openConnection();

	HttpURLConnection httpConn = (HttpURLConnection) conn;

	httpConn.setConnectTimeout(TIMEOUT);
	httpConn.setReadTimeout(TIMEOUT);

	httpConn.setAllowUserInteraction(false);
	httpConn.setInstanceFollowRedirects(true);
	httpConn.setRequestMethod("GET");
	httpConn.connect();

	response = httpConn.getResponseCode();
	if (response == HttpURLConnection.HTTP_OK) {
	    in = httpConn.getInputStream();
	}

	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String res = null;
	String[] events = new String[1000]; // Fix upper size? Not nice!

	while ((res = br.readLine()) != null) {
	    if (res.contains("BEGIN:VEVENT")) {
		event = true;
		events[index] = "";
	    }
	    if (res.contains("END:VEVENT")) {
		event = false;
		index++;
	    }
	    if (event) {
		events[index] += res + "<END>";
	    }
	}
	br.close();

	for (int i = 0; i < index; i++) {
	    eventList.add(Event.createEvent(events[i], courseId));
	}

	Event[] eventArray = new Event[eventList.size()];
	eventList.toArray(eventArray);

	return eventArray;
    }
}