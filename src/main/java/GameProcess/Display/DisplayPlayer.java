package GameProcess.Display;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(playerIndex);
        s.append(" (");
        s.append(cityCenterPosM);
        s.append(", ");
        s.append(cityCenterPosN);
        s.append(") ");
        s.append(budget);
        s.append(" ");
        s.append(status);
        s.append(" ");
        s.append(crew);
        return s.toString();
    }
}
