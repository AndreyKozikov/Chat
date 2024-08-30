package Server.Interfaces;

public interface IChatServer extends Runnable {
    void run();
    void stopServer();
}
