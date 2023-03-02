package AST;

import java.nio.file.Path;
import java.util.Iterator;

public interface Tree extends Iterable<Action.FinalActionState>{
    boolean changePlan(String s);

    boolean changePlan(Path path);

    @Override
    Iterator<Action.FinalActionState> iterator();
}
