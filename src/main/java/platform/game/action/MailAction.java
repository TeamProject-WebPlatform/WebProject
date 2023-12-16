package platform.game.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailAction {
    private JavaMailSender javaMailSender;

    public MailAction(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String toName, String subject, String content) {
        System.out.println("호출됨");

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			mimeMessage.addRecipient(
					RecipientType.TO,
					new InternetAddress(toEmail, toName, "utf-8"));
			mimeMessage.setSubject(subject, "utf-8");
			mimeMessage.setText(content, "utf-8", "html");
			
			mimeMessage.setSentDate(new Date());
			
			javaMailSender.send(mimeMessage);
			
			System.out.println("전송완료");

		} catch (MailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
