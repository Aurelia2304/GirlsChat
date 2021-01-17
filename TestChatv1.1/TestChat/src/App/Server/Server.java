package App.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class Server {

    private ArrayList<String> clientsNames = new ArrayList<>();

    //static Logger logger = LogManager.getLogger(Server.class);

    /*список клиентов*/
    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    /*запуск сервера*/
    public Server() {
        Socket clientS = null;
        ServerSocket serverS = null;
        /*создание серверного сокета*/
        try {
            serverS = new ServerSocket(8080);

            //logger.info("Сокет сервера создан");

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
           // logger.error("Ошибка запуска сервера");
        }
        finally {
            try {
                // закрываем подключение
                clientS.close();
                System.out.println("Сервер остановлен");
                //logger.info("Сервер остановлен");
                serverS.close();
            } catch (IOException ex) {
                //logger.error("Ошибка остановки сервера");
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


    public void printAllNames() {
        for (int i = 0; i < clientsNames.size(); i++) {
            sendMessageToAllClients("Сервер", clientsNames.get(i));
        }
    }
}

