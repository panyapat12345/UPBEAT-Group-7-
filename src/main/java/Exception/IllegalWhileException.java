package Exception;

import org.springframework.stereotype.Component;

@Component
public class IllegalWhileException extends RuntimeException{
    public IllegalWhileException() {
        super();
    }

    public IllegalWhileException(String s){
        super(s);
    }
}
