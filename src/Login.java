import javax.swing.*;
import java.sql.*;

public class Login extends JFrame {

    private JLabel Inicio;
    private JLabel Usuario;
    private JPasswordField text2;
    private JTextField text1;
    private JPanel PanelLogin;
    private JLabel Contraseña;
    private JButton btnIngresar;

    private int intentos = 0;

    public Login() {

        setTitle("Inicio de Sesión");

        setContentPane(PanelLogin);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnIngresar.addActionListener(e -> {

            String usuario = text1.getText();
            String clave = new String(text2.getPassword());

            if (usuario.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Ingrese usuario y contraseña");
                return;
            }

            String sql = "SELECT nombre, saldo FROM usuarios " +
                    "WHERE usuario=? AND password=? AND activo=1";

            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, usuario);
                ps.setString(2, clave);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    String nombre = rs.getString("nombre");
                    double saldo = rs.getDouble("saldo");

                    JFrame frame = new JFrame("Banco");
                    BancoForm ventana = new BancoForm(nombre, saldo, usuario);
                    frame.setContentPane(ventana.getBancoPanel());
                    frame.setSize(700, 700);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);

                    dispose();
                } else {
                    intentos++;

                    JOptionPane.showMessageDialog(null,
                            "Usuario o contraseña incorrectos\nIntento "
                                    + intentos + " de 3");

                    text1.setText("");
                    text2.setText("");

                    if (intentos >= 3) {
                        JOptionPane.showMessageDialog(null,
                                "Acceso bloqueado por demasiados intentos");
                        btnIngresar.setEnabled(false);
                    }
                }

                rs.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error de conexión con la base de datos");
            }
        });
    }
}