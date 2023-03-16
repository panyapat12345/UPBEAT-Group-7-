package Exception;

import org.springframework.stereotype.Component;

@Component
public class NoSuchRealTimeElementException extends Exception{
    public NoSuchRealTimeElementException() {
        super();
    }

    public NoSuchRealTimeElementException(String s) {
        super(s);
    }
}
