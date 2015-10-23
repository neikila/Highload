package handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private long fileSize;
    private String filename;

    public String getHeader() {
        return header;
    }

    public void setFile(String fileName) {
        this.filename = fileName;
    }

    public void readFile() throws IOException {
        // TODO что делать если сервер не поддерживает запрашивает тип данных? Сейчас говорит что файл не найден
        file = Files.readAllBytes(Paths.get(filename));
    }

    public void updateContentType() throws IllegalArgumentException{
        int point = filename.lastIndexOf('.');
        contentType = ContentType.valueOf((String) filename.subSequence(point + 1, filename.length()));
    }

    public void countSize() throws IOException {
        int point = filename.lastIndexOf('.');
        fileSize = Files.size(Paths.get(filename));
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
                    .append("Content-Length: ") .append(fileSize)    .append(rn)
                    .append("Content-Type: ")   .append(contentType) .append(rn);
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