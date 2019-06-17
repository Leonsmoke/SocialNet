package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import socialNet.Entity.UserEntity;
import socialNet.Service.MessageService;
import socialNet.repos.MessageRepo;
import socialNet.repos.UserRepo;

import static socialNet.constant.pages.*;

@Controller
@RequestMapping("/messages")
@PreAuthorize("hasAuthority('USER')||hasAuthority('ADMIN')")
public class MessageController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private MessageService messageService;

    @GetMapping("")
    public String user(Model model, @AuthenticationPrincipal UserEntity user){
        model = messageService.getUserDialogs(user,model);
        return MESSAGE_PAGE;
    }

    @GetMapping("/dialog{id}")
    public String dialog(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int id){
        model = messageService.getDialog(model,id,currentUser);
        return DIALOG_PAGE;
    }

    @PostMapping("/dialog{id}")
    public String sendMessage(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int id, @RequestParam String textMessage){
        messageService.sendMessage(currentUser,id,textMessage);
        return REDIRECT+DIALOG_LINK+id;
    }

}
