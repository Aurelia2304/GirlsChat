package App.Client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main (String[] args){
        try (Socket clientSocket = new Socket("localhost", 8080);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))
        {
            writer.write("Привет, я клиент");
            writer.newLine();
            writer.flush();
            String response = reader.readLine();
            System.out.println(response);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}