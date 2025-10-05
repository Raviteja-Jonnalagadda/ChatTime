package com.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.chat.core.ChatLoginHandler;


@RestController
public class ChatCoreController { 
	
	@Autowired
	private ChatLoginHandler ctlh;
	@PostMapping("/ctapLogin") 
	public String chatLogin(@RequestBody String val) {
		System.out.println(val+"   came ra babu");
		String result = ctlh.ctaploginchecker(val);
		System.out.println("resukt -->  "+result);			
		return result; 
	}
	

}
