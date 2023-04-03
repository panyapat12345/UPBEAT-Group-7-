package GameProcess;
import AST.Action;
import GameProcess.Display.DisplayPlayer;
import GameProcess.Display.DisplayRegion;
import Graph.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import Exception.*;
import org.springframework.stereotype.Component;

@Component
public class internalOperator implements internalOperatorInterface {
    private static internalOperator instance;
    private HashMap<String, Double> configVals = new HashMap<>();
    private Territory territory;
    private LinkedList<player> players = new LinkedList<>();
    private int totalPlayers = 0;
    private int totalTurn = 0;
    private int realTurn = 0;
    private player currentPlayer = null;
    private List<Map.Entry<Integer, Integer>> startCityCenter = new LinkedList<>();
    private int[] cityCenterPlayer1Pos;

    public internalOperator(HashMap<String, Double> configVals) {
        this.configVals = configVals;
        init();
        instance = this;
    }

    public internalOperator(double m, double n, double init_plan_min, double init_plan_sec, double init_budget, double init_center_dep, double plan_rev_min, double plan_rev_sec, double rev_cost, double max_dep, double interest_pct){
        configVals.put("m", m);
        configVals.put("n", n);
        configVals.put("init_plan_min", init_plan_min);
        configVals.put("init_plan_sec", init_plan_sec);
        configVals.put("init_budget", init_budget);
        configVals.put("init_center_dep", init_center_dep);
        configVals.put("plan_rev_min", plan_rev_min);
        configVals.put("plan_rev_sec", plan_rev_sec);
        configVals.put("rev_cost", rev_cost);
        configVals.put("max_dep", max_dep);
        configVals.put("interest_pct", interest_pct);
        init();
        instance = this;
    }

    private void init(){
        territory = new Territory(configVals);
        Graph.instance(configVals.get("m").intValue(), configVals.get("n").intValue());
    }

    public static internalOperator instance(){
        return instance;
    }

    public int[] randomCityCenterPos(){
        int m = ThreadLocalRandom.current().nextInt(0, configVals.get("m").intValue());
        int n = ThreadLocalRandom.current().nextInt(0, configVals.get("n").intValue());
        return new int[]{m, n};
    }

    public void addPlayer(String constructionPlan){
        // set up
        int[] pos = randomCityCenterPos();
        if(totalPlayers == 0){
            cityCenterPlayer1Pos = pos;
        } else {
            while (pos[0] == cityCenterPlayer1Pos[0] && pos[1] == cityCenterPlayer1Pos[1])
                pos = randomCityCenterPos();
        }
//        System.err.println(pos[0] + ", " + pos[1]);

        player newPlayer = new player(totalPlayers, pos[0], pos[1], configVals.get("init_budget"));
        totalPlayers++;
        territory.newCityCenter(newPlayer.getCityCrewInfo());
        players.add(newPlayer);
        if(totalPlayers == 1) currentPlayer = currentPlayer();
    }

    public HashMap<String, Double> GetConfigVals(){ return configVals; }
    public int rows() { return configVals.get("m").intValue(); }
    public int cols() { return configVals.get("n").intValue(); }

    @Override
    public int currow() {
        return currentPlayer.getCityCrewInfo().positionM;
    }

    @Override
    public int curcol() {
        return currentPlayer.getCityCrewInfo().positionN;
    }

    @Override
    public int budget() {
        return currentPlayer.budget();
    }

    @Override
    public int deposit() {
        peekCiryCrew crew = currentPlayer().getCityCrewInfo();
        return territory.getCurrentRegionInfo(crew).deposit.intValue();
    }

    @Override
    public int interest() {
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        return territory.getCurrentRegionInfo(crew).real_InterestRate.intValue();
    }

    @Override
    public int maxDeposit() {
        return configVals.get("max_dep").intValue();
    }

    public int random() { return ThreadLocalRandom.current().nextInt(0, 999); }

    private boolean isOpponentRegion(int m, int n){
        int currentPlayer = getCurrentPlayerIndex();
        peekRegion region = territory.getInfoOfRegion(m, n);
        if(!region.Type.equals("null") && !(region.playerOwnerIndex == -1) && region.playerOwnerIndex != currentPlayer)
            return true;
        return false;
    }

    private boolean isOpponentRegion(peekRegion region){
        return isOpponentRegion(region.positionM, region.positionN);
    }

