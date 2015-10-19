package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

/**
 * Created by neikila on 16.10.15.
 */
public class Main {
    public static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8081;
        }
        try {
            new Server(port).run();
            logger.error("Server started on port {}", port);
        } catch (Exception e) {
            logger.error(e);
            logger.error("Error in DiscardServer.run");
            System.exit(-1);
        }
    }
}
