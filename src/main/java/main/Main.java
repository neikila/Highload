package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.DiscardServer;

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
            port = 8080;
        }
        try {
            new DiscardServer(port).run();
        } catch (Exception e) {
            logger.error(e);
            logger.error("Error in DiscardServer.run");
            System.exit(-1);
        }
    }
}
