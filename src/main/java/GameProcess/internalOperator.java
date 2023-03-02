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
        return territory.getCurrentRegionInfo(crew.positionM, crew.positionN).Deposit.intValue();
    }

    @Override
    public int interest() {
        peekCiryCrew crew = currentPlayer().getCityCrewInfo();
        return territory.getCurrentRegionInfo(crew.positionM, crew.positionN).InterestRate.intValue();
    }

    @Override
    public int maxDeposit() {
        return variables.get("max_dep").intValue();
    }

    public int random() { return ThreadLocalRandom.current().nextInt(0, 999); }

    private boolean isRegionOfOpponent(int m, int n){
        int currentPlayer = turn%totalPlayers;
        peekRegion peek = territory.getInfoOfRegion(m, n);

        if(peek.Type.equals("null"));
        else if(peek.PlayerOwnerIndex == -1);
        else if(peek.PlayerOwnerIndex != currentPlayer) return true;

        return false;
    }

    @Override
    public int opponent() {
        int currentM = currentPlayer().getCityCrewInfo().positionM;
        int currentN = currentPlayer().getCityCrewInfo().positionM;
        int nearestRegion = 0;
        int distance = Integer.MAX_VALUE;

        int interestM;
        int interestN;
        for(int i = 1; i <= 6; i++){
            interestM = currentM;
            interestN = currentN;
            for(int j = 0; true; j++){
                switch (i){
                    case (1) -> interestM++;
                    case (2) -> {
                        interestN++;
                        interestM-=interestN%2;
                    }
                    case (3) -> {
                        interestN++;
                        interestM+=interestN%2;
                    }
                    case (4) -> interestM--;
                    case (5) -> {
                        interestN--;
                        interestM+=interestN%2;
                    }
                    case (6) -> {
                        interestN--;
                        interestM-=interestN%2;
                    }
                }
                if(!territory.isInBound(interestM, interestN)) break;
                else if(isRegionOfOpponent(interestM, interestN)) {
                    if(j < distance) {
                        distance = j;
                        nearestRegion = (10*distance)+i;
                    }
                    break;
                }
            }
        }

        return nearestRegion;
    }

    @Override
    public int nearby(int direction) {
        int interestM = currentPlayer().getCityCrewInfo().positionM;
        int interestN = currentPlayer().getCityCrewInfo().positionM;
        int returnValue = 0;
        for(int i = 0; true; i++){
            switch (direction){
                case (1) -> interestM++;
                case (2) -> {
                    interestN++;
                    interestM-=interestN%2;
                }
                case (3) -> {
                    interestN++;
                    interestM+=interestN%2;
                }
                case (4) -> interestM--;
                case (5) -> {
                    interestN--;
                    interestM+=interestN%2;
                }
                case (6) -> {
                    interestN--;
                    interestM-=interestN%2;
                }
            }
            if(!territory.isInBound(interestM, interestN)) break;
            else if(isRegionOfOpponent(interestM, interestN)) {
                returnValue = (100*i)+(int)(Math.floor(Math.log10(territory.getInfoOfRegion(interestM, interestN).Deposit)));
            }
        }
        return returnValue;
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



