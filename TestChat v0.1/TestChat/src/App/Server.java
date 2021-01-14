package App;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main (String[] args) {
        try (ServerSocket server = new ServerSocket(8080)){
            System.out.println("Сервер запущен...");
            while (true)
                try{
                    Socket socket = server.accept();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try (BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream()));
                                 BufferedWriter writer = new BufferedWriter(
                                         new OutputStreamWriter(socket.getOutputStream()));)
                            {
                                System.out.println("Клиент подключен...");
                                String request = reader.readLine();
                                String response = String.format("Привет, твой запрос = %s", request);
                                System.out.println(response);
                                writer.write(response);
                                writer.newLine();
                                writer.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}