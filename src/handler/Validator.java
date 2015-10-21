package handler;

/**
 * Created by neikila on 20.10.15.
 */
public class Validator {
    public static boolean validateFilename(String filename) {
        return filename.matches("(/[\\w\\_\\^\\-а-яА-Я ]+)+\\.\\w+");
    }
}
