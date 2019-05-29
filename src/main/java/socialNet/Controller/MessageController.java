package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import socialNet.Entity.MessageEntity;
import socialNet.Entity.UserEntity;
import socialNet.repos.MessageRepo;
import socialNet.repos.UserRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static socialNet.constant.pages.DIALOG_PAGE;
import static socialNet.constant.pages.MESSAGE_PAGE;

@Controller
@RequestMapping("/messages")
@PreAuthorize("hasAuthority('USER')")
public class MessageController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("")
    public String user(Model model, @AuthenticationPrincipal UserEntity user){
        model.addAttribute("currentUser",user);
        List<MessageEntity> allDialogs = messageRepo.findAllDialogs(user.getId());
        Set<UserEntity> users = new HashSet<>();
        for (MessageEntity message: allDialogs
             ) {
            if (message.getSender_id()==user.getId()){
                if (!users.contains(userRepo.findById(message.getRecipient_id()))){
                    users.add(userRepo.findById(message.getRecipient_id()));
                }
            } else {
                if (!users.contains(userRepo.findById(message.getSender_id()))){
                    users.add(userRepo.findById(message.getSender_id()));
                }
            }
        }
        model.addAttribute("users",users);
        return MESSAGE_PAGE;
    }

    @GetMapping("/dialog{id}")
    public String dialog(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int id){
        UserEntity user = userRepo.findById(id);
        model.addAttribute("user",user);
        model.addAttribute("currentUser",currentUser);
        List<MessageEntity> messages = messageRepo.findAllMessageForThisUsers(currentUser.getId(),user.getId());
        model.addAttribute("messageList",messages);
        return DIALOG_PAGE;
    }

    @PostMapping("/dialog{id}")
    public String sendMessage(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int id, @RequestParam String textMessage){
        MessageEntity message = new MessageEntity(currentUser.getId(),id,currentUser.getFirstName()+":  "+textMessage);
        messageRepo.save(message);
        return "redirect:/messages/dialog"+id;
    }

}
