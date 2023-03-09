package GameProcess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

interface playerInterface {
    boolean isDefeat();
    int budget();
    peekCiryCrew getCityCrewInfo();
}

interface cityCrewInterface {
}

class player implements playerInterface {
    private int playerIndex = -1;
    private HashMap<String, Double> variablesOfConstructionPlan;
    private int cityCenterPositionM ;
    private int cityCenterPositionN;
    private int playerPositionM;
    private int playerPositionN;
    private Double budget = 100000.0;
    private cityCrew crew = null;
    private String status = "alive";
    public Set<peekRegion> ownRegions;

    player(int Id, int cityCenterPositionM, int cityCenterPositionN){
        playerIndex = Id;
        variablesOfConstructionPlan = new HashMap<>();
        this.cityCenterPositionM = cityCenterPositionM;
        this.cityCenterPositionN = cityCenterPositionN;
        startTurn();
    }

    // new method
    public void startTurn() {
        playerPositionM = cityCenterPositionM;
        playerPositionN = cityCenterPositionN;
    }

    @Override
    public boolean isDefeat() {
        return !status.equals("alive");
    }

    @Override
    public int budget() {
        return budget.intValue();
    }

    public void spend(double amount) { budget-=amount; }

    @Override
    public peekCiryCrew getCityCrewInfo() {
        return crew.getCityCrewInfo();
    }

    public void newCityCrew(){
        crew = new cityCrew(playerIndex, cityCenterPositionM, cityCenterPositionN, budget);
    }

    public void moveCrew(peekRegion interestRegion) {
        crew.move(interestRegion);
    }

    public void reciveDeposit(Double amount) { budget+=amount; }

    public Double getConstructionPlanVarible(String name){
        return variablesOfConstructionPlan.get(name);
    }

    public void setConstructionPlanVarible(String name, Double var){
        variablesOfConstructionPlan.put(name, var);
    }

    public String lostRegion(peekRegion region){
        ownRegions.remove(region);
        if(region.Type == "cityCenter") this.status = "defeat";
        return "caseDefeat";
    }

    public Set<peekRegion> getOwnRegions(){
        return ownRegions;
    }

    // new method
    public int getPlayerPositionM() { return playerPositionM; }
    public void setPlayerPositionM(int m) {
        // need location checking
        playerPositionM = m;
    }

    public int getPlayerPositionN() { return playerPositionN; }
    public void setPlayerPositionN(int n) {
        // need location checking
        playerPositionN = n;
    }
}

class cityCrew{
    private int crewOfPlayer = -1;
    private int positionM = -1;
    private int positionN = -1;
    private Double budGet = -1.0;

    cityCrew(int playerIndex, int PositionN, int PositionM, Double budGet) {
        this.crewOfPlayer = playerIndex;
        this.positionM = PositionM;
        this.positionN = PositionN;
        this.budGet = budGet;
    }

    // for relocate!
    public void move(peekRegion target){
        this.positionM = target.positionM;
        this.positionN = target.positionN;
    }

    public peekCiryCrew getCityCrewInfo(){
        return new peekCiryCrew(crewOfPlayer, positionM, positionN, budGet);
    }
}

class peekCiryCrew implements cityCrewInterface{
    public int crewOfPlayer;
    public int positionM;
    public int positionN;
    public Double budGet;

    public peekCiryCrew(int crewOfPlayer, int positionM, int positionN, Double budGet){
        this.crewOfPlayer = crewOfPlayer;
        this.positionM = positionM;
        this.positionN = positionN;
        this.budGet = budGet;
    }
}