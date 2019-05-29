package socialNet.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import socialNet.Entity.FriendList;

import java.util.List;

public interface FriendRepo extends JpaRepository<FriendList,Long> {

    @Override
    List<FriendList> findAll();

}
