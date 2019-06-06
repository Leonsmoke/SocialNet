package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import socialNet.Entity.Community;
import socialNet.Entity.Post;
import socialNet.Entity.UserEntity;
import socialNet.repos.CommunityRepo;
import socialNet.repos.UserRepo;

import java.time.LocalDateTime;

import static socialNet.constant.pages.*;

@Controller
@PreAuthorize("hasAuthority('USER')")
public class CommunityController {

    @Autowired
    CommunityRepo communityRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/user/communities")
    public String communitiesPage(Model model, @AuthenticationPrincipal UserEntity currentUser) {
        model.addAttribute("user",currentUser);
        return USERS_COMMUNITIES_PAGE;
    }

    @PostMapping("/community/{id}/addPost")
    public String addPost(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @PathVariable("id") int id, @RequestParam String textPost) {
        Community community = communityRepo.findById(id);
        if (community.isMemberOfCommunity(currentUser)){
            String time = LocalDateTime.now().getDayOfMonth() + " " +LocalDateTime.now().getMonth() + "     "+
                    LocalDateTime.now().getHour() + "  :" + LocalDateTime.now().getMinute();
            community.getPosts().add(new Post(-1, id,textPost,time,currentUser.getId(), currentUser.getFirstName()+" "+ currentUser.getLastName(), currentUser.getAvatar(), community.getAvatar()));
            communityRepo.saveAndFlush(community);
        }
        
        return "redirect:/community/"+id;
    }

    @GetMapping("/user/communities/create")
    public String createCommunityPage(@AuthenticationPrincipal UserEntity currentUser) {
        return CREATE_COMMUNITY_PAGE;
    }

    @PostMapping("/user/communities/create")
    public String createNewCommunity(Model model, @AuthenticationPrincipal UserEntity currentUser, @RequestParam String name) {
        model.addAttribute("user",currentUser);
        Community newCommunity = new Community(name,currentUser.getId());
        communityRepo.save(newCommunity);
        newCommunity = communityRepo.findCommunityByName(name);
        newCommunity.addMember(currentUser);
        communityRepo.save(newCommunity);
        //userRepo.save(currentUser);
        return REDIRECT+COMMUNITY_LINK+"/"+newCommunity.getId();
    }

    @GetMapping("/community/{id}")
    public String createCommunityPage(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable ("id") int community_id) {
        model.addAttribute("user",currentUser);
        Community currentCommunity = communityRepo.findById(community_id);
        if (currentCommunity.isMemberOfCommunity(currentUser)){
            model.addAttribute("isJoined","truee");
        } else {
            model.addAttribute("isJoined","falsee");
        }
        if (currentCommunity.getAdmin_id()==currentUser.getId()){
            model.addAttribute("isAdmin","truee");
        } else {
            model.addAttribute("isAdmin","falsee");
        }
        model.addAttribute("community", currentCommunity);
        model.addAttribute("members",currentCommunity.getMembers());
        return COMMUNITY_PAGE;
    }

    @GetMapping("/community/{id}/join")
    public String joinToCommunity(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable ("id") int community_id) {
        model.addAttribute("user",currentUser);
        Community currentCommunity = communityRepo.findById(community_id);
        currentCommunity.addMember(currentUser);
        communityRepo.save(currentCommunity);
        //userRepo.save(currentUser);
        return REDIRECT+COMMUNITY_LINK+"/"+community_id;
    }

    @GetMapping("/community/{id}/leave")
    public String leaveCommunity(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable ("id") int community_id) {
        model.addAttribute("user",currentUser);
        Community currentCommunity = communityRepo.findById(community_id);
        currentCommunity.deleteMember(currentUser);
        communityRepo.save(currentCommunity);
        //userRepo.save(currentUser);
        return REDIRECT+COMMUNITY_LINK+"/"+community_id;
    }

}
