package milou;

import milou.service.AuthService;
import milou.service.EmailService;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final EmailService emailService = new EmailService(authService);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Milou Email Service ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Send Email");
            System.out.println("4. Logout");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // flush

            switch (choice) {
                case 1 -> signUp();
                case 2 -> login();
                case 3 -> sendEmail();
                case 4 -> logout();
                case 0 -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void signUp() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        authService.signUp(name, email, password);
    }

    private static void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        authService.login(email, password);
    }

    private static void sendEmail() {
        if (authService.getCurrentUser() == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Subject: ");
        String subject = scanner.nextLine();

        System.out.print("Body: ");
        String body = scanner.nextLine();

        System.out.print("Enter recipient emails (comma-separated): ");
        String line = scanner.nextLine();
        List<String> recipients = Arrays.stream(line.split(","))
                .map(String::trim)
                .toList();

        emailService.sendEmail(subject, body, recipients);
    }

    private static void logout() {
        authService.logout();
    }
}
