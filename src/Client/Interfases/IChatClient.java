package Client.Interfases;

public interface IChatClient {
    boolean connect();
    void sendMessage(String message);
    void disconnect();
    void receiveMessages();
}