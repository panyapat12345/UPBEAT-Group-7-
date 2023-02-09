package GameProcess;

import java.io.File;
import java.util.Scanner;

class Starter{
    Starter(){
        Long[] Variables = new Long[16];
        try{
            File ConfigFile = new File("src/main/java/GameProcess/ConfigurationFile.txt");
            Scanner ConfigReader = new Scanner(ConfigFile);
            while (ConfigReader.hasNextLine()) {
                String CurrentLine = ConfigReader.nextLine().replaceAll("\\\\s+", "").trim();
                String Variable = CurrentLine.substring(0, CurrentLine.indexOf('='));
                long Value = Math.abs(Long.valueOf(CurrentLine.substring(CurrentLine.indexOf('=')+1, Integer.valueOf(CurrentLine.length()))));
                switch (Variable){
                    case "m" -> { Variables[0] = Value; }
                    case "n" -> { Variables[1] = Value; }
                    case "init_plan_min" -> { Variables[2] = Value; }
                    case "init_plan_sec" -> { Variables[3] = Value; }
                    case "init_budget" -> { Variables[4] = Value; }
                    case "init_center_dep" -> { Variables[5] = Value; }
                    case "plan_rev_min" -> { Variables[6] = Value; }
                    case "plan_rev_sec" -> { Variables[7] = Value; }
                    case "rev_cost" -> { Variables[8] = Value; }
                    case "max_dep" -> { Variables[9] = Value; }
                    case "interest_pct" -> { Variables[10] = Value; }
                }
            }
        }
        catch(Exception e){ e.printStackTrace(); }

        new InternalOperator(Variables);
    }

    public static void main(String[] args) { new Starter(); }
}

class InternalOperator implements InternalOperatorInterface{
    int m;
    int n;
    private Long init_plan_min;
    private Long init_plan_sec;
    private Long init_budget;
    private Long init_center_dep;
    private Long plan_rev_min;
    private Long plan_rev_sec;
    private Long rev_cost;
    private Long max_dep;
    private Long interest_pct;
    private Long[] Variables;
    private GameProcess.Territory Territory;

    InternalOperator(Long[] Variables){
        m = Math.toIntExact(Variables[0]);
        n = Math.toIntExact(Variables[1]);
        init_plan_min = Variables[2];
        init_plan_sec = Variables[3];
        init_budget = Variables[4];
        init_center_dep = Variables[5];
        plan_rev_min = Variables[6];
        plan_rev_sec = Variables[7];
        rev_cost = Variables[8];
        max_dep = Variables[9];
        interest_pct = Variables[10];
        this.Variables = Variables;
        for(int i = 0; i < 11; i++) System.out.println(Variables[i]);

        Territory = new Territory(m, n);
    }

    public Long[] GetVariables() { return Variables; }

    @Override
    public int rows() {
        return m;
    }

    @Override
    public int cols() {
        return n;
    }
}

class Territory implements TerritoryInterface{
    private int m;
    private int n;
    Region[][] Territory = null;
    Territory(int m, int n){
        this.m = m;
        this.n = n;
        Territory = new Region[m][n];
    }
}

class Region {

}

