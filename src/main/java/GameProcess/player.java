package GameProcess;

import java.util.HashMap;

interface playerInterface {
    void isDefeat();
    int budget();
    peekCiryCrew getCityCrewInfo();
}

interface cityCrewInterface {
}

class player implements playerInterface, territoryInterface {
    private int playerIndex;
    private HashMap<String, Double> variablesOfConstructionPlan;
    private int cityCenterPositionM;
    private int cityCenterPositionN;
    private Double budget;
    private cityCrew crew = null;

    player(int Id){
        playerIndex = Id;
        variablesOfConstructionPlan = new HashMap<>();
    }

    @Override
    public void isDefeat() {

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

    @Override
    public peekRegion getCurrentRegionInfo(int m, int n) {
        return null;
    }

    @Override
    public void attackRegion(player player, region Target) {

    }

    @Override
    public void move(peekRegion target) {

    }

    @Override
    public void invest(player player, region region) {

    }

    @Override
    public void collect(player player, region region) {

    }

    @Override
    public void shoot(player player, int direction, region region) {

    }

    @Override
    public void relocate(region From, region To) {

    }

    @Override
    public int opponent(region currentRegion) {
        return 0;
    }

    @Override
    public int nearby(cityCrew cityCrew) {
        return 0;
    }
}

class cityCrew{
    private int crewOfPlayer;
    private int positionM;
    private int positionN;
    private Double budGet;

    cityCrew(int playerIndex, int PositionN, int PositionM, Double budGet) {
        this.crewOfPlayer = playerIndex;
        this.positionM = PositionM;
        this.positionN = PositionN;
        this.budGet = budGet;
    }

    public void move(peekRegion target){
        this.positionM = target.PositionM;
        this.positionN = target.PositionN;
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