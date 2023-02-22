package AST;
import java.util.Map;
import Exception.*;

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

    public void doState(Map<String, Integer> bindings) throws DoneExecuteException {
        bindings.put(left.toString(), right.eval(bindings));
    }
}
