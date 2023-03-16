package AST;
import java.util.Map;
import Exception.*;
import org.springframework.stereotype.Component;

@Component
public class Assign implements Command{
    private Identifier left;
    private Expr right;

    public Assign(Identifier left, Expr right){
        this.left = left;
        this.right = right;
    }

    public void prettyPrint(StringBuilder s) {
        left.prettyPrint(s);
        s.append(" = ");
        right.prettyPrint(s);
    }

    public void doState(Map<String, Integer> bindings) {
        bindings.put(left.toString(), right.eval(bindings));
    }
}
