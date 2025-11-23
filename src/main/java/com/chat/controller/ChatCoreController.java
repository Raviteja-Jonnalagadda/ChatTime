package com.chat.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat.services.ChatSemiCoreServices;
import com.chat.utils.ChatTimeUtils;

@RestController
public class ChatCoreController {

	@Autowired
	public ChatSemiCoreServices cscs;
	@Autowired
	public ChatTimeUtils ctu;

	@PostMapping("/CTSearchID")
	public String CheckUserId(@RequestBody String sval) {
	    System.out.println("[CheckUserId] RAW val ----> " + sval);
	    JSONObject req = new JSONObject(sval);
	    String searchId = req.optString("search_id", null);
	    if (searchId == null || searchId.trim().isEmpty()) {
	        System.out.println("[CheckUserId] search_id is NULL");
	        return "NLVL";
	    }
	    searchId=searchId+"%";
	    System.out.println("[CheckUserId] Searching for: " + searchId);
	    String searchResult = cscs.checkCTID(searchId);
	    String result = ctu.nvl(searchResult, "NO_DATA");
	    System.out.println("[CheckUserId] Result: " + result);
	    return result;
	}


}
