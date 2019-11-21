package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnector {

    private static final String urlConnection = "jdbc:postgresql://localhost:5432/LibraryData";

    private final String user = "postgres";

    private final String password = "root";

    public Connection getConnection() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(urlConnection, user, password);
        } catch (SQLException e) {
            System.out.println("Hmmm EXCEPTION!");
            e.printStackTrace();
        }

        return connection;
    }
}
