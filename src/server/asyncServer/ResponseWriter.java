package server.asyncServer;

import handler.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by neikila on 22.10.15.
 */
public class ResponseWriter implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel connection;
    private static Logger logger = LogManager.getLogger(Response.class.getName());

    public ResponseWriter(AsynchronousSocketChannel ch) {
        connection = ch;
    }

    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error(exc);
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        logger.debug("Bytes written: {}", result);
        logger.debug("Left: position = {}, limit = {}", attachment.position(), attachment.limit());
        try {
            connection.close();
            logger.debug("Connection closed");
        } catch (Exception e) {
            logger.error(e);
        }
    }
}