    private boolean isOpponentRegion(peekCiryCrew crew){
        return isOpponentRegion(crew.positionM, crew.positionN);
    }

    private boolean isOwnerRegion(int m, int n){
        int currentPlayer = getCurrentPlayerIndex();
        peekRegion region = territory.getInfoOfRegion(m, n);
        if (!region.Type.equals("null") && region.playerOwnerIndex == currentPlayer)
            return true;
        return false;
    }

    private boolean isOwnerRegion(peekRegion region){
        return isOwnerRegion(region.positionM, region.positionN);
    }

    private boolean isOwnerRegion(peekCiryCrew crew){
        return isOwnerRegion(crew.positionM, crew.positionN);
    }

    @Override
    public int opponent() {
        peekCiryCrew crew = currentPlayer().getCityCrewInfo();
        Graph.GraphNode node = Graph.instance().getGraph()[crew.positionM][crew.positionN];
        Graph.GraphNode[] current = new Graph.GraphNode[]{node, node, node, node, node, node,};
        int distance = 0;

        while(true){
            distance++;
            for(int i = 1; i <= 6; i++){
                if(current[i-1] == null) continue;
                switch (i) {
                    case 1 -> { current[0] = current[0].up(); }
                    case 2 -> { current[1] = current[1].upRight(); }
                    case 3 -> { current[2] = current[2].downRight(); }
                    case 4 -> { current[3] = current[3].down(); }
                    case 5 -> { current[4] = current[4].downLeft(); }
                    case 6 -> { current[5] = current[5].upLeft(); }
                }

                if(current[i-1] == null) continue;
                peekRegion region = territory.getInfoOfRegion(current[i-1].getRow(), current[i-1].getCol());
                if(region.playerOwnerIndex == -1)  continue;
                else if (isOpponentRegion(region)){
                    return distance*10 + i;
                }
            }
            if(current[0] == null && current[1] == null && current[2] == null && current[3] == null && current[4] == null && current[5] == null)
                return 0;
        }
    }

    @Override
    public int nearby(String direction) {
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        Graph.GraphNode node = Graph.instance().getGraph()[crew.positionM][crew.positionN], current, previous;
        for(int i=1; true; i++){
            current = node.getAtDirection(direction);
            if(current != null){
                previous = current;
                peekRegion region = territory.getInfoOfRegion(current.getRow(), current.getCol());
                if(region.playerOwnerIndex == -1)  continue;
                else if (isOpponentRegion(region)){
                    return (100*i)+(int)(Math.floor(Math.log10(region.deposit)));
                }
            } else return 0;
        }
    }

    public void NextTurn() throws  WonException{
        totalTurn++;
        realTurn = (int) Math.round(totalTurn * 1.0/totalPlayers);
        // debug
        System.err.println("Turn : " + realTurn);
        currentPlayer = currentPlayer();
        if(isCurrentPlayerWon())    throw new WonException(String.valueOf(getCurrentPlayerIndex()));
        territory.startTurn(currentPlayer.getCityCrewInfo(), realTurn);
        currentPlayer.startTurn();
    }

    public void actionProcess(Action.FinalActionState currentAction) throws DoneExecuteException, WonException{
        String key = currentAction.getAction();
        String direction = currentAction.getDirection();
        int value = currentAction.getValue();
        switch (key) {
            case "done" -> done();
            case "relocate" -> relocate();
            case "move" -> move(direction);
            case "invest" -> invest(value);
            case "collect" -> collect(value);
            case "shoot" -> shoot(value, direction);
        }
        if(currentPlayer.isDefeat())  throw new DoneExecuteException();
        if(isCurrentPlayerWon())    throw new WonException(String.valueOf(getCurrentPlayerIndex()));
    }

    @Override
    public player currentPlayer() {
        return players.get(getCurrentPlayerIndex());
    }

    public int getCurrentPlayerIndex(){
        return (totalTurn-1) % totalPlayers;
    }

    public void done() throws DoneExecuteException {
        throw new DoneExecuteException("Done");
    }

    public void relocate() throws DoneExecuteException{
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        int curM = currentPlayer.getCityCenterPositionM();
        int curN = currentPlayer.getCityCenterPositionN();
        // need distance calculation
        double distance = Graph.instance().findShortestDistance(curM, curN, crew.positionM, crew.positionN);
        double cost = 5*distance+10;
        if(!isOwnerRegion(crew) || cost > currentPlayer.budget()) done();
        else {
            territory.relocate(crew, territory.getInfoOfRegion(curM, curN), territory.getInfoOfRegion(crew.positionM, crew.positionN));
            currentPlayer.relocate();
            currentPlayer.spend(cost);
        }
    }

