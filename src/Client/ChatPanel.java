package Client;

import Client.Interfases.IChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private IChatClient chatClient;

    public ChatPanel() {
        setLayout(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setMargin(new Insets(5, 10, 5, 10));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        add(scrollPane, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");

        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        add(messagePanel, BorderLayout.SOUTH);

        // Добавляем обработчики событий для отправки сообщения
        sendButton.addActionListener(e -> sendMessageFromField());
        messageField.addActionListener(e -> sendMessageFromField());
    }

    public void setChatClient(IChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }

    public void sendMessage(String message) {
        if (chatClient != null) {
            chatClient.sendMessage(message);
        }
    }

    public void reset() {
        chatArea.setText("");
        messageField.setText("");
    }

    // Приватный метод для обработки отправки сообщений
    private void sendMessageFromField() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            sendMessage(message);
            messageField.setText("");
        }
    }
}
