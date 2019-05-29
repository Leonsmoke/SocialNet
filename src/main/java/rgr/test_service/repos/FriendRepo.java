package rgr.test_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rgr.test_service.Entity.FriendList;

import java.util.List;

public interface FriendRepo extends JpaRepository<FriendList,Long> {

    @Override
    List<FriendList> findAll();

}
