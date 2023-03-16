package Tokenizer;
import Exception.*;
import org.springframework.stereotype.Component;

@Component
public interface Tokenizer {
    boolean hasNextToken();
    String peek();
    boolean peek(String s);
    String consume();
    void consume(String s) throws SyntaxError;
    void back();
}
