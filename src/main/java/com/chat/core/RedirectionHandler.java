package com.chat.core;

public class RedirectionHandler {

	public String IframeHandler(String pid) {
		if(pid.trim().isBlank()||pid.trim().isEmpty()||pid == null) {
			return "Cant Process Null";
		}
		
		
		return pid;
		
	}
	
	public static void main(String[] args) {
		
	}
}
