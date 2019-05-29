package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import socialNet.repos.FriendRepo;

@Service
public class FriendService {

    @Autowired
    FriendRepo friendRepo;



}
