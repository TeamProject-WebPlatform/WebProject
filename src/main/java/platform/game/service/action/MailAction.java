package platform.game.service.action;

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

<<<<<<< HEAD
    public void sendMail(String toEmail, String toName, String subject, String content) {
=======
    public Boolean sendMail(String toEmail, String toName, String subject, String content) {
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
        //System.out.println("호출됨");

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			mimeMessage.addRecipient(
					RecipientType.TO,
					new InternetAddress(toEmail, toName, "utf-8"));
			mimeMessage.setSubject(subject, "utf-8");
			mimeMessage.setText(content, "utf-8", "html");
			
			mimeMessage.setSentDate(new Date());
			
			javaMailSender.send(mimeMessage);
<<<<<<< HEAD
			
		} catch (MailException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
=======
			System.out.println("메일 전송 완료");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
		}
	}
    public int createNumber(){
        int number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
		return number;
    }
}
