package uz.maniac4j.data.enums;

import java.util.ArrayList;
import java.util.List;

public enum RegisterType {
//    COIL,
//    DIGITAL,
    INPUT,
    HOLDING;


    public static List<String> names(){
        List<String> names=new ArrayList<>();
        for (RegisterType value : RegisterType.values()) {
            names.add(value.name());
        }
        return names;
    }


}
