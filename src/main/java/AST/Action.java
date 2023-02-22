package AST;
import Exception.*;
import java.util.Map;

public class Action implements Command{
    private String key;
    private String[] possibleKeys = {"done", "relocate", "move", "invest", "collect", "shoot"};
    private Dir left;
    private Expr right;

    public Action(Dir left, String key, Expr right) {
        this.left = left;
        this.key = key;
        this.right = right;
        check();
    }

    private void check() {
        for(String possibleKey : possibleKeys){
            if(key.equals(possibleKey))
                return;
        }
        throw new IllegalActionException(key);
    }

    public void prettyPrint(StringBuilder s) {
        if(key.equals("done")){
            s.append(key);
        } else if(key.equals("relocate")){
            s.append(key);
        } else if(key.equals("move")){
            s.append(key);
            s.append(" ");
            left.prettyPrint(s);
        } else if(key.equals("invest")){
            s.append(key);
            s.append(" ");
            right.prettyPrint(s);
        } else if(key.equals("collect")){
            s.append(key);
            s.append(" ");
            right.prettyPrint(s);
        } else if(key.equals("shoot")){
            s.append(key);
            s.append(" ");
            left.prettyPrint(s);
            s.append(" ");
            right.prettyPrint(s);
        }
    }

    public void doState(Map<String, Integer> bindings) throws DoneExecuteException{
        if(key.equals("done")){
            System.out.println(key);
            // gameSystem.instance().done();
            throw new DoneExecuteException("Done");
        } else if(key.equals("relocate")){
            System.out.println(key);
            // gameSystem.instance().relocate();
        } else if(key.equals("move")){
            System.out.println(key + " " + left.toString());
            // gameSystem.instance().move(left.toString());
        } else if(key.equals("invest")){
            System.out.println(key + " " + right.eval(bindings));
            // gameSystem.instance().invest(right.eval(bindings));
        } else if(key.equals("collect")){
            System.out.println(key + " " + right.eval(bindings));
            // gameSystem.instance().collect(right.eval(bindings));
        } else if(key.equals("shoot")){
            System.out.println(key + " " + left.toString() + " " + right.eval((bindings)));
            // gameSystem.instance().shoot(left.toString(), right.eval(bindings));
        }
    }
}
