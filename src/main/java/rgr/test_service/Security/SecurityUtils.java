package rgr.test_service.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import rgr.test_service.Entity.UserEntity;

public class SecurityUtils {
    private SecurityUtils() {
    }
    public static String currentLogin() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        String login = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                final UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                login = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                login = (String) authentication.getPrincipal();
            }
        }
        return login;
    }

    public static UserEntity currentProfile() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        UserEntity profile = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                profile = (UserEntity) authentication.getPrincipal();
            }
        }
        return profile;
    }
}
