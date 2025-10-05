package com.chat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;  
import java.util.Date;

public class TimeTest {

	public static void main(String[] args) {
		SimpleDateFormat  sdf = new SimpleDateFormat ("dd-mmm-yyyy hh:mm:ss");
		 Date now = new Date();
		 String formattedDate = sdf.format(now);
		 System.out.println(formattedDate);
		 
		 DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm:ss");
		 LocalDateTime n = LocalDateTime.now();
		 System.out.println(n.format(dtf));
		 
	}

}
