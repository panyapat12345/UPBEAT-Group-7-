package Exception;

import org.springframework.stereotype.Component;

@Component
public class DoneExecuteException extends Exception{
    public DoneExecuteException() {
        super();
    }

    public DoneExecuteException(String s) {
        super(s);
    }
}
