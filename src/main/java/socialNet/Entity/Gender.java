package socialNet.Entity;

import java.io.Serializable;

public enum Gender implements Serializable {

    UNDEFINED(0), MALE(1), FEMALE(2);

    private int id;

    Gender(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getGenderString(){
        switch (id){
            case 1: return "Male";
            case 2: return "Female";
            default: return "Undefined";
        }
    }

    public static Gender getGender(Integer id) {
        if (id == null)
            return null;
        for (Gender g : values()) {
            if (g.getId() == id)
                return g;
        }
        return null;
    }
}