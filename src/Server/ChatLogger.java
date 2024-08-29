package Server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ChatLogger {
    private PrintWriter writer;
    private String logFilePath;


    public ChatLogger(String fileName) throws IOException {
        this.logFilePath = fileName;
        // Открываем файл для записи
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)), true);
    }

    public void log(String message) {
        // Записываем сообщение в файл
        writer.println(message);
    }

    public void close() {
        // Закрываем файл
        if (writer != null) {
            writer.close();
        }
    }

    // Метод для чтения содержимого файла лога
    public String readLog() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
