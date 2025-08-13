package milou.service;

import milou.db.Database;
import milou.entity.User;

import java.sql.*;

public class AuthService {

    private User currentUser = null;

    public boolean signUp(String name, String email, String password) {
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO users(name,email,password) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                currentUser = new User(generatedKeys.getInt(1), name, email, password);
                currentUser = null;
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean login(String email, String password) {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentUser = new User(rs.getInt("id"), rs.getString("name"), email, password);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getUserByEmail(String email) {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM users WHERE email=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("name"), email, rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int id) {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM users WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(id, rs.getString("name"), rs.getString("email"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
