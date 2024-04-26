import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:task_manager.db";
    private static Connection connection;

    // Method to establish database connection
    public static void connect() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL);
        System.out.println("Connected to the database.");
    }

    // Method to close database connection
    public static void close() throws SQLException {
        if (connection != null) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    }

    // Method to create tables
    public static void createTables() throws SQLException {
        String createTaskTableSQL = "CREATE TABLE IF NOT EXISTS tasks (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    "title TEXT," +
                                    "description TEXT," +
                                    "due_date DATE," +
                                    "priority TEXT," +
                                    "status TEXT)";
        
        connection.createStatement().executeUpdate(createTaskTableSQL);
        System.out.println("Task table created.");
    }
}
