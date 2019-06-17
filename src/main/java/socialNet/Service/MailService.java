package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    ValidationService validationService;

    @Autowired
    private JavaMailSender mailSender;
    public void send(String email_to,String subject,String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(email_to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        if (validationService.checkValidText(message)){
            mailSender.send(mailMessage);
        }
    }
}
