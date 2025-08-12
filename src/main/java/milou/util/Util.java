package milou.util;

public class Util {
        public static String emailInput(String input) {
            if (input == null) return null;
            input = input.trim();
            if (!input.contains("@")) {
                input = input + "@milou.com";
            }
            return input.toLowerCase();
        }

        public static String generatrCode() {
            String chars = "jokapk2672jkdskakjdkllo";
            StringBuilder sb = new StringBuilder(6);
            java.util.Random rnd = new java.util.Random();
            for (int i = 0; i < 6; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
            return sb.toString();
        }

        public static String truncate(String subject) {
            if (subject == null) return "";
            return subject.length() <= 255 ? subject : subject.substring(0, 255);  }
    }
