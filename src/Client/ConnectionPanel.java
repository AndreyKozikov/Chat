package Client;

import javax.swing.*;
import java.awt.*;

public class ConnectionPanel extends JPanel {
    private JTextField ipField;
    private JTextField portField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public ConnectionPanel(ChatPanel chatPanel,  ChatClientGUI gui) {
        setLayout(new GridLayout(2, 1)); // Две строки

        JPanel row1 = new JPanel(new GridLayout(1, 2)); // Одна строка, два столбца
        ipField = new JTextField("127.0.0.1");
        portField = new JTextField("9999");
        //row1.add(new JLabel("Server IP:"));
        row1.add(ipField);
        //row1.add(new JLabel("Port:"));
        row1.add(portField);

        JPanel row2 = new JPanel(new GridLayout(1, 3)); // Одна строка, три столбца
        usernameField = new JTextField("Username");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        //row2.add(new JLabel("Username:"));
        row2.add(usernameField);
        //row2.add(new JLabel("Password:"));
        row2.add(passwordField);
        row2.add(loginButton);

        add(row1);
        add(row2);

        loginButton.addActionListener(e -> {
            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();
            new ChatClient(ip, port, username, password, chatPanel, gui);
        });
    }
}
