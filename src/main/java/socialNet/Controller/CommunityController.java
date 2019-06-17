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
import socialNet.Entity.UserEntity;
import socialNet.Service.CommunityService;
import socialNet.repos.CommunityRepo;
import socialNet.repos.PostRepo;
import socialNet.repos.UserRepo;

import static socialNet.constant.pages.*;

@Controller
@PreAuthorize("hasAuthority('USER')||hasAuthority('ADMIN')")
public class CommunityController {

    @Autowired
    CommunityRepo communityRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PostRepo postRepo;
    @Autowired
    CommunityService communityService;

    @GetMapping("/user/communities")
    public String communitiesPage(Model model, @AuthenticationPrincipal UserEntity currentUser) {
        model.addAttribute("user",currentUser);
        return USERS_COMMUNITIES_PAGE;
    }

    @PostMapping("/community/{id}/addPost")
    public String addPost(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @PathVariable("id") int id, @RequestParam String textPost) {
        communityService.addPost(id,currentUser,textPost);
        return REDIRECT+COMMUNITY_LINK+"/"+id;
    }
    @PostMapping("/community/addComment")
    public String addComment(Model model,@AuthenticationPrincipal UserEntity currentUser,
                           @RequestParam int post_id,@RequestParam int wall_id,@RequestParam String text) {
        communityService.addComment(wall_id,post_id,currentUser,text);
        return REDIRECT+COMMUNITY_LINK+"/"+wall_id;
    }

    @GetMapping("/user/communities/create")
    public String createCommunityPage(Model model,@AuthenticationPrincipal UserEntity currentUser) {
        model.addAttribute("currentUser",currentUser);
        return CREATE_COMMUNITY_PAGE;
    }
    @GetMapping("/community/search/getAll")
    public String getAllCommunities(Model model,@AuthenticationPrincipal UserEntity currentUser) {
        model = communityService.getAllCommunitiesFromSearch(model);
        model.addAttribute("currentUser",currentUser);
        return SEARCH_PAGE;
    }
    @PostMapping("/user/communities/create")
    public String createNewCommunity(Model model, @AuthenticationPrincipal UserEntity currentUser, @RequestParam String name) {
        model.addAttribute("user",currentUser);
        return REDIRECT+COMMUNITY_LINK+"/"+communityService.createCommunity(name,currentUser);
    }

    @GetMapping("/community/{id}")
    public String createCommunityPage(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable ("id") int community_id) {
        model = communityService.createCommunityOpenPage(model,currentUser,community_id);
        return COMMUNITY_PAGE;
    }

    @GetMapping("/community/{id}/join")
    public String joinToCommunity(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable ("id") int community_id) {
        model.addAttribute("user",currentUser);
        communityService.joinToCommunity(community_id,currentUser);
        return REDIRECT+COMMUNITY_LINK+"/"+community_id;
    }

    @GetMapping("/community/{id}/leave")
    public String leaveCommunity(Model model, @AuthenticationPrincipal UserEntity currentUser, @PathVariable ("id") int community_id) {
        model.addAttribute("user",currentUser);
        communityService.leaveFromCommunity(community_id,currentUser);
        return REDIRECT+COMMUNITY_LINK+"/"+community_id;
    }

}
