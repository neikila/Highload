package main;

import server.DiscardServer;

/**
 * Created by neikila on 16.10.15.
 */
public class Main {
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
            System.out.println(e);
            System.out.println("Error in DiscardServer.run");
            System.exit(-1);
        }
    }
}
