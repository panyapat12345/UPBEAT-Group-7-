import org.junit.jupiter.api.Test;
import GameProcess.*;
import AST.*;
import Exception.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class callVariableTest {

    private HashMap<String, Double> getConfigVals(){
        HashMap<String ,Double> configVals = new HashMap<>();
        configVals.put("m", 20.0*5);
        configVals.put("n", 15.0*10);
        configVals.put("init_plan_min", 5.0);
        configVals.put("init_plan_sec", 0.0);
        configVals.put("init_budget", 10000.0);
        configVals.put("init_center_dep", 100.0);
        configVals.put("plan_rev_min", 30.0);
        configVals.put("plan_rev_sec", 0.0);
        configVals.put("rev_cost", 100.0);
        configVals.put("max_dep", 1000000.0);
        configVals.put("interest_pct", 5.0);
        return configVals;
    }

    private void printIteratorTest(Path path) {
        try {
            Tree tree = new PlanTree(path);
            Iterator<Action.FinalActionState> i = tree.iterator();

            while(i.hasNext())
                System.out.println(i.next());

        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        }
    }

    private void printIteratorTestForGameSystem(Path path) {
        try {
            Tree tree = new PlanTree(path);
            internalOperatorInterface game = new internalOperator(getConfigVals());

            Iterator<Action.FinalActionState> i = tree.iterator();

            while(i.hasNext())
                System.out.println(i.next());

        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        }
    }

    @Test
    void caseOne() throws SyntaxError {
        // printIteratorTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable1.txt"));
        // printIteratorTestForGameSystem(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable1.txt"));
        // printIteratorTestForGameSystem(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable2.txt"));
        // printIteratorTestForGameSystem(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable3.txt"));

    }

}