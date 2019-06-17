package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import socialNet.Entity.UserEntity;
import socialNet.repos.UserRepo;

import static socialNet.constant.GreetingsMessage.GREETING;

@Service
public class RegistrationService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    public Model signIn(Model model, UserEntity user){
        UserEntity userFromDb=userRepo.findByUsername(user.getUsername());
        if(userFromDb!=null){
            model.addAttribute("message","User already exist");
            return model;
        }

        final UserEntity profile = userService.create(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getEmail());
        if(!profile.checkValid()){
            userRepo.delete(profile);
            model.addAttribute("message","Error validation");
            return model;
        }
        mailService.send(user.getEmail(),"SlowPokeNet Team", GREETING);
        return model;

    }

}
