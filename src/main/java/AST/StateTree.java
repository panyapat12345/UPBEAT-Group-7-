package AST;
import Exception.*;
import java.util.Map;

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

    public void doState(Map<String, Integer> bindings) {

    }

    public void setNext(State next){
        this.next = next;
    }
}
