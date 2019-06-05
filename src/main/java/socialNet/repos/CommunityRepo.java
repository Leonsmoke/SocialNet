package socialNet.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import socialNet.Entity.Community;

public interface CommunityRepo extends JpaRepository<Community,Long> {
    Community findById(int id);

    @Query(value="SELECT * FROM rgr.communities WHERE name = ?1",
            nativeQuery = true)
    Community findCommunityByName(String name);
}
