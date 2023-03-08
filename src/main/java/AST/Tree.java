package AST;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

public interface Tree extends Iterable<Action.FinalActionState>{
    boolean changePlan(String s);

    boolean changePlan(Path path);

    Map<String, Integer> getVariables();

    @Override
    Iterator<Action.FinalActionState> iterator();
}
