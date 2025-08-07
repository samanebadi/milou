package milou;

import milou.service.AuthService;

import java.util.List;
import java.util.Scanner;
import milou.service.EmailService;




public class MilouApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final EmailService emailService = new EmailService(authService);


    public static void main(String[] args) {
        while (true) {
            showMainMenu();
        }
    }

    private static void showMainMenu() {
        System.out.println("\nWelcome to Milou Email Service");
        System.out.println("1. Sign up");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                handleSignUp();
                break;
            case "2":
                handleLogin();
                break;
            case "3":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void handleSignUp() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        authService.signUp(name, email, password);
    }

    private static void handleLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (authService.login(email, password)) {
            showUserMenu();
        }
    }

    private static void showUserMenu() {
        EmailService emailService = new EmailService(authService);

        while (authService.getCurrentUser() != null) {
            System.out.println("\nWelcome, " + authService.getCurrentUser().getName());
            System.out.println("1. Send email");
            System.out.println("2. View inbox");
            System.out.println("3. View sent");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter email subject: ");
                    String subject = scanner.nextLine();
                    System.out.print("Enter email body: ");
                    String body = scanner.nextLine();
                    System.out.print("Enter recipient emails (comma-separated): ");
                    String recipientsInput = scanner.nextLine();
                    List<String> recipientEmails = List.of(recipientsInput.split(",\\s*"));

                    emailService.sendEmail(subject, body, recipientEmails);
                    break;

                case "2":
                    System.out.println("[Inbox feature not implemented yet]");
                    break;

                case "3":
                    System.out.println("[Sent feature not implemented yet]");
                    break;

                case "4":
                    authService.logout();
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

}

