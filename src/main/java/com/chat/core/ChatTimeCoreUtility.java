package com.chat.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.chat.db.DBConnecter;
import com.chat.mail.EmailHandler;

@Component 
public class ChatTimeCoreUtility {
	private ChatUtility cu;

    public static String ctidCheck(String ctid) {
        try {
            Thread.sleep(1000);
            String jsonquery = DBConnecter.getdata("CTAP1019");
            System.out.println("[ChatTimeCoreUtility] [ctidCheck] Query: " + jsonquery);
            Connection cs = DBConnecter.getcon();
            PreparedStatement ps = cs.prepareStatement(jsonquery);
            ps.setString(1, ctid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int check = rs.getInt("CTID_CNT");
                DBConnecter.close(ps, rs, cs);
                System.out.println("[ChatTimeCoreUtility] [ctidCheck] Count: " + check);
                return (check == 0) ? "DONE" : "NO";
            }
        } catch (Exception e) {
            System.out.println("[ChatTimeCoreUtility] [ctidCheck] Error: " + e);
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

        System.out.println("Sign: " + sign + ", To: " + tomail + ", Subject: " + subject+" emailmsg: "+emailmsg);

        try {
            if (sign.equalsIgnoreCase("ForgotPasswordOTP")) {
                String checkquery = DBConnecter.getdata("CTAP1020");
                Connection cs = DBConnecter.getcon();
                PreparedStatement ps = cs.prepareStatement(checkquery);
                ps.setString(1, tomail);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) check = rs.getInt("Email_Id");
                DBConnecter.close(ps, rs, cs);

                if (check >= 1) {
                    return EmailHandler.SentOTP("ForgotPasswordOTP", tomail, null) ? "DONE" : "EMAIL_FAIL";
                } else {
                    return "NO";
                }
            } else if (sign.equalsIgnoreCase("SIGNUPOTP")) {
                return EmailHandler.SentOTP("SignUpOTP", tomail, name) ? "DONE" : "EMAIL_FAIL";
            } else {
                return "FAIL";
            }
        } catch (Exception e) {
            System.out.println("[ChatTimeCoreUtility] [SendEmail] Error: " + e);
            return "FAIL";
        }
    }

    public static String getChatTimeUserId() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String VeriftOTP(String sign, String otp, String email) {
        String result;
        String query = DBConnecter.getdata("CTAP1023");
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String DBOTP = null;
        String DBDTTM = null;

        try {
            c = DBConnecter.getcon();
            ps = c.prepareCall(query);
            ps.setString(1, email);
            ps.setString(2, sign.equalsIgnoreCase("SIGNUPOTPVERIFY") ? sign.substring(0, sign.length() - 6) : sign);
            rs = ps.executeQuery();

            while (rs.next()) {
                DBOTP = rs.getString("OTP");
                DBDTTM = rs.getString("OTP_DB_DTTM");
            }

            if (DBOTP == null || !DBOTP.equals(otp)) {
                result = "OTPMISSMATCH";
            } else if (!isOtpExpired(DBDTTM)) {
                result = "OTPTIMEOUT";
            } else {
                result = "DONE";
            }
        } catch (Exception e) {
            System.out.println("[ChatTimeCoreUtility] [VeriftOTP] Error: " + e);
            result = "FAIL";
        } finally {
            DBConnecter.close(c, ps, rs);
        }
        return result;
    }

    public static boolean isOtpExpired(String dbTimeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dbTime = LocalDateTime.parse(dbTimeStr, formatter);
            Duration duration = Duration.between(dbTime, LocalDateTime.now());
            return duration.toMinutes() < 5;
        } catch (Exception e) {
            System.err.println("[ChatTimeCoreUtility] [isOtpExpired] Error parsing date: " + e.getMessage());
            return false;
        }
    }

    public String PasswordUpdate(String email, String newotp, String pword) {
        System.out.println("[ChatTimeCoreUtility] [PasswordUpdate] Email: " + email);

        String query = DBConnecter.getdata("CTAP1024");
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        LinkedList<String> val = new LinkedList<>();

        try {
            val.add((email != null) ? email : "NO_DATA");
            c = DBConnecter.getcon();
            ps = DBConnecter.safequeryvaluesetter(query, val, c);
            rs = ps.executeQuery();

            while (rs.next()) {
                count = rs.getInt("Count");
            }
            DBConnecter.close(c, ps, rs);

            if (count == 0) {
                return "FAIL~" + cu.getemsg("E007");
            } else if (count > 1) {
                return "FAIL~" + cu.getemsg("E008");
            }

            String updatequery = DBConnecter.getdata("CTAP1025");
            c = DBConnecter.getcon();
            val.clear();
            val.add(pword);
            val.add(newotp);
            val.add(email);

            ps = DBConnecter.safequeryvaluesetter(updatequery, val, c);
            int effectedrows = ps.executeUpdate();
            DBConnecter.close(c, ps, rs);

            return (effectedrows > 0) ? "DONE" : "FAIL~" + cu.getemsg("E010");

        } catch (Exception e) {
            System.out.println("[ChatTimeCoreUtility] [PasswordUpdate] Error: " + e);
            return "FAIL~" + cu.getemsg("E009");
        } finally {
            DBConnecter.close(c, ps, rs);
        }
    }

	public static void main(String[] args) {
		String a = "SIGNUPOTPVERIFY";
		System.out.println(a.substring(0, a.length() - 6));

	}

}
