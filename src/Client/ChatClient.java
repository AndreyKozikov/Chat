package Client;

import Client.Interfases.IChatClient;
import Client.Interfases.IChatClientGUI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ChatClient implements IChatClient {
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());
    private String ip;
    private int port;
    private String username;
    private char[] password;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private IChatClientGUI gui;

    public ChatClient(String ip, int port, String username, char[] password, IChatClientGUI gui) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.gui = gui;

        if (connect()) {
            gui.setPanelVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось подключиться к серверу", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean connect() {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(username);
            out.println(String.valueOf(password));

            new Thread(this::receiveMessages).start();
            return true;
        } catch (Exception e) {
            logger.severe("Ошибка подключения: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if ("/close".equalsIgnoreCase(message)) {
                    break;
                } else {
                    String finalMessage = message;
                    SwingUtilities.invokeLater(() -> gui.appendMessage(finalMessage));
                }
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> gui.appendMessage("Произошло отключение от сервера."));
        } finally {
            disconnect();
        }
    }

    @Override
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            SwingUtilities.invokeLater(() -> gui.appendMessage("You: " + message));
        }
    }

    @Override
    public void disconnect() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
            gui.setPanelVisible(false);
            gui.clearChat();
            JOptionPane.showMessageDialog(null, "Произошло отключение от сервера", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> gui.appendMessage("Ошибка при отключении: " + e.getMessage()));
        }
    }
}
