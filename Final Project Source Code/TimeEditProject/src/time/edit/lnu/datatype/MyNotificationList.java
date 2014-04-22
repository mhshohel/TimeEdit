/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.datatype
 *
 * @FileName MyNotificationList.java
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

import java.util.ArrayList;

/**
 * My NotificationList contains incoming notifications
 * 
 */
public class MyNotificationList {
    private CourseOrTeacher courseOrTeacher = null;
    private ArrayList<Event> oldest = new ArrayList<Event>();
    private ArrayList<Event> updated = new ArrayList<Event>();

    /**
     * 
     * @param CourseOrTeacher
     *            courseOrTeacher
     */
    public void setCourseOrTeacher(CourseOrTeacher courseOrTeacher) {
	this.courseOrTeacher = courseOrTeacher;
    }

    /**
     * 
     * @return <b>CourseOrTeacher</b> getCourseOrTeacher
     */
    public CourseOrTeacher getCourseOrTeacher() {
	return this.courseOrTeacher;
    }

    /**
     * 
     * @param Event
     *            event
     */
    public void addOldest(Event event) {
	oldest.add(event);
    }

    /**
     * 
     * @return <b>ArrayList<Event></b> getOldest
     */
    public ArrayList<Event> getOldest() {
	return this.oldest;
    }

    /**
     * 
     * @param Event
     *            event
     */
    public void addUpdated(Event event) {
	updated.add(event);
    }

    /**
     * 
     * @return <b>ArrayList<Event></b> getUpdated
     */
    public ArrayList<Event> getUpdated() {
	return this.updated;
    }
}