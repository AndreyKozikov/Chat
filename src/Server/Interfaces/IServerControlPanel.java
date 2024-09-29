package Server.Interfaces;

import java.io.IOException;

public interface IServerControlPanel {
    void startServer() throws IOException;
    void stopServer();
    void logMessageAppend(String message);
}
