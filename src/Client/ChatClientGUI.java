package Client;

import javax.swing.*;
import java.awt.*;

public class ChatClientGUI extends JFrame {
    private ConnectionPanel connectionPanel;
    private ChatPanel chatPanel;


    public ChatClientGUI() {
        setTitle("Chat Client");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());



        // Панель чата (отправка и отображение сообщений)
        chatPanel = new ChatPanel();
        add(chatPanel, BorderLayout.CENTER);

        // Панель для подключения (IP, порт, логин, пароль)
        connectionPanel = new ConnectionPanel(chatPanel, this);
        add(connectionPanel, BorderLayout.NORTH);
    }


    public void setPanelVisible(boolean isConnect) {
        if (isConnect) {
            connectionPanel.setVisible(false); // Скрыть панель подключения
            chatPanel.setVisible(true); // Показать панель чата
        } else {
            connectionPanel.setVisible(true);
            chatPanel.setVisible(false);
        }
    }
}
