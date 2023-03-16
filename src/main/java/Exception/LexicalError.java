package Exception;

import org.springframework.stereotype.Component;

@Component
public class LexicalError extends RuntimeException{
    public LexicalError() {
        super();
    }

    public LexicalError(String s) {
        super(s);
    }
}
