package Client;

import javax.swing.SwingUtilities;

public class MainChat {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClientGUI().setVisible(true));
    }
}
