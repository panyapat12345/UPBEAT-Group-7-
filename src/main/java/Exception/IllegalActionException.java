package Exception;

import org.springframework.stereotype.Component;

@Component
public class IllegalActionException extends RuntimeException {
    public IllegalActionException() {
        super();
    }

    public IllegalActionException(String s){
        super(s);
    }
}
