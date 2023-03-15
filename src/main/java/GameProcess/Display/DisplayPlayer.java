package GameProcess.Display;

public class DisplayPlayer {
    private int playerIndex;
    private int cityCenterPosM;
    private int cityCenterPosN;
    private int budget;
    private DisplayCityCrew crew;
    private String status;

    public DisplayPlayer(int playerIndex, int cityCenterPosM, int cityCenterPosN, int budget, DisplayCityCrew crew, String status){
        this.playerIndex = playerIndex;
        this.cityCenterPosM = cityCenterPosM;
        this.cityCenterPosN = cityCenterPosN;
        this.budget = budget;
        this.crew = crew;
        this.status = status;
    }
}
