package Exception;

import org.springframework.stereotype.Component;

@Component
public class SyntaxError extends Exception{
    public SyntaxError() {
        super();
    }
    
    public SyntaxError(String s) {
        super(s);
    }
}
