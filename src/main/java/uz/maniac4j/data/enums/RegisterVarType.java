package uz.maniac4j.data.enums;

import java.util.ArrayList;
import java.util.List;

public enum RegisterVarType {
    INT16,
//    UINT16,
//    FLOAT16,
    INT32,
//    UINT32,
    FLOAT32;


    public static List<String> names(){
        List<java.lang.String> names=new ArrayList<>();
        for (RegisterVarType value : RegisterVarType.values()) {
            names.add(value.name());
        }
        return names;
    }
}
