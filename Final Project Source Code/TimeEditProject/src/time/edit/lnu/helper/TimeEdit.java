package time.edit.lnu.helper;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * TimeEdit Class: Provided By Linnaeus University to handle the messages to and
 * from the TimeEdit WebService.
 */
public class TimeEdit {

    public static final int TIMEOUT = 10000;

    /**
     * Return Session Open
     * 
     * @param String
     *            UserName
     * @param String
     *            Password
     * @return <b>String</b> TE_Session_open
     * @throws Exception
     */
    public static String TE_Session_open(String UserName, String Password)
	    throws Exception {

	String text = "";

	String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
		+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
		+ "<soap:Body>"
		+ "<TE_Session_open xmlns=\"http://www.evolvera.se/timeedit/namespace/default\">"
		+ "<UserName>" + UserName + "</UserName>" + "<Password>"
		+ Password + "</Password>" + "</TE_Session_open>"
		+ "</soap:Body>" + "</soap:Envelope>";

	HttpURLConnection uc = null;
	URL url = null;
	byte[] data = message.getBytes();

	url = new URL("http://eclipse.lnu.se/4dsoap");

	uc = (HttpURLConnection) url.openConnection();

	uc.setConnectTimeout(TIMEOUT);

	uc.setDoInput(true);
	uc.setDoOutput(true);
	uc.setUseCaches(false);
	uc.setRequestProperty("Content-Length", "" + message.length());
	uc.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
	uc.setRequestProperty("SOAPAction",
		"TimeEdit_WebService#TE_Session_open");
	uc.setRequestMethod("POST");
	uc.connect();

	DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
	dos.write(data, 0, data.length);
	dos.flush();
	dos.close();

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document doc = db.parse(uc.getInputStream());

	doc.getDocumentElement().normalize();

	NodeList itemNodes = doc.getElementsByTagName("SessionId");
	text = "" + itemNodes.item(0).getChildNodes().item(0).getNodeValue();

	uc.disconnect();

	return text;
    }

    /**
     * Session Close
     * 
     * @param String
     *            SessionId
     * @throws Exception
     */
    public static void TE_Session_close(String SessionId) throws Exception {

	String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
		+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
		+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
		+ "<soap:Body>"
		+ "<TE_Session_close xmlns=\"http://www.evolvera.se/timeedit/namespace/default\">"
		+ "<SessionId>" + SessionId + "</SessionId>"
		+ "</TE_Session_close>" + "</soap:Body>" + "</soap:Envelope>";

	HttpURLConnection uc = null;
	URL url = null;
	byte[] data = message.getBytes();

	url = new URL("http://eclipse.lnu.se/4dsoap");

	uc = (HttpURLConnection) url.openConnection();

	uc.setConnectTimeout(TIMEOUT);

	uc.setDoInput(true);
	uc.setDoOutput(true);
	uc.setUseCaches(false);
	uc.setRequestProperty("Content-Length", "" + message.length());
	uc.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
	uc.setRequestProperty("SOAPAction",
		"TimeEdit_WebService#TE_Session_close");
	uc.setRequestMethod("POST");
	uc.connect();

	DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
	dos.write(data, 0, data.length);
	dos.flush();
	dos.close();

	uc.disconnect();
    }

