package Exception;

import org.springframework.stereotype.Component;

@Component
public class IllegalIfException extends RuntimeException{
    public IllegalIfException() {
        super();
    }

    public IllegalIfException(String s){
        super(s);
    }
}
