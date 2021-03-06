package App.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class ClientHandler implements Runnable {
    /*экземпляр нашего сервера*/
    private Server server;
    /*исходящее сообщение*/
    private PrintWriter outMessage;
    /*входящее собщение*/
    private Scanner inMessage;
    /*клиентский сокет*/
    private Socket clientSocket = null;
    /*количество клиентов в чате, статичное поле*/
    private static int clients_count = 0;
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    // конструктор, который принимает клиентский сокет и сервер
    public ClientHandler(Socket socket, Server server) {
        try {
            /*при присоединении нового клиента*/
            clients_count++;
            this.server = server;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                /*сервер отправляет сообщение*/
                server.sendMessageToAllClients("Неизвестный", "Нас подслушивает неизвестный...");
                //выводим количество подключенных клиентов
                server.sendMessageToAllClients("","Теперь пользователей на сервере аж "+ clients_count+ "!");
                break;
            }
            while (true) {
                /*если клиент отправляет сообщение*/
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    String nameMessage[] = clientMessage.split(":");
                    /*данное сообщение отправлятся всем клиентам*/
                    if (nameMessage.length == 2){
                        server.sendMessageToAllClients(nameMessage[0], nameMessage[1]);
                        if (nameMessage[1].contains("вышел из чата!")) {
                            server.removeClient(this);
                        } else if (nameMessage[1].contains("покинул нас")) {
                            server.removeClient(this);
                        }
                    }
                    if (clientMessage.equalsIgnoreCase("Сессия завершена")) {
                        break;
                    }
                }
                /*приостановка потока*/
                Thread.sleep(100);
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            this.close();
        }
    }

    /*отправка сообщения*/
    public void sendMsg(String name, String msg) {
        try {
            outMessage.println(name + ":" + msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*клиент выходит из чата*/
    public void close() {
        /*удаление клиента из списка*/
        server.removeClient(this);
        clients_count--;
    }
}
