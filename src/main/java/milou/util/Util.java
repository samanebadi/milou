package milou.util;

import java.util.Random;
import java.util.Scanner;

public class Util {

    private static final Scanner scanner = new Scanner(System.in);

    public static String emailInput(String email) {
       if(email == null) return null;
       email = email.trim();
        if(!email.contains("@"))
        {
            email = email + "@milou.com";
        }
        return email.toLowerCase();
    }
    public static String generateCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }



}
