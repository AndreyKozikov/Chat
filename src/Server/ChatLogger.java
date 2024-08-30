package Server;

import Server.Interfaces.IChatLogger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ChatLogger implements IChatLogger {
    private PrintWriter writer;
    private String logFilePath;

    public ChatLogger(String fileName) throws IOException {
        this.logFilePath = fileName;
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)), true);
    }

    @Override
    public void log(String message) {
        writer.println(message);
    }

    @Override
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }

    @Override
    public String readLog() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
