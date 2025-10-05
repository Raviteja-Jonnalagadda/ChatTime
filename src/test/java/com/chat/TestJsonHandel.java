package com.chat;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestJsonHandel {
	public static void main(String[] args) {
		String a = "{\"query_data\":[{\"CNT\":0}],\"executed_cmd\":\"SELECT\",\"sign\":\"DONE\",\"effected_row\":1,\"message\":\"[SELECT] executed. Rows fetched: 1\"}";
		JSONObject jo = new JSONObject(a);
		JSONArray ja = jo.getJSONArray("query_data");
		System.out.println(ja);
		JSONObject qdt = ja.getJSONObject(0);
		System.out.println(qdt);
		int val =qdt.getInt("CNT");
		System.out.println(val);
	}
}
