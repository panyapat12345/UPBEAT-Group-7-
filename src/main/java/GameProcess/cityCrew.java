package GameProcess;

import GameProcess.Display.DisplayCityCrew;
import org.springframework.stereotype.Component;

@Component
public class cityCrew{
    private int crewOfPlayer = -1;
    private int positionM = -1;
    private int positionN = -1;

    public cityCrew(int playerIndex, int PositionM, int PositionN) {
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

    public DisplayCityCrew getDisplay(){
        return new DisplayCityCrew(positionM, positionN);
    }
}
