package AST;
import Parser.*;
import Tokenizer.*;
import Exception.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
@Component
public class PlanTree implements Tree{
    private State root;
    private Map<String, Integer> bindings = new HashMap<>();;

    public PlanTree(String s) throws SyntaxError{
        init(s);
    }

    public PlanTree(Path path) throws SyntaxError{
        StringBuilder s = readFromFile(path);
        init(s.toString());
    }

    private void init(String s) throws SyntaxError{
        Parser p = new PlanParser(new PlanTokenizer(s));
        root = p.parse();
    }

    private StringBuilder readFromFile(Path path){
        if(path == null)
            return new StringBuilder();

        Charset charset = StandardCharsets.UTF_8;
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            StringBuilder s = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                s.append(line);
                s.append(" \n");
            }
            return s;
        } catch (NoSuchFileException | AccessDeniedException | FileNotFoundException e) {
            System.err.println("File not found.");
            return new StringBuilder();
        } catch (IOException e) {
            System.err.println("IOExcertion: " + e);
            return new StringBuilder();
        }
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

    public boolean changePlan(Path path){
        StringBuilder s = readFromFile(path);
        return changePlan(s.toString());
    }

    public Iterator<Action.FinalActionState> iterator() {
        return new planTreeIterator();
    }

    public Iterator<Action.FinalActionState> iteratorRealTime() {
        return new planTreeRealTimeIterator();
    }

    public Map<String, Integer> getVariables(){
        return bindings;
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
                if(current != null)
                    downCompute(current);

            } else {
                if(!stack.isEmpty()){
                    StateTree current = (StateTree) stack.pop();
                    State data = current.data();
                    if(data instanceof Action action){
                        if(current.hasNextState()){
                            current = (StateTree) current.nextState();
                            downCompute(current);
                        } else {
                            current = upCompute();
                            if(current != null){
                                downCompute(current);
                            }
                        }
                    }
                } else {
                    next = null;
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
                    current = downAndUpCompute(current);

                } else if(data instanceof If If){
                    stack.push(current);
                    current = (StateTree) If.nextState(bindings);

                } else {
                    While While = (While) data;
                    if(While.checkCon(bindings)){
                        stack.push(current);
                        current = (StateTree) While.nextState(bindings);
                    } else {
                        current = downAndUpCompute(current);
                    }
                }

                if(current == null){
                    if(stack.isEmpty())
                        return null;
                    else
                        current = upCompute();
                }

                if(current == null)
                    return null;
            }
        }

        private StateTree downAndUpCompute(StateTree current){
            if(current.hasNextState())
                return  (StateTree) current.nextState();
            else {
                return upCompute();
            }
        }

        private StateTree upCompute(){
            while(!stack.isEmpty()){
                StateTree current = (StateTree) stack.pop();
                State data = current.data();
                if(data instanceof While While){
                    if(While.checkCon(bindings)){
                        stack.push(current);
                        return (StateTree) While.nextState(bindings);
                    } else {
                        While.clearCounter();
                        if(current.hasNextState())
                            return (StateTree) current.nextState();
                    }
                } else {
                    if(current.hasNextState()){
                        return (StateTree) current.nextState();
                    }
                }
            }
            next = null;
            return null;
        }

        public boolean hasNext() {
            return next != null;
        }

        public Action.FinalActionState next() {
            if(hasNext()){
                Action.FinalActionState result = next;
                // done is last action only
                if(next.getAction().equals("done"))
                    next = null;
                else
                    computeNext();

                return result;
            }
            throw new NoSuchElementException("null next");
        }
    }

    private class planTreeRealTimeIterator implements Iterator<Action.FinalActionState> {
        private Stack<State> stack;
        private Action.FinalActionState next;
        private boolean isFrist = true;

        public planTreeRealTimeIterator() { }

        private void computeNext() {
            // first call
            if(stack == null){
                stack = new Stack<>();
                StateTree current = (StateTree) root;
                if(current != null)
                    downCompute(current);

            } else {
                if(!stack.isEmpty()){
                    StateTree current = (StateTree) stack.pop();
                    State data = current.data();
                    if(data instanceof Action action){
                        if(current.hasNextState()){
                            current = (StateTree) current.nextState();
                            downCompute(current);
                        } else {
                            current = upCompute();
                            if(current != null){
                                downCompute(current);
                            }
                        }
                    }
                } else {
                    next = null;
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
                    current = downAndUpCompute(current);

                } else if(data instanceof If If){
                    stack.push(current);
                    current = (StateTree) If.nextState(bindings);

                } else {
                    While While = (While) data;
                    if(While.checkCon(bindings)){
                        stack.push(current);
                        current = (StateTree) While.nextState(bindings);
                    } else {
                        current = downAndUpCompute(current);
                    }
                }

                if(current == null){
                    if(stack.isEmpty())
                        return null;
                    else
                        current = upCompute();
                }

                if(current == null)
                    return null;
            }
        }

        private StateTree downAndUpCompute(StateTree current){
            if(current.hasNextState())
                return  (StateTree) current.nextState();
            else {
                return upCompute();
            }
        }

        private StateTree upCompute(){
            while(!stack.isEmpty()){
                StateTree current = (StateTree) stack.pop();
                State data = current.data();
                if(data instanceof While While){
                    if(While.checkCon(bindings)){
                        stack.push(current);
                        return (StateTree) While.nextState(bindings);
                    } else {
                        While.clearCounter();
                        if(current.hasNextState())
                            return (StateTree) current.nextState();
                    }
                } else {
                    if(current.hasNextState()){
                        return (StateTree) current.nextState();
                    }
                }
            }
            next = null;
            return null;
        }

        public boolean hasNext() {
            return next != null;
        }

        public Action.FinalActionState next() {
            if (isFrist){
                isFrist = false;
                computeNext();
            } else {
                if (next != null){
                    if(next.getAction().equals("done")){
                        next = null;
                    } else {
                        computeNext();
                    }
                }
            }
            return next;
        }
    }
}
