package Server;

import Server.Interfaces.IServerControlPanel;

import javax.swing.*;
import java.awt.*;

public class ServerControlPanel extends JFrame implements IServerControlPanel {
    private boolean isServerWorking = false;
    private ChatServer chatServer;
    private JTextArea logArea;

    public ServerControlPanel() {
        setTitle("Управление сервером");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        logArea.setMargin(new Insets(5, 10, 5, 10));
        add(scrollPane, BorderLayout.CENTER);

        JButton startButton = new JButton("Запустить сервер");
        JButton stopButton = new JButton("Остановить сервер");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        add(buttonPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());
    }

    @Override
    public void startServer() {
        if (!isServerWorking) {
            isServerWorking = true;
            logArea.append("Сервер запущен.\n");
            chatServer = new ChatServer(9999, logArea);
            new Thread(chatServer).start();
        } else {
            logArea.append("Сервер уже работает.\n");
        }
    }

    @Override
    public void stopServer() {
        if (isServerWorking) {
            isServerWorking = false;
            chatServer.stopServer();
        } else {
            logArea.append("Сервер уже остановлен.\n");
        }
    }
}
