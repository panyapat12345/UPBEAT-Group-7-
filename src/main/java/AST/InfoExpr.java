package AST;
import Exception.*;
import java.util.Map;

public class InfoExpr implements Expr{
    private String key;
    private String[] possibleKeys = {"opponent", "nearby"};
    private Dir dir;

    public InfoExpr(String key, Dir dir) {
        this.key = key;
        this.dir = dir;
        check();
    }

    public InfoExpr(String key) {
        this(key, null);
    }

    private void check() {
        for(String possibleKey : possibleKeys){
            if(key.equals(possibleKey))
                return;
        }
        throw new IllegalInfoExpressionException(key);
    }

    public int eval(Map<String, Integer> bindings) {
        if(key.equals("opponent")){
            // internalOperator.instance().opponent();
        } else {
            // internalOperator.instance().nearby();
        }
        return 0;
    }

    public void prettyPrint(StringBuilder s) {
        s.append("(");
        s.append(key);
        if(dir != null){
            s.append(" ");
            dir.prettyPrint(s);
        }
        s.append(")");
    }
}
