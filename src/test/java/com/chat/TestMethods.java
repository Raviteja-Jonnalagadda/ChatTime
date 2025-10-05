package com.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONObject;

public class TestMethods {
	  public static String paramexecuter(String sval) {
	    	String result = null;
	    	String rawQuery =null;
	    	JSONObject params = new JSONObject();
	    	JSONObject val = new JSONObject(sval);
	    	
	    	if(val.isEmpty()||val == null) {
	    		return errormsgbuilder("JSNNUL","Json Object is null. Cant Process Null Json .").toString();
	    	}
	    	
	    	if(!val.has("raw_qry")||!val.has("params")) {
	    		return errormsgbuilder("PRMNUL","Paramaters Are null. Cant Process Null values .").toString();
	    	}
	    	rawQuery = val.optString("raw_qry","NO_DATA");
	        params = val.getJSONObject("params");
	    	System.out.println("rawQuery -->  "+rawQuery);
	    	 System.out.println("params -->  "+params.toString());
	    	 	    	 Set<String> key = params.keySet();
	    	 	    	 for(String tky:key) {
	    	 	    		  rawQuery =rawQuery.replace("{"+tky+"}", "'"+params.get(tky)+"'");
	    	 	    	 }
	    	 	    	 System.out.println(rawQuery);
	    	 	    	result = rawQuery;
	    	return result; 
	    	
	    }
	  
	  public static JSONObject errormsgbuilder(String code,String message) {
	    	JSONObject errormsg = new JSONObject();
	    	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
	    	 LocalDateTime now = LocalDateTime.now();
	    	 String tstamp = now.format(formatter);
	    	
	    	errormsg.put("ProcessedBY", "Smart CRUD Engine");
	    	errormsg.put("Status", "FAIL");
	    	errormsg.put("ecode", (code.trim().isEmpty()||message == null)?"Unknown Error Code":code);
	    	errormsg.put("emsg", (message.trim().isEmpty()||message == null)?"Unknown Error Message":message);
	    	errormsg.put("TimeStamp", tstamp);
	    	
	    	return errormsg;
	    }
	  
	  public static void main(String[] args) {
		  JSONObject hm = new JSONObject();
		  hm.put("UNM", "RAVI");
		  hm.put("PWD", "Pass12!@");
		  
		 // hm.clear();
		  String jsonSelect = "{\"main_sign\":\"select\",\"raw_qry\":\"select count(1) from ctuser_master where user_name={UNM} and password={PWD}\",\"params\":"+hm+",\"rtp\":\"execute\"}";
		 //System.out.println(paramexecuter("select count(1) from ctuser_master where user_name={UNM} and Password={PWD}",hm));
		 System.out.println(paramexecuter(jsonSelect));
	}
}
