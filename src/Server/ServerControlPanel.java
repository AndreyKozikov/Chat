package Server;

import javax.swing.*;
import java.awt.*;

public class ServerControlPanel extends JFrame {
    private boolean isServerWorking = false;
    private ChatServer chatServer;
    private JTextArea logArea;

    public ServerControlPanel() {
        setTitle("Управление сервером");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Создание текстового поля для вывода логов
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        // Установка отступов для текста внутри JTextArea
        logArea.setMargin(new Insets(5, 10, 5, 10)); // Отступы: сверху, слева, снизу, справа
        add(scrollPane, BorderLayout.CENTER);

        // Создание кнопок
        JButton startButton = new JButton("Запустить сервер");
        JButton stopButton = new JButton("Остановить сервер");

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        // Размещение панели с кнопками внизу окна
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчик для кнопки "Запустить сервер"
        startButton.addActionListener(e -> {
            if (!isServerWorking) {
                isServerWorking = true;
                logArea.append("Сервер запущен.\n");
                chatServer = new ChatServer(9999, logArea);
                new Thread(chatServer).start();
            } else {
                logArea.append("Сервер уже работает.\n");
            }
        });

        // Обработчик для кнопки "Остановить сервер"
        stopButton.addActionListener(e -> {
            if (isServerWorking) {
                isServerWorking = false;
                chatServer.stopServer();
            } else {
                logArea.append("Сервер уже остановлен.\n");
            }
        });
    }
}
