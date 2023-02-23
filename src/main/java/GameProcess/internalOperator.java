package GameProcess;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

class internalOperator implements internalOperatorInterface {
    private static internalOperator instance;
    HashMap<String, Double> variables;
    private Territory territory;
    private LinkedList<player> players = new LinkedList<>();
    private int totalPlayers = 0;
    private int turn = 0;

    internalOperator(HashMap<String, Double> variables) {
        this.variables = variables;
        territory = new Territory(variables);
        instance = this;
    }

    public static internalOperator instance(){
        return instance;
    }

    public void addPlayer() {
        players.add(new player(totalPlayers));
        totalPlayers++;
    }

    public HashMap<String, Double> GetVariables(){ return variables; }
    public int rows() { return variables.get("m").intValue(); }
    public int cols() { return variables.get("n").intValue(); }

    @Override
    public int currow() {
        return currentPlayer().getCityCrewInfo().positionM;
    }

    @Override
    public int curcol() {
        return currentPlayer().getCityCrewInfo().positionN;
    }

    @Override
    public int budget() {
        return players.get(turn/totalPlayers).budget();
    }

    @Override
    public int deposit() {
        peekCiryCrew crew = currentPlayer().getCityCrewInfo();
        return territory.getcurrentregioninfo(crew.positionM, crew.positionN).GetInfo().Deposit.intValue();
    }

    @Override
    public int interest() {
        peekCiryCrew crew = currentPlayer().getCityCrewInfo();
        return territory.getcurrentregioninfo(crew.positionM, crew.positionN).GetInfo().InterestRate.intValue();
    }

    @Override
    public int maxDeposit() {
        return variables.get("max_dep").intValue();
    }

    public int random() { return ThreadLocalRandom.current().nextInt(0, 999); }

    @Override
    public int opponent() {
        return 0;
    }

    @Override
    public int nearby() {
        return 0;
    }

    public void NextTurn(){
        turn++;
        player CurrentPlayer = currentPlayer();
        for(int cycle = 0; cycle < 10000; cycle++){
        }
    }

    @Override
    public player currentPlayer() {
        return players.get(turn%totalPlayers);
    }
}



