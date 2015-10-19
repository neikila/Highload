package handler;

/**
 * Created by neikila on 19.10.15.
 */
public enum Method {
    GET     (true),
    HEAD    (true),
    POST    (false),
    OPTIONS (false),
    PUT     (false),
    PATCH   (false),
    DELETE  (false),
    TRACE   (false),
    CONNECT (false);

    private boolean isImplemented;

    private Method(boolean isImplemented) {
        this.isImplemented = isImplemented;
    }

    public boolean isImplemented() {
        return isImplemented;
    }
}
