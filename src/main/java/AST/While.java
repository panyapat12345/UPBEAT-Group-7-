package AST;
import Exception.*;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class While implements ConState{
    private Expr expr;
    private State trueState;
    private int countLoop = 0;

    public While(Expr expr, State trueState) {
        if(expr == null)
            throw new IllegalWhileException("null Expr");
        this.expr = expr;
        this.trueState = trueState;
    }

    public void prettyPrint(StringBuilder s) {
        s.append("while( ");
        expr.prettyPrint(s);
        s.append(" ) { ");
        if(trueState != null){
            s.append("\n");
            trueState.prettyPrint(s);
            if(trueState instanceof Command)
                s.append("\n");
        }
        s.append("} \n");
    }

    public void doState(Map<String, Integer> bindings) throws DoneExecuteException{
        for(int counter = 0; counter < 10000 && checkCon(bindings); counter++){
            trueState.doState(bindings);
            // check # loops
//            System.err.println("Counter : " + (counter+1));
        }
    }

    public boolean checkCon(Map<String, Integer> bindings) {
        if(countLoop < 10000)
            return expr.eval(bindings) > 0;
        return false;
    }

    public State nextState(Map<String, Integer> bindings) {
        State result = null;
        if(countLoop < 10000){
            if(checkCon(bindings))
                result = trueState;
            countLoop++;
        }
        return result;
    }

    public void clearCounter(){
        countLoop = 0;
    }
}
