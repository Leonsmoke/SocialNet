package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socialNet.Entity.UserEntity;
import socialNet.repos.CommunityRepo;
import socialNet.repos.PostRepo;
import socialNet.repos.UserRepo;

@Service
public class AdminService  implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PostRepo postRepo;

    @Autowired
    CommunityRepo communityRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }
    @Transactional
    public void addToBanList(int id, int curId){
        if (curId!=id){
            UserEntity user = userRepo.findById(id);
            if (user.isActive()){
                user.setActive(false);
            } else {
                user.setActive(true);
            }

            userRepo.save(user);
        }

    }

}
