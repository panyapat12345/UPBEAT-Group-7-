package AST;
import java.util.Map;
import Exception.*;
import org.springframework.stereotype.Component;

@Component
public interface State extends Node{

    void doState(Map<String, Integer> bindings) throws DoneExecuteException;
}
