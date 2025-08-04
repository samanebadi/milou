package milou.service;

import milou.db.Database;
import milou.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    private User currentUser;

    public boolean signUp(String name, String email, String password) {
        try (Connection conn = Database.getConnection()) {

            PreparedStatement checkStmt = conn.prepareStatement("SELECT id FROM users WHERE email = ?");
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("This email already exists.");
                return false;
            }


            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO users (name, email, password) VALUES (?, ?, ?)");
            insertStmt.setString(1, name);
            insertStmt.setString(2, email);
            insertStmt.setString(3, password);

            insertStmt.executeUpdate();
            System.out.println("Sign up was successful.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String email, String password) {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, name FROM users WHERE email = ? AND password = ?");
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                currentUser = new User(id, name, email, password);
                System.out.println("Welcome to your account, " + currentUser.getName() + "!");
                return true;
            } else {
                System.out.println("Email or password is incorrect.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logout successfully completed.");
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
