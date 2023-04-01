package GameProcess.Display;

import AST.Action;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DisplayGameSystem {
    private int index;
    private String status;
    private Action.FinalActionState action;
    private DisplayPlayer[] players;
    private DisplayRegion[][] territory;

    public DisplayGameSystem(int index, String status,Action.FinalActionState action, DisplayPlayer[] players, DisplayRegion[][] territory){
        this.index = index;
        this.status = status;
        this.action = action;
        this.players = players;
        this.territory = territory;
    }
}
