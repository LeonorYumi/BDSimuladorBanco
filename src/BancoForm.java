import javax.swing.*;
import java.sql.*;

public class BancoForm extends JFrame {

    private JPanel BancoPanel;
    private JButton depósitoButton;
    private JButton retiroButton;
    private JButton transferenciaButton;
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

        Cliente.setText("Cliente:");
        SaldoSiponible.setText("Saldo disponible:");

        clientevalor.setText(nombre);
        actualizarSaldo();

        // DEPÓSITO
        depósitoButton.addActionListener(e -> {
            String valorStr = JOptionPane.showInputDialog(
                    null,
                    "Ingrese valor a depositar:",
                    "Depósito",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (valorStr == null) return; // cancelar

            try {
                double valor = Double.parseDouble(valorStr);

                if (valor <= 0) {
                    JOptionPane.showMessageDialog(null, "Ingrese un valor válido");
                    return;
                }

                saldo += valor;
                actualizarSaldo();
                guardarSaldoBD();
                agregarHistorial("Depósito de $" + valor);

                JOptionPane.showMessageDialog(null,
                        "Depósito exitoso\nNuevo saldo: $" + saldo);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido");
            }
        });

        // RETIRO
        retiroButton.addActionListener(e -> {
            String valorStr = JOptionPane.showInputDialog(
                    null,
                    "Ingrese valor a retirar:",
                    "Retiro",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (valorStr == null) return;

            try {
                double valor = Double.parseDouble(valorStr);

                if (valor <= 0) {
                    JOptionPane.showMessageDialog(null, "Ingrese un valor válido");
                    return;
                }

                if (valor > saldo) {
                    JOptionPane.showMessageDialog(null, "Saldo insuficiente");
                    return;
                }

                saldo -= valor;
                actualizarSaldo();
                guardarSaldoBD();
                agregarHistorial("Retiro de $" + valor);

                JOptionPane.showMessageDialog(null,
                        "Retiro exitoso\nNuevo saldo: $" + saldo);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido");
            }
        });

        // TRANSFERENCIA
        transferenciaButton.addActionListener(e -> {

            JTextField nombreDestino = new JTextField();
            JTextField monto = new JTextField();

            Object[] datos = {
                    "Nombre del destinatario:", nombreDestino,
                    "Monto a transferir:", monto
            };

            int opcion = JOptionPane.showConfirmDialog(
                    null,
                    datos,
                    "Transferencia",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (opcion == JOptionPane.OK_OPTION) {

                try {
                    String destino = nombreDestino.getText();

                    if (destino.isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                "Ingrese el nombre del destinatario");
                        return;
                    }

                    double cantidad = Double.parseDouble(monto.getText());

                    if (cantidad <= 0) {
                        JOptionPane.showMessageDialog(null,
                                "Ingrese un monto válido");
                        return;
                    }

                    if (cantidad > saldo) {
                        JOptionPane.showMessageDialog(null, "Saldo insuficiente");
                        return;
                    }

                    saldo -= cantidad;
                    actualizarSaldo();
                    guardarSaldoBD();
                    agregarHistorial("Transferencia a " + destino + " por $" + cantidad);

                    JOptionPane.showMessageDialog(null,
                            "Transferencia exitosa a " + destino +
                                    "\nMonto: $" + cantidad +
                                    "\nNuevo saldo: $" + saldo);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Monto inválido");
                }
            }
        });

        // SALIR
        salirButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "¡Gracias por usar el banco!");
            System.exit(0);
        });
    }

    private void actualizarSaldo() {
        Saldovalor.setText("$ " + String.format("%.2f", saldo));
    }

    private void agregarHistorial(String texto) {
        hisorialDeTransaccionesTextArea.append(texto + "\n");
    }

    private void guardarSaldoBD() {
        String sql = "UPDATE usuarios SET saldo=? WHERE usuario=?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, saldo);
            ps.setString(2, usuario);
            ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar saldo en la base de datos");
        }
    }

    public JPanel getBancoPanel() {
        return BancoPanel;
    }
}