package utility;

public class Utils {

    public static boolean isStringInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
