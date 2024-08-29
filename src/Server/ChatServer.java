package Server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer implements Runnable {
    private int port;
    private JTextArea logArea;
    private ServerSocket serverSocket;
    private boolean running = true;
    private ChatLogger chatLogger;
    private static List<ClientInfo> clients = new CopyOnWriteArrayList<>();


    public ChatServer(int port, JTextArea logArea) {
        this.port = port;
        this.logArea = logArea;
    }

    @Override
    public void run() {
        try {
            chatLogger = new ChatLogger("chat_logs.txt");
            serverSocket = new ServerSocket(port);
            logArea.append("Сервер слушает на порту " + port + "...\n");

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept(); //Ожидание нового пользователя в чате
                    logArea.append("Подключен новый клиент: " + clientSocket.getInetAddress() + "\n");
                    new Thread(new ClientHandler(clientSocket, logArea, chatLogger, clients)).start(); //Запуск нового потока для вновь подключенного пользователя
                } catch (SocketException e) {
                    if (!running) {
                        logArea.append("Сервер был остановлен.\n");
                    } else {
                        logArea.append("Ошибка при ожидании подключения: " + e.getMessage() + "\n");
                    }
                }
            }
        } catch (Exception e) {
            logArea.append("Ошибка при запуске сервера: " + e.getMessage() + "\n");
        } finally {
            if (chatLogger != null) {
                chatLogger.close(); // Закрытие логгера при завершении работы сервера
            }
        }
    }

    public void stopServer() {
        running = false;
        closeClientSockets(); // Закрыть все клиентские сокеты
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (Exception e) {
            logArea.append("Ошибка при остановке сервера: " + e.getMessage() + "\n");
        }
    }

    private void closeClientSockets() {
        for (ClientInfo clientInfo : clients) {
            try {
                Socket socket = clientInfo.getSocket();
                if (socket != null && !socket.isClosed()) {
                    try {
                        clientInfo.getWriter().println("Произошла остановка сервера. Соединение закрыто.");
                        clientInfo.getWriter().println("/close");
                    } catch (Exception e) {
                        logArea.append("Ошибка при отправке сообщения клиенту: " + e.getMessage() + "\n");
                    } finally {
                        try {
                            socket.close();
                            // Удаление клиента из списка после закрытия сокета
                            clients.remove(clientInfo);
                        } catch (IOException e) {
                            logArea.append("Ошибка при закрытии клиентского сокета: " + e.getMessage() + "\n");
                        }
                    }
                }
            } catch (Exception e) {
                logArea.append("Ошибка при обработке клиентского соединения: " + e.getMessage() + "\n");
            }
        }
    }
}
