package App.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    /*список клиентов*/
    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    /*запуск сервера*/
    public Server() {
        /*создание серверного сокета*/
        try (ServerSocket serverS = new ServerSocket(8080)) {
            System.out.println("Сервер запущен. Ждём людишек!");
            while (true)
                try {
                    /*создание клиентского сокета*/
                    Socket clientS = serverS.accept();

                    /*обработчик подключения к серверу */
                    ClientHandler client = new ClientHandler(clientS, this);
                    clients.add(client);

                    /*каждое подключение клиента обрабатывается в новом потоке*/
                    new Thread(client).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*отправка сообщения всем клиентам*/
    public void sendMessageToAllClients(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    /*удаление клиента при выходе*/
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

}