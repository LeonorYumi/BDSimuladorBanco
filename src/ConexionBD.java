import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://uvyw5bigiqdiolrs:nwOv43pTHs78RcHrT7jk@bq3trvid4jvidzlsqm0a-mysql.services.clever-cloud.com:3306/bq3trvid4jvidzlsqm0a";
    private static final String USER = "uvyw5bigiqdiolrs";
    private static final String PASS = "nwOv43pTHs78RcHrT7jk";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión: " + e.getMessage());
        }
    }
}