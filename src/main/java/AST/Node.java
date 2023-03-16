package AST;

import org.springframework.stereotype.Component;

@Component
public interface Node {
    void prettyPrint(StringBuilder s);
}
