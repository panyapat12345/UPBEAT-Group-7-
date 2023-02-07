import Exception.*;
import Tokenizer.*;
import AST.*;
import Parser.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class lab13 {
    public static void tokenizerTest (String readPath, String writePath) {
        Path readFile = Paths.get(readPath), writeFile = Paths.get(writePath);
        Charset charset = StandardCharsets.UTF_8;

        try (BufferedReader reader = Files.newBufferedReader(readFile, charset);
             BufferedWriter writer = Files.newBufferedWriter(writeFile, charset)) {
            String line = null;

            while((line = reader.readLine()) != null) {
                StringBuilder s = new StringBuilder();
                Tokenizer tkz = new PlanTokenizer(line);
                while(tkz.hasNextToken()) {
                    s.append(tkz.consume());
                    s.append(" ");
                }
                System.out.println(s.toString());

                writer.write(s.toString());
                writer.newLine();
            }
        } catch (NoSuchFileException | AccessDeniedException | FileNotFoundException e) {
            System.err.println("File not found.");
        } catch (IOException e) {
            System.err.println("IOExcertion: " + e);
        }
    }

    public static void main(String[] args) {
//        String readPath = "src/input.txt", writePath = "src/result.txt";
//        System.out.println("Read  : " + readPath);
//        System.out.println("Write : " + writePath);
//        fileCalculator(readPath, writePath);

        tokenizerTest("src/ConstructionPlan.txt", "src/tokenizerTestResult.txt");

    }
}
