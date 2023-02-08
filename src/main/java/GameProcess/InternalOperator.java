package GameProcess;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class InternalOperator implements GameInputInterface, OperatorInternalInterface, OperatorOutputInterface {
    public int row();
    public int cols();
    public int currow();
    public int curcol();
    public int budget();
    public int deposit();
    public int interest();
    public int maxdeposit();
    public int random(){ return ThreadLocalRandom.current().nextInt(0, 999 + 1); }

    //This is Action commands
    public void done();
    public void relocate();
    public void move();
    public void invest();
    public void collect();
    public void shoot();
}

class Game extends InternalOperator{
    List<Player> Players = new LinkedList<>();

    Game(){
        File configurator = new File(".src/configurator.txt");

    }
}

class Map extends InternalOperator{
    private int rows;
    private int colums;

    Region[][] regions;
    Map(){
        regions
    }
}

class Region extends InternalOperator{
    private double MaxDeposit;
    private double CurrentDeposit;
    private string structure = "Empty";

    private int GetMaxDeposit(){ return (int)MaxDeposit; }
}

class CityCrew extends InternalOperator{
    private int CurrentRows;
    private int CurrentColums;
}

class Player extends InternalOperator{
    private double budget;

}