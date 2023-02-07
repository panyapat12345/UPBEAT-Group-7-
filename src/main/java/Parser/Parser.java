package Parser;
import Exception.*;
import AST.*;

public interface Parser {
    Node parse() throws SyntaxError;

    static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    };
}
