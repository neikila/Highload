package handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by neikila on 19.10.15.
 */
public class Response {
    final private String rn = "\r\n";
    final private String serverName = "My_beautiful_server";
    final private String dateFormat = "EEE MMM  d HH:mm:ss yyyy";

    public String buildHeader(StatusCode status, long contentLength, ContentType contentType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("HTTP/1.1 ")        .append(status)         .append(rn)
                .append("Server: ")         .append(serverName)     .append(rn)
                .append("Date: ")           .append(getDate())      .append(rn)
                .append("Content-Type: ")   .append(contentType)    .append(rn)
                .append("Content-Length: ") .append(contentLength)  .append(rn)
                .append("Connection: close")                        .append(rn)
                .append(rn);
        return stringBuilder.toString();
    }

    public String buildHeader(StatusCode status) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("HTTP/1.1 ")        .append(status)         .append(rn)
                .append("Server: ")         .append(serverName)     .append(rn)
                .append("Date: ")           .append(getDate())      .append(rn)
                .append("Connection: close")                        .append(rn)
                .append(rn);
        return stringBuilder.toString();
    }

    private String getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }
}