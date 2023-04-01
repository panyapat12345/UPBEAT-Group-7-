package GameProcess.Display;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DisplayCityCrew {
    private int posM;
    private int posN;

    public DisplayCityCrew(int posM, int posN){
        this.posM = posM;
        this.posN = posN;
    }
}
