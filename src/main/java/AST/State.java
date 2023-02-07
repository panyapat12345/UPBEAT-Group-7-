package AST;
import java.util.Map;

public interface State extends Node{

    void doState(Map<String, Integer> bindings);
}
