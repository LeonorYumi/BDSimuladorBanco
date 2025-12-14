import javax.swing.*;
import java.sql.*;
import conexion.ConexionBD;

public class Login {

    private JLabel Inicio;
    private JLabel Usuario;
    private JPasswordField text2;
    private JTextField text1;
    private JPanel PanelLogin;
    private JLabel Contraseña;
    private JButton btnIngresar;

    public Login() {

        btnIngresar.addActionListener(e -> {

            String usuario = text1.getText();
            String clave = new String(text2.getPassword());

            String sql = "SELECT nombre, saldo FROM usuarios WHERE usuario=? AND password=?";

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
                    frame.setSize(600, 450);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);

                    SwingUtilities.getWindowAncestor(btnIngresar).dispose();

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Usuario o contraseña incorrectos");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error: " + ex.getMessage());
            }
        });
    }

    public JPanel getLoginPanel() {
        return PanelLogin;
    }
}
