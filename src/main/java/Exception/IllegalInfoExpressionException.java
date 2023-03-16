package Exception;

import org.springframework.stereotype.Component;

@Component
public class IllegalInfoExpressionException extends RuntimeException{
    public IllegalInfoExpressionException() {
        super();
    }

    public IllegalInfoExpressionException(String s) {
        super(s);
    }
}
