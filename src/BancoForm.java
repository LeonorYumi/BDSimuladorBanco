import javax.swing.*;
import conexion.ConexionBD;
import java.sql.*;

public class BancoForm {

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

        clientevalor.setText(nombre);
        actualizarSaldo();

        // DEPÓSITO
        depósitoButton.addActionListener(e -> {
            String valorStr = JOptionPane.showInputDialog("Ingrese valor a depositar:");
            try {
                double valor = Double.parseDouble(valorStr);
                if (valor <= 0) {
                    JOptionPane.showMessageDialog(null, "Valor inválido");
                    return;
                }

                saldo += valor;
                actualizarSaldo();
                guardarSaldoBD();
                agregarHistorial("Depósito: $" + valor);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido");
            }
        });

        // RETIRO
        retiroButton.addActionListener(e -> {
            String valorStr = JOptionPane.showInputDialog("Ingrese valor a retirar:");
            try {
                double valor = Double.parseDouble(valorStr);
                if (valor > saldo) {
                    JOptionPane.showMessageDialog(null, "Saldo insuficiente");
                    return;
                }

                saldo -= valor;
                actualizarSaldo();
                guardarSaldoBD();
                agregarHistorial("Retiro: $" + valor);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido");
            }
        });

        // TRANSFERENCIA
        transferenciaButton.addActionListener(e -> {

            JTextField nombreDestino = new JTextField();
            JTextField monto = new JTextField();

            Object[] datos = {
                    "Destinatario:", nombreDestino,
                    "Monto:", monto
            };

            if (JOptionPane.showConfirmDialog(null, datos,
                    "Transferencia", JOptionPane.OK_CANCEL_OPTION)
                    == JOptionPane.OK_OPTION) {

                try {
                    double cantidad = Double.parseDouble(monto.getText());
                    if (cantidad > saldo) {
                        JOptionPane.showMessageDialog(null, "Saldo insuficiente");
                        return;
                    }

                    saldo -= cantidad;
                    actualizarSaldo();
                    guardarSaldoBD();
                    agregarHistorial("Transferencia a " +
                            nombreDestino.getText() + ": $" + cantidad);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Monto inválido");
                }
            }
        });

        // SALIR
        salirButton.addActionListener(e -> System.exit(0));
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
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public JPanel getBancoPanel() {
        return BancoPanel;
    }
}
