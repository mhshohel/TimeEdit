/**
 *
 * @ProjectName TimeEditProject
 *
 * @PackageName time.edit.lnu.datatype
 *
 * @FileName MyList.java
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
package time.edit.lnu.datatype;

/**
 * Contains Custom Course Information
 * 
 */
public class MyList implements Comparable<MyList> {
    private int id;
    private String name;
    private String signature;
    private int color;
    private String type;
    private String alarmOption;

    /**
     * Constructor of MyList
     * 
     * @param Int
     *            id
     * @param String
     *            signature
     * @param String
     *            name
     * @param Int
     *            color
     * @param String
     *            type
     * @param String
     *            alarmOption
     */
    public MyList(int id, String signature, String name, int color,
	    String type, String alarmOption) {
	this.setId(id);
	this.setName(name);
	this.setSignature(signature);
	this.setColor(color);
	this.setType(type);
	this.setAlarmOption(alarmOption);
    }

    /**
     * @return <b>Int</b> the id
     */
    public int getId() {
	return id;
    }

    /**
     * @param Int
     *            id the id to set
     */
    private void setId(int id) {
	this.id = id;
    }

    /**
     * @return <b>String</b> the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param String
     *            name the name to set
     */
    private void setName(String name) {
	this.name = name;
    }

    /**
     * @return <b>String</b> the type
     */
    public String getType() {
	return type;
    }

    /**
     * @param String
     *            type the type to set
     */
    private void setType(String type) {
	this.type = type;
    }

    /**
     * @return <b>Int</b> the color
     */
    public int getColor() {
	return color;
    }

    /**
     * @param Int
     *            color the color to set
     */
    private void setColor(int color) {
	this.color = color;
    }

    /**
     * Comapare My List Object to sort ascending order
     */
    @Override
    public int compareTo(MyList myList) {
	return getName().compareTo(myList.getName());
    }

    /**
     * @return <b>String</b> the signature
     */
    public String getSignature() {
	return signature;
    }

    /**
     * @param String
     *            signature the signature to set
     */
    public void setSignature(String signature) {
	this.signature = signature;
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