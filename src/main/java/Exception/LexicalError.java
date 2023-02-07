package Exception;

public class LexicalError extends RuntimeException{
    public LexicalError() {
        super();
    }

    public LexicalError(String s) {
        super(s);
    }
}
