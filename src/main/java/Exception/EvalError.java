package Exception;

public class EvalError extends RuntimeException{
    public EvalError() {
        super();
    }

    public EvalError(String s) {
        super(s);
    }
}
