import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import javax.security.auth.Subject;
import java.io.FileNotFoundException;
import java.util.Properties;

public class EmailSender {
    //TODO: change to be more dynamic later on
    private static final String EMAIL_FROM = "code52e@gmail.com";
    private static final String EMAIL_TO = "zachp25@icloud.com";
    private static final String APP_PASSWORD = "hnss xxgj audz nbqa";

    public static void main(String[] args) throws MessagingException, FileNotFoundException {
        ContentManager contentManager = new ContentManager("july2025");
        contentManager.pullContent();

        String subject = contentManager.getSubject();
        String text = contentManager.getText();
        Message message = new MimeMessage(getEmailSession());

        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO));
        message.setSubject("FIM testing");
        message.setText("FIM testing text section");
        //Transport.send(message);

    }

    private static Session getEmailSession() {
        return Session.getInstance(getGmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
            }
        });
    }

    private static Properties getGmailProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return prop;
    }
}