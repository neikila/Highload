package handler;

/**
 * Created by neikila on 19.10.15.
 */
public enum StatusCode {
    OK                      ("200 OK"),
    BAD_REQUEST             ("400 BAD REQUEST"),
    FORBIDDEN               ("403 FORBIDDEN"),
    NOT_FOUND               ("404 NOT FOUND"),
    METHOD_NOT_ALLOWED      ("405 METHOD NOT ALLOWED"),
    UNSUPPORTED_MEDIA_TYPE  ("415 UNSUPPORTED MEDIA TYPE"),
    NOT_IMPLEMENTED         ("501 NOT IMPLEMENTED");

    private String representation;

    private StatusCode(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }
}
