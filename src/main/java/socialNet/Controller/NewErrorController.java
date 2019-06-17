package socialNet.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import socialNet.Entity.UserEntity;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static socialNet.Entity.Role.USER;
import static socialNet.constant.pages.*;

@Controller
public class NewErrorController implements ErrorController {

    @GetMapping("")
    public String greeting(@AuthenticationPrincipal UserEntity user) {
        if (user.getRole().toString().equals(USER)) {
            return REDIRECT_TO_PROFILE;
        } else return REDIRECT+ADMIN_PANEK_LINK;
    }



    @RequestMapping(ERROR_PATH)
    public String handleError(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return PAGE_404;
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return PAGE_500;
            } else if(statusCode==HttpStatus.FORBIDDEN.value()){
                return PAGE_403;
            }else if(statusCode==HttpStatus.BAD_REQUEST.value()){
                return PAGE_400;
            }
        }
        System.out.println(status.toString());
        return "error";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
