package AST;

import java.util.Iterator;

public interface Tree extends Iterable<Action.FinalActionState>{
    boolean changePlan(String s);

    @Override
    Iterator<Action.FinalActionState> iterator();
}
