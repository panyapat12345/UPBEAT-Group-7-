package GameProcess;

import java.util.HashMap;

interface playerInterface {
    boolean isDefeat();
    int budget();
    peekCiryCrew getCityCrewInfo();
}

interface cityCrewInterface {
}

class player implements playerInterface {
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
    public boolean isDefeat() {
        return budget <= 0.0;
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