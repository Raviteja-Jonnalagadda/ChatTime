package com.chat.error;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.json.JSONObject;

public class ErrorBuilder {

	protected static JSONObject erjob = new JSONObject();
	private static ResourceBundle rb = ResourceBundle.getBundle("application");
	
	public static JSONObject ctaperrorbuilder(String ecod,String emsg) {
		erjob.clear();
		int erid = 0;
		erjob.put("ecod", ecod);
		erjob.put("emsg", emsg);
		erjob.put("erid", erid);
		erjob.put("Processed_By", (rb.getString("spring.application.name")!=null)?rb.getString("spring.application.name"):"Chat Time Application");
		erjob.put("etim", getNow());
		
		return erjob;
	}
	
	public static JSONObject ctaperrorbuilder(String ecod,String emsg,String esug) {
		erjob.clear();
		int erid = 0;
		erjob.put("ecod", ecod);
		erjob.put("emsg", emsg);
		erjob.put("erid", erid);
		erjob.put("Processed_By", (rb.getString("spring.application.name")!=null)?rb.getString("spring.application.name"):"Chat Time Application");
		erjob.put("etim", getNow());
		
		
		return erjob;
	}
	
	public static String getNow() {
	    SimpleDateFormat sdh = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	    Date now = new Date();
	    String date = sdh.format(now);
	    System.out.println(date);
	    return date;
	}
	
	public static void main(String[] args) {
		getNow();

	}

}
