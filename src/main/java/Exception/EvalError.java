package Exception;

import org.springframework.stereotype.Component;

@Component
public class EvalError extends RuntimeException{
    public EvalError() {
        super();
    }

    public EvalError(String s) {
        super(s);
    }
}
