package Server.Interfaces;

import java.io.IOException;

public interface IChatLogger {
    void log(String message);

    void close();

    String readLog() throws IOException;
}