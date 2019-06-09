package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import socialNet.Entity.MessageEntity;
import socialNet.Entity.UserEntity;
import socialNet.repos.MessageRepo;
import socialNet.repos.UserRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MessageService {

    @Autowired
    MessageRepo messageRepo;
    @Autowired
    UserRepo userRepo;

    public Model getUserDialogs(UserEntity user, Model model){
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
        model.addAttribute("currentUser",user);
        model.addAttribute("users",users);
        return model;
    }

    public Model getDialog(Model model, int id, UserEntity currentUser){
        UserEntity user = userRepo.findById(id);
        List<MessageEntity> messages = messageRepo.findAllMessageForThisUsers(currentUser.getId(),user.getId());
        model.addAttribute("user",user);
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("messageList",messages);
        return model;
    }

    public void sendMessage(UserEntity currentUser, int id, String textMessage){
        MessageEntity message = new MessageEntity(currentUser.getId(),id,currentUser.getFirstName()+":  "+textMessage);
        messageRepo.save(message);
    }

}
