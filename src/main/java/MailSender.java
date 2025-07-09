import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.FileNotFoundException;
import java.util.Properties;

public class MailSender {
    private final String EMAIL_TO;
    private final String EMAIL_FROM;
    private final String APP_PASSWORD;
    private final String FILE_NAME;

    public MailSender(String emailTo, String emailFrom, String appPassword, String fileName) {
        this.EMAIL_TO = emailTo;
        this.EMAIL_FROM = emailFrom;
        this.APP_PASSWORD = appPassword;
        this.FILE_NAME = fileName;
    }

    public void sendMail() {
        try {
            ContentManager contentManager = new ContentManager(FILE_NAME);
            contentManager.pullContent();
            contentManager.getEmailPreview();
            Message message = new MimeMessage(getEmailSession());
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO));
            message.setSubject(contentManager.getSubject());
            message.setText(contentManager.getText());
            Transport.send(message);
            System.out.println("Email Sent");
        } catch (MessagingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Session getEmailSession() {
        return Session.getInstance(getGmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
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