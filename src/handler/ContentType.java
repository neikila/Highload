package handler;

/**
 * Created by neikila on 19.10.15.
 */
public enum ContentType {
    html("text/html"),
    css("text/css"),
    js("application/javascript"),
    jpg("image/jpeg"),
    jpeg("image/jpeg"),
    png("image/png"),
    gif("image/gif"),
    swf("application/x-shockwave-flash");

    private String representation;

    private ContentType(String representation) {
        this.representation = representation;
    }


    @Override
    public String toString() {
        return representation;
    }
}
