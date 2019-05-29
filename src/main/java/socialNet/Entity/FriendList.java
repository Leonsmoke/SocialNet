package socialNet.Entity;

import javax.persistence.*;

@Entity
@Table(name="friends_list")
public class FriendList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int friend_id1;
    private int friend_id2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriend_id1() {
        return friend_id1;
    }

    public void setFriend_id1(int friend_id1) {
        this.friend_id1 = friend_id1;
    }

    public int getFriend_id2() {
        return friend_id2;
    }

    public void setFriend_id2(int friend_id2) {
        this.friend_id2 = friend_id2;
    }



}
