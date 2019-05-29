package patrick.pramedia.wire.modul;

/**
 * Created by munil on 4/13/2019.
 */

public class VarGlobals {

    public static String header_salt = "FRESHLY SMOKED, Kosher salted ";
    public static String more_salt = "*SALT*";
    public static String end_salt = " sprinkled with more Sodium chloride and a little of the best Indonesian tiny black peppers to suit your taste and protect you from evildoers.!?";
    public static String code_qr = "";

    public static String [] str_error = new String [] {"01: Failed to connect to database."
    ,"02 The IP Address you are using is blocked from accessing this service, please contact us."
    ,"03: Please wait a few moment before reusing again."
    ,"04: ID not found, please log in again, if this problem persists, please contact us."
    ,"05: This ID is blocked from using this service, please contact us."
    ,"06: This ID is currently in use."
    ,"07: Your changes hasn't been saved."
    ,"08: Failed to register information."
    ,"09: Email information has already been registered, please use a different email."
    ,"10: Please check your email input"
    ,"11: Email not found"
    ,"12: Group already existed"
    ,"13: Group not found"};

    public static boolean notError(String input){
        for (String msg:str_error) {
            if(msg.equals(input)){
                return false;
            }
        }
        return true;
    }
}
