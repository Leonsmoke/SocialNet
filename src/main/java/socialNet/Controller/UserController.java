package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import socialNet.Entity.UserEntity;
import socialNet.Service.CommunityService;
import socialNet.Service.UserService;

import static socialNet.constant.pages.*;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')||hasAuthority('ADMIN')")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    CommunityService communityService;

    @GetMapping("")
    public String user(Model model, @AuthenticationPrincipal UserEntity user){
        model.addAttribute("currentUser",user);
        return "redirect:user/"+user.getId();
    }

    @GetMapping("/friends")
    public String userFriend(Model model, @AuthenticationPrincipal UserEntity user){
        model.addAttribute("currentUser",user);
        return "redirect:friends/"+user.getId();
    }

    @GetMapping("/friends/in")
    public String userFriendIn(Model model, @AuthenticationPrincipal UserEntity user){
        model.addAttribute("currentUser",user);
        return "redirect:/user/friends/in"+user.getId();
    }

    @GetMapping("/friends/out")
    public String userFriendOut(Model model, @AuthenticationPrincipal UserEntity user){
        model.addAttribute("currentUser",user);
        return "redirect:/user/friends/out"+user.getId();
    }

    @GetMapping("/search")
    public String searchUser(Model model, @AuthenticationPrincipal UserEntity user){
        model.addAttribute("currentUser",user);
        return SEARCH_PAGE;
    }

    @GetMapping("/search/getAll")
    public String getAllUser(Model model, @AuthenticationPrincipal UserEntity user){
        model = userService.getAllUserFromSearch(model);
        model.addAttribute("currentUser",user);
        return SEARCH_PAGE;
    }

    @PostMapping("/search/doSearch")
    public String doSearch(Model model, @AuthenticationPrincipal UserEntity user, @RequestParam String selectSearchType, @RequestParam String filter){
        model = userService.searchRequest(model,selectSearchType,filter);
        model.addAttribute("currentUser",user);
        return SEARCH_PAGE;
    }

    @GetMapping("/{id}")
    public String getUser(Model model,@AuthenticationPrincipal UserEntity currentUser,
            @PathVariable("id") int id, @RequestParam(name="comm_id", required = false) Integer comm_id) {
        if (id==-1){
            return "redirect:/community/"+comm_id;
        }
        model= userService.getUserPage(model,currentUser,id);
        return USER_PAGE;
    }

    @GetMapping("/changeTheme")
    public String changeTheme(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @RequestParam(name="theme", required = true) String theme) {
        if (currentUser.getTheme()!=theme){
            userService.changeTheme(currentUser,theme);
        }
        return "redirect:/user";
    }

    @PostMapping("/{id}/addPost")
    public String addPost(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @PathVariable("id") int id, @RequestParam String textPost) {
        model.addAttribute("currentUser",currentUser);
        userService.addPost(id,textPost,currentUser);
        return "redirect:/user/"+id;
    }

    @PostMapping("/{id}/deletePost/{post_id}")
    public String deletePost(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @PathVariable("id") int id,@PathVariable("post_id") int post_id) {
        if (id>0){
            userService.deletePost(id,post_id,currentUser.getId());
            return "redirect:/user/"+id;
        }else{

            return "redirect:/community/"+communityService.deletePost(post_id,currentUser.getId());
        }

    }

    @PostMapping("/feed/addComment")
    public String addCommentFromFeed(Model model,@AuthenticationPrincipal UserEntity currentUser, @RequestParam int post_id, @RequestParam int wall_id, @RequestParam String text){
        userService.addComment(wall_id,post_id,text,currentUser);
        model.addAttribute("currentUser",currentUser);
        return FEED_PAGE;
    }

    @GetMapping("/feed")
    public String getAllPost(Model model,@AuthenticationPrincipal UserEntity user){
        model = userService.getFeed(model,user);
        return FEED_PAGE;
    }



    @GetMapping("/friends/add/{id}")
    public String addFriend(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int friend_id, @RequestParam(value = "ffl", required=false) Integer fromFriendList){
        return userService.addFriend(currentUser,friend_id,fromFriendList);
    }

    @GetMapping("/friends/delete/{id}")
    public String deleteFriend(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int friend_id, @RequestParam(value = "ffl", required=false) Integer fromFriendList){
        return userService.deleteFriend(currentUser,friend_id,fromFriendList);
    }


    @GetMapping("/friends/{id}")
    public String getUsersFriends(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @PathVariable("id") int id) {
        userService.getFriendsList(model,currentUser,id);
        return FRIENDS_PAGE;
    }


    @GetMapping("/friends/in{id}")
    public String getUsersInFriends(Model model,@AuthenticationPrincipal UserEntity currentUser,
                                  @PathVariable("id") int id) {
        if (currentUser.getId()!=id) return "/friends/in"+currentUser.getId();
        model = userService.getIncomingFriends(model,currentUser,id);
        return FRIENDS_PAGE;

    }

    @GetMapping("/friends/out{id}")
    public String getUsersOutFriends(Model model,@AuthenticationPrincipal UserEntity currentUser,
                                    @PathVariable("id") int id) {
        if (currentUser.getId()!=id) return "/friends/out"+currentUser.getId();
        model=userService.getOutgoingFriends(model,currentUser,id);
        return FRIENDS_PAGE;
    }



}
