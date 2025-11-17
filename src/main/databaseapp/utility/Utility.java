package databaseapp.utility;

import java.util.Scanner;

public class Utility {

    private static final String STRING_REGEX = "[a-zA-Z0-9@. ]+";

    public static String getStandardInput(Scanner in) {
        String out = "";
        while (out.isBlank() || !out.matches(STRING_REGEX)) {
            try {
                out = in.next().strip();
                if (!out.matches(STRING_REGEX)) {
                    System.out.print("The input must only contain letters and numbers: ");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return out;
    }

    public static boolean isIn (String[] arr, String search) {
        boolean out = false;
        for (String term : arr) {
            if (term.equals(search)) {
                out = true;
                break;
            }
        }
        return out;
    }
}