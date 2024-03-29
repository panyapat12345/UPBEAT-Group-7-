package AST;
import Exception.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
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
            // simulate
            bindings.put("Budget", bindings.get("Budget")-1);

            System.out.println(key + " " + left.toString());
            // gameSystem.instance().move(left.toString());
        } else if(key.equals("invest")){
            int val = right.eval(bindings);
            // simulate
            bindings.put("Budget", bindings.get("Budget")-val-1);
            bindings.put("Deposit", bindings.get("Deposit")+val);

            System.out.println(key + " " + val);
            // gameSystem.instance().invest(right.eval(bindings));
        } else if(key.equals("collect")){
            int val = right.eval(bindings);
            // simulate
            bindings.put("Budget", bindings.get("Budget")+val-1);
            bindings.put("Deposit", bindings.get("Deposit")-val);

            System.out.println(key + " " + val);
            // gameSystem.instance().collect(right.eval(bindings));
        } else if(key.equals("shoot")){
            int val = right.eval((bindings));
            // simulate
            bindings.put("Budget", bindings.get("Budget")-val-1);

            System.out.println(key + " " + left.toString() + " " + val);
            // gameSystem.instance().shoot(left.toString(), right.eval(bindings));
        }
    }

    public FinalActionState getFinalAction(Map<String, Integer> bindings){
        if(left != null && right != null)
            return new FinalActionState(key, left.toString(), right.eval(bindings));
        else if(left != null && right == null)
            return new FinalActionState(key, left.toString(), -999);
        else if(left == null && right != null) {
            // System.out.println(right.eval(bindings));
            return new FinalActionState(key, "", right.eval(bindings));
        }
        else
            return new FinalActionState(key, "", -999);
    }

    public class FinalActionState {
        private final String key, dir;
        private final int val;

        public FinalActionState(String key, String dir, int val){
            this.key = key;
            this.dir = dir;
            this.val = val;
        }

        public String getAction(){
            return key;
        }

        public String getDirection(){
            return dir;
        }

        public int getValue(){
            return val;
        }

        @Override
        public String toString() {
            if(key.equals("done")){
                return key;
            } else  if(key.equals("relocate")){
                return key;
            } else  if(key.equals("move")){
                return key + " " + dir;
            } else  if(key.equals("invest")){
                return key + " " + val;
            } else  if(key.equals("collect")){
                return key + " " + val;
            } else {   // "shoot"
                return key + " " + dir + " " + val;
            }
        }
    }
}
