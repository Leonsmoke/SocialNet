package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import socialNet.Entity.UserEntity;
import socialNet.Service.AdminService;
import socialNet.Service.UserService;

import static socialNet.constant.pages.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @GetMapping("/panel")
    public String createCommunityPage(Model model, @AuthenticationPrincipal UserEntity currentUser) {
        model = userService.getAllUserFromSearch(model);
        model.addAttribute("currentUser",currentUser);
        return ADMIN_PANEL_PAGE;
    }

    @GetMapping("/panel/ban{id}")
    public String ban(@AuthenticationPrincipal UserEntity currentUser, @PathVariable int id) {
        adminService.addToBanList(id,currentUser.getId());
        return REDIRECT+ADMIN_PANEK_LINK;
    }

}
