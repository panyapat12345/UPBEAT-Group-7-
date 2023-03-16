package AST;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface Expr extends Node{
    int eval(Map<String, Integer> bindings);
}
