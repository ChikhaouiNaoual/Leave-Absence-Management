/***********************
package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String url = "jdbc:mysql://localhost:3306/login_page";
    private static final String username = "your_username";
    private static final String password= "your_password";

    public static Connection getConnection() throws SQLException {
        try {
            // Establishing a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");
            return connection;
        } catch (SQLException e) {
            // Handle SQL exceptions (e.g., incorrect credentials or unreachable database)
            System.err.println("Connection failed!");
            e.printStackTrace();
            throw e; // Rethrow the exception for further handling
        }
    }
}
****************/

