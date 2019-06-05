package socialNet.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Embeddable
@Table(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int comment_id;
    private int post_id;
    private int wall_id;
    @Column(length = 1000)
    private String text;
    private String time;
    private Long timeForSort;
    private int author_id;
    private String author_fullname;
    private String author_ava;

    public Comment(){
    }

    public Comment(int wall_id, int post_id, String text, String time, int author_id, String author_fullname,String author_ava) {
        this.wall_id = wall_id;
        this.post_id = post_id;
        this.text = text;
        this.time = time;
        this.author_id = author_id;
        this.author_fullname = author_fullname;
        this.author_ava = author_ava;
        timeForSort = (new Date()).getTime();
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getTimeForSort() {
        return timeForSort;
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

    public String getAuthor_ava() {
        return author_ava;
    }

    public void setAuthor_ava(String author_ava) {
        this.author_ava = author_ava;
    }

}
