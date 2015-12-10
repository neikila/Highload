package server.blockingServer;

import handler.Method;
import handler.Request;
import handler.Response;
import handler.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.asyncServer.RequestHandler;

import java.io.*;
import java.net.Socket;

/**
 * Created by neikila on 21.10.15.
 */
public class Task implements Runnable {
    private Socket connection;
    private String rootDir;
    private Logger logger = LogManager.getLogger(Task.class.getName());
    private OutputStream out;
    private BufferedReader in;

    public Task(Socket connection, String rootDir) {
        this.connection = connection;
        this.rootDir = rootDir;
        try {
            out = connection.getOutputStream();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            logger.error("Error while opening stream in socket");
            logger.error(e);
        }
    }

    @Override
    public void run() {
        StringBuilder requestMessage = new StringBuilder();
        String temp;
        try {
//            logger.debug("Start");
            while ((temp = in.readLine()) != null) {
                if (temp.equals(""))
                    break;
                requestMessage.append(temp).append('\n');
            }
            if (requestMessage.toString().length() > 0) {
//                logger.debug("Request:\n{}", requestMessage.toString());
                Request request = new Request(requestMessage.toString());
                Response response = new Response();
                StatusCode statusCode = RequestHandler.getResponse(request, response, rootDir);
                out.write(response.getHeader().getBytes());
                if (statusCode.equals(StatusCode.OK) && request.getMethod().equals(Method.GET)) {
                    out.write(response.getFile());
                }
            }
        } catch (Exception e) {
            logger.error("Error while closing sockets");
            logger.error(e);
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                connection.close();
            } catch (IOException e) {
                logger.error("Uuups");
            }
        }
    }
}
