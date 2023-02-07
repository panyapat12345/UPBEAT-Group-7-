package Exception;

public class ReservedWordException extends RuntimeException{
    public ReservedWordException() {
        super();
    }

    public ReservedWordException(String s) {
        super(s);
    }
}
