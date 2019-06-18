package socialNet.Entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="communities")
public class Community implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String avatar;
    private int admin_id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "community_members",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private Set<UserEntity> members = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "community_id", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<Post>();

    public Community() {
        avatar="default_comm.jpg";
    }

    public Community(String name, int admin_id) {
        this.name = name;
        this.admin_id = admin_id;
        avatar="default_comm.jpg";
    }

    public void addPost(Post post){
        posts.add(post);
        post.setCommunity_id(this.id);
    }

    public void preDelete(){
        for (UserEntity user: members
             ) {
            user.leaveCommunity(this);
            members.remove(user);
        }
        for (Post post: posts
             ) {
            posts.remove(post);
        }
    }

    public void deletePost(Post post){
        posts.remove(post);
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public void addMember(UserEntity creator){
        if (!members.contains(creator)){
            members.add(creator);
            creator.getCommunities().add(this);
        }
    }

    public boolean isMemberOfCommunity(UserEntity user){
        return members.contains(user);
    }

    public void deleteMember(UserEntity user){
        members.remove(user);
        user.leaveCommunity(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Community community = (Community) o;
        return id == community.id &&
                name.equals(community.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
