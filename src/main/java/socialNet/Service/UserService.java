package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import socialNet.Entity.*;
import socialNet.View.UserView;
import socialNet.other.PostComparator;
import socialNet.repos.CommunityRepo;
import socialNet.repos.PostRepo;
import socialNet.repos.UserRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PostRepo postRepo;

    @Autowired
    CommunityRepo communityRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }
    @Transactional
    public UserDetails findById(int id){
        return userRepo.findById(id);
    }

    @Transactional
    public String getAvatarFromId(int id){
        UserEntity user = userRepo.findById(id);
        return user.getAvatar();
    }

    @Transactional
    public UserEntity create(String username, String firstName, String lastName,String password, String email) {
        final UserEntity person = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .password(new BCryptPasswordEncoder().encode(password))
                .email(email)
                .build();
        return userRepo.save(person);
    }

    @Transactional
    public void SaveChangeFromEditor(UserEntity user, String status,  String firstName,String lastName,
                                     String information, Date birthDate, int gender){
        user.setStatus(status);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setInformation(information);
        user.setBirthDate(birthDate);
        user.setGender(Gender.getGender(gender));
        userRepo.save(user);
    }

    @Transactional
    public Model getUserPage(Model model, UserEntity currentUser, int id){
        UserEntity user = userRepo.findById(id);
        model.addAttribute("user",user);
        model.addAttribute("posts",user.getPosts());
        model.addAttribute("userView", new UserView(user,currentUser.isSendedRequest(user),user.isSendedRequest(currentUser)));
        model.addAttribute("currentUser",currentUser);
        return model;
    }
    @Transactional
    public void addPost(int id, String textPost, UserEntity currentUser){
        UserEntity user = userRepo.findById(id);
        String time = LocalDateTime.now().getDayOfMonth() + " " +LocalDateTime.now().getMonth() + "     "+
                LocalDateTime.now().getHour() + "  :" + LocalDateTime.now().getMinute();
        user.getPosts().add(new Post(user.getId(),-1,textPost,time,currentUser.getId(), currentUser.getFirstName()+" "+ currentUser.getLastName(), currentUser.getAvatar(), user.getAvatar()));
        userRepo.saveAndFlush(user);
    }
    @Transactional
    public void deletePost(int id, int post_id, int user_id){
        UserEntity user = userRepo.findById(id);
        Post postForDeleting = postRepo.findPostByPostID(post_id);
        if (user_id==postForDeleting.getAuthor_id()){
            user.deletePost(postForDeleting);
            postRepo.delete(postForDeleting);
            userRepo.save(user);
        }
    }

    public void changeTheme(UserEntity currentUser, String theme){
        currentUser.setTheme(theme);
        userRepo.saveAndFlush(currentUser);
    }

    @Transactional
    public void addComment(int wall_id, int post_id, String text, UserEntity currentUser){
        UserEntity user = userRepo.findById(wall_id);
        String time = LocalDateTime.now().getDayOfMonth() + " " +LocalDateTime.now().getMonth() + "     "+
                LocalDateTime.now().getHour() + "  :" + LocalDateTime.now().getMinute();
        Post postToComment = postRepo.findPostByPostID(post_id);
        Comment comm = new Comment(wall_id,post_id,text,time,currentUser.getId(),currentUser.getFirstName()+" "+ currentUser.getLastName(),currentUser.getAvatar());
        Post post = user.getPosts().get(user.getPosts().indexOf(postToComment));
        post.addComment(comm);
        user.addPost(post);
        userRepo.saveAndFlush(user);
    }
    @Transactional
    public Model getFeed(Model model, UserEntity user){
        List<Post> allPost = new ArrayList<>();
        for (UserEntity friend: user.getOutgoingFriend()
        ) {
            UserEntity userr = userRepo.findById(friend.getId());
            allPost.addAll(userr.getPosts());
        }
        for (Community community: user.getCommunities()
        ) {
            Community community1 = communityRepo.findById(community.getId());
            allPost.addAll(community1.getPosts());
        }
        allPost.sort(new PostComparator());
        model.addAttribute("user",user);
        model.addAttribute("posts",allPost);
        model.addAttribute("currentUser",user);
        return model;
    }
    @Transactional
    public String addFriend(UserEntity currentUser, int id, Integer fromFriendList){
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
    @Transactional
    public String deleteFriend(UserEntity currentUser, int id, Integer fromFriendList){
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
    @Transactional
    public Model getFriendsList(Model model, UserEntity currentUser, int id){
        UserEntity user = userRepo.findById(id);
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("pageId",id);
        model.addAttribute("usersFriends",user.getAllAcceptedFriend());
        model.addAttribute("pageType","main");
        return model;
    }
    @Transactional
    public Model getIncomingFriends(Model model, UserEntity currentUser, int id){

        Set<UserEntity> incomingFriends = currentUser.getIncomingFriend();
        Set<UserEntity> array = getArrayFromSetFriends(incomingFriends,currentUser);
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("pageId",id);
        model.addAttribute("usersFriends", array);
        model.addAttribute("pageType","in");
        return model;
    }
    @Transactional
    public Model getOutgoingFriends (Model model, UserEntity currentUser,int id){
        Set<UserEntity> outgoingFriends = currentUser.getOutgoingFriend();
        Set<UserEntity> array = getArrayFromSetFriends(outgoingFriends,currentUser);
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("pageId",id);
        model.addAttribute("usersFriends",array);
        model.addAttribute("pageType","out");
        return model;
    }
    @Transactional
    public Set<UserEntity> getArrayFromSetFriends(Set<UserEntity> friends,UserEntity currentUser){
        Set<UserEntity> array = new HashSet<>();
        for (UserEntity friend: friends
        ) {
            if (!currentUser.isAcceptedFriend(friend)){
                array.add(friend);
            }
        }
        return  array;
    }
    @Transactional
    public Model searchRequest(Model model, String selectSearchType, String filter){
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
        return model;
    }
    @Transactional
    public Model getAllUserFromSearch(Model model){
        List<UserEntity> users = userRepo.findAll();
        model.addAttribute("users",users);
        return model;
    }


    public void saveProfileChange(UserEntity currentUser, String status, String firstName,
                                  String lastName,String information, String stringBirthDate,int gender) throws ParseException{
        try{
            Date birthDate = null;
            birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringBirthDate);
            SaveChangeFromEditor(currentUser,status,firstName,lastName,information,birthDate,gender);
        }
        catch (ParseException e){
        }
    }
}
