package GameProcess.Display;

public class DisplayGameSystem {
    private DisplayPlayer[] players;
    private DisplayRegion[][] territory;

    public DisplayGameSystem(DisplayPlayer[] players, DisplayRegion[][] territory){
        this.players = players;
        this.territory = territory;
    }
}
