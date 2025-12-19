import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    // URL corregida y datos directos
    private static final String URL = "jdbc:mysql://briihid3bbx30ksg5mqi-mysql.services.clever-cloud.com:3306/briihid3bbx30ksg5mqi";
    private static final String USER = "uldhgpgtdwqlblgu";
    private static final String PASS = "qnGNNZxQzaSINdlt9bi2";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}