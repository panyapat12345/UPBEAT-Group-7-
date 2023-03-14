package GameProcess;

import java.io.File;
import java.util.*;

public class Starter {
    public Starter() throws Exception {
        HashMap<String, Double> Variables = new HashMap<>();
        HashSet<String> acceptVariableNames = new HashSet<>(Set.of(
                "m"
                , "n"
                , "init_plan_min"
                , "init_plan_sec"
                , "init_budget"
                , "init_center_dep"
                , "plan_rev_min"
                , "plan_rev_sec"
                , "rev_cost"
                , "max_dep"
                , "interest_pct"
        ));

        try {
            File ConfigFile = new File("src/main/java/GameProcess/ConfigurationFile.txt");
            Scanner ConfigReader = new Scanner(ConfigFile);
            while (ConfigReader.hasNextLine()) {
                String CurrentLine = ConfigReader.nextLine().replaceAll("\\\\s+", "").trim();
                String Variable = CurrentLine.substring(0, CurrentLine.indexOf('='));
                Double Value = Math.abs(Double.valueOf(CurrentLine.substring(CurrentLine.indexOf('=') + 1, Integer.valueOf(CurrentLine.length()))));
                if(acceptVariableNames.contains(Variable)) Variables.put(Variable, Value);
                else throw new Exception("Unrecognized Variable");
            }
        } catch (Exception e) { throw e; }

        new internalOperator(Variables);
    }

    public static void main(String[] args) throws Exception {
        new Starter();
    }
}
