package marshmallow.exceptions;

public class IllegalCommandException extends IllegalArgumentException  {
    public IllegalCommandException(String message, String prefix, String newCommand, String oldCommand) {
        this(String.format(message, prefix, newCommand, oldCommand));
    }

    public IllegalCommandException(String prefix, String newCommand, String oldCommand) {
        this("The \"%s\" prefix from the \"%s\" command is already registered to the \"%s\" command!", prefix, newCommand, oldCommand);
    }

    public IllegalCommandException(String message) {
        super(message);
    }
}
