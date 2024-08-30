//Обработчик подключения клинта
package Server;

import Server.Interfaces.IClientHandler;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements IClientHandler {
    private Socket clientSocket;
    private JTextArea logArea;
    private volatile boolean active;
    private static List<ClientInfo> clients;
    private static ChatLogger chatLogger;

    public ClientHandler(Socket clientSocket, JTextArea logArea, ChatLogger chatLogger, List<ClientInfo> clients) {
        this.clientSocket = clientSocket;
        this.logArea = logArea;
        this.chatLogger = chatLogger;
        this.active = false;
        this.clients = clients;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)) {

            String username = in.readLine();
            logArea.append(username + " подключился к чату.\n");
            clients.add(new ClientInfo(clientSocket, out)); // Добавляем сокет клиента в список
            //Отправка сообщений всем подключенным пользователям кроме отправителя
            String message;
            while ((message = in.readLine()) != null) {
                if ("/exit".equalsIgnoreCase(message)) {
                    logArea.append(username + " вышел из чата.\n");
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
                    logArea.append(username + ": " + message + "\n");
                    chatLogger.log(username + ": " + message); // Логирование сообщения
                    // Отправка ответа клиенту
                    broadcastMessage(username + ": " + message, clientSocket);
                }
            }

        } catch (Exception e) {
            logArea.append("Ошибка при работе с клиентом: " + e.getMessage() + "\n");
        } finally {
            try {
                active = false;
                clients.removeIf(clientInfo -> clientInfo.getSocket().equals(clientSocket));
                clientSocket.close();
            } catch (Exception ex) {
                logArea.append("Ошибка при закрытии соединения: " + ex.getMessage() + "\n");
            }
        }
    }

    private void broadcastMessage(String message, Socket senderSocket) {
        for (ClientInfo clientInfo : clients) {
            if (!clientInfo.getSocket().equals(senderSocket)) {
                try {
                    clientInfo.getWriter().println(message);
                } catch (Exception e) {
                    logArea.append("Ошибка при отправке сообщения клиенту: " + e.getMessage() + "\n");
                }
            }
        }
    }
}

