package rgr.test_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rgr.test_service.Entity.MessageEntity;

import java.util.List;

public interface MessageRepo extends JpaRepository<MessageEntity,Long> {

    @Query(value="SELECT * FROM rgr.messages WHERE (sender_id = ?1 AND recipient_id = ?2) OR (sender_id = ?2 AND recipient_id = ?1)",
            nativeQuery = true)
    List<MessageEntity> findAllMessageForThisUsers(Integer user1_id, Integer user2_id);

    @Query(value="SELECT * FROM rgr.messages WHERE (sender_id = ?1 OR recipient_id = ?1)",
            nativeQuery = true)
    List<MessageEntity> findAllDialogs(Integer user1_id);


}
