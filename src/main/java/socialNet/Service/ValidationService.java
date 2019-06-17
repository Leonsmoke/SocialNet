package socialNet.Service;


import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public boolean checkValidText(String text){
        if (text.length()<1 || text.length()>1999){
            return false;
        }
        if (text.contains("niger") || text.contains("bitch") || text.contains("putin")){
            return false;
        }
        return true;
    }


    public  boolean checkValidShort (String text){
        if (text.length()<1 || text.length()>30){
            return false;
        }
        if (text.contains("niger") || text.contains("bitch") || text.contains("putin")){
            return false;
        }
        return true;
    }

}
