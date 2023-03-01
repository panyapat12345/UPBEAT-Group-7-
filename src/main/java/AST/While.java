package AST;
import Exception.*;
import java.util.Map;

public class While implements ConState{
    private Expr expr;
    private State trueState;

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
        return expr.eval(bindings) > 0;
    }

    public State nextState(Map<String, Integer> bindings) {
        if(checkCon(bindings))
            return trueState;
        else
            return null;
    }
}
