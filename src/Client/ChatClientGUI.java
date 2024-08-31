package Client;

import Client.Interfases.IChatClientGUI;

import javax.swing.*;
import java.awt.*;

public class ChatClientGUI extends JFrame implements IChatClientGUI {
    private ConnectionPanel connectionPanel;
    private ChatPanel chatPanel;

    public ChatClientGUI() {
        setTitle("Chat Client");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        chatPanel = new ChatPanel();
        add(chatPanel, BorderLayout.CENTER);

        connectionPanel = new ConnectionPanel(chatPanel, this);
        add(connectionPanel, BorderLayout.NORTH);
    }

    @Override
    public void setPanelVisible(boolean isConnect) {
        connectionPanel.setVisible(!isConnect);
        chatPanel.setVisible(isConnect);
    }

    @Override
    public void appendMessage(String message) {
        chatPanel.appendMessage(message);
    }

    @Override
    public void clearChat() {
        chatPanel.reset();
    }
}
