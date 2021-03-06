package main;

import com.beust.jcommander.JCommander;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.asyncServer.AsyncServer;

/**
 * Created by neikila on 16.10.15.
 */
public class Main {
    public static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        ArgumentsParser parser = new ArgumentsParser();
        new JCommander(parser, args);
        int port = parser.getPort();
//        if (args.length > 0) {
//            port = Integer.parseInt(args[0]);
//        } else {
//            port = 8081;
//        }
        logger.info("Server started on port {}", port);
        logger.info("Root directory {}", parser.getRootDir());
        AsyncServer temp = new AsyncServer(port, parser.getPullSize(), parser.getRootDir());
        temp.start();
//        try {
//            new Server(port, parser.getRootDir()).run();
//        } catch (Exception e) {
//            logger.error("Error in DiscardServer.run");
//            logger.error(e.getStackTrace());
//            System.exit(-1);
//        }
    }
}
