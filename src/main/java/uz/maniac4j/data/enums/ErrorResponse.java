package uz.maniac4j.data.enums;

public enum ErrorResponse {
    ERROR,
    CLIENT_SWITCH_OFF;


    public static double check(String value){
        try {
            valueOf(value);
            return 0;
        }catch (Exception e){
            try {
                return Double.parseDouble(value);

            }catch (Exception g){
                return 0;
            }
        }
    }
}
