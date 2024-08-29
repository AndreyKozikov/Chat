package Client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private String ip;
    private int port;
    private String username;
    private char[] password;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ChatPanel chatPanel;
    private ChatClient chatClient;
    private ChatClientGUI gui;


    public ChatClient(String ip, int port, String username, char[] password, ChatPanel chatPanel, ChatClientGUI gui) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.chatPanel = chatPanel;
        this.gui = gui;
        if (connect()) {
            chatPanel.setChatClient(this);
            gui.setPanelVisible(true);
        } else {
            JOptionPane.showMessageDialog(chatPanel, "Не удалось подключиться к серверу", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean connect() {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Отправка имени пользователя и пароля
            out.println(username);
            out.println(String.valueOf(password));

            // Начать прослушивание сообщений от сервера
            new Thread(this::receiveMessages).start();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if ("/close".equalsIgnoreCase(message)) {
                    break;
                } else {
                    String finalMessage = message;
                    SwingUtilities.invokeLater(() -> chatPanel.appendMessage(finalMessage));
                }
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> chatPanel.appendMessage("Произошло отключение от сервера."));
        } finally {
            disconnect(); // Закрытие ресурсов в случае ошибки
        }

    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            SwingUtilities.invokeLater(() -> chatPanel.appendMessage("You: " + message));
        }
    }

    private void disconnect() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            gui.setPanelVisible(false);
            chatPanel.reset();
            SwingUtilities.invokeLater(() -> chatPanel.appendMessage("Вы отключились от сервера."));
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> chatPanel.appendMessage("Ошибка при отключении: " + e.getMessage()));
        }
    }
}
