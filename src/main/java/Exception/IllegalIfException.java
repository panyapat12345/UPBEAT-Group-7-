package Exception;

public class IllegalIfException extends RuntimeException{
    public IllegalIfException() {
        super();
    }

    public IllegalIfException(String s){
        super(s);
    }
}
