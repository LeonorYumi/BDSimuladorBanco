import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                Login login = new Login();
                login.setVisible(true);
            } catch (Throwable t) {
                t.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Error al iniciar la aplicación:\n" + t,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}