package AST;
import Parser.*;
import Tokenizer.*;
import Exception.*;

import java.util.*;

public class PlanTree implements Tree{
    private State root;
    private Map<String, Integer> bindings = new HashMap<>();;

    public PlanTree(String s) throws SyntaxError{
        Parser p = new PlanParser(new PlanTokenizer(s));
        root = p.parse();
    }

    public boolean changePlan(String s){
        try{
            Parser p = new PlanParser(new PlanTokenizer(s));
            root = p.parse();
            return true;
        } catch(SyntaxError e){
            return false;
        }
    }

    public Iterator<Action.FinalActionState> iterator() {
        return new planTreeIterator();
    }

    private class planTreeIterator implements Iterator<Action.FinalActionState> {
        private Stack<State> stack;
        private Action.FinalActionState next;

        public planTreeIterator() {
            computeNext();
        }

        private void computeNext() {
            // first call
            if(stack == null){
                stack = new Stack<>();
                StateTree current = (StateTree) root;
                downCompute(current);

            } else {
                StateTree current = (StateTree) stack.pop();
                State data = current.data();
                if(data instanceof Action action){
                    if(current.hasNextState()){
                        current = (StateTree) current.nextState();
                        downCompute(current);
                    } else {
                        current = upCompute(current);
                        if(current != null){
                            downCompute(current);
                        }
                    }
                }
            }
        }

        private StateTree downCompute(StateTree current){
            while(true){
                State data = current.data();
                if(data instanceof Action action){
                    stack.push(current);
                    next = ((Action) data).getFinalAction(bindings);
                    return current;

                } else if(data instanceof Assign assign){
                    assign.doState(bindings);
                    current = upCompute(current);

                } else if(data instanceof If If){
                    current = (StateTree) If.nextState(bindings);

                } else {
                    While While = (While) data;
                    if(While.checkCon(bindings)){
                        stack.push(current);
                        current = (StateTree) While.nextState(bindings);
                    } else {
                        current = upCompute(current);
                    }
                }

                if(current == null)
                    return null;
            }
        }

        private StateTree upCompute(StateTree current){
            if(current.hasNextState())
                return  (StateTree) current.nextState();
            else {
                while(!stack.isEmpty()){
                    current = (StateTree) stack.pop();
                    While While = (While) current.data();
                    if(While.checkCon(bindings)){
                        stack.push(current);
                        return (StateTree) While.nextState(bindings);
                    } else {
                        While.clearCounter();
                    }
                }
                next = null;
                return null;
            }
        }

        public boolean hasNext() {
            return next != null;
        }

        public Action.FinalActionState next() {
            if(hasNext()){
                Action.FinalActionState result = next;
                computeNext();
                return result;
            }
            throw new NoSuchElementException("null next");
        }
    }
}
