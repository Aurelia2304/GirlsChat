package App.Client;

import java.io.*;
import java.net.Socket;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class ClientWindow extends JFrame {
    private Socket clientS;
    private Scanner inMessage;
    private PrintWriter outMessage;
    /*элементы формы*/
    private JTextField jtfMessage;
    private JTextField jtfName;
    private JTextArea jtaTextAreaMessage;
    /*имя клиента*/
    private String clientName = "";

    public String getClientName() {
        return this.clientName;
    }

    public ClientWindow() {

        try {
            Socket clientS = new Socket("localhost", 8080);
            inMessage = new Scanner(clientS.getInputStream());
            outMessage = new PrintWriter(clientS.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*настройки элементов на форме*/
        setBounds(600, 300, 600, 500);
        setTitle("GirlsChat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaTextAreaMessage = new JTextArea();
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
        add(jsp, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton jbSendMessage = new JButton("Отправить");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField("Введите ваше сообщение: ");
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);
        jtfName = new JTextField("Введите ваше имя: ");
        bottomPanel.add(jtfName, BorderLayout.WEST);
    }
}
