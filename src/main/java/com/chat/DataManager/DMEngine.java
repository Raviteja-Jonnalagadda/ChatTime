package com.chat.DataManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
public class DMEngine {
	private static JSONObject response = new JSONObject();
	public static JSONObject SingleRequestHandler(JSONObject val) {
		
		if(val.isEmpty()) {
			return errorhandler("SingleRequestHandler","NULVAL","Cant process Null values ");
		}
		
		
		return response;
	}
	
	public static JSONObject errorhandler(String parant,String code,String msg) {
		response.clear();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
		LocalDateTime ld = LocalDateTime.now();
		String timestamp = ld.format(df);
		response.put("ProcessedBY", "DataManager."+parant);
		response.put("code", code);
		response.put("msg", msg);
		response.put("TimeStamp", timestamp);
		
		return response;
	}
	
	
	public static void main(String[] args) {
		
System.out.println(errorhandler("SingleRequestHandler","NULVAL","Cant process Null values "));
	}

}
