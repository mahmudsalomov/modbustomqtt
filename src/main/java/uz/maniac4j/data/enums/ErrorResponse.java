package uz.maniac4j.data.enums;

public enum ErrorResponse {
    ERROR,
    CLIENT_SWITCH_OFF;


    public static Double check(String value){
        try {
            valueOf(value);
            return null;
        }catch (Exception e){
            try {
                return Double.parseDouble(value);

            }catch (Exception g){
                return null;
            }
        }
    }
}
