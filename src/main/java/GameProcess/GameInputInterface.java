package GameProcess;

public interface GameInputInterface{
    //This is Special variables
    public int row();
    public int cols();
    public int currow();
    public int curcol();
    public int budget();
    public int deposit();
    public int interest();
    public int maxdeposit();
    public int random();

    //This is Action commands
    public void done();
    public void relocate();
    public void move();
    public void invest();
    public void collect();
    public void shoot();
}