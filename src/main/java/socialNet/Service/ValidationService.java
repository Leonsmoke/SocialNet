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

    public boolean checkValidUserPass(String text){
        if (text.contains(" ")||text.contains("!")){
            return false;
        }
        return true;
    }

    public String getDefence(String text){
        text.replace('c','с');
        return text;
    }

    public boolean checkValidTextNotReq(String text){
        if (text.length()>1999){
            return false;
        }
        if (text.contains("niger") || text.contains("bitch") || text.contains("putin")){
            return false;
        }
        return true;
    }

    public  boolean checkValidShortNotReq(String text){
        if (text.length()>30){
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
