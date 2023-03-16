package GameProcess;

import GameProcess.Display.DisplayRegion;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.AbstractMap;
import java.util.Map.Entry;
@Component
public class Territory implements territoryInterface {
    private int m = 0;
    private int n = 0;
    private  int turn = 0;
    private region[][] regions = null;
    private HashMap<String, Double> configVals = null;

    public Territory(HashMap<String, Double> configVals) {
        this.m = configVals.get("m").intValue();
        this.n = configVals.get("n").intValue();
        regions = new region[m][n];
        this.configVals = configVals;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                regions[i][j] = new region(m, n, configVals.get("max_dep"), configVals.get("interest_pct"));
            }
        }
    }

    public boolean isInBound(int M, int N) {
        return ((M >= 0 && N >= 0) && (M < m && N < n));
    }

    public boolean isInBound(peekRegion region) {
        return isInBound(region.positionM, region.positionN);
    }

    public peekRegion getInfoOfRegion(int M, int N) {
        if(isInBound(M, N)) return regions[M][N].getInfo(M, N, turn);
        else return new peekRegion(-1, 0.0, 0.0, 0.0, "null", -1, -1);
    }

    public void collect(peekCiryCrew crew, double amount){
        int oldDeposit = regions[crew.positionM][crew.positionN].deposit();
        if(regions[crew.positionM][crew.positionN].deposit() == amount){
            // if it is city center ?
            regions[crew.positionM][crew.positionN].returnOwner();
        }
        regions[crew.positionM][crew.positionN].takeDeposit(amount);
        // debug
        System.err.println("(" + crew.positionM + ", " + crew.positionN + ") deposit form " + oldDeposit + " to " + regions[crew.positionM][crew.positionN].deposit());
    }

    public String shoot(peekRegion region, double amount){
        // debug
        double oldDeposit = regions[region.positionM][region.positionN].deposit();
        System.err.println("shoot (" + region.positionM + ", " + region.positionN + ") from " + (int) oldDeposit + " to " + (int) (oldDeposit - amount));

        return regions[region.positionM][region.positionN].beAttack(amount);
    }

    public void takeRegion(peekCiryCrew crew){ regions[crew.positionM][crew.positionN].take(crew); }

    public void newCityCenter(peekCiryCrew crew){
        regions[crew.positionM][crew.positionN].newCityCenter(crew, configVals.get("init_center_dep"));
    }

    public territoryDirectionIterator getTerritoryDirectionIterator(int direction, int interestM, int interestN) {
        List<peekRegion> listRegion = new LinkedList<>();
        for(int i = 0; true; i++){
            switch (direction){
                case (1) -> interestM++;
                case (2) -> {
                    interestN++;
                    interestM-=interestN%2;
                }
                case (3) -> {
                    interestN++;
                    interestM+=interestN%2;
                }
                case (4) -> interestM--;
                case (5) -> {
                    interestN--;
                    interestM+=interestN%2;
                }
                case (6) -> {
                    interestN--;
                    interestM-=interestN%2;
                }
            }
            listRegion.add(getCurrentRegionInfo(interestM, interestN));
            if(listRegion.get(i).Type.equals("null")) break;
        }

        return new territoryDirectionIterator(listRegion);
    }

    public void relocate(peekCiryCrew crew, peekRegion from, peekRegion to){
        regions[to.positionM][to.positionN].returnOwner();
        regions[to.positionM][to.positionN].newCityCenter(crew, regions[to.positionM][to.positionN].deposit());
        regions[from.positionM][from.positionN].returnOwner();
        regions[from.positionM][from.positionN].take(crew);
        // debug
        System.err.println("relocate city center from (" + from.positionM + ", " + from.positionN + ") to (" + to.positionM + ", " +to.positionN + ")");
    }

    @Override
    public peekRegion getCurrentRegionInfo(int m, int n) {
        return regions[m][n].getInfo(m, n, turn);
    }

    public peekRegion getCurrentRegionInfo(peekCiryCrew crew) {
        return getCurrentRegionInfo(crew.positionM, crew.positionN);
    }

    public void invest(peekCiryCrew crew, peekRegion region, double amount) {
        int oldDeposit = regions[crew.positionM][crew.positionN].deposit();
        if(region.playerOwnerIndex == -1)   takeRegion(crew);
        regions[region.positionM][region.positionN].addDeposit(amount);
        // debug
        System.err.println("(" + region.positionM + ", " + region.positionN + ") deposit from " + oldDeposit + " to " + regions[region.positionM][region.positionN].deposit());
    }

    public void startTurn(peekCiryCrew crew, int turn){
        this.turn = turn;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(regions[i][j].getOwner() == crew.crewOfPlayer)
                    regions[i][j].calculateInterest(turn);
            }
        }
    }

    public void clearOwnerRegionOf(peekCiryCrew crew){
        // debug
        List<Entry<Integer, Integer>> returnOwner = new LinkedList<>();
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(regions[i][j].getOwner() == crew.crewOfPlayer){
                    regions[i][j].returnOwner();
                    // debug
                    returnOwner.add(new AbstractMap.SimpleEntry<>(i, j));
                }
            }
        }
        // debug
        System.err.println("return owner : " + returnOwner);
    }

    public DisplayRegion[][] getAllRegion(){
        DisplayRegion[][] result = new DisplayRegion[m][n];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(regions[i][j].getOwner() != -1)
                    result[i][j] = regions[i][j].getDisplay();
                else
                    result[i][j] = null;
            }
        }
        return result;
    }

    public DisplayRegion[][] getAllRegion(peekCiryCrew crew){
        DisplayRegion[][] result = new DisplayRegion[m][n];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(regions[i][j].getOwner() == crew.crewOfPlayer)
                    result[i][j] = regions[i][j].getDisplay();
                else
                    result[i][j] = null;
            }
        }
        return result;
    }
}