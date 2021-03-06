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
    swf("application/x-shockwave-flash"),
    txt("text/plain");
//    "; charset=utf-8"

    private String representation;

    private ContentType(String representation) {
        this.representation = representation;
    }

    static public ContentType getType(String filename) {
        try {
            int point = filename.lastIndexOf('.');
            return ContentType.valueOf((String) filename.subSequence(point + 1, filename.length()));
        } catch (IllegalArgumentException e) {
            return txt;
        }
    }

    @Override
    public String toString() {
        return representation;
    }
}
