package com.chat.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.chat.core.ChatLoginHandler;
import com.chat.core.ChatTimeCoreUtility;
import com.chat.core.ChatTimeUserRegister;

@RestController
public class ChatCoreController {

	@Autowired
	private ChatLoginHandler ctlh;

	@PostMapping("/ctapLogin")
	public String chatLogin(@RequestBody String val) {
		System.out.println(val + "   came ra babu");
		String result = ctlh.ctaploginchecker(val);
		System.out.println("resukt -->  " + result);
		return result;
	}

	@PostMapping("/ChatTimeIdCheck")
	public String checkId(@RequestParam("CTID") String ctid) {
		System.out.println("[ChatCoreController] [checkId] CID is  " + ctid);
		String sign = ChatTimeCoreUtility.ctidCheck(ctid);
		if (sign.equalsIgnoreCase("DONE")) {
			System.out.println("[ChatCoreController] [checkId]  is DONE");
			return "DONE";
		} else if (sign.equalsIgnoreCase("NO")) {
			System.out.println("[ChatCoreController] [checkId]  is DONE");
			return "NO";
		} else {
			System.out.println("[ChatCoreController] [checkId]  is FAIL");
			return "FAIL";
		}
	}

	@PostMapping("/SendOTPEmail")
	public String SendEmailOtp(@RequestParam("mailid") String mailid, @RequestParam("mailtype") String mailtype,
			@RequestParam("Name") String name) {
		System.out.println("ChatCoreController] [SendEmailOtp] The recived Email Id is -->  " + mailid);
		JSONObject jo = new JSONObject();
		jo.put("tomail", mailid);
		jo.put("sub", mailid);
		jo.put("message", mailid);
		jo.put("name", name);
		String sign = ChatTimeCoreUtility.SendEmail(mailtype, jo.toString());
		if (sign.equalsIgnoreCase("DONE")) {
			return "DONE";
		} else if (sign.equalsIgnoreCase("NO")) {
			return "NO";
		} else if (sign.equalsIgnoreCase("EMAIL_FAIL")) {
			return "EMAIL_FAIL";
		} else {
			return "FAIL";
		}
	}

	@PostMapping("/VerifyOTP")
	public String OTPVerifer(@RequestParam("sign") String type, @RequestParam("otp_val") String otp,
			@RequestParam("email") String emailid) {
		String result = null;
		if (emailid.isEmpty() || type.isEmpty() || otp.isEmpty()) {
			return "NULLVALUUES";
		}
		String verifystatus = ChatTimeCoreUtility.VeriftOTP(type, otp, emailid);
		System.out.println("[ChatCoreController] [OTPVerifer] The verify status is -->  [ " + verifystatus + " ] .");
		result = verifystatus;
		return result;
	}

	@Autowired
	public ChatTimeUserRegister ct;

	@PostMapping("/ChatTimeSignUp")
	public String ChatTimeSignUp(@RequestBody String signval) {
		try {
			JSONObject mval = new JSONObject(signval);
			String storeValue = ct.storedetails(mval);
			System.out.println("[ChatTimeSignUp] Stored value result --> " + storeValue);
			return storeValue;
		} catch (Exception e) {
			System.out.println("[ChatTimeSignUp] Error while parsing JSON --> " + signval);
			e.printStackTrace();
			return "FAIL";
		}
	}

}
