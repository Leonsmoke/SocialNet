package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import socialNet.Entity.UserEntity;
import socialNet.Service.UserService;

import java.text.ParseException;

import static socialNet.constant.pages.PROFILE_EDITOR_PAGE;
import static socialNet.constant.pages.REDIRECT_TO_PROFILE;

@Controller
@PreAuthorize("hasAuthority('USER')")
public class EditProfileController {
    @Autowired
    private UserService userService;
    @GetMapping("/user/edit")
    public String getEditor(Model model, @AuthenticationPrincipal UserEntity currentUser) {
        model.addAttribute("user",currentUser);
        return PROFILE_EDITOR_PAGE;
    }
    @PostMapping("/user/edit")
    public String saveProfile(@AuthenticationPrincipal UserEntity currentUser,@RequestParam String status, @RequestParam String firstName, @RequestParam String lastName,
                              @RequestParam String information, @RequestParam String stringBirthDate, @RequestParam int gender){
        try{
            userService.saveProfileChange(currentUser, status, firstName, lastName, information, stringBirthDate, gender);
        }
        catch (ParseException e){
            return REDIRECT_TO_PROFILE;
        }
        return REDIRECT_TO_PROFILE;
    }
}
