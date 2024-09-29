package Server;

import Server.Interfaces.IChatLogger;
import Server.Interfaces.IChatServer;
import Server.Interfaces.IServerControlPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ServerControlPanel extends JFrame implements IServerControlPanel {
    private boolean isServerWorking = false;
    private JTextArea logArea;
    private IChatServer chatServer;

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

        startButton.addActionListener(e -> {
            try {
                startServer();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        stopButton.addActionListener(e -> stopServer());
    }

    @Override
    public void startServer() throws IOException {
        if (!isServerWorking) {
            isServerWorking = true;
            logMessageAppend("Сервер запущен.\n");
            IChatLogger chatLogger = new ChatLogger("chat_logs.txt");
            chatServer = new ChatServer(9999, this, chatLogger);
            new Thread(chatServer).start();
        } else {
            logMessageAppend("Сервер уже работает.\n");
        }
    }

    @Override
    public void stopServer() {
        if (isServerWorking) {
            isServerWorking = false;
            chatServer.stopServer();
        } else {
            logMessageAppend("Сервер уже остановлен.\n");
        }
    }

    public void logMessageAppend(String message){
        logArea.append(message);
    }
}
