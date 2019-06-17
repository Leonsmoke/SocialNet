package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import socialNet.Entity.UserEntity;
import socialNet.Service.RegistrationService;

@Controller
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @GetMapping("/registration")
    public String registration(){
        return("/registration");
    }

    @PostMapping("/registration")
    public String addUser(UserEntity user, @RequestParam String role, Model model){
        model = registrationService.signIn(model, user);
        return("/registration");
    }

}
