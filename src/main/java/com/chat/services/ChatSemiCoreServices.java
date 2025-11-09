package com.chat.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.db.DBConnecter;
import com.chat.utils.ChatTimeUtils;
 
@Service
public class ChatSemiCoreServices {

	@Autowired
	public ChatTimeUtils ctu;

	public String checkCTID(String ctid) {
		String result = null;
		String query = DBConnecter.getdata("CTAP1027");
		LinkedList<String> data = new LinkedList<String>();
		data.add(ctu.nvl(ctid, "NO_DATA"));
		System.out.println("[ChatSemiCoreServices] [checkCTID] The recived data is ctid [ " + data.toString()
				+ " ] query [ " + query + " ]");
		JSONObject finaldata = new JSONObject();
		int sdatacnt = 0;
		try (Connection c = DBConnecter.getcon();
				PreparedStatement ps = DBConnecter.safequeryvaluesetter(query, data, c);
				ResultSet rs = ps.executeQuery();) {
			JSONArray jar = new JSONArray();
			if (rs.next()) {
				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					jar.put(rs.getString(i));
					sdatacnt++;
				}
				finaldata.put("status", "DONE");
				finaldata.put("sdata_cnt", sdatacnt);
				finaldata.put("Suggestion_data", jar);
			} else {
				finaldata.put("status", "NLDT");
				finaldata.put("sdata_cnt", sdatacnt);
				finaldata.put("Suggestion_data", "NO_RCD_FND");
			}
		} catch (Exception e) {
			System.out.println(
					"[ChatSemiCoreServices] [checkCTID] Error while getting the sugegstion -->  [ " + e + " ] . ");
			finaldata.put("status", "FAIL");
			finaldata.put("sdata_cnt", sdatacnt);
			finaldata.put("Suggestion_data", "Issue In search");
		}
		System.out.println("[ChatSemiCoreServices] [checkCTID] The Suggested Data for the ChatId [ " + ctid
				+ " ] is [ " + finaldata + " ] .");
		result=finaldata.toString();
		return result;
	}

}
