package alimov.com.metrafirebase.util;

/**
 * Created by Andrey on 14.12.2016.
 */

public class Validator {

    private static final int NAME_MIN_LENGTH = 1;
    private static final int NAME_MAX_LENGTH = 50;

    private static final int DESCRIPTION_MIN_LENGTH = 1;
    private static final int DESCRIPTION_MAX_LENGTH = 2000;

    public static boolean validateForName(String name) {
        if (name == null) {
            return false;
        }

        if (name.length() == 0) {
            return false;
        }

        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            return false;
        }

        return true;
    }

    public static boolean validateForDescription(String number) {
        if (number == null) {
            return false;
        }

        if (number.length() == 0) {
            return false;
        }

        if (number.length() < DESCRIPTION_MIN_LENGTH || number.length() > DESCRIPTION_MAX_LENGTH) {
            return false;
        }

        return true;
    }
}
