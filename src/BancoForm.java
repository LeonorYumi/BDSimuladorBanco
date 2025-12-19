import javax.swing.*;
import java.sql.*;

public class BancoForm extends JFrame {

    private JPanel BancoPanel;
    private JButton depósitoButton;
    private JButton retiroButton;
    private JButton transferenciaButton;
    private JButton eliminarButton; // Nuevo botón para el DELETE
    private JButton salirButton;
    private JLabel clientevalor;
    private JLabel Saldovalor;
    private JTextArea hisorialDeTransaccionesTextArea;
    private JLabel Cliente;
    private JLabel SaldoSiponible;
    private JLabel TransaccionesBancarias;
    private JLabel BancoForm;

    private double saldo;
    private String usuario;

    public BancoForm(String nombre, double saldoInicial, String usuario) {
        this.saldo = saldoInicial;
        this.usuario = usuario;

        // Configuración inicial de etiquetas
        Cliente.setText("Cliente:");
        SaldoSiponible.setText("Saldo disponible:");
        clientevalor.setText(nombre);
        actualizarSaldo();

        // --- 1. DEPÓSITO (UPDATE) ---
        depósitoButton.addActionListener(e -> {
            String valorStr = JOptionPane.showInputDialog(null, "Ingrese valor a depositar:", "Depósito", JOptionPane.QUESTION_MESSAGE);
            if (valorStr == null) return;
            try {
                double valor = Double.parseDouble(valorStr);
                if (valor <= 0) {
                    JOptionPane.showMessageDialog(null, "Ingrese un valor válido");
                    return;
                }
                saldo += valor;
                actualizarSaldo();
                guardarSaldoBD();
                agregarHistorial("Depósito: +$" + valor);
                JOptionPane.showMessageDialog(null, "Depósito exitoso. Nuevo saldo: $" + saldo);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Monto inválido");
            }
        });

        // --- 2. RETIRO (UPDATE) ---
        retiroButton.addActionListener(e -> {
            String valorStr = JOptionPane.showInputDialog(null, "Ingrese valor a retirar:", "Retiro", JOptionPane.QUESTION_MESSAGE);
            if (valorStr == null) return;
            try {
                double valor = Double.parseDouble(valorStr);
                if (valor <= 0 || valor > saldo) {
                    JOptionPane.showMessageDialog(null, "Saldo insuficiente o valor inválido");
                    return;
                }
                saldo -= valor;
                actualizarSaldo();
                guardarSaldoBD();
                agregarHistorial("Retiro: -$" + valor);
                JOptionPane.showMessageDialog(null, "Retiro exitoso. Nuevo saldo: $" + saldo);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Monto inválido");
            }
        });

        // --- 3. TRANSFERENCIA (UPDATE) ---
        transferenciaButton.addActionListener(e -> {
            JTextField nombreDestino = new JTextField();
            JTextField monto = new JTextField();
            Object[] mensaje = {"Destinatario:", nombreDestino, "Monto:", monto};

            int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Transferencia", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                try {
                    double cantidad = Double.parseDouble(monto.getText());
                    if (cantidad > 0 && cantidad <= saldo) {
                        saldo -= cantidad;
                        actualizarSaldo();
                        guardarSaldoBD();
                        agregarHistorial("Transferencia a " + nombreDestino.getText() + ": -$" + cantidad);
                        JOptionPane.showMessageDialog(null, "Transferencia realizada");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error en el monto o saldo");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error en los datos");
                }
            }
        });

        // --- 4. ELIMINAR USUARIO (DELETE) ---
        eliminarButton.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    "¿Seguro que desea ELIMINAR su cuenta?\nEsta acción borrará sus datos de manera definitiva .",
                    "Confirmar Borrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                if (eliminarUsuarioDeBD()) {
                    JOptionPane.showMessageDialog(null, "Cuenta eliminada con éxito.");
                    System.exit(0); // Cerrar programa
                }
            }
        });

        // --- 5. SALIR ---
        salirButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Cerrando sesión...");
            System.exit(0);
        });
    }

    // Método para refrescar el Label del saldo
    private void actualizarSaldo() {
        Saldovalor.setText("$ " + String.format("%.2f", saldo));
    }

    // Método para escribir en el JTextArea
    private void agregarHistorial(String texto) {
        hisorialDeTransaccionesTextArea.append(texto + "\n");
    }

    // OUPDATE: Guarda el saldo actual
    private void guardarSaldoBD() {
        String sql = "UPDATE usuarios SET saldo=? WHERE usuario=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, saldo);
            ps.setString(2, usuario);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al actualizar saldo: " + e.getMessage());
        }
    }

    // DELETE-Eliminar Cuenta
    private boolean eliminarUsuarioDeBD() {
        String sql = "DELETE FROM usuarios WHERE usuario=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    public JPanel getBancoPanel() {
        return BancoPanel;
    }
}