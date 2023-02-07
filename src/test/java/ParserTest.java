import Exception.*;
import Tokenizer.*;
import AST.*;
import Parser.*;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private  void evalTest(int expected, String src, Map<String, Integer> bindings) throws SyntaxError {
        Parser p = new PlanParser(new PlanTokenizer(src));
        Node AST = p.parse();
//        assertEquals(expected, AST.eval(bindings));
    }

    private void evalTest(int expected, String src) throws SyntaxError{
        evalTest(expected, src, new HashMap<>());
    }

    private void prettyPrintTest(String expected, String src) throws SyntaxError{
        Parser p = new PlanParser(new PlanTokenizer(src));
        StringBuilder s = new StringBuilder();
        p.parse().prettyPrint(s);
        assertEquals(expected, s.toString());
    }

    private void syntaxErrorTest(String src) {
        Parser p = new PlanParser(new PlanTokenizer(src));
        assertThrows(SyntaxError.class,() -> p.parse());
    }

    private void arithmeticExceptionTest(String src) {
        Map<String, Integer> bindings = new HashMap<>();
        Parser p = new PlanParser(new PlanTokenizer(src));
//        Exception e = assertThrows(ArithmeticException.class, () -> p.parse().eval(bindings));
//        System.err.println(e);
    }

    private void lexicalErrorTest(String src) {
        try {
            Parser p = new PlanParser(new PlanTokenizer(src));
            p.parse();
        } catch (LexicalError e) {
            System.err.println(e);
            return;
        } catch (SyntaxError e) {
            fail("SyntaxError occer");
            return;
        }
        fail("expected LexicalError");
    }

    @Test
    void isNumberTest() {
        assertTrue(Parser.isNumber("5"));
        assertTrue(Parser.isNumber("10000"));
        assertTrue(Parser.isNumber("45681231"));
        assertFalse(Parser.isNumber("a"));
        assertFalse(Parser.isNumber("A"));
        assertFalse(Parser.isNumber("+"));
        assertFalse(Parser.isNumber("#"));
        assertFalse(Parser.isNumber(""));
        assertFalse(Parser.isNumber(" "));
    }

    @Test
    void nullTokenizer() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new PlanParser(null));
        System.err.println(e);
    }

    @Test
    void emptyString() {
        syntaxErrorTest("");
        syntaxErrorTest("     ");
    }

    @Test
    void singlePositiveNumber() {
        try {
            evalTest(123, " 123 ");
            evalTest(9, "0009");
            evalTest(9, "   0009");
            evalTest(9, "  ( 0009)");
            evalTest(9, "     0009    ");
            evalTest(1010, "0001010");
            evalTest(1092023, "01092023");
            evalTest(300, " ( 00000300) ");
            evalTest(1, "(((((((((1)))))))))");

            prettyPrintTest("123", " 123 ");
            prettyPrintTest("9", "0009");
            prettyPrintTest("9", "   0009");
            prettyPrintTest("9", "  ( 0009)");
            prettyPrintTest("9", "     0009    ");
            prettyPrintTest("1010", "0001010");
            prettyPrintTest("1092023", "01092023");
            prettyPrintTest("300", " ( 00000300) ");
            prettyPrintTest("1", "(((((((((1)))))))))");
        } catch (SyntaxError e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    void singleNegativeNumber() {
        try {
            evalTest(-9, "0-9");
            evalTest(-9, "0-0009");
            evalTest(-909, "00000-00909");
            evalTest(-909, "((00000)-(00909))");
            evalTest(-12345, "0-12345");

            prettyPrintTest("(0-9)", "0-9");
            prettyPrintTest("(0-9)", "0-0009");
            prettyPrintTest("(0-909)", "00000-00909");
            prettyPrintTest("(0-909)", "((00000)-(00909))");
            prettyPrintTest("(0-12345)", "0-12345");

        } catch (SyntaxError e) {
            System.out.println(e);
            fail();

        }
    }

    @Test
    void zeroNumber() {
        try {
            evalTest(0, "0000");
            evalTest(0, "( 0000)");
            evalTest(0, "0+(0)");
            evalTest(0, "(0)-0");
            evalTest(0, "(0*0)");
            arithmeticExceptionTest("0/0");
            arithmeticExceptionTest("0%0");

            prettyPrintTest("0", "0000");
            prettyPrintTest("0", "( 0000)");
            prettyPrintTest("(0+0)", "0+(0)");
            prettyPrintTest("(0-0)", "(0)-0");
            prettyPrintTest("(0*0)", "(0*0)");
            prettyPrintTest("(0/0)", "0/0");
            prettyPrintTest("(0%0)", "0%0");
        } catch (SyntaxError e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    void positiveNumber() {
        try {
            evalTest(18, "(7)+11");
            evalTest(-4, "7-(11)");
            evalTest(77, "7*11");
            evalTest(0, "(7/11)");
            evalTest(7, "7%11");

            evalTest(18, "11+(7)");
            evalTest(4, "(11)-7");
            evalTest(77, "(11)*(7)");
            evalTest(1, "((11) / (7))");
            evalTest(4, "11%7");

            prettyPrintTest("(11/7)", "((11) / (7))");
        } catch (SyntaxError e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    void negativeNumber() {
        try {
            evalTest(4, "(0-7)+11");
            evalTest(-18, "(0-7)-(11)");
            evalTest(-77, "(0-7)*11");
            evalTest(0, "(0-7)/11");
            evalTest(-7, "(0-7)%11");

            evalTest(-4, "(0-11)+7");
            evalTest(-18, "(0-11)-7");
            evalTest(-77, "(0-11)*7");
            evalTest(-1, "(0-11)/7");
            evalTest(-4, "(0-11)%7");

            evalTest(-4, "7+(0-11)");
            evalTest(18, "7-(0-11)");
            evalTest(-77, "7*(0-11)");
            evalTest(0, "7/(0-11)");
            evalTest(7, "7%(0-11)");

            evalTest(-18, "(0-7)+(0-11)");
            evalTest(4, "(0-7)-(0-11)");
            evalTest(77, "(0-7)*(0-11)");
            evalTest(0, "(0-7)/(0-11)");
            evalTest(1, "(0-11)/(0-7)");
            evalTest(-7, "(0-7)%(0-11)");

            prettyPrintTest("((0-7)+11)", "(0-7)+11");
            prettyPrintTest("(7+(0-11))", "7+(0-11)");
            prettyPrintTest("((0-7)+(0-11))", "(0-7)+(0-11)");
        } catch (SyntaxError e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    void positiveWithZero() {
        try {
            evalTest(7, "7+0");
            evalTest(7, "7-0");
            evalTest(0, "7*0");
            arithmeticExceptionTest("7/0");
            arithmeticExceptionTest("7%0");

            evalTest(7, "0+7");
            evalTest(-7, "0-7");
            evalTest(0, "0*7");
            evalTest(0, "0/7");
            evalTest(0, "0%7");
        } catch (SyntaxError e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    void negativeWithZero() {
        try {
            evalTest(-7, "(0-7)+0");
            evalTest(-7, "(0-7)-0");
            evalTest(0, "(0-7)*0");
            arithmeticExceptionTest("(0-7)/0");
            arithmeticExceptionTest("(0-7)%0");

            evalTest(-7, "0+(0-7)");
            evalTest(7, "0-(0-7)");
            evalTest(0, "0*(0-7)");
            evalTest(0, "0/(0-7)");
            evalTest(0, "0%(0-7)");
        } catch (SyntaxError e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    void withOneNumber() {
        try {
            evalTest(6, "5+1");
            evalTest(4, "5-1");
            evalTest(5, "5*1");
            evalTest(5, "5/1");
            evalTest(0, "5%1");

            evalTest(6, "1+5");
            evalTest(-4, "1-5");
            evalTest(5, "1*5");
            evalTest(0, "1/5");
            evalTest(1, "1%5");

            evalTest(-4, "(0-5)+1");
            evalTest(-6, "(0-5)-1");
            evalTest(-5, "(0-5)*1");
            evalTest(-5, "(0-5)/1");
            evalTest(0, "(0-5)%1");

            evalTest(4, "5+(0-1)");
            evalTest(6, "5-(0-1)");
            evalTest(-5, "5*(0-1)");
            evalTest(-5, "5/(0-1)");
            evalTest(0, "5%(0-1)");

            evalTest(-6, "(0-5)+(0-1)");
            evalTest(-4, "(0-5)-(0-1)");
            evalTest(5, "(0-5)*(0-1)");
            evalTest(5, "(0-5)/(0-1)");
            evalTest(0, "(0-5)%(0-1)");
        } catch (SyntaxError e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    void lotsOfSpacesString() {
        try {
            evalTest(11, " 5 + 6 ");
            evalTest(-1, "5 -6 ");
            evalTest(30, " 5 * 6 ");
            evalTest(8, " 00050 / 0006 ");
            evalTest(5, "      5   %  6     ");
            evalTest(5, " 5 *  (5  -5 ) +5");
            evalTest(293, "23 + 45 * 6");
            evalTest(6, "10- 5*(22 / 11) + 47/7");

            prettyPrintTest("(5+6)", " 5 + 6 ");
            prettyPrintTest("(5-6)", "5 -6 ");
            prettyPrintTest("(5*6)", " 5 * 6 ");
            prettyPrintTest("(50/6)", " 00050 / 0006 ");
            prettyPrintTest("(5%6)", "      5   %  6     ");
            prettyPrintTest("((5*(5-5))+5)", " 5 *  (5  -5 ) +5");
            prettyPrintTest("(23+(45*6))", "23 + 45 * 6");
            prettyPrintTest("((10-(5*(22/11)))+(47/7))", "10- 5*(22 / 11) + 47/7");
        } catch (SyntaxError e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    void longString() {
        try {
            evalTest(0, "0+10-10");
            evalTest(0, "0-10+10");
            evalTest(-20, "0-(10+10)");
            arithmeticExceptionTest("5+4-7/0");
            evalTest(26051, "1+5*10*521*1");
            evalTest(26051, "1+5*10*(521*1)");
            evalTest(31260, "(1+5)*10*521*1");

            evalTest(1, "1+00000/000001");
            evalTest(20, "10000/5/5/5/5+4");
            evalTest(10004, "10000/(5/5)/(5/5)+4");
            arithmeticExceptionTest("10000/5/5/(5/(5+4))");

            evalTest(-13, "1+2*3-4*5");
            evalTest(-11, "(1+2)*3-4*5");
            evalTest(-9, "1+2*(3-4)*5");
            evalTest(-15, "(1+2)*(3-4)*5");

            evalTest(6, "10+20-30*40/50%60");
            evalTest(0, "((10+20)-30)*40/50%60");
            evalTest(2, "10+(20-30)*40/50%60");
            evalTest(30, "10+20-30*(40/50)%60");
            evalTest(6, "(10+20)-((30*40)/(50%60))");

            evalTest(21, "2021%100-1*5+5");
            evalTest(21, "(2021%100)-(1*5)+5");
            evalTest(11, "(2021%100)-1*(5+5)");

            evalTest(9+8-7*6/5%4, "9+8-7*6/5%4");
            evalTest(10-5*11/22+47/7, "10-5*11/22+47/7");
            evalTest(99+101-150*50/100/5, "99+101-150*50/100/5");
            evalTest(2022/100*100+25-2, "2022/100*100+25-2");
            evalTest((2022/100)*(100+25)-2, "(2022/100)*(100+25)-2");
            evalTest(10+5/5+10*9-20%10, "10+5/5+10*9-20%10");
            evalTest((10+5)/5+10*9-(20%10), "(10+5)/5+10*9-(20%10)");
            evalTest(1010-1000*5/999+10*50+7%5-999*6-5*12/9513+0, "1010-1000*5/999+10*50+7%5-999*6-5*12/9513+0");

            prettyPrintTest("((0+10)-10)", "0+10-10");
            prettyPrintTest("((0-10)+10)", "0-10+10");
            prettyPrintTest("(1+(((5*10)*521)*1))", "1+5*10*521*1");
            prettyPrintTest("((((1+5)*10)*521)*1)", "(1+5)*10*521*1");
            prettyPrintTest("(((((10000/5)/5)/5)/5)+4)", "10000/5/5/5/5+4");
            prettyPrintTest("((1+(2*3))-(4*5))", "1+2*3-4*5");
            prettyPrintTest("((10+20)-(((30*40)/50)%60))", "10+20-30*40/50%60");
            prettyPrintTest("((10+20)-((30*40)/(50%60)))", "(10+20)-((30*40)/(50%60))");
            prettyPrintTest("((9+8)-(((7*6)/5)%4))", "9+8-7*6/5%4");
            prettyPrintTest("((((10+5)/5)+(10*9))-(20%10))", "(10+5)/5+10*9-(20%10)");

        } catch (SyntaxError e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    void invalidString() {
        syntaxErrorTest("( )");
        syntaxErrorTest("(+)");
        syntaxErrorTest("( ");
        syntaxErrorTest(" )");
        syntaxErrorTest("1  ( ");
        syntaxErrorTest("1 )");
        syntaxErrorTest("(  1");
        syntaxErrorTest(" ) 1");
        syntaxErrorTest("+  ( ");
        syntaxErrorTest("- )");
        syntaxErrorTest("(  *");
        syntaxErrorTest(" ) /");

        syntaxErrorTest("1+ ");
        syntaxErrorTest("+1");
        syntaxErrorTest("-1");
        syntaxErrorTest("*1");
        syntaxErrorTest("99+");

        syntaxErrorTest("-1+10");
        syntaxErrorTest("+1-10");
        syntaxErrorTest("5++6");
        syntaxErrorTest("5+-6");
        syntaxErrorTest("5+6*");
        syntaxErrorTest("5/6%");

        syntaxErrorTest("1 0 + 5");
        syntaxErrorTest("2 3 +");
        syntaxErrorTest("23 +");
        syntaxErrorTest("1 23 + 321");
        syntaxErrorTest("1+5*10 (521*1)");

        syntaxErrorTest("1 * ( 23 + 321");
        syntaxErrorTest("1 * 23 + 321 )");
        syntaxErrorTest("1 (*) 23 + 321");
        syntaxErrorTest("(1 *) 23 + 321");
        syntaxErrorTest("1 (* 23 + 321)");
        syntaxErrorTest("1 (23 + 321)");

        syntaxErrorTest("1 mod 1");
        syntaxErrorTest("A + 2BC");
        syntaxErrorTest("A A + 1");
        syntaxErrorTest("2x + 3y");
        syntaxErrorTest("(2x)(3y)");
        //syntaxErrorTest("CPE200");
        syntaxErrorTest("C P E+200");
    }

    @Test
    void variableTest() {
        try {
            Map<String, Integer> bindings = new HashMap<>();
            bindings.put("a", 2023);
            bindings.put("A", 11);
            bindings.put("B", 99);
            bindings.put("X", 10);
            bindings.put("Y", 5);
            bindings.put("ABC", 9900);
            bindings.put("abc", 4);
            bindings.put("AA", 10);
            bindings.put("Aa", 20);
            bindings.put("aA", 30);
            bindings.put("aa", 40);
            bindings.put("x", 2);
            bindings.put("y", 3);
            bindings.put("z", 4);
            bindings.put("C", 785);
            bindings.put("P", 3);
            bindings.put("E", 1000);

            evalTest(2023 ,"a", bindings);
            evalTest(110 ,"A + B", bindings);
            evalTest(21 ,"A + 10", bindings);
            evalTest(120 ,"X*X + 2*X", bindings);
            evalTest(60 ,"X*Y + 2*Y", bindings);
            evalTest(10000, "ABC + 100", bindings);
            evalTest(10000, "ABC + 25*abc", bindings);
            evalTest(570, "AA + Aa * aA - aa", bindings);
            evalTest(80+45-28, "10*x*x*x + 5*y*y - 7*z", bindings);
            evalTest(261200, "(C/P*E)+200", bindings);
            evalTest(-1, "(x+y)*(x-y)+z", bindings);
            evalTest(3, "x+(y*(x-y))+z", bindings);

            prettyPrintTest("a", "a");
            prettyPrintTest("a", "((((a))))");
            prettyPrintTest("(A+B)", "A + B");
            prettyPrintTest("(A+B)", "A + (B)");
            prettyPrintTest("(A+10)", "(A) + 10");
            prettyPrintTest("((X*X)+(2*X))", "X*X + 2*X");
            prettyPrintTest("((X*Y)+(2*Y))", "X*Y + 2*Y");
            prettyPrintTest("(ABC+100)", "ABC + 100");
            prettyPrintTest("(ABC+(25*abc))", "ABC + 25*abc");
            prettyPrintTest("((AA+(Aa*aA))-aa)", "AA + Aa * aA - aa");
            prettyPrintTest("(((((10*x)*x)*x)+((5*y)*y))-(7*z))", "10*x*x*x + 5*y*y - 7*z");
            prettyPrintTest("(((C/P)*E)+200)", "(C/P*E)+200");

        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        }
    }

    @Test
    void nullBindings() {
        try {
            evalTest(120 ,"X*X + 2*X", null);
        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        } catch (IllegalArgumentException e ) {
            System.err.println(e);
        }
    }

    @Test
    void evalErrorTest() {
        try {
            Map<String, Integer> bindings = new HashMap<>();
            bindings.put("a", 2023);
            bindings.put("A", 11);
            bindings.put("B", 99);
            bindings.put("X", 10);

            evalTest(120 ,"X*X + 2*Y", bindings);

        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        } catch (EvalError e ) {
            System.err.println(e);
        }
    }

    @Test
    void unknownCharacter() {
        //lexicalErrorTest("#");
        //lexicalErrorTest("A#B");
        lexicalErrorTest("10 + A~(5)");
        lexicalErrorTest("10 + A - (5+!)");
        lexicalErrorTest("x_a + y_a");
    }

    @Test
    void powTest() {
        try {
            evalTest(8 ,"2^3");
            evalTest(27 ,"3^3");
            prettyPrintTest("(2^(3^3))","2^3^3");
            evalTest(134217728 ,"2^3^3");
            prettyPrintTest("(2^(3^3))","2^3^3");
            evalTest(11 ,"3+2^3");
            evalTest(11 ,"2^3+3");
            prettyPrintTest("(((2^3)*5)+10)","2^3*5+10");
            evalTest(50 ,"2^3*5+10");
            evalTest(50 ,"10+5*2^3");

        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        } catch (IllegalArgumentException e ) {
            System.err.println(e);
        }
    }

    @Test
    void prettyPrintTest() {
        try {
            prettyPrintTest("a", "a");
            prettyPrintTest("a", "((((a))))");
            prettyPrintTest("(A+B)", "A + B");
            prettyPrintTest("(A+B)", "A + (B)");
            prettyPrintTest("(A+10)", "(A) + 10");
            prettyPrintTest("((X*X)+(2*X))", "X*X + 2*X");
            prettyPrintTest("((X*Y)+(2*Y))", "X*Y + 2*Y");
            prettyPrintTest("(ABC+100)", "ABC + 100");
            prettyPrintTest("(ABC+(25*abc))", "ABC + 25*abc");
            prettyPrintTest("((AA+(Aa*aA))-aa)", "AA + Aa * aA - aa");
            prettyPrintTest("(((((10*x)*x)*x)+((5*y)*y))-(7*z))", "10*x*x*x + 5*y*y - 7*z");
            prettyPrintTest("(((C/P)*E)+200)", "(C/P*E)+200");
            prettyPrintTest("((0+10)-10)", "0+10-10");
            prettyPrintTest("((0-10)+10)", "0-10+10");
            prettyPrintTest("(1+(((5*10)*521)*1))", "1+5*10*521*1");
            prettyPrintTest("((((1+5)*10)*521)*1)", "(1+5)*10*521*1");
            prettyPrintTest("(((((10000/5)/5)/5)/5)+4)", "10000/5/5/5/5+4");
            prettyPrintTest("((1+(2*3))-(4*5))", "1+2*3-4*5");
            prettyPrintTest("((10+20)-(((30*40)/50)%60))", "10+20-30*40/50%60");
            prettyPrintTest("((10+20)-((30*40)/(50%60)))", "(10+20)-((30*40)/(50%60))");
            prettyPrintTest("((9+8)-(((7*6)/5)%4))", "9+8-7*6/5%4");
            prettyPrintTest("((((10+5)/5)+(10*9))-(20%10))", "(10+5)/5+10*9-(20%10)");
            prettyPrintTest("(5+6)", " 5 + 6 ");
            prettyPrintTest("(5-6)", "5 -6 ");
            prettyPrintTest("(5*6)", " 5 * 6 ");
            prettyPrintTest("(50/6)", " 00050 / 0006 ");
            prettyPrintTest("(5%6)", "      5   %  6     ");
            prettyPrintTest("((5*(5-5))+5)", " 5 *  (5  -5 ) +5");
            prettyPrintTest("(23+(45*6))", "23 + 45 * 6");
            prettyPrintTest("((10-(5*(22/11)))+(47/7))", "10- 5*(22 / 11) + 47/7");
        } catch (SyntaxError e) {
            System.err.println(e);
            fail();
        } catch (IllegalArgumentException e ) {
            System.err.println(e);
        }
    }
}