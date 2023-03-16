package AST;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface ConState extends State{
    boolean checkCon(Map<String, Integer> bindings);

    State nextState(Map<String, Integer> bindings);
}
