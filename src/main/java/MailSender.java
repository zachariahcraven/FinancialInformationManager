import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.FileNotFoundException;
import java.util.Properties;

public class MailSender {

    public void sendMail(String emailTo, String emailFrom, String appPassword, String fileName) {
        try {
            ContentManager contentManager = new ContentManager(fileName);
            contentManager.pullContent();
            contentManager.getEmailPreview();
            Message message = new MimeMessage(getEmailSession(emailFrom, appPassword));
            message.setFrom(new InternetAddress(emailFrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(contentManager.getSubject());
            message.setText(contentManager.getText());
            //Transport.send(message);
            //System.out.println("Email Sent");
        } catch (MessagingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Session getEmailSession(String emailFrom, String appPassword) {
        return Session.getInstance(getGmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, appPassword);
            }
        });
    }

    private Properties getGmailProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return prop;
    }
}