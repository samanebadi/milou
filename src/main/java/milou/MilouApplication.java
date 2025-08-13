package milou;

import milou.service.AuthService;
import milou.service.EmailService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("=== Milou Email Service ===");
        System.out.println("1. Login");
        System.out.println("2. Sign Up");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                handleLogin();
                break;
            case "2":
                handleSignUp();
                break;
            case "3":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void handleLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (authService.login(email, password)) {
            showUserMenu();
        } else {
            System.out.println("Login failed.");
        }
    }

    private static void handleSignUp() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (authService.signUp(name, email, password)) {
            System.out.println("Sign up successful. Please log in.");
        } else {
            System.out.println("Sign up failed.");
        }
    }

    private static void showUserMenu() {
        while (authService.getCurrentUser() != null) {
            System.out.println("\nWelcome, " + authService.getCurrentUser().getName());
            emailService.printUnreadSummary(authService.getCurrentUser());

            System.out.println("1. Send Email");
            System.out.println("2. View Inbox");
            System.out.println("3. View Sent");
            System.out.println("4. View by Code");
            System.out.println("5. Reply to Email");
            System.out.println("6. Forward Email");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    sendEmailFlow();
                    break;
                case "2":
                    System.out.print("Only unread? (y/n): ");
                    boolean onlyUnread = scanner.nextLine().equalsIgnoreCase("y");
                    emailService.viewInbox(onlyUnread);
                    break;
                case "3":
                    emailService.viewSent();
                    break;
                case "4":
                    System.out.print("Enter email code: ");
                    String code = scanner.nextLine();
                    emailService.viewByCode(code);
                    break;
                case "5":
                    System.out.print("Enter email code to reply: ");
                    String replyCode = scanner.nextLine();
                    System.out.print("Enter reply body: ");
                    String replyBody = scanner.nextLine();
                    emailService.replyEmail(replyCode, replyBody);
                    break;
                case "6":
                    System.out.print("Enter email code to forward: ");
                    String forwardCode = scanner.nextLine();
                    System.out.print("Enter recipient email: ");
                    String forwardRecipient = scanner.nextLine();
                    emailService.forwardEmail(forwardCode, forwardRecipient);
                    break;
                case "7":
                    authService.logout();
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void sendEmailFlow() {
        System.out.print("Subject: ");
        String subject = scanner.nextLine();
        System.out.print("Body: ");
        String body = scanner.nextLine();
        System.out.print("Recipients (comma separated): ");
        String recipientsLine = scanner.nextLine();

        List<String> recipientEmails = new ArrayList<>();
        for (String email : recipientsLine.split(",")) {
            recipientEmails.add(email.trim());
        }

        emailService.sendEmail(subject, body, recipientEmails);
    }
}
