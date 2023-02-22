import AST.State;
import Parser.Parser;
import Parser.PlanParser;
import Tokenizer.PlanTokenizer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Map<String, Integer> binding = new HashMap<>();
        try{
            File ConfigFile = new File("src/main/java/GameProcess/ConfigurationFile.txt");
            Scanner ConfigReader = new Scanner(ConfigFile);
            StringBuilder s = new StringBuilder();

            while (ConfigReader.hasNextLine()) {
                s.append(ConfigReader.nextLine());
                s.append(" ");
                Parser p = new PlanParser(new PlanTokenizer(s.toString()));
                State AST = p.parse();
                if (AST != null)
                    AST.doState(binding);
            }
        }
        catch(Exception e){ e.printStackTrace(); }

        // use this
        System.out.println(binding);
    }
}
