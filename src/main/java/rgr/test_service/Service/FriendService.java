package rgr.test_service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rgr.test_service.repos.FriendRepo;

@Service
public class FriendService {

    @Autowired
    FriendRepo friendRepo;



}
