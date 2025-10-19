package com.chat.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chat.db.DBConnecter;
import com.chat.error.ErrorBean;
import com.chat.mail.EmailHandler;

@Component
public class ChatTimeCoreUtility {

	public static String ctidCheck(String ctid) {

		try {
			Thread.sleep(1000);
			String jsonquery = DBConnecter.getdata("CTAP1019");
			System.out.println("[ChatTimeCoreUtility] [ctidCheck] the recived check query is --->  " + jsonquery);
			Connection cs = DBConnecter.getcon();
			PreparedStatement ps = cs.prepareStatement(jsonquery);
			ps.setString(1, ctid);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int check = rs.getInt("CTID_CNT");
				DBConnecter.close(ps, rs, cs);
				System.out.println("[ChatTimeCoreUtility] [ctidCheck] count is -->  " + check);
				if (check == 0) {
					return "DONE";
				} else {
					return "NO";
				}
			}
		} catch (Exception e) {
			System.out.println("[ChatTimeCoreUtility] [ctidCheck] the issue is --->  " + e);
			return "FAIL";
		}
		return "FAIL";
	}

	public static String SendEmail(String sign, String msg) {
		JSONObject jo = new JSONObject(msg);
		String tomail = jo.optString("tomail");
		String subject = jo.optString("sub");
		String emailmsg = jo.optString("message");
		String name = jo.optString("name");
		int check = 0;
		System.out.println("The Sign is --> [ " + sign + " ] Tomail is -->  [ " + tomail + " ] Subject is --> [ "
				+ subject + " ] Email Message is --> [ " + emailmsg + " ] ");
		if (sign.equalsIgnoreCase("ForgotPasswordOTP")) {
			String checkquery = DBConnecter.getdata("CTAP1020");
			try {
				Connection cs = DBConnecter.getcon();
				PreparedStatement ps = cs.prepareStatement(checkquery);
				ps.setString(1, tomail);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					check = rs.getInt("Email_Id");
				}
				DBConnecter.close(ps, rs, cs);
				System.out.println("[ChatTimeCoreUtility] [ctidCheck] count is -->  " + check);
				if (check >= 1) {
					System.out.println(
							"[ChatTimeCoreUtility] [SendEmail] In the Forgot Password Flow For sending the OTP");
					boolean emailcheck = EmailHandler.SentOTP("ForgotPasswordOTP", tomail, null);
					if (emailcheck) {
						return "DONE";
					} else {
						return "EMAIL_FAIL";
					}
				} else {
					return "NO";
				}
			} catch (Exception e) {
				System.out.println("[ChatTimeCoreUtility] [SendEmail]  Error In Checking the Email " + e);
				return "FAIL";
			}
		} else if (sign.equalsIgnoreCase("SIGNUPOTP")) {
			boolean emailcheck = EmailHandler.SentOTP("SignUpOTP", tomail, name);
			if (emailcheck) {
				return "DONE";
			} else {
				return "EMAIL_FAIL";
			}
		} else {
			System.out.println("[ChatTimeCoreUtility] [SendEmail] Unknow Email Type Sent ");
			return "FAIL";
		}
	}

	@Autowired
	private ErrorBean eb;

	public JSONObject errormsg(String ecode) {
		JSONObject errjo = new JSONObject();
		errjo.put("ecode", ecode);
		String mname = "get" + ecode;
		try {
			System.out.println(ecode);
			System.out.println(mname);
			Method actmt = ErrorBean.class.getMethod(mname);
			System.out.println(actmt);
			String emsg = (String) actmt.invoke(eb);
			System.out.println(emsg);
			errjo.put("emessage", emsg);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			System.out.println("error mssg --> " + e);
			System.out.println("[ChatTimeCoreUtility] [ctaploginchecker] No such method for ecode: " + ecode);
			errjo.put("emessage", "Unknown error");
		}
		System.out.println("errormessage --> " + errjo);
		System.out.println("[ChatTimeCoreUtility] [ctaploginchecker] " + errjo.toString());
		return errjo;
	}

	public static String getChatTimeUserId() {
		String result = null;
		Date date = new Date(System.currentTimeMillis());
		String a = date.toString().trim();
		result = a.replace(' ', '-');
		return result;
	}

	public static String VeriftOTP(String sign , String otp , String email) {
		String result = null;
		String query = DBConnecter.getdata("CTAP1023");
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String DBOTP = null;
		String DBDTTM = null;
		String DTTM = null;
		System.out.println("[ChatTimeCoreUtility] [VeriftOTP] The recived Email is ---> "+email);
		System.out.println("[ChatTimeCoreUtility] [VeriftOTP] The recived sign is ----> "+sign);
		System.out.println("[ChatTimeCoreUtility] [VeriftOTP] The recived otp is -----> "+otp);
		try {
			c=DBConnecter.getcon();
			ps=c.prepareCall(query);
			ps.setString(1, email);
			ps.setString(2, sign.substring(0,sign.length()-6));
			rs = ps.executeQuery();
			while(rs.next()) {
				DBOTP=rs.getString("OTP");
				DBDTTM=rs.getString("OTP_DB_DTTM");
				DTTM=rs.getString("OTP_DTTM");
			}
			System.out.println("[ChatTimeCoreUtility] [VeriftOTP] The DBOTP -->  "+DBOTP);
			System.out.println("[ChatTimeCoreUtility] [VeriftOTP] The DBDTTM ->  "+DBDTTM);
			System.out.println("[ChatTimeCoreUtility] [VeriftOTP] The DTTM --->  "+DTTM);
			if(!DBOTP.equals(otp)) {
				System.out.println("[ChatTimeCoreUtility] [VeriftOTP] OTP MISS MATCH ");
				result= "OTPMISSMATCH";
			}
			else if(!isOtpExpired(DBDTTM)) {
				System.out.println("[ChatTimeCoreUtility] [VeriftOTP] OTP IS EXPIRED");
				result= "OTPTIMEOUT";
			}
			else if(DBOTP.equals(otp)&&isOtpExpired(DBDTTM)) {
				System.out.println("[ChatTimeCoreUtility] [VeriftOTP] Okay it is Fine Matched the OTP Before 5 min time OUT");
				result = "DONE";
			}
			else {
				System.out.println("[ChatTimeCoreUtility] [VeriftOTP] Unexpected Error Has occured");
				System.out.println("");
				result= "FAIL";
			}
		}
		catch(Exception e) {
			System.out.println("[ChatTimeCoreUtility] [VeriftOTP] Error in Verifing the OTP "+e);
			result = "FAIL";
		}
		finally {
			DBConnecter.close(c,ps,rs);
		}
		return result;
	}
	
	 public static boolean isOtpExpired(String dbTimeStr) {
	        try {
	            // Define the format of the input time string
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	            // Parse the string to LocalDateTime
	            LocalDateTime dbTime = LocalDateTime.parse(dbTimeStr, formatter);

	            // Get current system time
	            LocalDateTime now = LocalDateTime.now();

	            // Calculate time difference
	            Duration duration = Duration.between(dbTime, now);

	            // Return true if more than 5 minutes have passed
	            return duration.toMinutes() < 5;
	        } catch (Exception e) {
	            System.err.println("Error parsing date: " + e.getMessage());
	            return false; // Assume expired in case of parsing error
	        }
	    }

	public static void main(String[] args) {
		String a ="SIGNUPOTPVERIFY";
System.out.println(a.substring(0,a.length()-6));

	}

}
