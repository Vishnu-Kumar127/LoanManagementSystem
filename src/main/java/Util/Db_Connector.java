package Util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Db_Connector {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Properties props = new Properties();

        try (InputStream input = Db_Connector.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
            }

            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            String driver = props.getProperty("db.driver");

            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            throw new RuntimeException("Error loading database configuration", e);
        }
    }
}

