package App.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    /*список клиентов*/
    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    /*запуск сервера*/
    public Server() {
        Socket clientS = null;
        ServerSocket serverS = null;
        /*создание серверного сокета*/

        try {
            serverS = new ServerSocket(8080);
            System.out.println("Сервер запущен. Ждём людишек!");
            while (true) {
                /*создание клиентского сокета*/
                clientS = serverS.accept();
                /*обработчик подключения к серверу */
                ClientHandler client = new ClientHandler(clientS, this);
                clients.add(client);
                /*каждое подключение клиента обрабатывается в новом потоке*/
                new Thread(client).start();

            }
        }

        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                // закрываем подключение
                clientS.close();
                System.out.println("Сервер остановлен");
                serverS.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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