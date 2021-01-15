package App.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class ClientHandler implements Runnable {
    /*экземпляр сервера*/
    private Server server;
    /*исходящее сообщение*/
    private PrintWriter outMessage;
    /*входящее собщение*/
    private Scanner inMessage;
    /*клиентский сокет*/
    private Socket clientSocket = null;

    // конструктор, который принимает клиентский сокет и сервер
    public ClientHandler(Socket socket, Server server) {
        try {
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
                server.sendMessageToAllClients("Нас подслушивает неизвестный...");
                break;
            }
            while (true) {
                /*если клиент отправляет сообщение*/
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    /*данное сообщение отправлятся всем клиентам*/
                    server.sendMessageToAllClients(clientMessage);
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
    public void sendMsg(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*клиент выходит из чата*/
    public void close() {
        /*удаление клиента из списка*/
        server.removeClient(this);
    }
}
