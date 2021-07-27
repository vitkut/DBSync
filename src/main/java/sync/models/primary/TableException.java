package sync.models.primary;

public class TableException extends Exception {

    public TableException() {
        super("Something is bad with table");
    }

    public TableException(String message) {
        super(message);
    }
}
