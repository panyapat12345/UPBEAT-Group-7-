package Exception;

import org.springframework.stereotype.Component;

@Component
public class IllegalStateTreeException extends RuntimeException{
    public IllegalStateTreeException(){
        super();
    }

    public IllegalStateTreeException(String s){
        super(s);
    }
}
