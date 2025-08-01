import milou.db.Database;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            System.out.println("Connected successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
