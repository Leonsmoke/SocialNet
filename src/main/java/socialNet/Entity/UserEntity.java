package socialNet.Entity;


import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name="users")
public class UserEntity implements UserDetails, Serializable {



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
    private Date birthDate;
    private String theme;

    @Column(length = 210)
    private String status;

    @Column(length = 2000)
    private String information;

    private Gender gender = Gender.UNDEFINED;


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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "community_members",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> communities = new HashSet<>();

    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name="role",joinColumns=@JoinColumn(name="user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    public void addCommunity(Community community){
        communities.add(community);
    }

    public void addPost(Post post){
        posts.add(post);
        post.setWall_id(this.id);
    }



    public void leaveCommunity(Community community){
        communities.remove(community);
    }

    public void deletePost(Post post){
        posts.remove(post);
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Set<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(Set<Community> communities) {
        this.communities = communities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
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

    public UserEntity(int id,String username, String firstName, String lastName, String password, String email, Date birthDate, Gender gender, Set<Role> role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role;
        this.avatar="default_ava.jpg";
        this.information="";
        this.status="";
        this.theme="normalstyle.css";
        this.active=true;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
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

    public String getBirthDate() {
        if ((birthDate)==null){
            return "1900-12-31";
        } else
        return new SimpleDateFormat("yyyy-MM-dd").format(this.birthDate);
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getGender() {
        return gender.getId();
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getGenderInString(){
        return gender.toString();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
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

    public static class UserBuilder{
        private int id;
        private String username = "";
        private String firstName = "";
        private String lastName = "";
        private String password = "";
        private String email = "";
        private Date birthDate;
        private Gender gender = Gender.UNDEFINED;
        @Enumerated(EnumType.STRING)
        private Set<Role> role = Collections.singleton(Role.USER);
        UserBuilder(){
        }

        public UserBuilder id(@NonNull int id){
            this.id = id;
            return this;
        }

        public UserBuilder username(@NonNull String username) {
            this.username = username;
            return this;
        }

        public UserBuilder firstName(@NonNull String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(@NonNull String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(@NonNull String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(@NonNull String password) {
            this.password = password;
            return this;
        }

        public UserBuilder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }


        public UserBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public UserBuilder role(Set<Role> roles) {
            this.role = roles;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(id, username, firstName, lastName, password, email, birthDate, gender, role);
        }
    }

}
