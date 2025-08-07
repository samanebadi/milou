package milou;

import milou.service.AuthService;
import java.util.Scanner;

public class MilouApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();

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
        while (true) {
            System.out.println("\nWelcome, " + authService.getCurrentUser().getName());
            System.out.println("1. Send email");
            System.out.println("2. View inbox");
            System.out.println("3. View sent");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("[Sending email logic will go here]");
                    break;
                case "2":
                    System.out.println("[Inbox logic will go here]");
                    break;
                case "3":
                    System.out.println("[Sent emails logic will go here]");
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
