package AST;

import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class Number implements Expr{
    private int val;

    public Number(int val) {
        this.val = val;
    }

    public int eval(Map<String, Integer> bindings) {
        if (bindings == null)
            throw new IllegalArgumentException("null bindings");
        return val;
    }

    public void prettyPrint(StringBuilder s) {
        s.append(val);
    }
}