    /**
     * return ObjectSearchResponse by Searching Object
     * 
     * @param String
     *            SessionId
     * @param Int
     *            TypeId
     * @param String
     *            Signature
     * @param StringName
     * @return <b>ObjectSearchResponse</b> TE_Object_search
     * @throws Exception
     */
    public static ObjectSearchResponse TE_Object_search(String SessionId,
	    int TypeId, String Signature, String Name) throws Exception {

	ArrayList<Integer> ObjectIds = null;
	ArrayList<String> ObjectSignatures = null;
	ArrayList<String> ObjectNames = null;

	String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> "
		+ "<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"  "
		+ "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "
		+ "xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" "
		+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
		+ "<SOAP-ENV:Body>"
		+ "<ns1:TE_Object_search xmlns:ns1=\"http://www.evolvera.se/timeedit/namespace/default\">"
		+

		"<SessionId>"
		+ SessionId
		+ "</SessionId>"
		+ "<TypeId>"
		+ TypeId
		+ "</TypeId>"
		+ "<Signature>"
		+ Signature
		+ "</Signature>"
		+ "<Name>"
		+ Name
		+ "</Name>"
		+ "<CategoryIds href=\"#ref-1\" />"
		+ "<ExtRef></ExtRef>"
		+ "<ResUnit></ResUnit>"
		+

		"</ns1:TE_Object_search>"
		+ "<SOAP-ENC:Array id=\"ref-1\" SOAP-ENC:arrayType=\"xsd:int[0]\">"
		+ "</SOAP-ENC:Array>" + "</SOAP-ENV:Body>"
		+ "</SOAP-ENV:Envelope>";

	HttpURLConnection uc = null;
	URL url = null;
	byte[] data = null;

	data = message.getBytes("UTF8");

	url = new URL("http://eclipse.lnu.se/4dsoap");

	uc = (HttpURLConnection) url.openConnection();

	uc.setConnectTimeout(TIMEOUT);

	uc.setDoInput(true);
	uc.setDoOutput(true);
	uc.setUseCaches(false);
	uc.setRequestProperty("Content-Length", "" + data.length);
	uc.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
	uc.setRequestProperty("SOAPAction",
		"TimeEdit_WebService#TE_Object_search");
	uc.setRequestMethod("POST");
	uc.connect();

	ObjectIds = new ArrayList<Integer>();
	ObjectSignatures = new ArrayList<String>();
	ObjectNames = new ArrayList<String>();

	DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
	dos.write(data, 0, data.length);
	dos.flush();
	dos.close();

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document doc = db.parse(uc.getInputStream());

	doc.getDocumentElement().normalize();

	NodeList itemNodes = doc.getElementsByTagName("SOAP-ENC:Array");

	ObjectIds = new ArrayList<Integer>();
	int index = 0;
	for (int i = 0; i < itemNodes.item(index).getChildNodes().getLength(); i++) {
	    if (itemNodes.item(index).getChildNodes().item(i).getChildNodes()
		    .getLength() == 1) {
		ObjectIds.add(Integer.valueOf(itemNodes.item(index)
			.getChildNodes().item(i).getChildNodes().item(0)
			.getNodeValue()));
	    }
	}

	ObjectSignatures = new ArrayList<String>();
	index = 1;
	for (int i = 0; i < itemNodes.item(index).getChildNodes().getLength(); i++) {
	    if (itemNodes.item(index).getChildNodes().item(i).getChildNodes()
		    .getLength() == 1) {
		ObjectSignatures.add(itemNodes.item(index).getChildNodes()
			.item(i).getChildNodes().item(0).getNodeValue());
	    }
	}

	ObjectNames = new ArrayList<String>();
	index = 2;
	for (int i = 0; i < itemNodes.item(index).getChildNodes().getLength(); i++) {
	    if (itemNodes.item(index).getChildNodes().item(i).getChildNodes()
		    .getLength() == 1) {
		ObjectNames.add(itemNodes.item(index).getChildNodes().item(i)
			.getChildNodes().item(0).getNodeValue());
	    }
	}

	uc.disconnect();

	return new ObjectSearchResponse(ObjectIds, ObjectSignatures,
		ObjectNames);
    }

    /**
     * 
     * Object Search Response Class
     * 
     */
    public static class ObjectSearchResponse {
	public Integer[] ObjectIds;
	public String[] ObjectSignatures;
	public String[] ObjectNames;

	/**
	 * Constructor of ObjectSearchResponse Class
	 * 
	 * @param ArrayList
	 *            <Integer> ObjectIds
	 * @param ArrayList
	 *            <String> ObjectSignatures
	 * @param ArrayList
	 *            <String> ObjectNames
	 */
	public ObjectSearchResponse(ArrayList<Integer> ObjectIds,
		ArrayList<String> ObjectSignatures,
		ArrayList<String> ObjectNames) {
	    this.ObjectIds = new Integer[ObjectIds.size()];
	    this.ObjectSignatures = new String[ObjectIds.size()];
	    this.ObjectNames = new String[ObjectIds.size()];
	    ObjectIds.toArray(this.ObjectIds);
	    ObjectSignatures.toArray(this.ObjectSignatures);
	    ObjectNames.toArray(this.ObjectNames);
	}
    }

