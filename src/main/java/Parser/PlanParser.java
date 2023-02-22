package Parser;
import AST.Number;
import Exception.*;
import Tokenizer.*;
import AST.*;
import java.util.NoSuchElementException;

public class PlanParser implements Parser{
    private Tokenizer tkz;
    private final String[] possibleActionKeys = {"done", "relocate", "move", "invest", "collect", "shoot"};
    private final String[] specialWords = {"Rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random"};
//    private final String[] ArithOp = {'+', '-', '*', '/', '%', '(', ')', '^'};

    public PlanParser(Tokenizer tkz) {
        if(tkz == null)
            throw new IllegalArgumentException("null tokenizer");
        this.tkz = tkz;
    }

    public State parse() throws SyntaxError {
        State v;
        try {
            v = parsePlan();
        } catch (NoSuchElementException e) {
            throw new SyntaxError(e.getMessage());
        }
        if(tkz.hasNextToken()){
            // debug
            StringBuilder s = new StringBuilder();
            while (tkz.hasNextToken()) {
                s.append(tkz.consume());
                s.append(" ");
            }
            System.out.println(s.toString());

            throw new SyntaxError("leftover token");
        }
        return v;
    }

    private State parsePlan() throws SyntaxError {
        StateTree plan = null, current = null;
        try {
            while(tkz.hasNextToken()){
                State parseStateResult = (State) parseState();
                if(parseStateResult != null) {
                    if(plan == null) {
                        plan = new StateTree(parseStateResult, null);
                        current = plan;
                    } else {
                        StateTree next = new StateTree(parseStateResult, null);
                        current.setNext(next);
                        current = next;
                    }
                }
            }
            return plan;
        } catch (IllegalStateTreeException e) {
            throw new SyntaxError("Illegal StateTree : " + e.getMessage());
        }
    }

    private Node parseState() throws SyntaxError {
        if(tkz.peek("{"))
            return parseBlock();
        else if(tkz.peek("if"))
            return parseIf();
        else if(tkz.peek("while"))
            return parseWhile();
        else
            return parseCom();
    }

    private Node parseCom() throws SyntaxError {
        for(String possibleActionKey : possibleActionKeys){
            if(tkz.peek(possibleActionKey))
                return parseAction();
        }
        return parseAssign();
    }

    private Node parseAssign() throws SyntaxError {
        try {
            for(String specialWord : specialWords){
                if(tkz.peek(specialWord)){
                    tkz.consume();
                    if(tkz.peek("=")) {
                        // ignore assignment
                        tkz.consume();
                        parseE();
                        return null;
                    } else {
                        tkz.back();
                        return parseE();
                    }
                }
            }

            String identifier = tkz.consume();
            if(tkz.peek("=")){
                tkz.consume();
                return new Assign(new Identifier(identifier), parseE());
            } else {
                tkz.back();
                return parseE();
            }
        } catch (ReservedWordException e) {
            throw new SyntaxError("Reserved Word : " + e.getMessage());
        }
    }

    private Action parseAction() throws SyntaxError {
        try {
            if(tkz.peek("done") || tkz.peek("relocate")) {
                return new Action(null, tkz.consume(), null);
            } else if(tkz.peek("move")) {
                return parseMove();
            } else if(tkz.peek("invest") || tkz.peek("collect")) {
                return parseRegion();
            } else
                return parseAttack();
        } catch (IllegalActionException e) {
            throw new SyntaxError("Illegal Action : " + e.getMessage());
        }
    }

    private Action parseMove() throws SyntaxError {
        String move = tkz.consume();
        return new Action(parseDirect(), move, null);
    }

    private Action parseRegion() throws SyntaxError {
        return new Action(null, tkz.consume(), parseE());
    }

    private Action parseAttack() throws SyntaxError {
        String shoot = tkz.consume();
        return new Action(parseDirect(), shoot, parseE());
    }

