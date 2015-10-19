package response;

/**
 * Created by neikila on 19.10.15.
 */
public enum StatusCode {
    OK("200 OK"),
    FORBIDDEN("403 FORBIDDEN"),
    NOT_FOUND("404 NOT FOUND"),
    METHOD_NOT_ALLOWED(" 405 METHOD NOT ALLOWED");

    private String representation;

    private StatusCode(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }
}
