package AST;
import Exception.*;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class If implements ConState{
    private Expr expr;
    private State trueState, falseState;

    public If(Expr expr, State trueState, State falseState) {
        if(expr == null)
            throw new IllegalIfException("null Expr");
        this.expr = expr;
        this.trueState = trueState;
        this.falseState = falseState;
    }

    public void prettyPrint(StringBuilder s) {
        s.append("if( ");
        expr.prettyPrint(s);
        s.append(" ) then { ");
        if(trueState != null){
            s.append("\n");
            trueState.prettyPrint(s);
            if(trueState instanceof Command)
                s.append("\n");
        }
        s.append("} else { ");
        if(falseState != null){
            s.append("\n");
            falseState.prettyPrint(s);
            if(falseState instanceof Command)
                s.append("\n");
        }
        s.append("} \n");
    }

    public void doState(Map<String, Integer> bindings) throws DoneExecuteException{
        if(checkCon(bindings)){
            if (trueState != null)
                trueState.doState(bindings);
        } else {
            if(falseState != null)
                falseState.doState(bindings);
        }
    }

    public boolean checkCon(Map<String, Integer> bindings) {
        return expr.eval(bindings) > 0;
    }

    public State nextState(Map<String, Integer> bindings) {
        if(checkCon(bindings))
            return trueState;
        else
            return falseState;
    }
}