    private Dir parseDirect() throws SyntaxError {
        try {
            return new Dir(tkz.consume());
        } catch (IllegalDirectionException e) {
            throw new SyntaxError("Illegal Direction : " + e.getMessage());
        }
    }

    private Node parseBlock() throws SyntaxError {
        StateTree s = null, current = null;
        tkz.consume("{");
        while(!tkz.peek("}")){
            if(s == null){
                s = new StateTree((State) parseState(), null);
                current = s;
            } else {
                StateTree next = new StateTree((State) parseState(), null);
                current.setNext(next);
                current = next;
            }
        }
        tkz.consume("}");
        return s;
    }

    private Node parseIf() throws SyntaxError {
        try {
            tkz.consume("if");
            tkz.consume("(");
            Expr expr = parseE();
            tkz.consume(")");
            tkz.consume("then");
            State trueState = (State) parseState();
            tkz.consume("else");
            State falseState = (State) parseState();
            return new If(expr, trueState, falseState);
        } catch (IllegalIfException e) {
            throw new SyntaxError("Illegal If : " + e.getMessage());
        }
    }

    private Node parseWhile() throws SyntaxError {
        try {
            tkz.consume("while");
            tkz.consume("(");
            Expr expr = parseE();
            tkz.consume(")");
            State trueState = (State) parseState();
            return new While(expr, trueState);
        } catch(IllegalWhileException e) {
            throw new SyntaxError("Illegal While : " + e.getMessage());
        }
    }

    // E → E + T | E - T | T
    // E → T ((+|-)T)* (Bottom up)
    private Expr parseE() throws SyntaxError{
        Expr v =  parseT();
        while(tkz.peek("+") || tkz.peek("-")) {
            if(tkz.peek("+")) {
                tkz.consume();
                v = new BinaryArithExpr(v, "+", parseT());
            } else if(tkz.peek("-")) {
                tkz.consume();
                v = new BinaryArithExpr(v, "-", parseT());
            }
        }
        return v;
    }

    // T → T * F | T / F | T % F | F
    // T → F ((*|/|%)F)* (Bottom up)
    private Expr parseT() throws SyntaxError {
        Expr v = parseF();
        while(tkz.peek("*") || tkz.peek("/") || tkz.peek("%")) {
            if (tkz.peek("*")) {
                tkz.consume();
                v = new BinaryArithExpr(v, "*", parseF());
            } else if(tkz.peek("/")) {
                tkz.consume();
                v = new BinaryArithExpr(v, "/", parseF());
            } else if(tkz.peek("%")) {
                tkz.consume();
                v = new BinaryArithExpr(v, "%", parseF());
            }
        }
        return v;
    }

    // F → P ^ F | P
    private Expr parseF() throws SyntaxError {
        Expr v = parseP();
        if(tkz.peek("^")){
            tkz.consume();
            v = new BinaryArithExpr(v, "^", parseF());
        }
        return v;
    }

    // P → n | x | ( E )
    private Expr parseP() throws SyntaxError {
        // number
        if(Parser.isNumber(tkz.peek())){
            return new Number(Integer.parseInt(tkz.consume()));
        // identifier (variable)
        } else if(Character.isLetter(tkz.peek().charAt(0)) && !tkz.peek("nearby") && !tkz.peek("opponent")) {
            return new Identifier(tkz.consume());
        // ( s )
        } else if(tkz.peek("(")) {
            tkz.consume("(");
            Expr v = parseE();
            tkz.consume(")");
            return v;
        } else {
            return parseInfo();
        }
    }

    private Expr parseInfo() throws SyntaxError {
        try {
            if(tkz.peek("opponent")){
                return new InfoExpr(tkz.consume());
            } else {
                return new InfoExpr(tkz.consume(), parseDirect());
            }
        } catch (IllegalInfoExpressionException e) {
            throw new SyntaxError("IllegalInfo Expression : " + e.getMessage());
        }
    }
}
