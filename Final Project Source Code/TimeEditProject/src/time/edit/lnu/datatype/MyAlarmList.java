/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.datatype
 *
 * @FileName MyAlarmList.java
 * 
 * @FileCreated Oct 25, 2011
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
 * Take Alarm List by CourseOrTeacher Id, Event Id and Alarm Options
 */
public class MyAlarmList {
    private int alarmPrimaryKey;
    private int courseTeacherId;
    private long eventId;
    private long alarmStartTimeStamp;
    private AlarmOption alarmOption;

    /**
     * Constructor of MyAlarmList
     * 
     * @param Int
     *            courseTeacherId
     * @param Long
     *            eventId
     * @param Long
     *            alarmStartTimeStamp
     * @param AlarmOption
     *            alarmOption
     */
    public MyAlarmList(int courseTeacherId, long eventId,
	    long alarmStartTimeStamp, AlarmOption alarmOption) {
	this.setCourseTeacherId(courseTeacherId);
	this.setEventId(eventId);
	this.setAlarmStartTime(alarmStartTimeStamp);
	this.setAlarmOption(alarmOption);
    }

    /**
     * @return <b>Int</b> the courseId
     */
    public int getCourseTeacherId() {
	return courseTeacherId;
    }

    /**
     * @param Int
     *            courseId the courseId to set
     */
    public void setCourseTeacherId(int courseId) {
	this.courseTeacherId = courseId;
    }

    /**
     * @return <b>Long</b> the eventId
     */
    public long getEventId() {
	return eventId;
    }

    /**
     * @param Long
     *            eventId the eventId to set
     */
    public void setEventId(long eventId) {
	this.eventId = eventId;
    }

    /**
     * @return <b>Long</b> the alarmStartTime
     */
    public long getAlarmStartTimeStamp() {
	return alarmStartTimeStamp;
    }

    /**
     * @param Long
     *            alarmStartTime the alarmStartTime to set
     */
    public void setAlarmStartTime(long alarmStartTimeStamp) {
	this.alarmStartTimeStamp = alarmStartTimeStamp;
    }

    /**
     * @return <b>AlarmOption</b> the alarmOption
     */
    public AlarmOption getAlarmOption() {
	return alarmOption;
    }

    /**
     * @param AlarmOption
     *            alarmOption the alarmOption to set
     */
    public void setAlarmOption(AlarmOption alarmOption) {
	this.alarmOption = alarmOption;
    }

    /**
     * @return <b>Int</b> the alarmPrimaryKey
     */
    public int getAlarmPrimaryKey() {
	return alarmPrimaryKey;
    }

    /**
     * @param Int
     *            alarmPrimaryKey the alarmPrimaryKey to set
     */
    public void setAlarmPrimaryKey(int alarmPrimaryKey) {
	this.alarmPrimaryKey = alarmPrimaryKey;
    }
}