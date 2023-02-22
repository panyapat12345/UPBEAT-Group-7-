package AST;
import java.util.Map;
import Exception.*;

public interface State extends Node{

    void doState(Map<String, Integer> bindings) throws DoneExecuteException;
}
