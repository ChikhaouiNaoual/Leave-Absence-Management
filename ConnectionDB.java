package sample;

import java.sql.*;

public class ConnectionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/leave_absence";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;


    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ تم إنشاء أو إعادة الاتصال بقاعدة البيانات");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ لم يتم العثور على درايفر JDBC");
            e.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("❌ خطأ في إنشاء الاتصال بقاعدة البيانات");
            ex.printStackTrace();
        }
        return connection;
    }

    /**
     * ✅ Use for INSERT, UPDATE, DELETE
     */
    public static int executeUpdate(String query) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.executeUpdate(query);
    }

    /**
     * ✅ Use for SELECT queries
     */
    public static ResultSet executeQuery(String query) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.executeQuery(query);
    }

    /**
     * ✅ Use for Prepared Statements (safe SQL)
     */
    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    /**
     * ✅ Optional: check connection validity
     */
    public static boolean isConnectionValid() {
        try {
            return connection != null && connection.isValid(5);
        } catch (SQLException e) {
            System.err.println("⚠️ خطأ في التحقق من صلاحية الاتصال");
            return false;
        }
    }
}



