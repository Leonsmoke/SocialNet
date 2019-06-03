package socialNet.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import socialNet.Entity.Comment;

public interface CommentRepo extends JpaRepository<Comment,Long> {
    @Transactional
    @Modifying
    @Query(value="UPDATE rgr.comments SET author_ava=?1 WHERE author_id=?2",
            nativeQuery = true)
    void changeAllCommentsAva(String author_ava ,Integer author_id);

}
