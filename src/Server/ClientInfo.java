package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientInfo {
    private Socket socket;
    private String name;
    private String password;
    private PrintWriter writer;

    public ClientInfo(Socket socket, PrintWriter out) throws IOException {
        this.socket = socket;
        this.writer = out;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}