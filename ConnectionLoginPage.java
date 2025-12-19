
package sample;

import java.sql.*;

class ConnectionLoginPage {

    private static String url = "jdbc:mysql://localhost:3306/login_page";
    private static String username = "root";
    private static String password = "";

    private static Connection connection = null;

    public static Connection Connection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connection established!");
            } catch (ClassNotFoundException e) {
                System.out.println("JDBC Driver not found: " + e);
                e.printStackTrace();
            } catch (SQLException ex) {
                System.out.println("Error in Method Connection: " + ex);
                ex.printStackTrace();
            }
        }
        return connection;
    }

    public static void Insert(String query) {
        try (Statement statement = Connection().createStatement()) {
            statement.execute(query);
        } catch (SQLException ex) {
            System.out.println("Error in Method Insertion: " + ex);
            ex.printStackTrace();
        }
    }

    public static ResultSet Display(String query) {
        ResultSet result = null;
        try {
            Connection conn = Connection();
            if (conn != null) {
                Statement statement = conn.createStatement();
                result = statement.executeQuery(query);
            } else {
                System.out.println("Connection is null!");
            }
        } catch (SQLException ex) {
            System.out.println("Error in Method Display: " + ex);
            ex.printStackTrace();
        }
        return result;
    }

    public static int Update(String query) {
        int rowsAffected = 0;
        try (Statement statement = Connection().createStatement()) {
            rowsAffected = statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error in Method Update: " + ex);
            ex.printStackTrace();
        }
        return rowsAffected;
    }

    public static void Delete(String query) {
        try (Statement statement = Connection().createStatement()) {
            statement.execute(query);
        } catch (SQLException ex) {
            System.out.println("Error in Method Delete: " + ex);
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {

        return null;
    }
}
