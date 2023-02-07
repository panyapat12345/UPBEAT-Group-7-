package AST;
import Exception.*;
import java.util.Map;

public class BinaryArithExpr implements Expr{
    private Expr left, right;
    private String op;

    public BinaryArithExpr(Expr left, String op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public int eval(Map<String, Integer> bindings) {
        if (bindings == null)
            throw new IllegalArgumentException("null bindings");

        int lv = left.eval(bindings);
        int rv = right.eval(bindings);
        return switch (op) {
            case "+" -> lv + rv;
            case "-" -> lv - rv;
            case "*" -> lv * rv;
            case "/" -> lv / rv;
            case "%" -> lv % rv;
            case "^" -> (int) Math.pow(lv, rv);
            default -> throw new EvalError("unknown op: " + op);
        };
    }

    public void prettyPrint(StringBuilder s) {
        s.append("(");
        left.prettyPrint(s);
        //s.append(" ");
        s.append(op);
        //s.append(" ");
        right.prettyPrint(s);
        s.append(")");
    }
}
