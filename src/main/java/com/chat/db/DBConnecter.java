package com.chat.db;

import java.sql.*;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class DBConnecter {
	private static Logger log = Logger.getLogger("DBConnecter.class");
	private static ResourceBundle rb = ResourceBundle.getBundle("dbdetails");

	public static Connection getcon() {
		try {
			Class.forName(rb.getString("dbdrv"));
			return DriverManager.getConnection(rb.getString("dburl"), rb.getString("dbunm"), rb.getString("dbpwd"));
		} catch (Exception e) {
			System.err.println("[DBConnecter] DB Connection Error: " + e);
			return null;
		}
	}

	public static void closeconnection(Connection cn) {
		try {
			if (cn != null)
				cn.close();
		} catch (Exception e) {
			System.err.println("[DBConnecter] Error closing DB connection: " + e);
		}
	}

	public static void close(AutoCloseable... resources) {
		for (AutoCloseable res : resources) {
			try {
				if (res != null)
					res.close();
			} catch (Exception e) {
				System.err.println("[DBConnecter] Error closing resource: " + e);
			}
		}
	}

	public static String getdata(String qid) {
		JSONObject response = new JSONObject();
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		System.out.println("[DBConnecter] [getdata] qid --->   " + qid);
		try {
			if (qid == null || qid.trim().isEmpty()) {
				qid = "CTAP1002"; // Query ID is null
			}

			cn = getcon();
			ps = cn.prepareStatement("SELECT COUNT(*) FROM QUERY_MASTER WHERE QUERY_ID = ?");
			ps.setString(1, qid);
			rs = ps.executeQuery();

			int count = 0;
			if (rs.next())
				count = rs.getInt(1);
			close(rs, ps);
			System.out.println("[DBConnecter] [getdata] count of qid is --->   " + count);
			if (count == 0) {
				qid = "CTAP1003";
			} else if (count > 1) {
				qid = "CTAP1005";
			}

			System.out.println("[DBConnecter] [getdata] getting the query type");
			ps = cn.prepareStatement("SELECT QUERY_TYPE FROM QUERY_MASTER WHERE QUERY_ID = ?");
			ps.setString(1, qid);
			rs = ps.executeQuery();
			System.out.println("[DBConnecter] [getdata] extracting the query and query type");
			String qrtype = null;

			if (rs.next()) {
				qrtype = rs.getString("QUERY_TYPE");
			}
			System.out.println("[DBConnecter] [getdata] query type --->  " + qrtype);
			if (qrtype.equalsIgnoreCase("BREAKED_QUERY")) {
				String result = null;
				String qclm = null;
				String qtbl = null;
				String qcond = null;
				String query = null;
				String rtype = null;
				ps = cn.prepareStatement("SELECT CRUD_TYPE FROM Query_Master WHERE Query_ID = ?");
				ps.setString(1, qid);
				rs = ps.executeQuery();
				String crud_typ = null;
				if (rs.next()) {
					System.out.println("[DBConnecter] [getdata] in the if rs");
					crud_typ = rs.getString("CRUD_TYPE");
				}
				System.out.println("[DBConnecter] [getdata] got the crud type for breaked query --->  " + crud_typ);
				ps = cn.prepareStatement(
						"SELECT Query_column,Query_Table,Query_conditions,Return_type FROM Query_Master WHERE Query_ID = ?");
				ps.setString(1, qid);
				rs = ps.executeQuery();
				if (rs.next()) {
					qclm = rs.getString("Query_column");
					qtbl = rs.getString("Query_Table");
					qcond = rs.getString("Query_conditions");
					rtype = rs.getString("Return_type");
				}
				System.out.println("[DBConnecter] [getdata] got the query paramaters");
				if (qclm.trim().isEmpty() || qtbl.trim().isEmpty() || qclm.equalsIgnoreCase("NA")
						|| qtbl.equalsIgnoreCase("NA") || qcond.equalsIgnoreCase("NA")) {
					System.out.println("[DBConnecter] [getdata] some of the field are missing for breaked query");
				}
				System.out.println("[DBConnecter] [getdata] starting the switch stattement ");
				switch (crud_typ) {
				case "SELECT":
					if (qclm.length() > 0 && qtbl.length() > 0 || qcond.length() > 0) {
						query = crud_typ + " " + qclm + " FROM " + qtbl + " WHERE " + qcond;
					}
					break;
				case "INSERT":
					if (qclm.length() > 0 && qtbl.length() > 0) {
						query = crud_typ + " " + " INTO " + qtbl + " " + qclm;
					}
					break;
				case "UPDATE":
					if (qclm.length() > 0 && qtbl.length() > 0 || qcond.length() > 0) {
						query = crud_typ + " " + qtbl + " SET  " + qclm + "  WHERE " + qcond;
					}
					break;
				case "DELETE":
					if (qclm.length() > 0 && qtbl.length() > 0 || qcond.length() > 0) {
						query = crud_typ + " " + qtbl + " WHERE " + qcond;
					}
					break;
				}
				if (!query.trim().isEmpty() || query.length() > 0) {
					try {
						System.out.println("[DBConnecter] [getdata] final query --->  " + query);
						if (rtype.equalsIgnoreCase("EXECUTE")) {
							result = SmartCrudEngine.dbexecuter(query, crud_typ);
						} else {
							result = query;
						}
					} catch (Exception e) {
						response.put("status", "FAIL");
						response.put("message", "Error during DBConnecter.getdata: " + e.getMessage());
						result = response.toString();
					} finally {
						close(rs, ps);
						closeconnection(cn);
					}
				}

				return result;
			}

			ps = cn.prepareStatement("SELECT QUERY, CRUD_TYPE, Return_type FROM QUERY_MASTER WHERE QUERY_ID = ?");
			ps.setString(1, qid);
			rs = ps.executeQuery();
			System.out.println("extracting the query and query type");
			String query = null;
			String qtype = null;
			String rtype = null;
			String result = null;

			if (rs.next()) {
				query = rs.getString("QUERY");
				qtype = rs.getString("CRUD_TYPE");
				rtype = rs.getString("Return_type");

			} else {
				qid = "CTAP1006";
				close(rs, ps);
				ps = cn.prepareStatement("SELECT QUERY, CRUD_TYPE FROM QUERY_MASTER WHERE QUERY_ID = ?");
				ps.setString(1, qid);
				rs = ps.executeQuery();
				if (rs.next()) {
					query = rs.getString("QUERY");
					qtype = rs.getString("CRUD_TYPE");
				} else {
					response.put("status", "FAIL");
					response.put("message", "Unable to process even fallback query ID");
					return response.toString();
				}
			}

			if (!qtype.equalsIgnoreCase("INSERT") && !qtype.equalsIgnoreCase("SELECT")
					&& !qtype.equalsIgnoreCase("UPDATE") && !qtype.equalsIgnoreCase("DELETE")) {
				System.out.println("CTAP1004" + qtype);
				qid = "CTAP1004";
				close(rs, ps);
				ps = cn.prepareStatement("SELECT QUERY, CRUD_TYPE FROM QUERY_MASTER WHERE QUERY_ID = ?");
				ps.setString(1, qid);
				rs = ps.executeQuery();
				if (rs.next()) {
					query = rs.getString("QUERY");
					qtype = rs.getString("CRUD_TYPE");
				}
			}
			System.out.println("[DBConnecter] [getdata] query is --->   " + query);
			System.out.println("[DBConnecter] [getdata] query type is --->  " + qtype);
			if (rtype.equalsIgnoreCase("EXECUTE")) {
				result = SmartCrudEngine.dbexecuter(query, qtype);
			} else {
				result = query;
			}
			// System.out.println(result); 
			return result;

		} catch (Exception e) {
			response.put("status", "FAIL");
			response.put("message", "Error during DBConnecter.getdata: " + e.getMessage());
			return response.toString();
		} finally {
			close(rs, ps);
			closeconnection(cn);
		}
	}

	public static void main(String[] args) {
		System.out.println(getdata("CTAP1022"));
	}
}
