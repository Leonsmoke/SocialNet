package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import socialNet.Entity.UserEntity;
import socialNet.Service.UserService;
import socialNet.repos.UserRepo;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return("/registration");
    }
    @PostMapping("/registration")
    public String addUser(UserEntity user, @RequestParam String role, Model model){
        UserEntity userFromDb=userRepo.findByUsername(user.getUsername());
        if(userFromDb!=null){
            model.addAttribute("message","User already exist");
            return ("registration");
        }

        final UserEntity profile = userService.create(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getEmail());

        return("redirect:/user");
    }

}
