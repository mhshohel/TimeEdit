/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.datatype
 *
 * @FileName CourseOrTeacher.java
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
 * Take Course or Teacher Information and Sort them ascending order
 * 
 */
public class CourseOrTeacher implements Comparable<CourseOrTeacher> {
    private int courseTeacherId;
    private String courseTeacherName;
    private String courseTeacherSignature;
    private int courseTeacherColor;
    private String type;
    private String alarmOption;

    /**
     * Constructor of CourseOrTeacher Class
     * 
     * @param Int
     *            courseTeacherId
     * @param String
     *            courseTeacherSignature
     * @param String
     *            courseTeacherName
     * @param Int
     *            courseTeacherColor
     * @param String
     *            type
     * @param String
     *            alarmOption
     */
    public CourseOrTeacher(int courseTeacherId, String courseTeacherSignature,
	    String courseTeacherName, int courseTeacherColor, String type,
	    String alarmOption) {
	this.courseTeacherId = courseTeacherId;
	this.courseTeacherName = courseTeacherName;
	this.courseTeacherSignature = courseTeacherSignature;
	this.courseTeacherColor = courseTeacherColor;
	this.type = type;
	this.alarmOption = alarmOption;
    }

    /**
     * Return Course Teacher Name in nice way
     */
    @Override
    public String toString() {
	return getCourseTeacherName();
    }

    /**
     * Compare CourseOrTeacher object to sort them in ascending order
     * 
     * 
     */
    @Override
    public int compareTo(CourseOrTeacher course) {
	return getCourseTeacherName().compareTo(course.getCourseTeacherName());
    }

    /**
     * @return <b>Int</b> the courseTeacherId
     */
    public int getCourseTeacherId() {
	return courseTeacherId;
    }

    /**
     * @return <b>String</b> the courseTeacherSignature
     */
    public String getCourseTeacherSignature() {
	return courseTeacherSignature;
    }

    /**
     * @return <b>String</b> the courseTeacherName
     */
    public String getCourseTeacherName() {
	return courseTeacherName;
    }

    /**
     * @return <b>Int</b> the courseTeacherColor
     */
    public int getCourseTeacherColor() {
	return courseTeacherColor;
    }

    /**
     * @return <b>String</b> the type
     */
    public String getType() {
	return type;
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