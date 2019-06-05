package socialNet.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Embeddable
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int post_id;
    private int wall_id;
    private int community_id;
    @Column(length = 2000)
    private String text;
    private String time;
    private Long timeForSort;
    private int author_id;
    private String author_fullname;
    private String author_ava;
    private String wall_ava;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post_id", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<Comment>();

    public Post() {
    }

    public Post(int wall_id, String text, String time, Long timeForSort, int author_id, String author_fullname, String author_ava, String wall_ava) {
        this.wall_id = wall_id;
        this.text = text;
        this.time = time;
        this.timeForSort = timeForSort;
        this.author_id = author_id;
        this.author_fullname = author_fullname;
        this.author_ava = author_ava;
        this.wall_ava = wall_ava;
    }

    public Post(int wall_id, String text, String time, int author_id, String author_fullname, String author_ava, String wall_ava) {
        this.wall_id = wall_id;
        this.text = text;
        this.time = time;
        this.author_id = author_id;
        this.author_fullname = author_fullname;
        this.author_ava = author_ava;
        this.wall_ava=wall_ava;
        timeForSort = (new Date()).getTime();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
        comment.setPost_id(this.post_id);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getWall_ava() {
        return wall_ava;
    }

    public void setWall_ava(String wall_ava) {
        this.wall_ava = wall_ava;
    }

    public String getAuthor_ava() {
        return author_ava;
    }

    public void setAuthor_ava(String author_ava) {
        this.author_ava = author_ava;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getTimeForSort() {
        return timeForSort;
    }

    public int compareTime(Post other){
        if (timeForSort < other.getTimeForSort()) {
            return -1;
        }
        if (timeForSort > other.getTimeForSort()) {
            return 1;
        }
        return 0;
    }

    public int getId() {
        return post_id;
    }

    public void setId(int id) {
        this.post_id = id;
    }

    public int getWall_id() {
        return wall_id;
    }

    public void setWall_id(int wall_id) {
        this.wall_id = wall_id;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_fullname() {
        return author_fullname;
    }

    public void setAuthor_fullname(String author_fullname) {
        this.author_fullname = author_fullname;
    }
}
