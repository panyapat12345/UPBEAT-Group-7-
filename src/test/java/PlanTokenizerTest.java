import Exception.*;
import Tokenizer.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PlanTokenizerTest {
    private void printTokens(String src) {
        List<String> list = new ArrayList<>();
        Tokenizer t = new PlanTokenizer(src);
        while(t.hasNextToken()){
            list.add(t.consume());
        }
        System.out.println(list);
    }

    private void lexicalErrorTest(String src) {
        Exception e = assertThrows(LexicalError.class, () -> printTokens(src));
        System.err.println(e);
    }

    @Test
    void isLetterTest() {
        assertTrue(Character.isLetter('a'));
        assertTrue(Character.isLetter('A'));
        assertTrue(Character.isLetter('b'));
        assertTrue(Character.isLetter('z'));
        assertTrue(Character.isLetter('Z'));

        assertFalse(Character.isLetter('1'));
        assertFalse(Character.isLetter('5'));
        assertFalse(Character.isLetter('9'));
        assertFalse(Character.isLetter('6'));
        assertFalse(Character.isLetter('2'));

        assertFalse(Character.isLetter('+'));
        assertFalse(Character.isLetter('-'));
        assertFalse(Character.isLetter('*'));
        assertFalse(Character.isLetter('/'));
        assertFalse(Character.isLetter('%'));
        assertFalse(Character.isLetter('('));
        assertFalse(Character.isLetter(')'));
        assertFalse(Character.isLetter('#'));
    }

    @Test
    void validCase1() {
        printTokens("23+4-99 * 55 / 10 % 123");
        printTokens("0001 + (  999) / 1000* 12314++-*/ 79 84 )");
        printTokens("10- 5*22 / 11 + 47/7");
        printTokens("A * 5(B)");
        printTokens("ABCdef");
        printTokens("ABC def");
        printTokens("AaASd kasS Dasd ASDk");
        printTokens("(A*BC+d123)ef");
        printTokens("(A*B C+d12 3)ef");
        printTokens("#");
        printTokens("#23");
        printTokens("23#");
        printTokens("1askdmk a1das555as6aasd a*5545 a564/68s4d");
    }

    @Test
    void validCase2() {
        Tokenizer t = new PlanTokenizer("1 + 3 * 5(100)");
        assertTrue(t.peek("1"));
        assertFalse(t.peek("5"));
        assertThrows(SyntaxError.class, () -> t.consume("2"));
        assertThrows(SyntaxError.class,() -> t.consume("5"));
        try {
            t.consume("1");
            t.consume("+");
            t.consume("3");
            t.consume("*");
            t.consume("5");
            t.consume("(");
            t.consume("100");
            t.consume(")");
        } catch (SyntaxError e) {
            System.out.println(e);
        }
        assertThrows(SyntaxError.class,() -> t.consume("+"));
    }

    @Test
    void ThrowCase1() {
        lexicalErrorTest("!AB");
        lexicalErrorTest("AB!");
        lexicalErrorTest("A~B");
        lexicalErrorTest("A ~ B");
    }


    void identifierTest() {
        printTokens("m=20");
        printTokens("n=15");
        printTokens("init_plan_min=5");
        printTokens("init_plan_sec=0");
        printTokens("init_budget=10000");
        printTokens("init_center_dep=100");
        printTokens("plan_rev_min=30");
        printTokens("plan_rev_sec=0");
        printTokens("rev_cost=100");
        printTokens("max_dep=1000000");
        printTokens("interest_pct=5");
    }

    @Test
    void backTest1() {
        Tokenizer t = new PlanTokenizer("1 + 3 * 5(100)");
        System.out.println(t.consume());
        System.out.println(t.consume());
        System.out.println(t.consume());
        System.out.println(t.peek());
        t.back();
        System.out.println(t.peek());
        System.out.println(t.consume());
        System.out.println(t.peek());
    }

    @Test
    void backTest2() {
        Tokenizer t = new PlanTokenizer("1 + 3 * 5(100)");
        t.back();
        System.out.println(t.consume());
        System.out.println(t.peek());
    }
}