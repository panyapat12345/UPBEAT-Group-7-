package GameProcess;

import java.util.Set;

public class player implements playerInterface {
    private int playerIndex = -1;
    private int cityCenterPositionM ;
    private int cityCenterPositionN;
    private Double budget;
    private cityCrew crew = null;
    private String status = "alive";
    public Set<peekRegion> ownRegions;

    public player(int Id, int cityCenterPositionM, int cityCenterPositionN, double budget){
        playerIndex = Id;
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

    public void reciveDeposit(double amount) { budget+=amount; }

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