package GameProcess;
import AST.Action;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

interface InternalOperatorInterface{
    int rows();
    int cols();
    void UpLoadConstructionPlan(int PlayerIndex, Action Plan);
    void ChangeConstructionPlan(int PlayerIndex, String Plan);
}

class InternalOperator implements InternalOperatorInterface{
    HashMap<String, Double> Variables;
    private GameProcess.Territory Territory;
    private LinkedList<Player> Players = new LinkedList<>();
    private int TotalPlayers = 0;
    private int Turn = 0;

    InternalOperator(HashMap<String, Double> Variables){
        this.Variables = Variables;
        Territory = new Territory(Variables);
    }

    public void AddPlayer(Action ConstructionPlan){
        Players.add(new Player(TotalPlayers, ConstructionPlan));
        TotalPlayers++;
    }

    public HashMap<String, Double> GetVariables(){ return Variables; }
    public int rows() { return Variables.get("m").intValue(); }
    public int cols() { return Variables.get("m").intValue(); }
    public void ChangeConstructionPlan(int PlayerIndex, String Plan) {  }
    public void UpLoadConstructionPlan(int PlayerIndex, Action Plan) { Players.get(PlayerIndex).ChangeConstructionPlan(Plan); }
    public PeekRegion GetInfoOfRegion(int M, int N) { return Territory.GetInfoOfRegion(M, N); }
    public int maxdeposit() { return Variables.get("max_dep").intValue(); }
    public static int random() { return ThreadLocalRandom.current().nextInt(0, 999); }

    public void NextTurn(){
        Player CurrentPlayer = Players.get(Turn/TotalPlayers);
        for(int cycle = 0; cycle < 10000; cycle++){
            break;
        }
    }
}



