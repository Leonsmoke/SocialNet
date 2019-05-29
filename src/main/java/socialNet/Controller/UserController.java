package socialNet.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import socialNet.Entity.FriendList;
import socialNet.Entity.UserEntity;
import socialNet.Service.UserService;
import socialNet.View.UserView;
import socialNet.repos.FriendRepo;
import socialNet.repos.UserRepo;

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
    private UserService userService;
    @Autowired
    private FriendRepo friendRepo;

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
        List<FriendList> allFriend = friendRepo.findAll();
        boolean isMyFriend = false;
        boolean IHisFriend = false;
        if (!allFriend.isEmpty()){
            for (FriendList friends:allFriend
            ) {
                if (friends.getFriend_id1()==currentUser.getId() && friends.getFriend_id2()==user.getId()){
                    isMyFriend=true;
                }
                if (friends.getFriend_id2()==currentUser.getId() && friends.getFriend_id1()==user.getId()){
                    IHisFriend=true;
                }
            }
        }
            //return ResponseEntity.ok(new UserView(user));
        model.addAttribute("user",user);
        model.addAttribute("userView", new UserView(user,isMyFriend,IHisFriend));
        model.addAttribute("currentUser",currentUser);
        return USER_PAGE;
    }


    @GetMapping("/friends/add/{id}")
    public String addFriend(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int id, @RequestParam(value = "ffl", required=false) Integer fromFriendList){
        UserEntity friend = userRepo.findById(id);

        if (null == friend || null == currentUser) {
            return "redirect:/user";
        } else {
            List<FriendList> allFriend = friendRepo.findAll();
            if (!allFriend.isEmpty()){
                for (FriendList friends:allFriend
                ) {
                    if (friends.getFriend_id1()==currentUser.getId() && friends.getFriend_id2()==friend.getId()){
                        if (fromFriendList==null){
                            return "redirect:/user/"+id;
                        } else {
                            return "redirect:/user/friends/in"+currentUser.getId();
                        }
                    } else {
                        FriendList newFriend = new FriendList();
                        newFriend.setFriend_id1(currentUser.getId());
                        newFriend.setFriend_id2(friend.getId());
                        friendRepo.save(newFriend);
                        if (fromFriendList==null){
                            return "redirect:/user/"+id;
                        } else {
                            return "redirect:/user/friends/in"+currentUser.getId();
                        }
                    }
                }
            } else {
                FriendList newFriend = new FriendList();
                newFriend.setFriend_id1(currentUser.getId());
                newFriend.setFriend_id2(friend.getId());
                friendRepo.save(newFriend);
                return "redirect:/user/"+id;
            }

        }

        return "redirect:/user/"+id;
    }

    @GetMapping("/friends/delete/{id}")
    public String deleteFriend(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") int id, @RequestParam(value = "ffl", required=false) Integer fromFriendList){
        UserEntity friend = userRepo.findById(id);

        if (null == friend || null == currentUser) {
            return "redirect:/user";
        } else {
            List<FriendList> allFriend = friendRepo.findAll();
            if (!allFriend.isEmpty()){
                for (FriendList friends:allFriend
                ) {
                    if (friends.getFriend_id1()==currentUser.getId() && friends.getFriend_id2()==friend.getId()){
                        friendRepo.delete(friends);
                        if (fromFriendList==null){
                            return "redirect:/user/"+id;
                        } else {
                            return "redirect:/user/friends/"+currentUser.getId();
                        }

                    }
                }
            }
        }

        return "redirect:/user/"+id;
    }


    @GetMapping("/friends/{id}")
    public String getUsersFriends(Model model,@AuthenticationPrincipal UserEntity currentUser,
                          @PathVariable("id") int id) {
        UserEntity user = userRepo.findById(id);
        List<FriendList> allFriends = friendRepo.findAll();
        List<FriendList> usersFriends = friendRepo.findAll();
        List<FriendList> friendsOfUser = friendRepo.findAll();
        Set<UserEntity> friendsEntities = new HashSet<UserEntity>();
        if (!allFriends.isEmpty()) {
            for (FriendList friends : allFriends
            ) {
                if (friends.getFriend_id1()==user.getId()){
                    usersFriends.add(friends);
                }
                if (friends.getFriend_id2()==user.getId()){
                    friendsOfUser.add(friends);
                }
            }
            for (FriendList friends: usersFriends
                 ) {
                boolean isAcceptedFriendship = false;
                for (FriendList outFriends:friendsOfUser
                     ) {
                    if (friends.getFriend_id1() == outFriends.getFriend_id2() && friends.getFriend_id2() == outFriends.getFriend_id1()){
                        if (user.getId()==friends.getFriend_id1()){
                            friendsEntities.add(userRepo.findById(friends.getFriend_id2()));
                        } else {
                            friendsEntities.add(userRepo.findById(friends.getFriend_id1()));
                        }
                    }
                }
            }

        }
        model.addAttribute("usersFriends",friendsEntities);
        model.addAttribute("pageType","main");
        return FRIENDS_PAGE;
    }


    @GetMapping("/friends/in{id}")
    public String getUsersInFriends(Model model,@AuthenticationPrincipal UserEntity currentUser,
                                  @PathVariable("id") int id) {
        UserEntity user = userRepo.findById(id);
        List<FriendList> allFriends = friendRepo.findAll();
        List<FriendList> usersFriends = friendRepo.findAll();
        List<FriendList> friendsOfUser = friendRepo.findAll();
        Set<UserEntity> friendsEntities = new HashSet<UserEntity>();
        if (!allFriends.isEmpty()) {
            for (FriendList friends : allFriends
            ) {
                if (friends.getFriend_id1()==user.getId()){
                    usersFriends.add(friends);
                }
                if (friends.getFriend_id2()==user.getId()){
                    friendsOfUser.add(friends);
                }
            }
            for (FriendList friends: friendsOfUser
            ) {
                boolean isAcceptedFriendship = false;
                for (FriendList outFriends:usersFriends
                ) {
                    if (friends.getFriend_id1() == outFriends.getFriend_id2() && friends.getFriend_id2() == outFriends.getFriend_id1()){
                        isAcceptedFriendship=true;
                        break;
                    }
                }
                if (!isAcceptedFriendship && friends.getFriend_id1()!=currentUser.getId()){
                    friendsEntities.add(userRepo.findById(friends.getFriend_id1()));
                }
            }

        }
        model.addAttribute("usersFriends",friendsEntities);
        model.addAttribute("pageType","in");
            return FRIENDS_PAGE;

    }

    @GetMapping("/friends/out{id}")
    public String getUsersOutFriends(Model model,@AuthenticationPrincipal UserEntity currentUser,
                                    @PathVariable("id") int id) {
        UserEntity user = userRepo.findById(id);
        List<FriendList> allFriends = friendRepo.findAll();
        List<FriendList> usersFriends = friendRepo.findAll();
        List<FriendList> friendsOfUser = friendRepo.findAll();
        Set<UserEntity> friendsEntities = new HashSet<UserEntity>();
        if (!allFriends.isEmpty()) {
            for (FriendList friends : allFriends
            ) {
                if (friends.getFriend_id1()==user.getId()){
                    usersFriends.add(friends);
                }
                if (friends.getFriend_id2()==user.getId()){
                    friendsOfUser.add(friends);
                }
            }
            for (FriendList friends: usersFriends
            ) {
                boolean isAcceptedFriendship = false;
                for (FriendList outFriends:friendsOfUser
                ) {
                    if (friends.getFriend_id1() == outFriends.getFriend_id2() && friends.getFriend_id2() == outFriends.getFriend_id1()){
                        isAcceptedFriendship=true;
                        break;
                    }
                }
                if (!isAcceptedFriendship && friends.getFriend_id2()!=currentUser.getId()){
                    friendsEntities.add(userRepo.findById(friends.getFriend_id2()));
                }
            }

        }
        model.addAttribute("usersFriends",friendsEntities);
        model.addAttribute("pageType","out");
        return FRIENDS_PAGE;
    }

}
