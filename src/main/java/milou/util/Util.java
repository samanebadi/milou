package milou.util;

import java.util.Random;
import java.util.Scanner;

public class Util {

    private static final Scanner scanner = new Scanner(System.in);

    public static String emailInput() {
        String email;
        while (true) {
            email = scanner.nextLine().trim();
            if (email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) break;
            System.out.print("Invalid email. Try again: ");
        }
        return email;
    }

    public static String generateCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    public static String truncate(String text, int length) {
        if (text.length() <= length) return text;
        return text.substring(0, length) + "...";
    }
}