    /**
     * Return Extended Object Search Result
     * 
     * @param String
     *            SessionId
     * @param Int
     *            TypeId
     * @param String
     *            SearchText
     * @return <b>ObjectSearchResponse</b> TE_Object_searchExtended
     * @throws Exception
     */
    public static ObjectSearchResponse TE_Object_searchExtended(
	    String SessionId, int TypeId, String SearchText) throws Exception {

	ArrayList<Integer> ObjectIds = null;
	ArrayList<String> ObjectSignatures = null;
	ArrayList<String> ObjectNames = null;

	String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> "
		+ "<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" "
		+ "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "
		+ "xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" "
		+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
		+ "<SOAP-ENV:Body>"
		+ "<ns1:TE_Object_searchExtended xmlns:ns1=\"http://www.evolvera.se/timeedit/namespace/default\">"
		+

		"<SessionId>"
		+ SessionId
		+ "</SessionId>"
		+ "<TypeId>"
		+ TypeId
		+ "</TypeId>"
		+ "<Signature>"
		+ SearchText
		+ "</Signature>"
		+ "<Name>"
		+ SearchText
		+ "</Name>"
		+ "<CategoryIds href=\"#ref-1\" />"
		+ "<ExtRef></ExtRef>"
		+ "<ResUnit></ResUnit>"
		+ "<IntersectionObjectIds href=\"#ref-2\" />"
		+ "<RelatedObjectIds href=\"#ref-3\" />"
		+ "<UnionBetweenSignatureAndName>true</UnionBetweenSignatureAndName>"
		+ "<MaxObjects>0</MaxObjects>"
		+ "<FirstObject>0</FirstObject>"
		+ "<SortOrder>0</SortOrder>"
		+

		"</ns1:TE_Object_searchExtended>"
		+

		"<SOAP-ENC:Array id=\"ref-1\" SOAP-ENC:arrayType=\"xsd:int[0]\">"
		+ "</SOAP-ENC:Array>"
		+ "<SOAP-ENC:Array id=\"ref-2\" SOAP-ENC:arrayType=\"xsd:int[0]\">"
		+ "</SOAP-ENC:Array>"
		+ "<SOAP-ENC:Array id=\"ref-3\" SOAP-ENC:arrayType=\"xsd:int[0]\">"
		+ "</SOAP-ENC:Array>"
		+ "</SOAP-ENV:Body>"
		+ "</SOAP-ENV:Envelope>";

	HttpURLConnection uc = null;
	URL url = null;
	byte[] data = null;

	data = message.getBytes("UTF8");

	url = new URL("http://eclipse.lnu.se/4dsoap");

	uc = (HttpURLConnection) url.openConnection();

	uc.setConnectTimeout(TIMEOUT);

	uc.setDoInput(true);
	uc.setDoOutput(true);
	uc.setUseCaches(false);
	uc.setRequestProperty("Content-Length", "" + data.length);
	uc.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
	uc.setRequestProperty("SOAPAction",
		"TimeEdit_WebService#TE_Object_searchExtended");
	uc.setRequestMethod("POST");
	uc.connect();

	ObjectIds = new ArrayList<Integer>();
	ObjectSignatures = new ArrayList<String>();
	ObjectNames = new ArrayList<String>();

	DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
	dos.write(data, 0, data.length);
	dos.flush();
	dos.close();

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document doc = db.parse(uc.getInputStream());

	doc.getDocumentElement().normalize();

	NodeList itemNodes = doc.getElementsByTagName("SOAP-ENC:Array");

	ObjectIds = new ArrayList<Integer>();
	int index = 0;
	for (int i = 0; i < itemNodes.item(index).getChildNodes().getLength(); i++) {
	    if (itemNodes.item(index).getChildNodes().item(i).getChildNodes()
		    .getLength() == 1) {
		ObjectIds.add(Integer.valueOf(itemNodes.item(index)
			.getChildNodes().item(i).getChildNodes().item(0)
			.getNodeValue()));
	    }
	}

	ObjectSignatures = new ArrayList<String>();
	index = 1;
	for (int i = 0; i < itemNodes.item(index).getChildNodes().getLength(); i++) {
	    if (itemNodes.item(index).getChildNodes().item(i).getChildNodes()
		    .getLength() == 1) {
		ObjectSignatures.add(itemNodes.item(index).getChildNodes()
			.item(i).getChildNodes().item(0).getNodeValue());
	    }
	}

	ObjectNames = new ArrayList<String>();
	index = 2;
	for (int i = 0; i < itemNodes.item(index).getChildNodes().getLength(); i++) {
	    if (itemNodes.item(index).getChildNodes().item(i).getChildNodes()
		    .getLength() == 1) {
		ObjectNames.add(itemNodes.item(index).getChildNodes().item(i)
			.getChildNodes().item(0).getNodeValue());
	    }
	}

	uc.disconnect();

	return new ObjectSearchResponse(ObjectIds, ObjectSignatures,
		ObjectNames);
    }
}