package handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by neikila on 19.10.15.
 */
public class Response {
    final private static Logger logger = LogManager.getLogger(Response.class.getName());

    final private String rn = "\r\n";
    final private String serverName = "My_beautiful_server";
    final private String dateFormat = "EEE MMM  d HH:mm:ss yyyy";

    private String header;
    private byte[] file;
    private ContentType contentType;

    public String getHeader() {
        return header;
    }

    public void readFile(String filename) throws IOException {
        int point = filename.lastIndexOf('.');
        try {
            contentType = ContentType.valueOf((String) filename.subSequence(point + 1, filename.length()));
            // TODO что делать если сервер не поддерживает запрашивает тип данных? Сейчас говорит что файл не найден
        } catch (IllegalArgumentException e) {
            logger.debug("Error");
            throw new IOException();
        }
        file = MyFileReader.getFile(filename);
    }

    public byte[] getFile() {
        return file;
    }

    public void buildHeader(StatusCode status) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("HTTP/1.1 ")        .append(status)         .append(rn)
                .append("Server: ")         .append(serverName)     .append(rn)
                .append("Date: ")           .append(getDate())      .append(rn)
                .append("Connection: close")                        .append(rn);

        /* Additional field "Allow" */
        if (status.equals(StatusCode.NOT_IMPLEMENTED) || status.equals(StatusCode.METHOD_NOT_ALLOWED)) {
            stringBuilder.append("Allow: ");
            for (Method method: Method.values()) {
                if (method.isImplemented()) {
                    stringBuilder.append(method).append(", ");
                }
            }
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length() - 1, rn);
        }

        if (status.equals(StatusCode.OK)) {
            stringBuilder
                    .append("Content-Length: ") .append(file.length)    .append(rn)
                    .append("Content-Type: ")   .append(contentType)    .append(rn);
        }

        stringBuilder.append(rn);
        header = stringBuilder.toString();
    }

    private String getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }
}