package sync.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.Main;

import java.util.Scanner;

public class Console {

    private static final Logger logger = LoggerFactory.getLogger(Console.class);

    public static void main(String[] args) {
        boolean needToStop = false;
        Scanner scanner = new Scanner(System.in);
        logger.info("Command list: [start], [pause], [stop]");
        while (!Main.isError() && !needToStop){
            if(scanner.hasNextLine()){
                String command = scanner.nextLine();
                switch (command){
                    case "start":{
                        Main.start();
                        break;
                    }
                    case "pause":{
                        Main.pause();
                        if(Main.isPaused()){
                            logger.info("-Paused-");
                        } else {
                            logger.info("-Unpaused-");
                        }
                        break;
                    }
                    case "stop":{
                        Main.stop();
                        needToStop = true;
                        break;
                    }
                    default:{
                        logger.info("Wrong command (command list: [start], [pause], [stop]");
                        break;
                    }
                }
            }
        }
    }
}
