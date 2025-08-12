package milou;

import milou.service.AuthService;
import milou.service.EmailService;

import java.util.Scanner;

public class MilouApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static AuthService authService;
    private static EmailService emailService;

    public static void main(String[] args) {
        authService = new AuthService();
        emailService = new EmailService();

        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("Welcome to Milou Email Service");
            System.out.println("[L]ogin  [S]ignup  [E]xit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice.toUpperCase()) {
                case "L":
                case "LOGIN":
                    handleLogin();
                    break;
                case "S":
                case "SIGNUP":
                    handleSignup();
                    break;
                case "E":
                case "EXIT":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (authService.login(email, password)) {
            showUserMenu();
        } else {
            System.out.println("Login failed. Try again.");
        }
    }

    private static void handleSignup() {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (authService.signup(name, email, password)) {
            System.out.println("Signup successful! You can now login.");
        } else {
            System.out.println("Signup failed. Email might already be in use.");
        }
    }

    private static void showUserMenu() {
        System.out.println("\nWelcome, " + authService.getCurrentUser().getName());

        while (true) {
            // نمایش خلاصه ایمیل‌های خوانده نشده
            emailService.printUnreadSummary(authService.getCurrentUser());

            // نمایش منوی کوتاه
            System.out.print("[S]end, [V]iew, [R]eply, [F]orward, [L]ogout: ");
            String action = scanner.nextLine().trim();

            switch (action.toUpperCase()) {
                case "S":
                case "SEND":
                    emailService.sendEmail(authService.getCurrentUser());
                    break;

                case "V":
                case "VIEW":
                    viewMenu();
                    break;

                case "R":
                case "REPLY":
                    emailService.replyEmail(authService.getCurrentUser());
                    break;

                case "F":
                case "FORWARD":
                    emailService.forwardEmail(authService.getCurrentUser());
                    break;

                case "L":
                case "LOGOUT":
                    authService.logout();
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid action.");
            }
        }
    }

    private static void viewMenu() {
        System.out.print("View [A]ll, [U]nread, [S]ent, [C]ode: ");
        String choice = scanner.nextLine().trim();

        switch (choice.toUpperCase()) {
            case "A":
                emailService.viewInbox(authService.getCurrentUser(), false);
                break;
            case "U":
                emailService.viewInbox(authService.getCurrentUser(), true);
                break;
            case "S":
                emailService.viewSent(authService.getCurrentUser());
                break;
            case "C":
                System.out.print("Enter email code: ");
                String code = scanner.nextLine().trim();
                emailService.viewEmailByCode(authService.getCurrentUser(), code);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
