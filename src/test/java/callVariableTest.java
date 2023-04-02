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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class callVariableTest {
    private GameSystem game;

    private HashMap<String, Double> getConfigVals(){
        HashMap<String ,Double> configVals = new HashMap<>();
        configVals.put("m", 20.0*5);
        configVals.put("n", 15.0*10);
        configVals.put("init_plan_min", 5.0);
        configVals.put("init_plan_sec", 0.0);
        configVals.put("init_budget", 10000.0);
        configVals.put("init_center_dep", 500.0);
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

    private void GameSystemTest(Path path) throws WonException {
        game = new GameSystem(getConfigVals());
        game.addPlayer(readFromFile(path).toString());
        game.addPlayer(readFromFile(path).toString());
        game.nextTurn();

        while (true){
            try{
                game.nextAction();
            } catch (DoneExecuteException e){
//                System.out.println(game.getCurrentPlayer());
                return;
            }
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
    void caseOne() throws WonException {
        // printIteratorTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable1.txt"));

//         GameSystemTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable1.txt"));
//         GameSystemTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable2.txt"));
//         GameSystemTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable3.txt"));
//        GameSystemTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable4.txt"));
//        GameSystemTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable5.txt"));
//        GameSystemTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable6.txt"));
//        GameSystemTest(Paths.get("src/test/AllPlanTestFiles/callVariableTestFiles/variable7.txt"));
        System.out.println(GameSystem.isCorrectSyntax(readFromFile(Paths.get("src/ConstructionPlan.txt")).toString()));
    }

    @Test
    void caseClock(){
        Clock clock = new Clock(1,0,1,0);
        System.out.println("Init_plan_sec : " + clock.getInit_plan_sec());
        System.out.println("Plan_rev_sec : " + clock.getPlan_rev_sec());
        clock.setPlan_rev_sec(555);
        System.out.println("Plan_rev_sec : " + clock.getPlan_rev_sec());

    }

}