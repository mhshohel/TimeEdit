/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.datatype
 *
 * @FileName MyEvents.java
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
package time.edit.lnu.datatype;

/**
 * MyEvents contains custom events that is selected by user
 * 
 */
public class MyEvents {
    private int courseTeacherId;
    private Event[] events;

    /**
     * Constructor of MyEvents Class
     * 
     * @param Event
     *            [] events
     * @param Int
     *            courseTeacherId
     */
    public MyEvents(Event[] events, int courseTeacherId) {
	this.courseTeacherId = courseTeacherId;
	this.events = events;
    }

    /**
     * 
     * @return <b>Int</b> getCourseTeacherId
     */
    public int getCourseTeacherId() {
	return courseTeacherId;
    }

    /**
     * 
     * @return <b>Event[]</b> getEvents
     */
    public Event[] getEvents() {
	return events;
    }
}