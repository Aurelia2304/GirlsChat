package App.Server;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 0){
            if (args[0].equals("-1")){
                Logger logger = Logger.getLogger("ServerLogger");
                FileHandler fileHandler = new FileHandler("serverLogger" + System.currentTimeMillis()+".log");
                logger.addHandler(fileHandler);
                logger.info("Логгер работает!");
            }
        }

        Server server = new Server();
    }
}