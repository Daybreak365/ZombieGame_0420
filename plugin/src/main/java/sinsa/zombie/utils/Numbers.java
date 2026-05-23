package sinsa.zombie.utils;

public class Numbers {

    private Numbers() {}

    public static boolean isInt(String s) {
        boolean isInt = true;
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            isInt = false;
        }
        return isInt;
    }

}
