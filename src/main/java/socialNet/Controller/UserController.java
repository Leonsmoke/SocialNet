package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import socialNet.Entity.Comment;
import socialNet.Entity.Post;
import socialNet.Entity.UserEntity;
import socialNet.View.UserView;
import socialNet.other.PostComparator;
import socialNet.repos.PostRepo;
import socialNet.repos.UserRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static socialNet.constant.pages.*;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")

public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

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
        List<UserEntity> users = userRepo.findAll();
        model.addAttribute("users",users);
        return SEARCH_PAGE;
    }

    @PostMapping("/search/doSearch")
    public String doSearch(Model model, @AuthenticationPrincipal UserEntity user, @RequestParam String selectSearchType, @RequestParam String filter){
        List<UserEntity> Allusers = userRepo.findAll();
        List<UserEntity> goodResultUsers = new ArrayList<>();
        if (selectSearchType.equalsIgnoreCase("s1")){
            for (UserEntity profile :Allusers
            ) {
                if (profile.getUsername().contains(filter)){
                    goodResultUsers.add(profile);
                }
            }
        } else if (selectSearchType.equalsIgnoreCase("s2")){
            for (UserEntity profile :Allusers
            ) {
                if (profile.getFirstName().contains(filter)){
                    goodResultUsers.add(profile);
                }
            }
        } else if (selectSearchType.equalsIgnoreCase("s3")){
            for (UserEntity profile :Allusers
            ) {
                if (profile.getLastName().contains(filter)){
                    goodResultUsers.add(profile);
                }
            }
        }

        model.addAttribute("users",goodResultUsers);
        return SEARCH_PAGE;
    }

    @GetMapping("/{id}")
    //public ResponseEntity<UserView> getUser(
    public String getUser(Model model,@AuthenticationPrincipal UserEntity currentUser,
            @PathVariable("id") int id) {
        UserEntity user = userRepo.findById(id);
        model.addAttribute("user",user);
        model.addAttribute("posts",user.getPosts());
        model.addAttribute("userView", new UserView(user,currentUser.isSendedRequest(user),user.isSendedRequest(currentUser)));
        model.addAttribute("currentUser",currentUser);
        return USER_PAGE;
    }

    @PostMapping("/{id}/addPost")
    public String addPost(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @PathVariable("id") int id, @RequestParam String textPost) {
        UserEntity user = userRepo.findById(id);
        String time = LocalDateTime.now().getDayOfMonth() + " " +LocalDateTime.now().getMonth() + "     "+
                LocalDateTime.now().getHour() + "  :" + LocalDateTime.now().getMinute();
        user.getPosts().add(new Post(user.getId(),-1,textPost,time,currentUser.getId(), currentUser.getFirstName()+" "+ currentUser.getLastName(), currentUser.getAvatar(), user.getAvatar()));
        userRepo.saveAndFlush(user);
        return "redirect:/user/"+id;
    }

    @PostMapping("/{id}/deletePost/{post_id}")
    public String deletePost(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @PathVariable("id") int id,@PathVariable("post_id") int post_id) {
        UserEntity user = userRepo.findById(id);
        Post postForDeleting = postRepo.findPostByPostID(post_id);
        if (currentUser.getId()==postForDeleting.getAuthor_id()){
            user.deletePost(postForDeleting);
            postRepo.delete(postForDeleting);
            userRepo.save(user);
        }

        return "redirect:/user/"+id;
    }

    @PostMapping("/feed/addComment")
    public String addCommentFromFeed(Model model,@AuthenticationPrincipal UserEntity currentUser, @RequestParam int post_id, @RequestParam int wall_id, @RequestParam String text){
        UserEntity user = userRepo.findById(wall_id);
        String time = LocalDateTime.now().getDayOfMonth() + " " +LocalDateTime.now().getMonth() + "     "+
                LocalDateTime.now().getHour() + "  :" + LocalDateTime.now().getMinute();
        Post postToComment = postRepo.findPostByPostID(post_id);
        //user.getPosts().get(user.getPosts().indexOf(postToComment)).getComments().add(new Comment(wall_id,post_id,text,time,currentUser.getId(),currentUser.getFirstName()+" "+ currentUser.getLastName(),currentUser.getAvatar()));
        Comment comm = new Comment(wall_id,post_id,text,time,currentUser.getId(),currentUser.getFirstName()+" "+ currentUser.getLastName(),currentUser.getAvatar());
        Post post = user.getPosts().get(user.getPosts().indexOf(postToComment));
        post.addComment(comm);
        user.addPost(post);
            userRepo.saveAndFlush(user);
        return FEED_PAGE;
    }

    @GetMapping("/feed")
    public String getAllPost(Model model,@AuthenticationPrincipal UserEntity user){
        List<Post> allPost = new ArrayList<>();
        for (UserEntity friend: user.getOutgoingFriend()
             ) {
            UserEntity userr = userRepo.findById(friend.getId());
            allPost.addAll(userr.getPosts());
        }
        allPost.sort(new PostComparator());
        model.addAttribute("user",user);
        model.addAttribute("posts",allPost);
        return FEED_PAGE;
    }



    @GetMapping("/friends/add/{id}")
    public String addFriend(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int id, @RequestParam(value = "ffl", required=false) Integer fromFriendList){
        UserEntity friend = userRepo.findById(id);
        if (null == friend || null == currentUser) {
            return "redirect:/user";
        } else {
            if (friend.getId()==currentUser.getId()) return "redirect:/user";
            if (!currentUser.isSendedRequest(friend)){
                currentUser.sendFriendRequest(friend);
                userRepo.save(friend);
            }
            if (fromFriendList==null){
                return "redirect:/user/"+id;
            } else {
                return "redirect:/user/friends/in"+currentUser.getId();
            }
    }
    }

    @GetMapping("/friends/delete/{id}")
    public String deleteFriend(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int id, @RequestParam(value = "ffl", required=false) Integer fromFriendList){
        UserEntity friend = userRepo.findById(id);
        if (null == friend || null == currentUser) {
            return "redirect:/user";
        } else {
            if (friend.getId()==currentUser.getId()) return "redirect:/user";
            if (currentUser.isSendedRequest(friend)){
                currentUser.deleteFriend(friend);
                userRepo.save(friend);
            }
            if (fromFriendList==null){
                return "redirect:/user/"+id;
            } else {
                return "redirect:/user/friends/"+currentUser.getId();
            }
    }
    }


    @GetMapping("/friends/{id}")
    public String getUsersFriends(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @PathVariable("id") int id) {
        UserEntity user = userRepo.findById(id);
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("pageId",id);
        model.addAttribute("usersFriends",user.getAllAcceptedFriend());
        model.addAttribute("pageType","main");
        return FRIENDS_PAGE;
    }


    @GetMapping("/friends/in{id}")
    public String getUsersInFriends(Model model,@AuthenticationPrincipal UserEntity currentUser,
                                  @PathVariable("id") int id) {
        if (currentUser.getId()!=id) return "/friends/in"+currentUser.getId();
        Set<UserEntity> incomingFriends = currentUser.getIncomingFriend();
        Set<UserEntity> array = new HashSet<>();
        for (UserEntity friend: incomingFriends
             ) {
            if (!currentUser.isAcceptedFriend(friend)){
               array.add(friend);
            }
        }
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("pageId",id);
        model.addAttribute("usersFriends", array);
        model.addAttribute("pageType","in");
            return FRIENDS_PAGE;

    }

    @GetMapping("/friends/out{id}")
    public String getUsersOutFriends(Model model,@AuthenticationPrincipal UserEntity currentUser,
                                    @PathVariable("id") int id) {
        if (currentUser.getId()!=id) return "/friends/out"+currentUser.getId();
        Set<UserEntity> outgoingFriends = currentUser.getOutgoingFriend();
        Set<UserEntity> array = new HashSet<>();
        for (UserEntity friend: outgoingFriends
        ) {
            if (!currentUser.isAcceptedFriend(friend)){
                array.add(friend);
            }
        }
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("pageId",id);
        model.addAttribute("usersFriends",array);
        model.addAttribute("pageType","out");
        return FRIENDS_PAGE;
    }



}
