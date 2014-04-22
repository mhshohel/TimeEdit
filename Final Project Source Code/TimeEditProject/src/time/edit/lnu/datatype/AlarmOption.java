/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.datatype
 *
 * @FileName AlarmOption.java
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
 * Return state of Alarm
 * 
 */
public enum AlarmOption {
    ENABLED, DISABLED, CUSTOMIZED;

    /**
     * Return string of Alarm Option
     */
    public String toString() {
	switch (this) {
	case ENABLED:
	    return "Enabled";
	case DISABLED:
	    return "Disabled";
	case CUSTOMIZED:
	    return "Customized";
	default:
	    return "Unspecified";
	}
    }
}