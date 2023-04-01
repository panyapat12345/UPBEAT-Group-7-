package GameProcess.Display;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
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
