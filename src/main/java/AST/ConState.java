package AST;
import java.util.Map;

public interface ConState extends State{
    boolean checkCon(Map<String, Integer> bindings);
}
