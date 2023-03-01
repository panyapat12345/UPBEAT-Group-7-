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
            stack = new Stack<>();
            computeNext();
        }

        private void computeNext() {

        }

        public boolean hasNext() {
            return next != null;
        }

        public Action.FinalActionState next() {
            if(hasNext())
                return next;
            throw new NoSuchElementException("null next");
        }
    }
}
