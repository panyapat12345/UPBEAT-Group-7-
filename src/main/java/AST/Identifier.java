package AST;
import Exception.*;
import java.util.Map;

public class Identifier implements Expr{
    private String name;
    private final String[] reservedWords = {"collect", "done", "down", "downleft", "downright", "else",
                                            "if", "invest", "move", "nearby", "opponent", "relocate",
                                            "shoot", "then", "up", "upleft", "upright", "while"};
    private final String[] specialWords = {"rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random"};

    public Identifier(String name){
        this.name = name;
        check();
    }

    private void check() {
        for(String reservedWord : reservedWords){
            if(name.equals(reservedWord))
                throw new ReservedWordException(name);
        }
    }

    public int eval(Map<String, Integer> bindings) {
        if (bindings == null)
            throw new IllegalArgumentException("null bindings");

        // specialWords read from GameSystem
        if(name.equals("rows")){
            // return GameSystem.instance().rows;
        } else if(name.equals("cols")){
            // return GameSystem.instance().cols();
        } else if(name.equals("currow")){
            // return GameSystem.instance().currow();
        } else if(name.equals("curcol")){
            // return GameSystem.instance().curcol();
        } else if(name.equals("budget")){
            // return GameSystem.instance().budget();
        } else if(name.equals("deposit")){
            // return GameSystem.instance().deposit();
        } else if(name.equals("int")){
            // return GameSystem.instance().interest();
        } else if(name.equals("maxdeposit")){
            // return GameSystem.instance().maxDeposit();
        } else {
            // return GameSystem.instance().random();
        }

        if(bindings.containsKey(name))
            return bindings.get(name);
        else {
            bindings.put(name, 0);
            return 0;
        }
        // throw new EvalError("undefined variable: " + name);
    }

    public void prettyPrint(StringBuilder s) {
        s.append(name);
    }

    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof String s){
            return name.equals(s);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
