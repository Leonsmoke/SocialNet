package socialNet.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import socialNet.Entity.UserEntity;
import socialNet.Service.MailService;

import static socialNet.constant.GreetingsMessage.TICKET_SUBJECT;
import static socialNet.constant.pages.FEEDBACK_PAGE;

@Controller
@PreAuthorize("hasAuthority('USER')||hasAuthority('ADMIN')")
public class FeedbackController {

    @Autowired
    MailService mailService;

    @Value("${spring.mail.username}")
    private String username;

    @GetMapping("/feedback")
    public String getFeedback(Model model, @AuthenticationPrincipal UserEntity currentUser) {
        model.addAttribute("currentUser",currentUser);
        return FEEDBACK_PAGE;
    }

    @PostMapping("/feedback")
    public String sendFeedbackTicket(Model model, @AuthenticationPrincipal UserEntity currentUser, @RequestParam String ticketText){
        model.addAttribute("currentUser",currentUser);
        mailService.send(username,TICKET_SUBJECT+currentUser.getUsername(),ticketText);
        return FEEDBACK_PAGE;
    }
}
