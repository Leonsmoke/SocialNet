package socialNet.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import socialNet.Entity.Post;

public interface PostRepo extends JpaRepository<Post,Long> {
    @Query(value="SELECT * FROM rgr.posts WHERE post_id = ?1",
            nativeQuery = true)
    Post findPostByPostID(Integer post_id);
}
