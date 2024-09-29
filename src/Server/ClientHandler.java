//Обработчик подключения клинта
package Server;

import Server.Interfaces.IChatLogger;
import Server.Interfaces.IClientHandler;
import Server.Interfaces.IServerControlPanel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements IClientHandler {
    private Socket clientSocket;
    private IServerControlPanel gui;
    private volatile boolean active;
    private static List<ClientInfo> clients;
    private static IChatLogger chatLogger;

    public ClientHandler(Socket clientSocket, IServerControlPanel gui, IChatLogger chatLogger, List<ClientInfo> clients) {
        this.clientSocket = clientSocket;
        this.gui = gui;
        this.chatLogger = chatLogger;
        this.active = false;
        this.clients = clients;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)) {

            String username = in.readLine();
            gui.logMessageAppend(username + " подключился к чату.\n");
            clients.add(new ClientInfo(clientSocket, out)); // Добавляем сокет клиента в список
            //Отправка сообщений всем подключенным пользователям кроме отправителя
            String message;
            while ((message = in.readLine()) != null) {
                if ("/exit".equalsIgnoreCase(message)) {
                    gui.logMessageAppend(username + " вышел из чата.\n");
                    broadcastMessage(username + " вышел из чата.", clientSocket);
                    break;
                }
                if (!active) {
                    active = true;
                    broadcastMessage(username + " подключился к чату", clientSocket);
                    // Отправка содержимого лог файла новому клиенту
                    String logContent = chatLogger.readLog();
                    out.println(logContent);
                } else {
                    gui.logMessageAppend(username + ": " + message + "\n");
                    chatLogger.log(username + ": " + message); // Логирование сообщения
                    // Отправка ответа клиенту
                    broadcastMessage(username + ": " + message, clientSocket);
                }
            }

        } catch (Exception e) {
            gui.logMessageAppend("Ошибка при работе с клиентом: " + e.getMessage() + "\n");
        } finally {
            try {
                active = false;
                clients.removeIf(clientInfo -> clientInfo.getSocket().equals(clientSocket));
                clientSocket.close();
            } catch (Exception ex) {
                gui.logMessageAppend("Ошибка при закрытии соединения: " + ex.getMessage() + "\n");
            }
        }
    }

    private void broadcastMessage(String message, Socket senderSocket) {
        for (ClientInfo clientInfo : clients) {
            if (!clientInfo.getSocket().equals(senderSocket)) {
                try {
                    clientInfo.getWriter().println(message);
                } catch (Exception e) {
                    gui.logMessageAppend("Ошибка при отправке сообщения клиенту: " + e.getMessage() + "\n");
                }
            }
        }
    }
}

