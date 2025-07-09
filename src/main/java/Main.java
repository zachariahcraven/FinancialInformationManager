import jakarta.mail.MessagingException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("What budget file do you want to get data from?");
        String filename = scanner.next();
        System.out.println("""Application will not send email until you have confirmed correct data\n
                All must be correct\n
                Email From address\n
                Email To address\n
                App Password (See Readme.md for set up)""");
        System.out.println("Enter from address:");
        String from = scanner.next();
        System.out.println("Enter app password");
        String password = scanner.next();
        System.out.println("Enter emails to addresses");
        String to = scanner.next();

        MailSender emailSender = new MailSender("zachp25@icloud.com",
                "code52e@gmail.com",
                "hnss xxgj audz nbqa",
                "july2025");
        //show preview
        //are these the emails that you want to send too
        //Send email? yes no
        emailSender.sendMail();

    }
}
