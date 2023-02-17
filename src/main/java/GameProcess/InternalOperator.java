package GameProcess;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class Starter{
    Starter(){
        Double[] Variables = new Double[16];
        try{
            File ConfigFile = new File("src/main/java/GameProcess/ConfigurationFile.txt");
            Scanner ConfigReader = new Scanner(ConfigFile);
            while (ConfigReader.hasNextLine()) {
                String CurrentLine = ConfigReader.nextLine().replaceAll("\\\\s+", "").trim();
                String Variable = CurrentLine.substring(0, CurrentLine.indexOf('='));
                Double Value = Math.abs(Double.valueOf(CurrentLine.substring(CurrentLine.indexOf('=')+1, Integer.valueOf(CurrentLine.length()))));
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
    private Double init_plan_min;
    private Double init_plan_sec;
    private Double init_budget;
    private Double init_center_dep;
    private Double plan_rev_min;
    private Double plan_rev_sec;
    private Double rev_cost;
    private Double max_dep;
    private Double interest_pct;
    private Double[] Variables;
    private GameProcess.Territory Territory;
    private List<Player> Players = new LinkedList<>();
    private int TotalPlayers = 0;
    private int Turn = 0;

    InternalOperator(Double[] Variables){
        m = Variables[0].intValue();
        n = Variables[1].intValue();
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

    public void AddPlayer(){
        Players.add(new Player(TotalPlayers, "Null"));
        TotalPlayers++;
    }

    public Double[] GetVariables(){ return Variables; }

    @Override
    public int rows() {
        return m;
    }

    @Override
    public int cols() {
        return n;
    }

    public Region GetCurrentRegionInfo(int M, int N){
        return Territory.GetCurrentRegionInfo(M, N);
    }

    public int maxdeposit() { return max_dep.intValue(); }

    public int random() { return ThreadLocalRandom.current().nextInt(0, 999); }

    public void NextTurn(){
        Player CurrentPlayer = Players.get(Turn/TotalPlayers);
        for(int cycle = 0; cycle < 1000; cycle++){
            CurrentPlayer.NextConstructionPlanNode();
        }
    }
}

class Territory implements TerritoryInterface{
    private int m;
    private int n;
    private Region[][] Territory = null;
    Territory(int m, int n){
        this.m = m;
        this.n = n;
        Territory = new Region[m][n];
    }
    public Region GetCurrentRegionInfo(int M, int N){
        if((M >= 0 && N >= 0) && (M < m && N < n)) return Territory[M][N];
        else return new Region();
    }

    public int opponent(int PlayerIndex, int CurrentM, int CurrentN){
        return 0;
    }
}

class Region {
    private int PlayerOwnerIndex;
    private Double Deposit;
    private Double InterestRate;
    private String Type = "Empty";
    public int GetOwner(){ return PlayerOwnerIndex; }
    public int deposit(){ return Deposit.intValue(); }
    public int interest() { return (int)(Deposit*InterestRate/100.0); }
}

class Player {
    private int PlayerIndex;
    String ConstructionPlan = null;
    private int CapitalPositionN;
    private int CapitalPositionM;
    CityCrew crew = null;
    Player(int Id, String constructionPlan){
        PlayerIndex = Id;
        ConstructionPlan = constructionPlan;
    }
    private void NewCityCrew(){
        crew = new CityCrew(CapitalPositionN, CapitalPositionM);
    }
    public void NextConstructionPlanNode(){
        this.NewCityCrew();
        return;
    }
}

class CityCrew {
    private int PositionN;
    private int PositionM;
    Double BudGet;
    CityCrew(int PositionN, int PositionM){
        this.PositionN = PositionN;
        this.PositionM = PositionM;
    }
    public int currow(){ return PositionN; }
    public int curcol(){ return PositionM; }
}

