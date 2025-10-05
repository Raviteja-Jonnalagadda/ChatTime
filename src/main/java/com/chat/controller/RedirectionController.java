package com.chat.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chat.dto.CommonDTO;

@Controller
public class RedirectionController {

	@GetMapping("/chattest")
	@ResponseBody
	public String test() {
		return "Connected sucessfully";
	}

	@GetMapping("/ctms")
	public String ctmsPage() {
		return "ChatTimeCore";
	}
 
	@GetMapping("/SucessLogin")
	public String UnidentifiedLogin() {
		return "error/unIdentifiedLogin";
	}
	
	@Autowired
	public CommonDTO cd;

	@PostMapping("/SucessLogin")
	public String sucessLogin(@RequestParam("ctap_uid") String unm, @RequestParam("ctap_pwd") String pwd) {
		System.out.println(unm);
		String ckval = cd.getLoginstatus();
		System.out.println("check : " + ckval);
		System.out.println("the unnm name is -->  " + ckval.substring(0, unm.length()));
		if (unm.equals(ckval.substring(0, unm.length()))) {
			if (ckval.substring(unm.length() + 1).equals("DONE")) {
				System.out.println("Sucess Login for --> " + unm);
				return "ChatTimeCore";
			} else if (ckval.substring(0, unm.length() + 1).equals("FAIL")) {
				System.err.println("FAIL Login for --> " + unm);
				return "CTMF";
			} else {
				System.err.println("Unknow Login Status for --> " + unm);
				return "CTMF";
			}
		} else {
			System.err.println("Unidentified Login Status -->  " + unm);
			return "CTMF";
		}
	}
	
	@PostMapping("/Page/AllRedirect")
	@ResponseBody
	public String AllRedirection(@RequestBody String jsonData) {
		if(jsonData==null||jsonData.trim().isEmpty()) {
			return "Invalid_Redirection";
		}
		JSONObject jo = new JSONObject();
		try{ jo = new JSONObject(jsonData);}
		catch(Exception e) {
			System.err.println("[RedirectionController] [AllRedirection] Recived data is not a valid JSON --> "+jsonData+"  error is --> "+e.getMessage());
		}
		if(!jo.has("pname")||!jo.has("pdata")) {
			System.err.println("[RedirectionController] [AllRedirection] pname or pdata node is missing in the recived json -->  "+jo.toString(2));
			return "Invalid_Redirection";
		}
		String pagename = jo.optString("pname","NO_DATA");
		String passdata = jo.optString("pdata","NO_DATA");
		if(pagename == null ||pagename.trim().isEmpty()||passdata==null||passdata.isEmpty()) {
			System.err.println("[RedirectionController] [AllRedirection] Data is missing in the pname or pdata -->  "+jo.toString(2));
			return "Invalid_Redirection";
		}
		System.out.println("[RedirectionController] [AllRedirection] The recived pagename is ---> [ "+pagename+" ] "
				+ " The data that need to pass to that page is ---> [ "+passdata+" ]");
		switch(pagename) {
		case "DSB_WelcomePage":
			System.out.println("[RedirectionController] [AllRedirection] Redirecting to the Welcome.jsp");
			return "Welcome";
		case "Chats":
			System.out.println("[RedirectionController] [AllRedirection] Redirecting to the ctap_ChatList.jsp");
			cd.setPagetype(pagename);
			return "chatlist";
		default :
			System.err.println("[RedirectionController] [AllRedirection] No matching code found for the page redirection");
			return "chatlist";
		}
		
	}

	@GetMapping("/chatlist")
	public String showChatList() {
		System.out.println("hii in the http://localhost:8111/chatlist");
	    return "ctap_ChatList"; // matches your JSP filename: ctap_ChatList.jsp
	}
	
}
