package GameProcess;

import java.util.HashMap;
import java.util.Set;

interface playerInterface {
    boolean isDefeat();
    int budget();
    peekCiryCrew getCityCrewInfo();
}

interface cityCrewInterface {
}

public class player implements playerInterface {
    private int playerIndex = -1;
    private HashMap<String, Double> variablesOfConstructionPlan;
    private int cityCenterPositionM ;
    private int cityCenterPositionN;
    private Double budget;
    private cityCrew crew = null;
    private String status = "alive";
    public Set<peekRegion> ownRegions;

    player(int Id, int cityCenterPositionM, int cityCenterPositionN, double budget){
        playerIndex = Id;
        variablesOfConstructionPlan = new HashMap<>();
        this.cityCenterPositionM = cityCenterPositionM;
        this.cityCenterPositionN = cityCenterPositionN;
        this.budget = budget;
    }

    public void startTurn() {
        crew.startTurn(cityCenterPositionM, cityCenterPositionN);
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
        crew = new cityCrew(playerIndex, cityCenterPositionM, cityCenterPositionN);
    }

    public void relocate(){
        cityCenterPositionM = crew.getCityCrewInfo().positionM;
        cityCenterPositionN = crew.getCityCrewInfo().positionN;
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
//        ownRegions.remove(region);
        if(region.Type.equals("cityCenter")) {
            this.status = "defeat";
            System.err.println("lost city center");
        }
        return status;
    }

    public Set<peekRegion> getOwnRegions(){
        return ownRegions;
    }

    public int getCityCenterPositionM(){ return cityCenterPositionM; }

    public int getCityCenterPositionN(){ return cityCenterPositionN; }
}

class cityCrew{
    private int crewOfPlayer = -1;
    private int positionM = -1;
    private int positionN = -1;

    cityCrew(int playerIndex, int PositionN, int PositionM) {
        this.crewOfPlayer = playerIndex;
        this.positionM = PositionM;
        this.positionN = PositionN;
    }

    public void startTurn(int cityCenterPositionM, int cityCenterPositionN) {
        positionM = cityCenterPositionM;
        positionN = cityCenterPositionN;
    }

    public void move(peekRegion target){
        this.positionM = target.positionM;
        this.positionN = target.positionN;
    }

    public peekCiryCrew getCityCrewInfo(){
        return new peekCiryCrew(crewOfPlayer, positionM, positionN);

    }
}

class peekCiryCrew implements cityCrewInterface{
    public int crewOfPlayer;
    public int positionM;
    public int positionN;

    public peekCiryCrew(int crewOfPlayer, int positionM, int positionN){
        this.crewOfPlayer = crewOfPlayer;
        this.positionM = positionM;
        this.positionN = positionN;
    }
}