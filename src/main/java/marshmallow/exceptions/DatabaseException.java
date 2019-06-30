package marshmallow.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String s, Exception e) {
        super(s, e);
    }
}
