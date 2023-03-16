package Exception;

import org.springframework.stereotype.Component;

@Component
public class IllegalDirectionException extends RuntimeException{
    public IllegalDirectionException() {
        super();
    }

    public IllegalDirectionException(String s){
        super(s);
    }
}
