package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socialNet.Entity.UserEntity;
import socialNet.repos.UserRepo;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }
    @Transactional
    public UserDetails findById(int id){
        return userRepo.findById(id);
    }

    @Transactional
    public String getAvatarFromId(int id){
        UserEntity user = userRepo.findById(id);
        return user.getAvatar();
    }

}
