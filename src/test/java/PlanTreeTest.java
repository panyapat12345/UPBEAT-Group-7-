import Exception.*;
import AST.*;
import Parser.Parser;
import Tokenizer.PlanTokenizer;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlanTreeTest {
    void printIteratorTest(String s) {
        try {
            Tree tree = new PlanTree(s);

            Iterator<Action.FinalActionState> i = tree.iterator();

            while(i.hasNext())
                System.out.println(i.next());

/*             */

        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        }
    }

    private void readFileTest (String readPath, String writePath) {
        Path readFile = Paths.get(readPath), writeFile = Paths.get(writePath);
        Charset charset = StandardCharsets.UTF_8;

        try (BufferedReader reader = Files.newBufferedReader(readFile, charset);
             BufferedWriter writer = Files.newBufferedWriter(writeFile, charset)) {
            StringBuilder parseString = new StringBuilder();
            StringBuilder result = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                parseString.append(line);
                parseString.append(" ");
            }
            System.err.println(parseString.length());
            //System.out.println(parseString.toString());
            printIteratorTest(parseString.toString());

            // writer.write(result.toString());
        } catch (NoSuchFileException | AccessDeniedException | FileNotFoundException e) {
            System.err.println("File not found.");
        } catch (IOException e) {
            System.err.println("IOExcertion: " + e);
        }
    }

    @Test
    void caseOne() {
        printIteratorTest("budget=10000");
        printIteratorTest("m=20");
        printIteratorTest("n=15");
        printIteratorTest("init_plan_min=5");
        printIteratorTest("init_plan_sec=0");
        printIteratorTest("init_budget=10000");
        printIteratorTest("init_center_dep=100");
        printIteratorTest("plan_rev_min=30");
        printIteratorTest("plan_rev_sec=0");
        printIteratorTest("rev_cost=100");
        printIteratorTest("max_dep=1000000");
        printIteratorTest("interest_pct=5");
    }

    @Test
    void caseTwo() {
        printIteratorTest("t = t + 1");
        printIteratorTest("m = 0");
        printIteratorTest("collect (deposit / 4)");
        printIteratorTest("invest 25");
        printIteratorTest("done");
        printIteratorTest("opponentLoc = opponent");
        printIteratorTest("move downleft");
        printIteratorTest("move down");
        printIteratorTest("move downright");
        printIteratorTest("move upright");
        printIteratorTest("move up");
        printIteratorTest("cost = 10 ^ (nearby upleft % 100 + 1)");
        printIteratorTest("shoot upleft cost");
        printIteratorTest("cost = 10 ^ (nearby downleft % 100 + 1)");
        printIteratorTest("shoot downleft cost");
        printIteratorTest("cost = 10 ^ (nearby down % 100 + 1)");
        printIteratorTest("shoot down cost");
        printIteratorTest("cost = 10 ^ (nearby downright % 100 + 1)");
        printIteratorTest("shoot downright cost");
        printIteratorTest("cost = 10 ^ (nearby upright % 100 + 1)");
        printIteratorTest("shoot upright cost");
        printIteratorTest("cost = 10 ^ (nearby up % 100 + 1)");
        printIteratorTest("shoot up cost");
        printIteratorTest("dir = random % 6");
        printIteratorTest("move upleft");
        printIteratorTest("move downleft");
        printIteratorTest("move down");
        printIteratorTest("move upright");
        printIteratorTest("move up");
        printIteratorTest("m = m + 1");
        printIteratorTest("invest 1");
    }

    @Test
    void configurationFileWithOutCommentTest() {
        StringBuilder s = new StringBuilder();
        s.append("m=20 ");
        s.append("n=15 ");
        s.append("init_plan_min=5 ");
        s.append("init_plan_sec=0 ");
        s.append("init_budget=10000 ");
        s.append("init_center_dep=100 ");
        s.append("plan_rev_min=30 ");
        s.append("plan_rev_sec=0 ");
        s.append("rev_cost=100 ");
        s.append("max_dep=1000000 ");
        s.append("interest_pct=5 ");
        printIteratorTest(s.toString());
    }

    @Test
    void subExamplePlanTest() {
        StringBuilder s = new StringBuilder();
        s.append("t = t + 1 ");
        s.append("m = 0 ");
        s.append("collect (deposit / 4) ");
        s.append("invest 25 ");
        s.append("done ");
        s.append("opponentLoc = opponent ");
        s.append("move downleft ");
        s.append("move down ");
        s.append("move downright ");
        s.append("move upright ");
        s.append("move up ");
        s.append("cost = 10 ^ (nearby upleft % 100 + 1) ");
        s.append("shoot upleft cost ");
        s.append("cost = 10 ^ (nearby downleft % 100 + 1) ");
        s.append("shoot downleft cost ");
        s.append("cost = 10 ^ (nearby down % 100 + 1) ");
        s.append("shoot down cost ");
        s.append("cost = 10 ^ (nearby downright % 100 + 1) ");
        s.append("shoot downright cost ");
        s.append("cost = 10 ^ (nearby upright % 100 + 1) ");
        s.append("shoot upright cost ");
        s.append("cost = 10 ^ (nearby up % 100 + 1) ");
        s.append("shoot up cost ");
        s.append("dir = random % 6 ");
        s.append("move upleft ");
        s.append("move downleft ");
        s.append("move down ");
        s.append("move upright ");
        s.append("move up ");
        s.append("m = m + 1 ");
        s.append("invest 1 ");
        printIteratorTest(s.toString());
    }

    @Test
    void planIfReadTest() {
//         readFileTest("src/test/AllPlanTestFiles/IfTestFiles/ifPlan0.txt", "src/test/AllPlanTestFiles/IfTestFiles/ifParseTestResult0.txt");
//         readFileTest("src/test/AllPlanTestFiles/IfTestFiles/ifPlan1.txt", "src/test/AllPlanTestFiles/IfTestFiles/ifParseTestResult0.txt");
//         readFileTest("src/test/AllPlanTestFiles/IfTestFiles/ifPlan2.txt", "src/test/AllPlanTestFiles/IfTestFiles/ifParseTestResult2.txt");
//         readFileTest("src/test/AllPlanTestFiles/IfTestFiles/ifPlan3.txt", "src/test/AllPlanTestFiles/IfTestFiles/ifParseTestResult3.txt");
//         readFileTest("src/test/AllPlanTestFiles/IfTestFiles/ifPlan4.txt", "src/test/AllPlanTestFiles/IfTestFiles/ifParseTestResult4.txt");
//         readFileTest("src/test/AllPlanTestFiles/IfTestFiles/ifPlan5.txt", "src/test/AllPlanTestFiles/IfTestFiles/ifParseTestResult5.txt");
//         readFileTest("src/test/AllPlanTestFiles/IfTestFiles/ifPlan6.txt", "src/test/AllPlanTestFiles/IfTestFiles/ifParseTestResult6.txt");
    }

    @Test
    void planWhileReadTest() {
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles/whilePlan1.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult1.txt");
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whilePlan2.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult2.txt");
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whilePlan3.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult3.txt");
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whilePlan4.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult4.txt");
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whilePlan5.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult5.txt");
//
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whilePlan6.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult6.txt");
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whilePlan7.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult7.txt");
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whilePlan8.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult8.txt");
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whilePlan9.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult9.txt");
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whilePlan10.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileParseTestResult10.txt");
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles//whileFinalPlan.txt", "src/test/AllPlanTestFiles/WhileTestFiles//whileFinalParseTestResult6.txt");
    }

    @Test
    void planTest() {
//        readFileTest("src/test/AllPlanTestFiles/WhileTestFiles/whileFinalPlan.txt", "src/test/AllPlanTestFiles/PlanTestFiles/parseTestResult.txt");
        readFileTest("src/ConstructionPlan.txt", "src/test/AllPlanTestFiles/PlanTestFiles/parseTestResult.txt");
    }
}