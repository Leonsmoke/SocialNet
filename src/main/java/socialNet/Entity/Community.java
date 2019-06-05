package socialNet.Entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="communities")
public class Community implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String avatar;
    private int admin_id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserEntity> members = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "community_id", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<Post>();

    public Community() {
        avatar="default_comm.jpg";
    }

    public Community(String name, int admin_id) {
        this.name = name;
        this.admin_id = admin_id;

    }

    public void addMember(UserEntity creator){
        members.add(creator);
        creator.getCommunities().add(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<UserEntity> getMembers() {
        return members;
    }

    public void setMembers(Set<UserEntity> members) {
        this.members = members;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
