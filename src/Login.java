import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {

    private JLabel Inicio;
    private JLabel Usuario;
    private JPasswordField text2;
    private JTextField text1;
    private JPanel PanelLogin;
    private JLabel Contraseña;
    private JButton btnIngresar;
    private JButton btnRegistrar;
    private JLabel lblImagen;

    private int intentos = 0;

    public Login() {
        setTitle("Inicio de Sesión");
        setContentPane(PanelLogin);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnIngresar.addActionListener(e -> ejecutarIngreso());
        btnRegistrar.addActionListener(e -> ejecutarRegistro());
    }

    private void ejecutarIngreso() {
        String usuarioInput = text1.getText();
        String claveInput = new String(text2.getPassword());

        if (usuarioInput.isEmpty() || claveInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese usuario y contraseña");
            return;
        }

        // Buscamos solo por usuario para verificar si existe
        String sql = "SELECT password, nombre, saldo FROM usuarios WHERE usuario=? AND activo=1";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuarioInput);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // EL USUARIO EXISTE, ahora validamos la contraseña
                String claveBD = rs.getString("password");

                if (claveBD.equals(claveInput)) {
                    // Informacion valida ingresa directo al sistema
                    String nombre = rs.getString("nombre");
                    double saldo = rs.getDouble("saldo");

                    BancoForm ventana = new BancoForm(nombre, saldo, usuarioInput);
                    JFrame frame = new JFrame("Banco");
                    frame.setContentPane(ventana.getBancoPanel());
                    frame.setSize(700, 700);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                    dispose();
                } else {
                    // USUARIO BIEN, PERO CLAVE MAL
                    intentos++;
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta.\nIntento " + intentos + " de 3");
                }
            } else {
                // EL USUARIO NO EXISTE EN LA BASE DE DATOS
                intentos++;
                JOptionPane.showMessageDialog(null, "El usuario '" + usuarioInput + "' no existe.");
            }

            // Bloqueo de seguridad
            if (intentos >= 3) {
                btnIngresar.setEnabled(false);
                JOptionPane.showMessageDialog(null, "Acceso bloqueado.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void ejecutarRegistro() {
        String nuevoUser = JOptionPane.showInputDialog("Usuario nuevo:");
        String nuevaPass = JOptionPane.showInputDialog("Contraseña:");
        String nombreReal = JOptionPane.showInputDialog("Nombre del cliente:");
        String saldoStr = JOptionPane.showInputDialog("Saldo inicial:");

        if (nuevoUser != null && nuevaPass != null && nombreReal != null && saldoStr != null) {
            try {
                double saldoInicial = Double.parseDouble(saldoStr);
                String sqlInsert = "INSERT INTO usuarios (usuario, password, nombre, saldo, activo) VALUES (?, ?, ?, ?, 1)";

                try (Connection con = ConexionBD.conectar();
                     PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                    ps.setString(1, nuevoUser);
                    ps.setString(2, nuevaPass);
                    ps.setString(3, nombreReal);
                    ps.setDouble(4, saldoInicial);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }
}