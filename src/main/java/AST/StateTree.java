package AST;
import Exception.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NoSuchElementException;
@Component
public class StateTree implements State{
    private  State data, next;

    public StateTree(State data, State next) {
        if(data == null)
            throw new IllegalStateTreeException("null data");
        this.data = data;
        this.next = next;
    }

    public void prettyPrint(StringBuilder s) {
        data.prettyPrint(s);
        if(!(data instanceof ConState))
            s.append("\n");
        if(next != null)
            next.prettyPrint(s);
    }

    public void doState(Map<String, Integer> bindings) throws DoneExecuteException{
        data.doState(bindings);
        if(next != null)
            next.doState(bindings);
    }

    public void setNext(State next){
        this.next = next;
    }

    public boolean hasNextState(){
        return next != null;
    }

    public State nextState(){
        if(hasNextState())
            return next;
        throw new NoSuchElementException("null next state");
    }

    public Action.FinalActionState doData(Map<String, Integer> bindings){
        if (data instanceof Action action){
            return action.getFinalAction(bindings);
        } else if (data instanceof Assign assign){
            assign.doState(bindings);
        }
        return null;
    }

    public State data() {
        return data;
    }
}
