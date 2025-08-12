package milou.service;

import milou.db.Database;
import milou.entity.User;

import java.sql.*;
import java.util.Optional;

public class AuthService {
    private final Database database = Database.getInstance();
    private User currentUser;

    public boolean signUp(String name, String email, String password) {
        try (Connection conn = database.getConnection()) {

            String checkSql = "SELECT id FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("This email is already registered.");
                    return false;
                }
            }


            String insertSql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.executeUpdate();
                System.out.println("Sign up successful! You can now log in.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String email, String password) {
        try (Connection conn = database.getConnection()) {
            String sql = "SELECT id, name, email FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    currentUser = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null
                    );
                    System.out.println("Login successful!");
                    return true;
                } else {
                    System.out.println("Invalid email or password.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
