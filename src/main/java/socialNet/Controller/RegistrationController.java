package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import socialNet.Entity.Role;
import socialNet.Entity.UserEntity;
import socialNet.repos.UserRepo;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;


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

        user.setActive(true);
        if(role.equals("USER")) {
            user.setRole(Collections.singleton(Role.USER));
        } else{
            user.setRole(Collections.singleton(Role.ADMIN));
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepo.save(user);
        return("redirect:/login");
    }

}
