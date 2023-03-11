package GameProcess;
import AST.Action;
import AST.PlanTree;
import AST.Tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import Exception.DoneExecuteException;

public class internalOperator implements internalOperatorInterface {
    private static internalOperator instance;
    HashMap<String, Double> variables;
    private Territory territory;
    private LinkedList<player> players = new LinkedList<>();
    private LinkedList<Tree> constructionPlans = new LinkedList<>();
    private int totalPlayers = 0;
    private int totalTurn = 0;
    private int realTurn = 0;
    private player currentPlayer = null;

    public internalOperator(HashMap<String, Double> variables) {
        this.variables = variables;
        territory = new Territory(variables);
        instance = this;
        /*
        StringBuilder constructionPlan = new StringBuilder();
        FileReader constructionPlanReader = null;
        try {
            constructionPlanReader = new FileReader("src/ConstructionPlan.txt");
            int i;
            while((i = constructionPlanReader.read()) != -1) constructionPlan.append((char)i);
            constructionPlanReader.close();
        }
        catch (Exception e) { e.printStackTrace(); }

        addPlayer(constructionPlan.toString());
        */
    }

    public static internalOperator instance(){
        return instance;
    }

    public void addPlayer(String constructionPlan) {
        int cityCenterPositionM = 50, cityCenterPositionN = 50;
        player newPlayer = new player(totalPlayers, cityCenterPositionM, cityCenterPositionN, variables.get("init_budget"));
        totalPlayers++;
        newPlayer.newCityCrew();
        territory.newCityCenter(newPlayer.getCityCrewInfo());
        players.add(newPlayer);
        Tree tree = null;
        try{ tree = new PlanTree(constructionPlan); }
        catch (Exception e) {
            System.out.println(e);
        }
        constructionPlans.add(tree);
        if(totalPlayers == 1) currentPlayer = currentPlayer();
    }

