package com.chat.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chat.Admin.Services.QueryRepoManager;
import com.chat.db.DBConnecter;
import com.chat.db.dto.QueryRepo;

@Configuration
@Controller
@RequestMapping("/admin")
public class AdminPanelController {
	private static JSONObject resmsg = new JSONObject();

	@GetMapping("/")
	public String AdminDashboard() {
		return "redirect:/html/admin_dashboard.html";
	}

	@PostMapping("/query_repo/view")
	@ResponseBody 
	public String getequerymaster() {
		String result = DBConnecter.getdata("CTAP1007");
		JSONObject DBrst = new JSONObject(result);
		System.out.println("result --->  " + result);
		if (result.trim().isEmpty()) {
			resmsg.put("status", "FAIL");
			resmsg.put("message", "Result is null");
			resmsg.put("data", "NO_DATA");
		} else if (DBrst.has("sign") && DBrst.getString("sign").equalsIgnoreCase("DONE")) {
			System.out.println("done");
			resmsg.put("status", "DONE");
			resmsg.put("message", "Result is GOOD");
			resmsg.put("data", DBrst);
		} else {
			resmsg.put("status", "FAIL");
			resmsg.put("message", "Result is ERROR");
			resmsg.put("data", DBrst);
		}
		System.out.println("\n\n\n\n\n" + resmsg.toString());
		return resmsg.toString();
	}

	@PostMapping("/query_repo/getNextQueryId")
	@ResponseBody
	public String getqueryid() {
		String qid = null;
		String result = null;
		result = DBConnecter.getdata("CTAP1012");
		System.out.println("Generated Query Id is ---->   " + result);
		JSONObject mhj = new JSONObject(result);
		if (mhj.getString("sign").equalsIgnoreCase("DONE")) {
			JSONArray ja = mhj.getJSONArray("query_data");
			JSONObject obj = ja.getJSONObject(0);
			qid = obj.getString("QID");
		}
		System.out.println("qid --->  " + qid);
		result = "{\"queryId\":\"" + qid + "\"}";
		return qid;
	}

	@PostMapping("/query_repo/addnewrepo")
	@ResponseBody
	public String addnewQueryRepo(QueryRepo nqr) {
		String result = null;
		System.out.println("in the query repo add ");
		System.out.println(nqr.getqueryId());
		result = QueryRepoManager.addrecord(nqr);
		System.out.println("\n\n\nresult is --->   " + result);
		return result;
	}

	@PostMapping("/query_repo/getQueryById/{qid}")
	@ResponseBody
	public String editQueryRepo(@PathVariable String qid) {
		String result = null;
		System.out.println("rescived query id is --->  " + qid);
		result = QueryRepoManager.getrecordforedit("CTAP1013", qid);
		System.out.println("result Query Id is ---->   " + result);
		return result;
	}

	@PostMapping("/query_repo/updateQueryRepo/{queryid}")
	@ResponseBody
	public String updateQuerybyid(@PathVariable String queryid, QueryRepo eqr) {
		String result = null;
		System.out.println("rescived query id is --->  " + queryid);
		result = QueryRepoManager.edirrecordbyid(queryid, eqr);
		System.out.println("Generated Query Id is ---->   " + result);
		return result;
	}

	@PostMapping("/query_repo/delete")
	public @ResponseBody String DeleteQuerybyId(@RequestParam String queryId) {
		Map<String, Object> result = new HashMap<>();
		System.out.println("the requested query id is --->  " + queryId);

		String res = QueryRepoManager.deleterepo(queryId);
		System.out.println("res --->   "+res);
		
		result.put("executed_cmd", "DELETE");
		result.put("sign", "DONE");
		result.put("effected_row", 1);
		result.put("message", "[DELETE] executed. Rows affected: 1");

		return res;
	}

}
