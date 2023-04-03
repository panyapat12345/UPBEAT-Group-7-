package Tokenizer;
import Exception.*;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
@Component
public class PlanTokenizer implements Tokenizer{
    private String src, next, prevNext;
    private int pos, prevPos;
    private final char[] oneLengthTokens = {'+', '-', '*', '/', '%', '(', ')', '^', '=', '{', '}'};

    public PlanTokenizer(String src) {
        this.src = src;
        pos = 0;
        prevPos = 0;
        computeNext(true);
    }

    private boolean isSpace(char c) {
        return (c == ' ')? true : false;
    }

    private boolean oneLengthToken(char c) {
        for(char oneLengthToken : oneLengthTokens){
            if(c == oneLengthToken)
                return true;
        }
        return false;
    }

    private void computeNext(boolean isFirstCall) {
        if(isFirstCall){
            prevPos = pos;
            prevNext = next;
        }

        StringBuilder s = new StringBuilder();
        while(pos < src.length() && isSpace(src.charAt(pos)))
            pos++;
        if(pos == src.length()){
            next = null;
            return;
        }
        char c = src.charAt(pos);
        // number case
        if(Character.isDigit(c)){
            s.append(c);
            for(pos++; pos < src.length() && Character.isDigit(src.charAt(pos)); pos++){
                s.append(src.charAt(pos));
            }
        // symbol case
        } else if(oneLengthToken(c)) {
            s.append(c);
            pos++;
        // variable case : letter first then other
        } else if(Character.isLetter(c)){
            s.append(c);
            for(pos++; pos < src.length() && (Character.isLetter(src.charAt(pos)) || Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '_'); pos++) {
                s.append(src.charAt(pos));
            }
        // comment case : ignore path of line from # to \n
        } else if(c == '#') {
            for(pos++; pos < src.length() && src.charAt(pos) != '\n'; pos++) { }
            if(pos == src.length()){
                next = null;
            } else {
                pos++;
                computeNext(false);
            }
            return;
        } else if(c == '\n') {
            pos++;
            computeNext(false);
            return;
        } else throw new LexicalError("unknown character: " + c);
        next = s.toString();
    }

    public boolean hasNextToken() {
        return (next != null || pos != src.length());
    }

    public String peek() {
        if(!hasNextToken())
            throw new NoSuchElementException("no more tokens");
        return next;
    }
    public boolean peek(String s) {
        if(!hasNextToken())
            return false;
        return peek().equals(s);
    }

    public String consume() {
        if(!hasNextToken())
            throw new NoSuchElementException("no more tokens");
        else {
            String result = next;
            computeNext(true);
            return result;
        }
    }

    public void consume(String s) throws SyntaxError{
        if(peek(s))
            consume();
        else throw new SyntaxError(s + " expected");
    }

    public void back() {
        pos = prevPos;
        next = prevNext;
    }
}
