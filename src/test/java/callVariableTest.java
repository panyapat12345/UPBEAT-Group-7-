import org.junit.jupiter.api.Test;
import GameProcess.*;
import AST.*;
import Exception.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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
            internalOperator game = new internalOperator(getConfigVals());

            Iterator<Action.FinalActionState> i = tree.iterator();

            while(i.hasNext())
                System.out.println(i.next());

        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        }
    }

    private void GameSystemTest(Path path){
        internalOperator game = new internalOperator(getConfigVals());
        game.addPlayer(readFromFile(path).toString());
        game.NextTurn();
    }

    private StringBuilder readFromFile(Path path){
        if(path == null)
            return new StringBuilder();

        Charset charset = StandardCharsets.UTF_8;
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            StringBuilder s = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                s.append(line);
                s.append(" \n");
            }
            return s;
        } catch (NoSuchFileException | AccessDeniedException | FileNotFoundException e) {
            System.err.println("File not found.");
            return new StringBuilder();
        } catch (IOException e) {
            System.err.println("IOExcertion: " + e);
            return new StringBuilder();
        }
    }

    @Test
    void planIfTest() {
        printIteratorTest(Paths.get("src/test/AllPlanTestFiles/IfTestFiles/ifPlan0.txt"));
//         printIteratorTest(Paths.get("src/test/AllPlanTestFiles/IfTestFiles/ifPlan1.txt"));
//         printIteratorTest(Paths.get("src/test/AllPlanTestFiles/IfTestFiles/ifPlan2.txt"));
//         printIteratorTest(Paths.get("src/test/AllPlanTestFiles/IfTestFiles/ifPlan3.txt"));
//         printIteratorTest(Paths.get("src/test/AllPlanTestFiles/IfTestFiles/ifPlan4.txt"));
//         printIteratorTest(Paths.get("src/test/AllPlanTestFiles/IfTestFiles/ifPlan5.txt"));
//         printIteratorTest(Paths.get("src/test/AllPlanTestFiles/IfTestFiles/ifPlan6.txt"));
    }

    @Test
    void caseOne() throws SyntaxError {
        // printIteratorTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable1.txt"));

        // printIteratorTestForGameSystem(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable1.txt"));
        // printIteratorTestForGameSystem(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable2.txt"));
//         printIteratorTestForGameSystem(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable3.txt"));
         printIteratorTestForGameSystem(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable4.txt"));
    }

    @Test
    void caseOneRunGame() {
        GameSystemTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/runningPlans/plan1.txt"));
    }


}