package App.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class Server {

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