package com.chat.mail;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.chat.db.DBConnecter;

public class EmailHandler {

	private static ResourceBundle emailrb = ResourceBundle.getBundle("emailconfig");

	public static String SendGmail(String tomail, String subject, String msg) {
		String result = null;
		final String senderEmail = "chattimeapplication@gmail.com";
		final String senderPassword = "bhlw bvpx rblf ahsh";

		String recipientEmail = tomail;
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587"); // TLS port
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail, "ChatTime"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			message.setSubject(subject);
			message.setContent(msg, "text/html; charset=utf-8");

			Transport.send(message);
			System.out.println("[EmailHandler] [SendGmail] ✅ Email sent successfully!");
			result = "DONE";
		} catch (Exception e) {
			System.out.println("[EmailHandler] [SendGmail] ❌ Failed to send email  " + e);
			e.printStackTrace();
			result = "FAIL";
		}
		return result;
	}

	public static boolean SentOTP(String sign, String tomail, String name) {
		String sub = null;
		String msg = null;
		if (sign.equalsIgnoreCase("ForgotPasswordOTP")) {
			System.out.println("[EmailHandler] [SentOTP] In the Send OTP");
			String otp = getOtp();
			System.out.println("[EmailHandler] [SentOTP] Gnerated OTP is -->  [ " + otp + " ]");
			boolean dbstor = StoreOTP(otp, tomail, "", sign, name);
			if (!dbstor) {
				System.out.println(
						"[EmailHandler] [SentOTP] Error in Storing the OTP in DB . please check the DB Connevctibity and try again");
				return false;
			}
			sub = emailrb.getString("ForgotPassword.Subject");
			String rawmsg = emailrb.getString("ForgotPassword.Message").replace("${OTP}", "" + otp + "")
					.replace("${CHAT_TIME_LINK}", emailrb.getString("ForgotPassword.RedirectionLink"));
			msg = rawmsg;
		} else if (sign.equalsIgnoreCase("SIGNUPOTP")) {
			System.out.println("[EmailHandler] [SentOTP] In the Send OTP");
			String otp = getOtp();
			System.out.println("[EmailHandler] [SentOTP] Gnerated OTP is -->  [ " + otp + " ]");
			boolean dbstor = StoreOTP(otp, tomail, "", sign, name);
			if (!dbstor) {
				System.out.println(
						"[EmailHandler] [SentOTP] Error in Storing the OTP in DB . please check the DB Connevctibity and try again");
				return false;
			}
			sub = emailrb.getString("SignUp.Subject");
			String rawmsg = emailrb.getString("SignUp.Message").replace("${OTP}", "" + otp + "").replace("${NAME}",
					name);
			msg = rawmsg;
		} else {
			System.out.println("[EmailHandler] [SentOTP] Unknown Sign Recived");
			return false;
		}
		if (SendGmail(tomail, sub, msg).equalsIgnoreCase("DONE")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean StoreOTP(String otp, String email, String mob, String otp_type, String name) {
		boolean result = false;
		Connection c = null;
		String query = DBConnecter.getdata("CTAP1022");
		Date d = new Date(System.currentTimeMillis());
		String dateval = d.toString().replace(" ", "-");
		PreparedStatement ps = null;
		try {
			c = DBConnecter.getcon();
			ps = c.prepareStatement(query);
			ps.setString(1, otp);
			ps.setString(2, email);
			ps.setString(3, mob);
			ps.setString(4, otp_type);
			ps.setString(5, dateval);
			ps.setString(6, name);
			int effectedrows = ps.executeUpdate();
			if (effectedrows == 1) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			System.out.println("Error While Storing the OTP in DB " + e);
			result = false;
		}
		return result;
	}

	public static String getOtp() {
		String result = null;
		SecureRandom sr = new SecureRandom();
		int number = sr.nextInt(900000) + 100000;
		System.out.println("[ChatTimeCoreUtility] [getOtp] " + number);
		result = String.valueOf(number);
		return result;
	}

	public static void main(String[] args) {
		String tomail = "raviteja032766@gmail.com";
		String sub = emailrb.getString("SignUp.Subject");
		String rawmsg = emailrb.getString("SignUp.Message").replace("${OTP}", "123456").replace("${NAME}", "Ravi Teja")
				.replace("${CHAT_TIME_LINK}", "http://localhost:8111/html/ChatLogin.html");

		String msg = rawmsg.replace("${OTP}", "123456").replace("${CHAT_TIME_LINK}",
				"http://localhost:8111/html/ChatLogin.html");
		// String msg="hi i am sending this mail for the mail is because to send so you
		// got the mail becasue it is the mail so the mail is finally the
		// mail\n\n\n\n\nThanks for the thanks a lot thanks of the thanks ";
		System.out.println(SendGmail(tomail, sub, msg));

	}

}
