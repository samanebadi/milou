package milou.service;

import milou.db.Database;
import milou.entity.Email;
import milou.entity.Recipient;
import milou.entity.User;
import milou.entity.EmailStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class EmailService {
    private final AuthService authService;



    public EmailService(AuthService authService) {
        this.authService=   authService;
    }


    public boolean sendEmail(String subject, String body, List<String> recipientEmails) {
        User sender = authService.getCurrentUser();

        if (sender == null) {
            System.out.println("You must be logged in to send an email.");
            return false;
        }

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);


            String emailCode = generateEmailCode();
            PreparedStatement emailStmt = conn.prepareStatement(
                    "INSERT INTO emails (subject, body, code, sender_id, creation_date, status) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            emailStmt.setString(1, subject);
            emailStmt.setString(2, body);
            emailStmt.setString(3, emailCode);
            emailStmt.setInt(4, sender.getId());
            emailStmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            emailStmt.setString(6, EmailStatus.SENT.name());
            emailStmt.executeUpdate();

            ResultSet emailKeys = emailStmt.getGeneratedKeys();
            if (!emailKeys.next()) {
                conn.rollback();
                System.out.println("Failed to create email.");
                return false;
            }
            int emailId = emailKeys.getInt(1);


            PreparedStatement userLookup = conn.prepareStatement("SELECT id FROM users WHERE email = ?");
            PreparedStatement recipientStmt = conn.prepareStatement(
                    "INSERT INTO recipients (email_id, recipient_id, is_read) VALUES (?, ?, false)"
            );

            for (String recipientEmail : recipientEmails) {
                userLookup.setString(1, recipientEmail);
                ResultSet rs = userLookup.executeQuery();

                if (rs.next()) {
                    int recipientId = rs.getInt("id");

                    recipientStmt.setInt(1, emailId);
                    recipientStmt.setInt(2, recipientId);
                    recipientStmt.executeUpdate();
                } else {
                    System.out.println("Recipient not found: " + recipientEmail);
                }
            }

            conn.commit();
            System.out.println("Email sent successfully.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateEmailCode() {
        return "EML-" + System.currentTimeMillis();
    }
}
