package com.chat.Admin.Services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.json.JSONObject;

import com.chat.db.DBConnecter;
import com.chat.db.SmartCrudEngine;
import com.chat.db.dto.QueryRepo;

public class QueryRepoManager {

	public static String addrecord(QueryRepo repodata) {
		String result = null;
		JSONObject inscmd = new JSONObject();
		JSONObject querydet = new JSONObject();

		querydet.put("Query_ID", repodata.getqueryId());
		querydet.put("Query", repodata.getQuery());
		querydet.put("Query_Dec", repodata.getDec());
		querydet.put("Query_param_count", repodata.getQry_prm_cnr());
		querydet.put("Query_column", repodata.getQry_prm_cnr());
		querydet.put("Query_Table", repodata.getQry_table());
		querydet.put("Query_conditions", repodata.getQry_cnd());
		querydet.put("Query_type", repodata.getQry_typ());
		querydet.put("CRUD_TYPE", repodata.getCrud_type());
		querydet.put("RETURN_TYPE", repodata.getReturn_type());
		querydet.put("Project_Id", repodata.getPrj_id());
		querydet.put("Maker_Id", repodata.getMkr_id());
		querydet.put("Maker_dttm", getTime());

		inscmd.put("main_sign", "INSERT");
		inscmd.put("qtn", "Query_Master");
		inscmd.put("qdt", querydet);
		inscmd.put("rtp", "execute");

		try {
			result = SmartCrudEngine.executer(inscmd);
		} catch (Exception e) {
			JSONObject errormsg = new JSONObject();
			errormsg.put("sign", "FAIL");
			errormsg.put("message", e.toString());
			errormsg.put("effected_row", "0");
			result = errormsg.toString();
		}
		return result;

	}

	public static String getTime() {
		LocalDate ld = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		String formattedDate = ld.format(formatter);
		System.out.println(formattedDate);
		return formattedDate;
	}

	public static String getrecordforedit(String qid, String actqid) {
		String result = null;
		String query = null;
		JSONObject res = new JSONObject();
		JSONObject req = new JSONObject();
		query = DBConnecter.getdata(qid);
		if (query.trim().isEmpty() || query.length() <= 0 || query == null) {
			res.put("status", "FAIL");
			res.put("message", "Result is ERROR");
			res.put("data", res);
			result = res.toString();
		} else {
			query = query.replace("?", "'" + actqid + "'");
			System.out.println("query is --->  " + query);
			try {
				result = SmartCrudEngine.dbexecuter(query, "SELECT");
			} catch (Exception e) {
				res.put("status", "FAIL");
				res.put("message", "Result is ERROR");
				res.put("data", e);
				result = res.toString();
			}
		}
		System.out.println(result.toUpperCase());
		return result.toUpperCase();
	}

	public static String edirrecordbyid(String qid, QueryRepo repodata) {
		String result = null;
		String query = null;
		JSONObject res = new JSONObject();
		JSONObject req = new JSONObject();
		JSONObject insert = new JSONObject();

		String sft_query = DBConnecter.getdata("CTAP1015");
		if (sft_query.trim().isEmpty() || sft_query.length() <= 0 || sft_query == null) {
			res.put("status", "FAIL");
			res.put("message", "Result is ERROR");
			res.put("data", res);
			result = res.toString();
		} else {
			System.out.println("query  -->   " + sft_query);
			sft_query = sft_query.replace("?", "'" + qid + "'");
			sft_query = sft_query.replace("v_act_type", "'UPDATE'");
			try {
				result = SmartCrudEngine.dbexecuter(sft_query, "INSERT");
				JSONObject sft_req = new JSONObject(result);
				System.out.println("insertion record is --->  " + sft_req);
				if (sft_req.has("sign") && sft_req.has("effected_row")) {
					if (sft_req.optString("sign", "FAIL").equalsIgnoreCase("DONE")
							&& sft_req.optInt("effected_row", 0) == 1) {
						insert.put("QUERY", repodata.getQuery());
						insert.put("QUERY_DEC", repodata.getDec());
						insert.put("QUERY_PARAM_COUNT", repodata.getQry_prm_cnr());
						insert.put("QUERY_COLUMN", repodata.getQry_clm());
						insert.put("QUERY_TABLE", repodata.getQry_table());
						insert.put("QUERY_CONDITIONS", repodata.getQry_cnd());
						insert.put("QUERY_TYPE", repodata.getQry_typ());
						insert.put("CRUD_TYPE", repodata.getCrud_type());
						insert.put("PROJECT_ID", repodata.getPrj_id());
						insert.put("MAKER_ID", repodata.getMkr_id());
						insert.put("MAKER_DTTM", getTime());
						insert.put("RETURN_TYPE", repodata.getReturn_type());

						req.put("main_sign", "UPDATE");
						req.put("qtn", "QUERY_MASTER");
						req.put("qdt", insert);
						req.put("qcn", "QUERY_ID = '" + qid + "'");
						req.put("rtp", "execute");

						System.out.println("final req is --->   " + req.toString(3));
						try {
							result = SmartCrudEngine.executer(req);
						} catch (Exception e) {
							res.put("status", "FAIL");
							res.put("message", "Result is ERROR");
							res.put("data", e);
							result = res.toString();
						}
					}
				}
			} catch (Exception e) {
				res.put("status", "FAIL");
				res.put("message", "Result is ERROR");
				res.put("data", e);
				result = res.toString();
			}
		}

		return result;
	}

	public static String deleterepo(String qid) {
		String result = null;
		JSONObject res = new JSONObject();
		String query = DBConnecter.getdata("CTAP1015");
		if (query.trim().isEmpty() || query.length() <= 0 || query == null) {
			res.put("status", "FAIL");
			res.put("message", "Result is ERROR");
			res.put("data", res);
			result = res.toString();
		} else {
			System.out.println("query  -->   " + query);
			query = query.replace("?", "'" + qid + "'");
			query = query.replace("v_act_type", "'DELETE'");
			try {
				result = SmartCrudEngine.dbexecuter(query, "INSERT");
				JSONObject req = new JSONObject(result);
				System.out.println("insertion record is --->  " + req);
				if (req.has("sign") && req.has("effected_row")) {
					if (req.optString("sign", "FAIL").equalsIgnoreCase("DONE") && req.optInt("effected_row", 0) == 1) {
						query = DBConnecter.getdata("CTAP1016");
						if (query.trim().isEmpty() || query.length() <= 0 || query == null) {
							res.put("status", "FAIL");
							res.put("message", "Result is ERROR");
							res.put("data", res);
							result = res.toString();
						} else {
							query = query.replace("?", "'" + qid + "'");
							result = SmartCrudEngine.dbexecuter(query, "DELETE");
						}
					}

				}

			} catch (ClassNotFoundException e) {
				System.out.println("Error while deleting the repo --->   " + e);
				e.printStackTrace();
			}

		}

		return result;
	}

	public static void main(String[] args) {
//		/getrecordforedit("CTAP1013");
		// QueryRepo qr = new QueryRepo();
		// System.out.println(edirrecordbyid("CTAP1014", qr));
		System.out.println(deleterepo("CTAP1014"));
	}
}
