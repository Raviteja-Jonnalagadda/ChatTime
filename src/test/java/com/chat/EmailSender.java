package com.chat;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

	public static String SendGmail(String tomail, String subject,String msg) {
		String result = null;
        final String senderEmail = "raviteja032766@gmail.com";
        final String senderPassword = "kdtm xztk elpl ztvy";

        String recipientEmail = tomail;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // TLS port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "Mr.Introvert"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);
            result="✅ Email sent successfully!";
        } catch (Exception e) {
            result="❌ Failed to send email";
            e.printStackTrace();
        }
		return result;
	}
	
    public static void main(String[] args) {
    	String tomail="raviteja032766@gmail.com";
    	String sub="Namastha Guru Sample Mail From Java";
    	String msg="hi i am sending this mail for the mail is because to send so you got the mail becasue it is the mail so the mail is finally the mail\n\n\n\n\nThanks for the thanks a lot thanks of the thanks ";
        System.out.println(SendGmail(tomail, sub, msg));

    }
}

