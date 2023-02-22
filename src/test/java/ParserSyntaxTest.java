import AST.*;
import Exception.EvalError;
import Exception.LexicalError;
import Exception.SyntaxError;
import Parser.Parser;
import Parser.PlanParser;
import Tokenizer.PlanTokenizer;
import Tokenizer.Tokenizer;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParserSyntaxTest {
    private void parseTest(String src){
        try {
            Parser p = new PlanParser(new PlanTokenizer(src));
            State AST =  p.parse();

            // parse testing
            StringBuilder s = new StringBuilder();
            if(AST != null)
                AST.prettyPrint(s);
            //System.out.print(s.toString());

            // doState testing
            Map<String, Integer> binding = new HashMap<>();
            if(AST != null)
                AST.doState(binding);
            printMap(binding);
        } catch (SyntaxError e) {
//            System.err.println(e.getMessage() + " : " + src);
            System.err.println(e.getMessage());
            fail();
        }
    }

    private void printMap(Map<String, Integer> binding){
        System.out.println("{");
        for ( String identifier : binding.keySet()){
            System.out.println("  " + identifier + " = " + binding.get(identifier) + ",");
        }
        System.out.println("}");
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

            try {
                Parser p = new PlanParser(new PlanTokenizer(parseString.toString()));
                // parse testing
                State AST = p.parse();
                if(AST != null)
                    AST.prettyPrint(result);
                // System.out.println(result.toString());

                // doState testing
                Map<String, Integer> binding = new HashMap<>();
                if(AST != null)
                    AST.doState(binding);
                printMap(binding);
            } catch(SyntaxError e) {
                System.err.println(e.getMessage());
                fail();
            }

            writer.write(result.toString());
        } catch (NoSuchFileException | AccessDeniedException | FileNotFoundException e) {
            System.err.println("File not found.");
        } catch (IOException e) {
            System.err.println("IOExcertion: " + e);
        }
    }

    @Test
    void caseOne() {
        parseTest("budget=10000");
        parseTest("m=20");
        parseTest("n=15");
        parseTest("init_plan_min=5");
        parseTest("init_plan_sec=0");
        parseTest("init_budget=10000");
        parseTest("init_center_dep=100");
        parseTest("plan_rev_min=30");
        parseTest("plan_rev_sec=0");
        parseTest("rev_cost=100");
        parseTest("max_dep=1000000");
        parseTest("interest_pct=5");
    }

    @Test
    void caseTwo() {
        parseTest("t = t + 1");
        parseTest("m = 0");
//        parseTest("deposit");
//        parseTest("deposit - 100");
        parseTest("collect (deposit / 4)");
//        parseTest("budget - 25");
        parseTest("invest 25");
//        parseTest("budget - 100");
        parseTest("done");
        parseTest("opponentLoc = opponent");
//        parseTest("opponentLoc / 10 - 1");
//        parseTest("opponentLoc % 10 - 5");
        parseTest("move downleft");
//        parseTest("opponentLoc % 10 - 4");
        parseTest("move down");
//        parseTest("opponentLoc % 10 - 3");
        parseTest("move downright");
//        parseTest("opponentLoc % 10 - 2");
//        parseTest("opponentLoc % 10 - 1");
        parseTest("move upright");
        parseTest("move up");
//        parseTest("opponentLoc");
//        parseTest("opponentLoc % 10 - 5");
        parseTest("cost = 10 ^ (nearby upleft % 100 + 1)");
//        parseTest("budget - cost");
        parseTest("shoot upleft cost");
//        parseTest("opponentLoc % 10 - 4");
        parseTest("cost = 10 ^ (nearby downleft % 100 + 1)");
        parseTest("shoot downleft cost");
//        parseTest("opponentLoc % 10 - 3");
        parseTest("cost = 10 ^ (nearby down % 100 + 1)");
        parseTest("shoot down cost");
//        parseTest("opponentLoc % 10 - 2");
        parseTest("cost = 10 ^ (nearby downright % 100 + 1)");
        parseTest("shoot downright cost");
//        parseTest("opponentLoc % 10 - 1");
        parseTest("cost = 10 ^ (nearby upright % 100 + 1)");
        parseTest("shoot upright cost");
        parseTest("cost = 10 ^ (nearby up % 100 + 1)");
        parseTest("shoot up cost");
        parseTest("dir = random % 6");
//        parseTest("dir - 4");
        parseTest("move upleft");
//        parseTest("dir - 3");
        parseTest("move downleft");
//        parseTest("dir - 2");
        parseTest("move down");
//        parseTest("dir - 1");
//        parseTest("dir");
        parseTest("move upright");
        parseTest("move up");
        parseTest("m = m + 1");
//        parseTest("budget - 1");
        parseTest("invest 1");
    }

    @Test
    void stateOne() {
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
        parseTest(s.toString());
    }

    @Test
    void stateTwo() {
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
        parseTest(s.toString());
    }

    @Test
    void planIfTest() {
        // readFileTest("src/test/Plan1.txt", "src/test/parseTestResult.txt");
        // readFileTest("src/test/ifPlan1.txt", "src/test/ifParseTestResult1.txt");
        // readFileTest("src/test/ifPlan2.txt", "src/test/ifParseTestResult2.txt");
        // readFileTest("src/test/ifPlan3.txt", "src/test/ifParseTestResult3.txt");
        // readFileTest("src/test/ifPlan4.txt", "src/test/ifParseTestResult4.txt");
        // readFileTest("src/test/ifPlan5.txt", "src/test/ifParseTestResult5.txt");
        // readFileTest("src/test/ifPlan6.txt", "src/test/ifParseTestResult6.txt");
    }

    @Test
    void planWhileTest() {
        readFileTest("src/test/whilePlan1.txt", "src/test/whileParseTestResult1.txt");
    }
}