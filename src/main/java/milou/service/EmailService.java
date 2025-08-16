package milou.service;

import milou.db.Database;
import milou.entity.User;
import milou.util.Util;

import java.sql.*;
import java.util.List;

public class EmailService {

    private final AuthService authService;

    public EmailService(AuthService authService) {
        this.authService = authService;
    }


    public void sendEmail(String subject, String body, List<String> recipientEmails) {
        User sender = authService.getCurrentUser();
        if (sender == null) {
            System.out.println("No logged in user.");
            return;
        }
        String code = Util.generateCode();
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            String insertEmail = "INSERT INTO emails(subject,body,code,sender_id) VALUES(?,?,?,?)";
            PreparedStatement psEmail = conn.prepareStatement(insertEmail, Statement.RETURN_GENERATED_KEYS);
            psEmail.setString(1, subject);
            psEmail.setString(2, body);
            psEmail.setString(3, code);
            psEmail.setInt(4, sender.getId());
            psEmail.executeUpdate();

            ResultSet rs = psEmail.getGeneratedKeys();
            if (!rs.next()) {
                conn.rollback();
                System.out.println("Email sending failed.");
                return;
            }
            int emailId = rs.getInt(1);

            String insertRecipient = "INSERT INTO recipients(email_id,recipient_id) VALUES(?,?)";
            PreparedStatement psRecipient = conn.prepareStatement(insertRecipient);
            for (String recEmail : recipientEmails) {
                User recipient = authService.getUserByEmail(recEmail);
                if (recipient != null) {
                    psRecipient.setInt(1, emailId);
                    psRecipient.setInt(2, recipient.getId());
                    psRecipient.addBatch();
                }
            }
            psRecipient.executeBatch();
            conn.commit();
            System.out.println("Email sent with code: " + code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewInbox(boolean onlyUnread) {
        User user = authService.getCurrentUser();
        if (user == null) return;
        String query = "SELECT e.code, e.subject, e.body, e.creation_date, r.is_read, u.name AS sender_name " +
                "FROM emails e JOIN recipients r ON e.id=r.email_id JOIN users u ON e.sender_id=u.id " +
                "WHERE r.recipient_id=?" + (onlyUnread ? " AND r.is_read=FALSE" : "") +
                " ORDER BY e.creation_date DESC";
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.printf("[%s] From: %s | Subject: %s | Read: %s\n", rs.getString("code"),
                        rs.getString("sender_name"), Util.truncate(rs.getString("subject"), 20), rs.getBoolean("is_read"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewSent() {
        User user = authService.getCurrentUser();
        if (user == null) return;
        String query = "SELECT code, subject, creation_date FROM emails WHERE sender_id=? ORDER BY creation_date DESC";
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.printf("[%s] Subject: %s | Date: %s\n",
                        rs.getString("code"), Util.truncate(rs.getString("subject"), 20), rs.getTimestamp("creation_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewByCode(String code) {
        String query = "SELECT e.*, u.name AS sender_name FROM emails e JOIN users u ON e.sender_id=u.id WHERE code=?";
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.printf("Code: %s\nFrom: %s\nSubject: %s\nBody: %s\nDate: %s\n",
                        rs.getString("code"), rs.getString("sender_name"), rs.getString("subject"),
                        rs.getString("body"), rs.getTimestamp("creation_date"));
            } else {
                System.out.println("Email not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void replyEmail(String parentCode, String body) {
        User sender = authService.getCurrentUser();
        if (sender == null) return;

        try (Connection conn = Database.getConnection()) {
            String getParent = "SELECT * FROM emails WHERE code=?";
            PreparedStatement psGet = conn.prepareStatement(getParent);
            psGet.setString(1, parentCode);
            ResultSet rs = psGet.executeQuery();
            if (!rs.next()) {
                System.out.println("Original email not found.");
                return;
            }
            int parentId = rs.getInt("id");
            int originalSenderId = rs.getInt("sender_id");

            String subject = "[Re]: " + rs.getString("subject");
            String code = Util.generateCode();

            String insertEmail = "INSERT INTO emails(subject,body,code,sender_id,parent_email_id) VALUES(?,?,?,?,?)";
            PreparedStatement psEmail = conn.prepareStatement(insertEmail, Statement.RETURN_GENERATED_KEYS);
            psEmail.setString(1, subject);
            psEmail.setString(2, body);
            psEmail.setString(3, code);
            psEmail.setInt(4, sender.getId());
            psEmail.setInt(5, parentId);
            psEmail.executeUpdate();

            ResultSet generatedKeys = psEmail.getGeneratedKeys();
            if (generatedKeys.next()) {
                int emailId = generatedKeys.getInt(1);
                String insertRecipient = "INSERT INTO recipients(email_id,recipient_id) VALUES(?,?)";
                PreparedStatement psRec = conn.prepareStatement(insertRecipient);
                psRec.setInt(1, emailId);
                psRec.setInt(2, originalSenderId);
                psRec.executeUpdate();
                System.out.println("Reply sent with code: " + code);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printUnreadSummary(User currentUser) {
        User user = authService.getCurrentUser();
        if (user == null) return;

        String query = "SELECT e.code, e.subject, u.name AS sender_name " +
                "FROM emails e " +
                "JOIN recipients r ON e.id=r.email_id " +
                "JOIN users u ON e.sender_id=u.id " +
                "WHERE r.recipient_id=? AND r.is_read=FALSE " +
                "ORDER BY e.creation_date DESC";

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            System.out.println("Unread Emails:");
            while (rs.next()) {
                System.out.printf("[%s] From: %s | Subject: %s\n",
                        rs.getString("code"),
                        rs.getString("sender_name"),
                        Util.truncate(rs.getString("subject"), 20));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void forwardEmail(String codeToForward, String recipientEmail) {
        User sender = authService.getCurrentUser();
        if (sender == null) return;

        try (Connection conn = Database.getConnection()) {
            String getEmail = "SELECT * FROM emails WHERE code=?";
            PreparedStatement psGet = conn.prepareStatement(getEmail);
            psGet.setString(1, codeToForward);
            ResultSet rs = psGet.executeQuery();
            if (!rs.next()) {
                System.out.println("Original email not found.");
                return;
            }
            String subject = "[Fw]: " + rs.getString("subject");
            String body = rs.getString("body");
            String code = Util.generateCode();

            String insertEmail = "INSERT INTO emails(subject,body,code,sender_id,parent_email_id) VALUES(?,?,?,?,?)";
            PreparedStatement psEmail = conn.prepareStatement(insertEmail, Statement.RETURN_GENERATED_KEYS);
            psEmail.setString(1, subject);
            psEmail.setString(2, body);
            psEmail.setString(3, code);
            psEmail.setInt(4, sender.getId());
            psEmail.setInt(5, rs.getInt("id"));
            psEmail.executeUpdate();

            ResultSet genKeys = psEmail.getGeneratedKeys();
            if (genKeys.next()) {
                int emailId = genKeys.getInt(1);
                User recipient = authService.getUserByEmail(recipientEmail);
                if (recipient != null) {
                    String insertRecipient = "INSERT INTO recipients(email_id,recipient_id) VALUES(?,?)";
                    PreparedStatement psRec = conn.prepareStatement(insertRecipient);
                    psRec.setInt(1, emailId);
                    psRec.setInt(2, recipient.getId());
                    psRec.executeUpdate();
                    System.out.println("Email forwarded with code: " + code);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}