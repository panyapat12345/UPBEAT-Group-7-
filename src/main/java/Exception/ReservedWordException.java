package Exception;

import org.springframework.stereotype.Component;

@Component
public class ReservedWordException extends RuntimeException{
    public ReservedWordException() {
        super();
    }

    public ReservedWordException(String s) {
        super(s);
    }
}
