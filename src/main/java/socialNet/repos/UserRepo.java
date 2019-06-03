package socialNet.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import socialNet.Entity.UserEntity;

import java.util.List;

public interface UserRepo extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername(String username);
    UserEntity findById(int id);

    //List<UserEntity> getAllFriends();

    @Override
    List<UserEntity> findAll();

    /*@Query(value="SELECT * FROM rgr.posts WHERE (post_id = ?1 AND wall_id=?2)",
            nativeQuery = true)
    Post findPostById(Integer post_id, Integer wall_id);*/
}
