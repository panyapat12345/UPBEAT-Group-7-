package GameProcess;
import AST.Action;
import AST.PlanTree;
import AST.Tree;

import java.io.FileReader;
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
    private int turn = 0;
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
        player newPlayer = new player(totalPlayers, 50, 50);
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
        return currentPlayer.getPlayerPositionM();
    }

    @Override
    public int curcol() {
        return currentPlayer.getPlayerPositionN();
    }

    @Override
    public int budget() {
        return currentPlayer.budget();
    }

    @Override
    public int deposit() {
        peekCiryCrew crew = currentPlayer().getCityCrewInfo();
        return territory.getCurrentRegionInfo(crew.positionM, crew.positionN).deposit.intValue();
    }

    @Override
    public int interest() {
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        return territory.getCurrentRegionInfo(crew).interestRate.intValue();
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
        else if(peek.playerOwnerIndex == -1);
        else if(peek.playerOwnerIndex != currentPlayer) return true;

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
        territory.nextTurn();
        turn++;
        while(currentPlayer().isDefeat()){ turn++; }
        currentPlayer = currentPlayer();
        currentPlayer.startTurn();
        Iterator<Action.FinalActionState> currentPlan =  constructionPlans.get(turn%totalPlayers).iteratorRealTime();
        Action.FinalActionState currentAction;

        while(currentPlan.hasNext()){
            currentAction = currentPlan.next();
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
        int directionInt;
        switch (direction) {
            case"up" -> directionInt = 1;
            case"upright" -> directionInt = 2;
            case"downright" -> directionInt = 3;
            case"down" -> directionInt = 4;
            case"downleft" -> directionInt = 5;
            case"upleft" -> directionInt = 6;
            default -> directionInt = 0;
        }
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

    @Override
    public player currentPlayer() {
        return players.get(turn%totalPlayers);
    }

    public void done() throws DoneExecuteException {
        throw new DoneExecuteException("Done");
    }

    public void relocate() throws DoneExecuteException{
        peekCiryCrew currentCrew = currentPlayer.getCityCrewInfo();
        currentPlayer.newCityCrew();
        int cityM = currentPlayer.getCityCrewInfo().positionM;
        int cityN = currentPlayer.getCityCrewInfo().positionN;
        int cityMDistance = cityM-currentCrew.positionM;
        int cityNDistance = cityN-currentCrew.positionN;
        double distance = Math.sqrt(Math.pow(cityMDistance, 2)+Math.pow(cityNDistance, 2));
        double cost = 5*distance+10;
        if(isRegionOfOpponent(currentCrew));
        else if(cost > currentPlayer.budget());
        else{
            territory.relocate(territory.getInfoOfRegion(cityM, cityN), territory.getInfoOfRegion(currentCrew.positionM, currentCrew.positionN));
            currentPlayer.spend(cost);
        }
        done();
    }

    public void move(int direction) throws DoneExecuteException{
//        int interestM = currentPlayer().getCityCrewInfo().positionM;
//        int interestN = currentPlayer().getCityCrewInfo().positionN;
        int interestM = currentPlayer().getPlayerPositionM();
        int interestN = currentPlayer().getPlayerPositionN();
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
//        System.out.println("pre " + interestM + " " + interestN);
        peekRegion interestRegion = territory.getCurrentRegionInfo(interestM, interestN);
//        System.out.println("post " + interestRegion.positionM + " " + interestRegion.positionN);
        if(!territory.isInBound(interestRegion)) {
//            System.out.println("1 " + territory.isInBound(interestRegion));
            return;
        }
        else if(isRegionOfOpponent(interestRegion)) return;
        else if (currentPlayer.budget() > 0.0) {
            currentPlayer.setPlayerPositionM(interestRegion.positionM);
            currentPlayer.setPlayerPositionN(interestRegion.positionN);
            System.out.println("Player " + currentPlayer.getPlayerPositionM() + " " + currentPlayer.getPlayerPositionN());
//            currentPlayer.moveCrew(interestRegion);
        }
        else done();
    }

    public void invest(Double amount){
        peekRegion interestRegion = territory.getCurrentRegionInfo(currentPlayer.getCityCrewInfo());
        if(!territory.isInBound(interestRegion));
        else if(isRegionOfOpponent(interestRegion));
        else if(amount > currentPlayer.budget());
        else{
            currentPlayer.spend(amount);
            territory.invest(interestRegion, amount);
        }
    }

    public void collect(Double amount) throws DoneExecuteException{
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        peekRegion region = territory.getInfoOfRegion(crew.positionM, crew.positionN);
        if(currentPlayer.budget() < 1) done();
        else if (region.deposit < amount);
        else{
            territory.collect(crew, amount);
            currentPlayer.reciveDeposit(amount);
        }
    }

    public void shoot(Double amount, int direction){
        if(currentPlayer.budget() > amount+1) return;
        currentPlayer.spend(1);
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
        currentPlayer.spend(amount);
        peekRegion target = territory.getInfoOfRegion(interestM, interestN);
        if(territory.shoot(target, amount).equals("lostRegion")){
            player targetPlayer = players.get(target.playerOwnerIndex);
            if(targetPlayer.lostRegion(target).equals("caseDefeat")){
                peekCiryCrew ghostCrew = new peekCiryCrew(-1, -1, -1, -1.0);
                Iterator<peekRegion> regions =  targetPlayer.getOwnRegions().iterator();
                while(regions.hasNext()){
                    peekRegion current = regions.next();
                    ghostCrew.positionM = current.positionM;
                    ghostCrew.positionN = current.positionN;
                    territory.takeRegion(ghostCrew);
                }
            }
        }
    }

    public void takeRegion(){
        peekCiryCrew crew = currentPlayer.getCityCrewInfo();
        peekRegion region = territory.getInfoOfRegion(crew.positionM, crew.positionN);
        if(region.playerOwnerIndex == -1) territory.takeRegion(crew);
    }
}



