package gl.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPostgre {

    private static ConnectionPostgre instance;
    private Connection connection;

    private ConnectionPostgre() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    Configuration.url, Configuration.user, Configuration.password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionPostgre getInstance() {
        if (instance == null) {
            instance = new ConnectionPostgre();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
