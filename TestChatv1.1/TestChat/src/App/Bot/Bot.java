package App.Bot;

import App.Server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Bot {

    public final static String NAME_BOT = "БОТЯРА";

    public final static String COMMAND_PRINT_ALL_NAMES = "allnames";

    private Socket socket;
    private Scanner inMessage;
    private PrintWriter outMessage;

    public Bot(Server server) {
        try {
            /*запуск сокета бота*/
            socket = new Socket("localhost", 8080);
            inMessage = new Scanner(socket.getInputStream());
            outMessage = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (true) {
                /*если есть входящее сообщение*/
                if (inMessage.hasNext()) {
                    /*считываем его*/
                    String inMes = inMessage.nextLine();
                    if (inMes.contains(NAME_BOT)) {
                        if (inMes.contains(COMMAND_PRINT_ALL_NAMES)) {
                            server.printAllNames();
                        } else {
                            generateAnswer(inMes.replace(NAME_BOT, ""));
                        }
                    }
                }
            }
        }

         catch(Exception e){
                e.printStackTrace();
            }

            outMessage.flush();
            outMessage.close();
            inMessage.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    String dirtyPhrases[] = {"Невоспитанный пес!", "Осуждаю", "Иди худей!", "Ля,Krisa"};
    private void generateAnswer(String message){
        if (message.contains("Привет")) {
            sendMsg("Бот: И тебе привет, котеночек)");
        } else {
            Random random = new Random(System.currentTimeMillis());
            sendMsg("Бот:" + dirtyPhrases[random.nextInt(dirtyPhrases.length)]);
        }
    }
    /*отправкасообщения*/
    public void sendMsg(String message){
        outMessage.println(message);
        outMessage.flush();
    }
}