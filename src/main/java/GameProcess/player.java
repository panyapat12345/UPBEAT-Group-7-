package GameProcess;

import GameProcess.Display.DisplayPlayer;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
public class player {
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
        newCityCrew();
    }

    public void startTurn() {
        crew.startTurn(cityCenterPositionM, cityCenterPositionN);
    }

    public boolean isDefeat() {
        return status.equals("defeat");
    }

    public int budget() {
        return budget.intValue();
    }

    public void spend(double amount) { budget -= amount; }

    public void receiveDeposit(double amount) { budget += amount; }

    public peekCiryCrew getCityCrewInfo() {
        return crew.getCityCrewInfo();
    }

    public void newCityCrew(){
        crew = new cityCrew(playerIndex, cityCenterPositionM, cityCenterPositionN);
    }

    public void relocate(){
        peekCiryCrew crew = this.crew.getCityCrewInfo();
        cityCenterPositionM = crew.positionM;
        cityCenterPositionN = crew.positionN;
    }

    public void moveCrew(peekRegion destinationRegion) {
        crew.move(destinationRegion);
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

    public DisplayPlayer getDisplay(){
        return new DisplayPlayer(playerIndex, cityCenterPositionM, cityCenterPositionN, budget.intValue(), crew.getDisplay(), status);
    }
}