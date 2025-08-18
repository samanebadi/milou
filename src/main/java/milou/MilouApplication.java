package milou;

import milou.service.AuthService;
import milou.service.EmailService;

import java.util.Arrays;
import java.util.Scanner;

public class MilouApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        EmailService emailService = new EmailService(authService);

        while (true) {
            System.out.println("\nwelcome to email service ");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Send Email");
            System.out.println("4. View Inbox");
            System.out.println("5. Reply to Email");
            System.out.println("6. Forward Email");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    authService.signup(name, email, password);
                    break;

                case 2:
                    System.out.print("Enter email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
                    authService.login(loginEmail, loginPassword);
                    break;

                case 3:
                    System.out.print("Enter subject: ");
                    String subject = scanner.nextLine();
                    System.out.print("Enter body: ");
                    String body = scanner.nextLine();
                    System.out.print("Enter recipients (comma separated): ");
                    String recipients = scanner.nextLine();
                    emailService.sendEmail(subject, body, Arrays.asList(recipients.split(",")));
                    break;

                case 4:
                    emailService.viewInbox();
                    break;

                case 5:
                    System.out.print("Enter recipient ID to reply: ");
                    int replyId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter reply body: ");
                    String replyBody = scanner.nextLine();
                    emailService.replyEmail(replyId, replyBody);
                    break;

                case 6:
                    System.out.print("Enter recipient ID to forward: ");
                    int forwardId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter recipient email to forward to: ");
                    String forwardTo = scanner.nextLine();
                    emailService.forwardEmail(forwardId, forwardTo);
                    break;

                case 7:
                    System.out.println("Exited");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
