package com.chat.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chat.db.DBConnecter;

@Component
public class ChatTimeUserRegister {

	@Autowired
	private ChatUtility cu;
	@Autowired
	ChatTimeCoreUtility ccu;

	@SuppressWarnings("static-access")
	public String storedetails(JSONObject val) {
		String fname = val.optString("fnm", "NO_DATA");
		String emaid = val.optString("emd", "NO_DATA");
		String mobno = val.optString("mbn", "NO_DATA");
		String datbr = val.optString("dob", "NO_DATA");
		String gendr = val.optString("ged", "NO_DATA");
		String ctaid = val.optString("ctid", "NO_DATA");
		String paswd = val.optString("pwd", "NO_DATA");
		String pwhnt = val.optString("pwh", "NO_DATA");
		String snotp = val.optString("sgnotp", "NO_DATA");
		System.out.println("[ChatTimeUserRegister] [storedetails] The Fname -->  " + fname);
		System.out.println("[ChatTimeUserRegister] [storedetails] The emaid -->  " + emaid);
		System.out.println("[ChatTimeUserRegister] [storedetails] The mobno -->  " + mobno);
		System.out.println("[ChatTimeUserRegister] [storedetails] The datbr -->  " + datbr);
		System.out.println("[ChatTimeUserRegister] [storedetails] The gendr -->  " + gendr);
		System.out.println("[ChatTimeUserRegister] [storedetails] The ctaid -->  " + ctaid);
		System.out.println("[ChatTimeUserRegister] [storedetails] The paswd -->  " + paswd);
		System.out.println("[ChatTimeUserRegister] [storedetails] The pwhnt -->  " + pwhnt);
		System.out.println("[ChatTimeUserRegister] [storedetails] The snotp -->  " + snotp);
		if (fname.equalsIgnoreCase("NO_DATA") || emaid.equalsIgnoreCase("NO_DATA") || mobno.equalsIgnoreCase("NO_DATA")
				|| datbr.equalsIgnoreCase("NO_DATA") || gendr.equalsIgnoreCase("NO_DATA")
				|| ctaid.equalsIgnoreCase("NO_DATA") || paswd.equalsIgnoreCase("NO_DATA")
				|| pwhnt.equalsIgnoreCase("NO_DATA") || snotp.equalsIgnoreCase("NO_DATA")) {
			return "FAIL~" + cu.getemsg("E001");
		} else {
			if(!checkuser(emaid,paswd)) {
				return "FAIL~" + cu.getemsg("E011");
			}
			System.out.println(
					"[ChatTimeUserRegister] [storedetails] Checking the Eamilid and phonenumber match to avoid the multiple accounts for the same users");
			String regquery = DBConnecter.getdata("CTAP1021");
			System.out.println(
					"[ChatTimeUserRegister] [storedetails] Insert query for Registing the user is -->  " + regquery);
			String CTUID = ctaid + "_" + ccu.getChatTimeUserId();
			Connection c = null;
			PreparedStatement ps = null;
			try {
				c = DBConnecter.getcon();
				ps = c.prepareStatement(regquery);
				ps.setString(1, ctaid);
				ps.setString(2, CTUID);
				ps.setString(3, fname);
				ps.setString(4, mobno);
				ps.setString(5, emaid);
				ps.setString(6, gendr);
				ps.setString(7, datbr);
				ps.setString(8, "ChatTimeApplication");
				ps.setString(9, paswd);
				ps.setString(10, pwhnt);
				ps.setString(11, snotp);
				int effectedrow = ps.executeUpdate();
				System.out.println("No of rows inserted is --->  " + effectedrow);
				if (effectedrow == 1) {
					System.out.println("[ChatTimeUserRegister] [storedetails] Registreg Sucessfully");
					return "DONE";
				} else {
					System.out.println(
							"[ChatTimeUserRegister] [storedetails] Issue In registrection the effected rows is --->  "
									+ effectedrow);
					return "FAIL~" + cu.getemsg("E005");
				}
			} catch (Exception e) {
				System.out.println(
						"[ChatTimeUserRegister] [storedetails] Error While Registrectioon the user --->   " + e);
				return "FAIL~" + cu.getemsg("E006");
			} finally {
				DBConnecter.close(c, ps);
			}
		}
	}

	public static boolean checkuser(String email, String phonenumber) {
		boolean result = false;
		LinkedList<String> qrval = new LinkedList<String>();
		qrval.add(email);
		qrval.add(phonenumber);
		String regquery = DBConnecter.getdata("CTAP1026");
		   try (Connection c = DBConnecter.getcon();
				PreparedStatement ps = DBConnecter.safequeryvaluesetter(regquery, qrval, c);
				ResultSet rs = ps.executeQuery();) {
			while(rs.next()) {
				int cnt = rs.getInt("CNT");
				   System.out.println("[ChatTimeUserRegister] [checkuser] The count of users with the combination is --> [ "+cnt+" ]");
				if(cnt==0) {
					result=true;
				}
				else if(cnt == 1) {
				   System.out.println("[ChatTimeUserRegister] [checkuser] The combination of user already exiest [ "+cnt+" ]");
					result=false;
				}
			}

		} catch (Exception e) {

		}

		return result;
	}

	public static void main(String[] args) {
		ChatTimeUserRegister ct = new ChatTimeUserRegister();
		ct.storedetails(null);

	}

}
