package GameProcess.Display;

import AST.Action;

public class DisplayGameSystem {
    private String status;
    private Action.FinalActionState action;
    private DisplayPlayer[] players;
    private DisplayRegion[][] territory;

    public DisplayGameSystem(String status,Action.FinalActionState action, DisplayPlayer[] players, DisplayRegion[][] territory){
        this.status = status;
        this.action = action;
        this.players = players;
        this.territory = territory;
    }
}
