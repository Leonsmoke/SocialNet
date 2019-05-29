package socialNet.View;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import socialNet.Entity.UserEntity;
import socialNet.Security.SecurityUtils;
import socialNet.Service.FriendService;

import java.io.Serializable;

public class UserView implements Serializable {

    @Autowired
    private FriendService friendService;

    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatar;
    private boolean isMyFriend;
    private boolean IHisFriend;
    private  String isFriend;
    public UserView(UserEntity user, boolean isMyFriend, boolean IHisFriend) {
        final UserEntity profile = SecurityUtils.currentProfile();

        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.isMyFriend=isMyFriend;
        this.IHisFriend=IHisFriend;
        if (user.getId()!=profile.getId()){
            if (isMyFriend && IHisFriend){
                this.isFriend="It's your Friend";
            } else if(isMyFriend && !IHisFriend){
                this.isFriend="You send friend request";
            } else if(!isMyFriend && IHisFriend)
            {
                this.isFriend="It's requested you for friend";
            } else {
                this.isFriend="It's not your friend";
            }
        } else {
            this.isFriend = "It's your profile";
        }



    }



    @Transactional
    public String getIsFriend() {
        return isFriend;
    }
    @Transactional
    public boolean isMyFriend() {
        return isMyFriend;
    }

    public void setMyFriend(boolean myFriend) {
        isMyFriend = myFriend;
    }

    public boolean isFriendOfMine() {
        return IHisFriend;
    }

    public void setFriendOfMine(boolean friendOfMine) {
        IHisFriend = friendOfMine;
    }
}
