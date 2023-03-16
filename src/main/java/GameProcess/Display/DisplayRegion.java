package GameProcess.Display;

import org.springframework.stereotype.Component;

@Component
public class DisplayRegion {
    private int playerOwnerIndex;
    private String type;
    private Double deposit;

    public DisplayRegion(int playerOwnerIndex, String type, double deposit){
        this.playerOwnerIndex = playerOwnerIndex;
        this.type = type;
        this.deposit = deposit;
    }
}
