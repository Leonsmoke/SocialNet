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
}
