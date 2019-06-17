package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import socialNet.Entity.Comment;
import socialNet.Entity.Community;
import socialNet.Entity.Post;
import socialNet.Entity.UserEntity;
import socialNet.repos.CommunityRepo;
import socialNet.repos.PostRepo;

import java.time.LocalDateTime;

@Service
public class CommunityService {

    @Autowired
    CommunityRepo communityRepo;
    @Autowired
    PostRepo postRepo;
    @Autowired
    ValidationService validationService;
    public void addPost(int id, UserEntity currentUser, String textPost){
        Community community = communityRepo.findById(id);
        if (community.isMemberOfCommunity(currentUser) && validationService.checkValidText(textPost)){
            String time = LocalDateTime.now().getDayOfMonth() + " " +LocalDateTime.now().getMonth() + "     "+
                    LocalDateTime.now().getHour() + "  :" + LocalDateTime.now().getMinute();
            community.getPosts().add(new Post(-1, id,textPost,time,currentUser.getId(), currentUser.getFirstName()+" "+ currentUser.getLastName(), currentUser.getAvatar(), community.getAvatar()));
            communityRepo.saveAndFlush(community);
        }
    }

    public void addComment( int wall_id, int post_id, UserEntity currentUser, String text){
        if (validationService.checkValidText(text)){
            Community community = communityRepo.findById(wall_id);
            String time = LocalDateTime.now().getDayOfMonth() + " " +LocalDateTime.now().getMonth() + "     "+
                    LocalDateTime.now().getHour() + "  :" + LocalDateTime.now().getMinute();
            Post postToComment = postRepo.findPostByPostID(post_id);
            Comment comm = new Comment(wall_id,post_id,text,time,currentUser.getId(),currentUser.getFirstName()+" "+ currentUser.getLastName(),currentUser.getAvatar());
            Post post = community.getPosts().get(community.getPosts().indexOf(postToComment));
            post.addComment(comm);
            community.addPost(post);
            communityRepo.save(community);
        }

    }

    public int createCommunity(String name, UserEntity currentUser){
        if (validationService.checkValidShort(name)){
            Community newCommunity = new Community(name,currentUser.getId());
            communityRepo.save(newCommunity);
            newCommunity = communityRepo.findCommunityByName(name);
            newCommunity.addMember(currentUser);
            communityRepo.save(newCommunity);
            return newCommunity.getId();
        }
        return 0;
    }

    public Model createCommunityOpenPage(Model model, UserEntity currentUser, int community_id){
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
        return model;
    }

    public void joinToCommunity(int community_id, UserEntity currentUser){
        Community currentCommunity = communityRepo.findById(community_id);
        currentCommunity.addMember(currentUser);
        communityRepo.save(currentCommunity);
    }

    public void leaveFromCommunity(int community_id, UserEntity currentUser){
        Community currentCommunity = communityRepo.findById(community_id);
        currentCommunity.deleteMember(currentUser);
        communityRepo.save(currentCommunity);
    }
}
