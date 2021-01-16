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

    public ClientWindow(){

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
        JLabel jlNumberOfClients = new JLabel("Количество клиентов в чате: ");
        add(jlNumberOfClients, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton jbSendMessage = new JButton("Отправить");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField("Введите ваше сообщение: ");
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);
        jtfName = new JTextField("Введите ваше имя: ");
        bottomPanel.add(jtfName, BorderLayout.WEST);

        /*обработчик события нажатия кнопки отправки сообщения*/
        jbSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*если имя клиента, и сообщение непустые, то отправляем сообщение*/
                if (!jtfMessage.getText().trim().isEmpty() && !jtfName.getText().trim().isEmpty()) {
                    clientName = jtfName.getText();
                    sendMsg();
                    /*фокус на текстовое поле с сообщением*/
                    jtfMessage.grabFocus();
                }
            }
        });

        /*при фокусе поле сообщения очищается*/
        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessage.setText("");
            }
        });

        /*при фокусе поле имя очищается*/
        jtfName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfName.setText("");
            }
        });

        /*работа с сервером в отдельном потоке*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {

                        /*если есть входящее сообщение*/
                        if (inMessage.hasNext()) {
                            /*считываем его*/
                            String inMes = inMessage.nextLine();
                            String clientsInChat = "Клиентов в чате = ";
                            if (inMes.indexOf(clientsInChat) == 0) {
                                jlNumberOfClients.setText(inMes);
                            } else {
                                /*выводим сообщение*/
                                jtaTextAreaMessage.append(inMes);
                                /*добавляем строку перехода*/
                                jtaTextAreaMessage.append("\n");
                            }
                        }
                    }
                }
                catch (Exception e) {
                }
            }
        }).start();

        /*обработчик события закрытия окна клиентского приложения*/
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    // здесь проверяем, что имя клиента непустое и не равно значению по умолчанию
                    if (!clientName.isEmpty() && clientName != "Введите ваше имя: ") {
                        outMessage.println(clientName + " вышел из чата!");
                    } else {
                        outMessage.println("Тайный слушатель покинул нас");
                    }
                    /*служебное сообщение*/
                    outMessage.flush();
                    outMessage.close();
                    inMessage.close();
                    clientS.close();
                } catch (IOException exc) {

                }
            }
        });
        // отображаем форму
        setVisible(true);
    }
    /*отправка сообщения*/
    public void sendMsg() {
        /*формирование сообщения для отправки на сервер */
        String messageStr = jtfName.getText() + ": " + jtfMessage.getText();
        /*отправка сообщения*/
        outMessage.println(messageStr);
        outMessage.flush();
        jtfMessage.setText("");
    }

}
