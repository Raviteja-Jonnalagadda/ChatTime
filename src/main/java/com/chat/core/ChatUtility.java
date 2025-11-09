package com.chat.core;

import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

@Component
public class ChatUtility {

	private static ResourceBundle errbn = ResourceBundle.getBundle("error");
	 
	public String getemsg(String eid) {
		String result = null;
		String emsg = null;
		if(eid.isBlank()||eid.isEmpty()||eid.length()<=0||eid.trim().isBlank()) {
			System.out.println("[ChatUtility] [getemsg] Eid is null returning the unexpected error");
			result = "Unepected Error";
		}
		try {
			 emsg = errbn.getString("ctap.error."+eid);
			 System.out.println("[ChatUtility] [getemsg] Error Message Identified is -->  [ "+emsg+" ] ");
		}
		catch(Exception e) {
			emsg = "Unknown Error";
			System.out.println("[ChatUtility] [getemsg] Error occured while getting the Error Message from the error properites for the error id -->  [ "+eid+" ] ");
		}
		result = (emsg!=null)?emsg:"UnIdentified Error";
		return result;
	}
	
	public static void main(String[] args) {
		

	}

}
