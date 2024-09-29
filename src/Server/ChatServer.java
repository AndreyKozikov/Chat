package Server;

import Server.Interfaces.IChatLogger;
import Server.Interfaces.IChatServer;
import Server.Interfaces.IServerControlPanel;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer implements IChatServer {
    private int port;
    private IServerControlPanel gui;
    private ServerSocket serverSocket;
    private boolean running = true;
    private IChatLogger chatLogger;
    private static List<ClientInfo> clients = new CopyOnWriteArrayList<>();

    public ChatServer(int port, IServerControlPanel gui, IChatLogger chatLogger) {
        this.port = port;
        this.gui = gui;
        this.chatLogger = chatLogger;
    }


    @Override
    public void run() {
        try {

            serverSocket = new ServerSocket(port);
            gui.logMessageAppend("Сервер слушает на порту " + port + "...\n");

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept(); //Ожидание нового пользователя в чате
                    gui.logMessageAppend("Подключен новый клиент: " + clientSocket.getInetAddress() + "\n");
                    new Thread(new ClientHandler(clientSocket, gui, chatLogger, clients)).start(); //Запуск нового потока для вновь подключенного пользователя
                } catch (SocketException e) {
                    if (!running) {
                        gui.logMessageAppend("Сервер был остановлен.\n");
                    } else {
                        gui.logMessageAppend("Ошибка при ожидании подключения: " + e.getMessage() + "\n");
                    }
                }
            }
        } catch (Exception e) {
            gui.logMessageAppend("Ошибка при запуске сервера: " + e.getMessage() + "\n");
        } finally {
            if (chatLogger != null) {
                chatLogger.close(); // Закрытие логера при завершении работы сервера
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
            gui.logMessageAppend("Ошибка при остановке сервера: " + e.getMessage() + "\n");
        }
    }

    private void closeClientSockets() {
        for (ClientInfo clientInfo : clients) {
            try {
                Socket socket = clientInfo.getSocket();
                if (socket != null && !socket.isClosed()) {
                    try {
                        clientInfo.getWriter().println("/close");
                    } catch (Exception e) {
                        gui.logMessageAppend("Ошибка при отправке сообщения клиенту: " + e.getMessage() + "\n");
                    } finally {
                        try {
                            socket.close();
                            // Удаление клиента из списка после закрытия сокета
                            clients.remove(clientInfo);
                        } catch (IOException e) {
                            gui.logMessageAppend("Ошибка при закрытии клиентского сокета: " + e.getMessage() + "\n");
                        }
                    }
                }
            } catch (Exception e) {
                gui.logMessageAppend("Ошибка при обработке клиентского соединения: " + e.getMessage() + "\n");
            }
        }
    }
}
