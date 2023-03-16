package GameProcess;

import org.springframework.stereotype.Component;

@Component
public class peekCiryCrew {
    public int crewOfPlayer;
    public int positionM;
    public int positionN;

    public peekCiryCrew(int crewOfPlayer, int positionM, int positionN){
        this.crewOfPlayer = crewOfPlayer;
        this.positionM = positionM;
        this.positionN = positionN;
    }
}
