package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socialNet.Entity.Gender;
import socialNet.Entity.UserEntity;
import socialNet.repos.UserRepo;

import java.util.Date;


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

    @Transactional
    public UserEntity create(String username, String firstName, String lastName,String password, String email) {
        final UserEntity person = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .password(new BCryptPasswordEncoder().encode(password))
                .email(email)
                .build();
        return userRepo.save(person);
    }

    @Transactional
    public void SaveChangeFromEditor(UserEntity user, String status,  String firstName,String lastName,
                                     String information, Date birthDate, int gender){
        user.setStatus(status);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setInformation(information);
        user.setBirthDate(birthDate);
        user.setGender(Gender.getGender(gender));
        userRepo.save(user);
    }

}
