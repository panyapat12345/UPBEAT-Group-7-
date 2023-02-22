package GameProcess;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Starter {
    Starter() throws Exception {
        HashMap<String, Double> Variables = new HashMap<>();
        try {
            File ConfigFile = new File("src/main/java/GameProcess/ConfigurationFile.txt");
            Scanner ConfigReader = new Scanner(ConfigFile);
            while (ConfigReader.hasNextLine()) {
                String CurrentLine = ConfigReader.nextLine().replaceAll("\\\\s+", "").trim();
                String Variable = CurrentLine.substring(0, CurrentLine.indexOf('='));
                Double Value = Math.abs(Double.valueOf(CurrentLine.substring(CurrentLine.indexOf('=') + 1, Integer.valueOf(CurrentLine.length()))));
                switch (Variable) {
                    case "m" -> Variables.put("m", Value);
                    case "n" -> Variables.put("n", Value);
                    case "init_plan_min" -> Variables.put("init_plan_min", Value);
                    case "init_plan_sec" -> Variables.put("init_plan_sec", Value);
                    case "init_budget" -> Variables.put("init_budget", Value);
                    case "init_center_dep" -> Variables.put("init_center_dep", Value);
                    case "plan_rev_min" -> Variables.put("plan_rev_min", Value);
                    case "plan_rev_sec" -> Variables.put("plan_rev_sec", Value);
                    case "rev_cost" -> Variables.put("rev_cost", Value);
                    case "max_dep" -> Variables.put("max_dep", Value);
                    case "interest_pct" -> Variables.put("interest_pct", Value);
                    default -> throw new Exception("Unrecognized Variable");
                }
            }
        } catch (Exception e) {
            throw e;
        }

        new InternalOperator(Variables);
    }

    public static void main(String[] args) throws Exception {
        new Starter();
    }
}