    public void move(String direction) throws DoneExecuteException{
        if(currentPlayer.budget() < 1)  done();
        currentPlayer.spend(1);

        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        int oldM = crew.positionM, oldN = crew.positionN;

        Graph.GraphNode node = Graph.instance().getGraph()[crew.positionM][crew.positionN].getAtDirection(direction);
        if(node == null) return;

        peekRegion destinationRegion = territory.getCurrentRegionInfo(node.getRow(), node.getCol());
        if (isOpponentRegion(destinationRegion))    return;
        else  {
            currentPlayer.moveCrew(destinationRegion);
            //debug
            System.err.println("citycrew from (" + oldM + ", " + oldN + ") to (" + currentPlayer.getCityCrewInfo().positionM + ", " + currentPlayer.getCityCrewInfo().positionN + ")");
        }
    }

    public void invest(double amount) throws DoneExecuteException{
        if(currentPlayer.budget() < 1) done();
        currentPlayer.spend(1);
        if(amount <= 0)  return;
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        peekRegion region = territory.getCurrentRegionInfo(crew);

        if(territory.isInBound(region) && !isOpponentRegion(region) && currentPlayer.budget() >= amount) {
            double gap = configVals.get("max_dep") - region.deposit;
            if(gap >= amount){
                currentPlayer.spend(amount);
                territory.invest(crew, region, amount);
            } else {
                currentPlayer.spend(gap);
                territory.invest(crew, region, gap);
            }
        }
    }

    public void collect(double amount) throws DoneExecuteException{
        if(currentPlayer.budget() < 1) done();
        currentPlayer.spend(1);
        if(amount <= 0)  return;
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        peekRegion region = territory.getInfoOfRegion(crew.positionM, crew.positionN);
//        System.out.println(isOwnerRegion(region));

        if(territory.isInBound(region) && isOwnerRegion(region) && region.deposit >= amount) {
            territory.collect(crew, amount);
            currentPlayer.receiveDeposit(amount);
            if(region.deposit == amount && currentPlayer.lostRegion(region).equals("defeat")){
                territory.clearOwnerRegionOf(crew);
            }
        }
    }

    public void shoot(double amount, String direction) throws DoneExecuteException{
        if(currentPlayer.budget() < 1) done();
        currentPlayer.spend(1);
        if(amount <= 0) return;
        if(currentPlayer.budget() < amount) return;
        currentPlayer.spend(amount);
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();

        Graph.GraphNode node = Graph.instance().getGraph()[crew.positionM][crew.positionN].getAtDirection(direction);
        if(node == null) return;

        peekRegion target = territory.getInfoOfRegion(node.getRow(), node.getCol());
        if(territory.shoot(target, amount).equals("lostRegion")){
            player targetPlayer = players.get(target.playerOwnerIndex);
            if(targetPlayer.lostRegion(target).equals("defeat")){
                // need clear owner region
                territory.clearOwnerRegionOf(targetPlayer.getCityCrewInfo());
            }
        }
    }

    private boolean isCurrentPlayerWon(){
        int count = 0;
        for (player player : players){
            if ((player != currentPlayer && player.isDefeat()) || (player == currentPlayer && !player.isDefeat())){
                count++;
            }
        }
        return players.size() == count;
    }

    public boolean changePlan(){
        if(currentPlayer.budget() < configVals.get("rev_cost"))
            return false;
        currentPlayer.spend(configVals.get("rev_cost"));
        return true;
    }

    public DisplayPlayer[] getAllPlayers(){
        DisplayPlayer[] result = new DisplayPlayer[players.size()];
        for(int i=0; i<players.size(); i++){
            result[i] = players.get(i).getDisplay();
        }
        return result;
    }

    public DisplayPlayer getCurrentPlayer(){
        return currentPlayer.getDisplay();
    }

    public DisplayRegion[][] getAllTerritory(){
        return territory.getAllRegion();
    }

    public DisplayRegion[][] getCurrentTerritory(){
        return territory.getAllRegion(currentPlayer.getCityCrewInfo());
    }
}