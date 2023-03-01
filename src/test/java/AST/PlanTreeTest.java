package AST;

import Exception.*;
import Tokenizer.PlanTokenizer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlanTreeTest {
    void printIteratorTest(String s) {
        try {
            Tree tree = new PlanTree(s);
/*
            Iterator<Action.FinalActionState> i = tree.iterator();

            while(i.hasNext())
                System.out.println(i.next());

             */

        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        }
    }


    @Test
    void caseOne() {
/*
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

 */
        //printIteratorTest("budget=10000");
    }
}