    public HashMap<String, Double> GetVariables(){ return variables; }
    public int rows() { return variables.get("m").intValue(); }
    public int cols() { return variables.get("n").intValue(); }

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
        return variables.get("max_dep").intValue();
    }

    public int random() { return ThreadLocalRandom.current().nextInt(0, 999); }

    private boolean isRegionOfOpponent(int m, int n){
        int currentPlayer = totalTurn %totalPlayers;
        peekRegion peek = territory.getInfoOfRegion(m, n);
/*
        // out of scope
        if(peek.Type.equals("null"));
        // no owner
        else if(peek.playerOwnerIndex == -1);
        else if(peek.playerOwnerIndex != currentPlayer) return true;
*/
        if(!peek.Type.equals("null") && !(peek.playerOwnerIndex == -1) && peek.playerOwnerIndex != currentPlayer)
            return true;
        return false;
    }

    private boolean isRegionOfOpponent(peekRegion region){
        return isRegionOfOpponent(region.positionM, region.positionN);
    }

    private boolean isRegionOfOpponent(peekCiryCrew crew){
        return isRegionOfOpponent(crew.positionM, crew.positionN);
    }

    @Override
    public int opponent() {
        int interestM = currentPlayer().getCityCrewInfo().positionM;
        int interestN = currentPlayer().getCityCrewInfo().positionM;
        int nearestRegion = 0;
        int distance = Integer.MAX_VALUE;
        peekRegion current;

        for(int i = 1; i <= 6; i++){
            territoryDirectionIterator itr = territory.getTerritoryDirectionIterator(i, interestM, interestN);
            for(int j = 0; true; j++){
                current = itr.next();
                if(current.Type.equals("null")) break;
                else if(isRegionOfOpponent(current.positionM, current.positionN)) {
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
    public int nearby(String direction) {
        return nearby(stringDirToInt(direction));
    }

    public int nearby(int direction) {
        int interestM = currentPlayer().getCityCrewInfo().positionM;
        int interestN = currentPlayer().getCityCrewInfo().positionM;
        territoryDirectionIterator itr = territory.getTerritoryDirectionIterator(direction, interestM, interestN);
        peekRegion current;
        for(int i = 0; true; i++){
            current = itr.next();
            if(current.Type.equals("null")) break;
            else if(isRegionOfOpponent(current.positionM, current.positionN)) {
                return (100*i)+(int)(Math.floor(Math.log10(territory.getInfoOfRegion(interestM, interestN).deposit)));
            }
        }
        return 0;
    }

    public void NextTurn(){
        totalTurn++;
        realTurn = (int) Math.round(totalTurn * 1.0/totalPlayers);
        // debug
        System.err.println("Turn : " + realTurn);
        currentPlayer = currentPlayer();
        if(currentPlayer().isDefeat())  return;
        territory.nextTurn(currentPlayer.getCityCrewInfo(), realTurn);
        currentPlayer.startTurn();

        Iterator<Action.FinalActionState> currentPlan =  constructionPlans.get(totalTurn %totalPlayers).iteratorRealTime();
        Action.FinalActionState currentAction;
/*
        while(currentPlan.hasNext()){
            currentAction = currentPlan.next();
            try {
                System.out.println(currentAction);
                actionProcess(currentAction);
            } catch (DoneExecuteException e) {
                return;
            }
        }
*/
        while(true){
            currentAction = currentPlan.next();
            if (currentAction == null) break;
            try {
                System.out.println(currentAction);
                actionProcess(currentAction);
            } catch (DoneExecuteException e) {
                return;
            }
        }
    }

    public void actionProcess(Action.FinalActionState currentAction) throws DoneExecuteException{
        String key = currentAction.getAction();
        String direction = currentAction.getDirection();
        int directionInt = stringDirToInt(direction);
        int value = currentAction.getValue();
        switch (key) {
            case "done" -> done();
            case "relocate" -> relocate();
            case "move" -> move(directionInt);
            case "invest" -> invest((double) value);
            case "collect" -> collect((double) value);
            case "shoot" -> shoot((double) value, directionInt);
        }
    }

    private int stringDirToInt(String direction){
        int directionInt;
        switch (direction) {
            case"up" -> directionInt = 4;
            case"upright" -> directionInt = 2;
            case"downright" -> directionInt = 3;
            case"down" -> directionInt = 1;
            case"downleft" -> directionInt = 5;
            case"upleft" -> directionInt = 6;
            default -> directionInt = 0;
        }
        return directionInt;
    }

    @Override
    public player currentPlayer() {
        return players.get(totalTurn %totalPlayers);
    }

    public void done() throws DoneExecuteException {
        throw new DoneExecuteException("Done");
    }

    public void relocate() throws DoneExecuteException{
        peekCiryCrew currentCrew = currentPlayer.getCityCrewInfo();
        int cityM = currentPlayer.getCityCenterPositionM();
        int cityN = currentPlayer.getCityCenterPositionN();
        int cityMDistance = cityM-currentCrew.positionM;
        int cityNDistance = cityN-currentCrew.positionN;
        // need distance calculation
        double distance = Math.round(Math.sqrt(Math.pow(cityMDistance, 2)+Math.pow(cityNDistance, 2)));
        double cost = 5*distance+10;
        if(isRegionOfOpponent(currentCrew) || cost > currentPlayer.budget()) done();
        else {
            territory.relocate(currentCrew , territory.getInfoOfRegion(cityM, cityN), territory.getInfoOfRegion(currentCrew.positionM, currentCrew.positionN));
            currentPlayer.relocate();
            currentPlayer.spend(cost);
        }
    }

    public void move(int direction) throws DoneExecuteException{
        int interestM = currentPlayer().getCityCrewInfo().positionM;
        int interestN = currentPlayer().getCityCrewInfo().positionN;
        int oldM = interestM, oldN = interestN;
        switch (direction){
            case (1) -> interestM++;
            case (2) -> {
                interestN++;
                interestM-=interestN%2;
            }
            case (3) -> {
                interestN++;
                interestM+=(interestN+1)%2;
            }
            case (4) -> interestM--;
            case (5) -> {
                interestN--;
                interestM+=(interestN+1)%2;
            }
            case (6) -> {
                interestN--;
                interestM-=interestN%2;
            }
        }
        peekRegion interestRegion = territory.getCurrentRegionInfo(interestM, interestN);

        if(!territory.isInBound(interestRegion) || isRegionOfOpponent(interestRegion)) return;
        else if (currentPlayer.budget() > 0.0) {
            currentPlayer.moveCrew(interestRegion);
            //debug
            System.err.println("citycrew from (" + oldM + ", " + oldN + ") to (" + currentPlayer.getCityCrewInfo().positionM + ", " + currentPlayer.getCityCrewInfo().positionN + ")");
        }
        else done();
    }

    public void invest(Double amount) throws DoneExecuteException{
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        peekRegion region = territory.getCurrentRegionInfo(crew);
        if(currentPlayer.budget() < 1) done();
        if(territory.isInBound(region) && !isRegionOfOpponent(region) && currentPlayer.budget() >= amount+1) {
            currentPlayer.spend(amount);
            territory.invest(crew, region, amount);
        }
        currentPlayer.spend(1);
    }

    public void collect(Double amount) throws DoneExecuteException{
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        peekRegion region = territory.getInfoOfRegion(crew.positionM, crew.positionN);
        if(currentPlayer.budget() < 1) done();
        else if (region.deposit >= amount) {
            territory.collect(crew, amount);
            currentPlayer.reciveDeposit(amount);
        }
        currentPlayer.spend(1);
    }

    public void shoot(Double amount, int direction){
        if(amount+1 > currentPlayer.budget()) return;
        int interestM = currentPlayer().getCityCrewInfo().positionM;
        int interestN = currentPlayer().getCityCrewInfo().positionM;
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
        currentPlayer.spend(amount+1);
        peekRegion target = territory.getInfoOfRegion(interestM, interestN);
        if(territory.shoot(target, amount).equals("lostRegion")){
            player targetPlayer = players.get(target.playerOwnerIndex);
            if(targetPlayer.lostRegion(target).equals("defeat")){
                // need clear owner region
                territory.clearOwnerRegionOf(targetPlayer.getCityCrewInfo());
//                peekCiryCrew ghostCrew = new peekCiryCrew(-1, -1, -1);
//                Iterator<peekRegion> regions =  targetPlayer.getOwnRegions().iterator();
//                while(regions.hasNext()){
//                    peekRegion current = regions.next();
//                    ghostCrew.positionM = current.positionM;
//                    ghostCrew.positionN = current.positionN;
//                    territory.takeRegion(ghostCrew);
//                }
            }
        }
    }

    public void takeRegion(){
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        peekRegion region = territory.getInfoOfRegion(crew.positionM, crew.positionN);
        if(region.playerOwnerIndex == -1) territory.takeRegion(crew);
    }
}



