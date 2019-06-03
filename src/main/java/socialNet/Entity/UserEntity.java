package socialNet.Entity;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="users")
public class UserEntity implements UserDetails {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private boolean active;
    private String avatar;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "wall_id", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<Post>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "friend1_id"),
            inverseJoinColumns = @JoinColumn(name = "friend2_id"))
    private Set<UserEntity> incomingFriend = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "friend2_id"),
            inverseJoinColumns = @JoinColumn(name = "friend1_id"))
    private Set<UserEntity> outgoingFriend = new HashSet<>();

    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name="role",joinColumns=@JoinColumn(name="user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;


    public void addPost(Post post){
        posts.add(post);
        post.setWall_id(this.id);
    }

    public void sendFriendRequest(UserEntity friend){
        if (!outgoingFriend.contains(friend)){
            outgoingFriend.add(friend);
            friend.putIncomingRequest(this);
        }
    }

    public Set<UserEntity> getAllAcceptedFriend(){
        Set<UserEntity> allAcceptedFriends = new HashSet<>();
        for (UserEntity friend: outgoingFriend
             ) {
            if (friend.isSendedRequest(this)){
                allAcceptedFriends.add(friend);
            }
        }
        return allAcceptedFriends;
    }

    public boolean isAcceptedFriend(UserEntity friend){
        if (this.isSendedRequest(friend) && friend.isSendedRequest(this)){
            return true;
        } else return false;
    }

    public boolean isSendedRequest(UserEntity friend){
        return outgoingFriend.contains(friend);
    }

    public boolean isGettedRequest(UserEntity friend){
        return incomingFriend.contains(friend);
    }

    private void putIncomingRequest(UserEntity friend){
        incomingFriend.add(friend);
    }

    public void deleteIncomingRequest(UserEntity friend){
        incomingFriend.remove(friend);
    }

    public void deleteFriend(UserEntity friend){
        outgoingFriend.remove(friend);
        friend.deleteIncomingRequest(this);
    }

    public Set<UserEntity> getIncomingFriend() {
        return incomingFriend;
    }

    public void setIncomingFriend(Set<UserEntity> incomingFriend) {
        this.incomingFriend = incomingFriend;
    }

    public Set<UserEntity> getOutgoingFriend() {
        return outgoingFriend;
    }

    public void setOutgoingFriend(Set<UserEntity> outgoingFriend) {
        this.outgoingFriend = outgoingFriend;
    }

    public UserEntity() {
        this.avatar="default_ava.jpg";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return getRole();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    //@Transactional
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }


}
