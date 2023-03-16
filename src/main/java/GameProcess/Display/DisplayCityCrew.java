package GameProcess.Display;

import org.springframework.stereotype.Component;

@Component
public class DisplayCityCrew {
    private int posM;
    private int posN;

    public DisplayCityCrew(int posM, int posN){
        this.posM = posM;
        this.posN = posN;
    }
}
