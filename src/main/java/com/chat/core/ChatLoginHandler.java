package com.chat.core;

import java.time.LocalDateTime;

//import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chat.db.DBConnecter;
import com.chat.db.SmartCrudEngine;
import com.chat.dto.CommonDTO;


@Component
public class ChatLoginHandler {
	//private static Logger log = Logger.getLogger("ChatLoginHandler.class");

	@Autowired 
	 public CommonDTO cd;
	public ChatTimeCoreUtility cu;
	public String ctaploginchecker(String val) {
		System.out.println("val -->  "+val);
		if (val.trim().equalsIgnoreCase("") || val.isEmpty() || val.length() <= 0) {
			System.out.println("e001");
			cd.setLoginstatus("FAIL"+"~"+cu.errormsg("E003"));
			return "FAIL~" + cu.errormsg("E001");
		}
		JSONObject mnjo = new JSONObject(val);
		if (!mnjo.has("uname") || !mnjo.has("pwd")) {
			System.out.println("e002");
			cd.setLoginstatus("FAIL"+"~"+cu.errormsg("E003"));
			return "FAIL~" + cu.errormsg("E002");
		}
		String unam = mnjo.optString("uname", "NO_DATA");
		String pwd = mnjo.optString("pwd", "NO_DATA");
		if (unam.equalsIgnoreCase("NO_DATA") || pwd.equalsIgnoreCase("NO_DATA")) {
			System.out.println("e003");
			cd.setLoginstatus(unam+"~FAIL"+"~"+cu.errormsg("E003"));
			return "FAIL~" + cu.errormsg("E003");
		}
		int unval = 0;
		try {
			String query = DBConnecter.getdata("CTAP1018");
			System.out.println(query);
			JSONObject setdata = new JSONObject();
			JSONObject userdata = new JSONObject();
			userdata.put("UNM", unam);
			userdata.put("PWD", pwd);
			setdata.put("main_sign", "SELECT"); 
			setdata.put("raw_qry", query);
			setdata.put("params", userdata);
			setdata.put("rtp", "execute");
			String ctnresult = SmartCrudEngine.paramexecuter(setdata.toString());
			System.out.println(ctnresult);
			System.out.println("the result is -->  " + ctnresult);
			JSONObject userresult = new JSONObject(ctnresult);
			if (userresult.getString("sign").equalsIgnoreCase("done")) {
				JSONArray ja = userresult.getJSONArray("query_data");
				JSONObject qdt = ja.getJSONObject(0);
				unval = qdt.getInt("CNT");
				System.out.println(unval);
			}
			else {
				System.out.println("Check the SMART CRUD ENGINE LOGS fail else");
				cd.setLoginstatus(unam+"~FAIL"+"~"+cu.errormsg("E003"));
				return "FAIL~" + cu.errormsg("E003");
			}
			if (unval == 0) {
				System.out.println("fail 0");
				cd.setLoginstatus(unam+"~FAIL"+"~"+cu.errormsg("E003"));
				return "FAIL~" + cu.errormsg("E003");
			} else if (unval == 1) {
				System.out.println("pass");
				cd.setLoginstatus(unam+"~DONE");
				String ckval = cd.getLoginstatus();
				System.out.println("done : "+ckval);
				cd.setUserid(unam);
				cd.setLogintime(LocalDateTime.now());
				return "DONE~"+unam;
				
			} else {
				System.out.println(" fail else");
				cd.setLoginstatus(unam+"~FAIL"+"~"+cu.errormsg("E003"));
				return "FAIL~" + cu.errormsg("E003");
			} 
		} catch (Exception e) {
			System.err.println("err  "+e);
			cd.setLoginstatus(unam+"~FAIL"+"~"+cu.errormsg("E003"));
			return "FAIL~" + cu.errormsg("E004");
		}

	}



	public static void main(String[] args) {
		//System.out.println(errormsg("E001"));
	}
}