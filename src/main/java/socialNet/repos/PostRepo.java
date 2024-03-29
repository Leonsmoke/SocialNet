package socialNet.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import socialNet.Entity.Post;

public interface PostRepo extends JpaRepository<Post,Long> {
    @Query(value="SELECT * FROM rgr.posts WHERE post_id = ?1",
            nativeQuery = true)
    Post findPostByPostID(Integer post_id);

    @Transactional
    @Modifying
    @Query(value="UPDATE rgr.posts SET author_ava=?1 WHERE author_id=?2",
            nativeQuery = true)
    void changeAllPostsAva(String author_ava ,Integer author_id);

}
