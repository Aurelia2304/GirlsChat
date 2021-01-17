package App.Server;

import App.Bot.Bot;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ArrayList<String> clientsNames = new ArrayList<>();

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
            startBot();
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
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                // закрываем подключение
                clientS.close();
                System.out.println("Сервер остановлен");
                serverS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*отправка сообщения всем клиентам*/
    public void sendMessageToAllClients(String name, String msg) {
        if (!clientsNames.contains(name)) {
            if (name.length() <= 25) {
                clientsNames.add(name);
            }
        }
        for (ClientHandler o : clients) {
            o.sendMsg(name, msg);
        }
    }

    /*удаление клиента при выходе*/
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

public void startBot(){
        new Thread(()->{
            Bot bot = new Bot(this);
        }).start();
}

}