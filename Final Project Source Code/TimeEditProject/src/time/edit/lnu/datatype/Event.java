/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.datatype
 *
 * @FileName Event.java
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
package time.edit.lnu.datatype;

/**
 * Event is a representation of an event, take schedule from TimeEdit.
 */
public class Event implements Comparable<Event> {
    private long primaryKey;
    private int courseTeacherId;
    private String start;
    private String stop;
    private String room;
    private String personnel;
    private String moment;
    private String remark;
    private String summary;
    private String alarmOption;

    /**
     * Constructor of Event Class
     * 
     * @param Int
     *            courseTeacherId
     * @param String
     *            start
     * @param String
     *            stop
     * @param String
     *            room
     * @param String
     *            personnel
     * @param String
     *            moment
     * @param String
     *            remark
     * @param String
     *            summary
     * @param String
     *            alarmOption
     */
    public Event(int courseTeacherId, String start, String stop, String room,
	    String personnel, String moment, String remark, String summary,
	    String alarmOption) {
	this.setCourseTeacherId(courseTeacherId);
	this.setStart(start);
	this.setStop(stop);
	this.setRoom(room);
	this.setPersonnel(personnel);
	this.setMoment(moment);
	this.setRemark(remark);
	this.setSummary(summary);
	this.setAlarmOption(alarmOption);
    }

    /**
     * Return nice look of Start and Stop time with room number
     * 
     * @return String
     */
    public String getCaption() {
	return getStart().substring(8, 10) + ":" + getStart().substring(10, 12)
		+ "-" + getStop().substring(8, 10) + ":"
		+ getStop().substring(10, 12) + ", " + getRoom();
    }

    /**
     * Return an Event containing all the information of an Event
     * 
     * @param String
     *            text
     * @param Int
     *            courseId
     * @return Event
     */
    public static Event createEvent(String text, int courseId) {
	String start = text.substring(
		text.indexOf(":", text.indexOf("DTSTART")) + 1,
		text.indexOf("<END>", text.indexOf("DTSTART")));
	String stop = text.substring(
		text.indexOf(":", text.indexOf("DTEND")) + 1,
		text.indexOf("<END>", text.indexOf("DTEND")));
	start = start.replace("T", "");
	stop = stop.replace("T", "");

	String room;
	String summary;
	if (text.contains("LOCATION")) {
	    room = text.substring(text.indexOf("LOCATION") + 9,
		    text.indexOf("<END>", text.indexOf("LOCATION")));
	    if (room.substring(room.length() - 2, room.length()).equals("_V"))
		room = room.substring(0, room.length() - 2) + " (Växjö)";
	    else if (room.substring(room.length() - 2, room.length()).equals(
		    "_K"))
		room = room.substring(0, room.length() - 2) + " (Kalmar)";
	    else if (room.substring(room.length() - 1, room.length()).equals(
		    "V"))
		room = room.substring(0, room.length() - 1) + " (Växjö)";
	    else if (room.substring(room.length() - 1, room.length()).equals(
		    "K"))
		room = room.substring(0, room.length() - 1) + " (Kalmar)";

	    summary = text.substring(text.indexOf("SUMMARY") + 8,
		    text.lastIndexOf("<END>", text.indexOf("LOCATION")));

	} else {
	    room = "n/a";
	    summary = text.substring(text.indexOf("SUMMARY") + 8,
		    text.lastIndexOf("<END>"));
	}

	summary = summary.replace("<END> ", "");
	summary = summary.replace("\\n", "\n");
	summary = summary.replace("\\", "");

	String remark = "";
	if (summary.contains("#")) {
	    String[] remarkSplit = summary.split("#");
	    summary = remarkSplit[0];
	    remark = remarkSplit[1];
	}

	remark = remark.replace("\n", "");
	String[] split = summary.split(System.getProperty("line.separator"));

	String prsonnel = "";
	String moment = "";
	if (split.length > 1) {
	    prsonnel = split[split.length - 2];
	}
	if (split.length > 0) {
	    moment = split[split.length - 1];
	}

	return new Event(courseId, start, stop, room, prsonnel, moment, remark,
		summary, AlarmOption.DISABLED.toString());
    }

    /**
     * @return <b>Int</b> the courseId
     */
    public int getCourseTeacherId() {
	return courseTeacherId;
    }

    /**
     * @param int courseId the courseId to set
     */
    private void setCourseTeacherId(int courseTeacherId) {
	this.courseTeacherId = courseTeacherId;
    }

    /**
     * @return <b>String</b> the start
     */
    public String getStart() {
	return start;
    }

    /**
     * @param String
     *            start the start to set
     */
    private void setStart(String start) {
	this.start = start;
    }

    /**
     * @return <b>String</b> the stop
     */
    public String getStop() {
	return stop;
    }

    /**
     * @param String
     *            stop the stop to set
     */
    private void setStop(String stop) {
	this.stop = stop;
    }

    /**
     * @return <b>String</b> the room
     */
    public String getRoom() {
	return room;
    }

    /**
     * @param String
     *            room the room to set
     */
    private void setRoom(String room) {
	this.room = room;
    }

    /**
     * @return <b>String</b> the personnel
     */
    public String getPersonnel() {
	return personnel;
    }

    /**
     * @param String
     *            personnel the personnel to set
     */
    private void setPersonnel(String personnel) {
	this.personnel = personnel;
    }

    /**
     * @return <b>String</b> the moment
     */
    public String getMoment() {
	return moment;
    }

    /**
     * @param String
     *            moment the moment to set
     */
    private void setMoment(String moment) {
	this.moment = moment;
    }

    /**
     * @return <b>String</b> the remark
     */
    public String getRemark() {
	return remark;
    }

    /**
     * @param String
     *            remark the remark to set
     */
    private void setRemark(String remark) {
	this.remark = remark;
    }

    /**
     * @return <b>String</b> the summary
     */
    public String getSummary() {
	return summary;
    }

    /**
     * @param String
     *            summary the summary to set
     */
    private void setSummary(String summary) {
	this.summary = summary;
    }

    /**
     * @return <b>Long</b> the primaryKey
     */
    public long getPrimaryKey() {
	return primaryKey;
    }

    /**
     * @param Long
     *            primaryKey the primaryKey to set
     */
    public void setPrimaryKey(long primaryKey) {
	this.primaryKey = primaryKey;
    }

    /**
     * Compare Events to sort ascending order
     */
    @Override
    public int compareTo(Event event) {
	return getStart().compareTo(event.getStart());
    }

    /**
     * Compare two Events
     * 
     * @param Object
     *            obeject
     */
    @Override
    public boolean equals(Object object) {
	if (object instanceof Event) {
	    Event event = (Event) object;

	    if (this.courseTeacherId == event.courseTeacherId
		    && this.start.equalsIgnoreCase(event.start)
		    && this.room.equalsIgnoreCase(event.room)
		    && this.personnel.equalsIgnoreCase(event.personnel)
		    && this.moment.equalsIgnoreCase(event.moment)
		    && this.remark.equalsIgnoreCase(event.remark)) {

		return true;
	    }
	}

	return false;
    }

    /**
     * @return <b>String</b> the alarmOption
     */
    public String getAlarmOption() {
	return alarmOption;
    }

    /**
     * @param String
     *            alarmOption the alarmOption to set
     */
    public void setAlarmOption(String alarmOption) {
	this.alarmOption = alarmOption;
    }
}