package Exception;

import org.springframework.stereotype.Component;

@Component
public class WonException extends Exception{
    public WonException() {
        super();
    }

    public WonException(String s) {
        super(s);
    }
}
