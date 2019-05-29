package rgr.test_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rgr.test_service.Entity.UserEntity;

import java.util.List;

public interface UserRepo extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername(String username);
    UserEntity findById(int id);



    @Override
    List<UserEntity> findAll();
}
