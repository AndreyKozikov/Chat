package Server;

public class MainServer {
    public static void main(String[] args) {
        // Запуск GUI в потоке обработки событий Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            ServerControlPanel controlPanel = new ServerControlPanel();
            controlPanel.setVisible(true);
        });
    }
}
