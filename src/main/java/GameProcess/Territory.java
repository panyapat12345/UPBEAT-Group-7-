package GameProcess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.AbstractMap;
import java.util.Map.Entry;

interface territoryInterface {
    peekRegion getCurrentRegionInfo(int m, int n);
    boolean isInBound(int M, int N);
    peekRegion getInfoOfRegion(int M, int N);
    String shoot(peekRegion region, Double amount);
    territoryDirectionIterator getTerritoryDirectionIterator(int direction, int interestM, int interestN);
    peekRegion getCurrentRegionInfo(peekCiryCrew crew);
    void invest(peekCiryCrew crew, peekRegion region, Double amount);
}

interface RegionInterface{

}

class territoryDirectionIterator{
    List<peekRegion> listRegion = null;
    int index = 0;

    territoryDirectionIterator(List<peekRegion> listRegion) {
        this.listRegion = listRegion;
    }

    peekRegion next(){
        if(index < listRegion.size()){
            peekRegion current = listRegion.get(index);
            index++;
            return current;
        }
        else return new peekRegion(-1, 0.0, 0.0, 0.0, "null", -1, -1);
    }
}

class Territory implements territoryInterface {
    private int m = 0;
    private int n = 0;
    private  int turn = 0;
    private region[][] regions = null;
    HashMap<String, Double> Variables = null;

    Territory(HashMap<String, Double> Variables) {
        this.m = Variables.get("m").intValue();
        this.n = Variables.get("n").intValue();
        regions = new region[m][n];
        this.Variables = Variables;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                regions[i][j] = new region(m, n, Variables.get("max_dep"), Variables.get("interest_pct"));
            }
        }
    }

    public boolean isInBound(int M, int N) {
        return ((M >= 0 && N >= 0) && (M < m && N < n));
    }

    public boolean isInBound(peekRegion target) {
        return isInBound(target.positionM, target.positionN);
    }

    public peekRegion getInfoOfRegion(int M, int N) {
        if(isInBound(M, N)) return regions[M][N].getInfo(M, N, turn);
        else return new peekRegion(-1, 0.0, 0.0, 0.0, "null", -1, -1);
    }

    public void collect(peekCiryCrew crew, Double amount){
        int oldDeposit = regions[crew.positionM][crew.positionN].deposit();
        if(regions[crew.positionM][crew.positionN].deposit() == amount){
            // if it is city center ?
            regions[crew.positionM][crew.positionN].returnOwner();
        }
        regions[crew.positionM][crew.positionN].takeDeposit(amount);
        // debug
        System.err.println("(" + crew.positionM + ", " + crew.positionN + ") deposit form " + oldDeposit + " to " + regions[crew.positionM][crew.positionN].deposit());
    }

    public String shoot(peekRegion region, Double amount){
        // debug
        double oldDeposit = regions[region.positionM][region.positionN].deposit();
        System.err.println("shoot (" + region.positionM + ", " + region.positionN + ") from " + oldDeposit + " to " + (oldDeposit - amount.intValue()));

        return regions[region.positionM][region.positionN].beAttack(amount);
    }

    public void takeRegion(peekCiryCrew crew){ regions[crew.positionM][crew.positionN].take(crew); }

    public void newCityCenter(peekCiryCrew crew){
        regions[crew.positionM][crew.positionN].newCityCenter(crew, Variables.get("init_center_dep"));
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
        regions[to.positionM][to.positionN].newCityCenter(crew, Variables.get("init_center_dep"));
        regions[from.positionM][from.positionN].returnOwner();
        regions[from.positionM][from.positionN].take(crew);
        // debug
        System.err.println("relocate citycenter from (" + from.positionM + ", " + from.positionN + ") to (" + to.positionM + ", " +to.positionN + ")");
    }

    public peekRegion getCurrentRegionInfo(peekCiryCrew crew) {
        return getCurrentRegionInfo(crew.positionM, crew.positionN);
    }

    @Override
    public peekRegion getCurrentRegionInfo(int m, int n) {
        return regions[m][n].getInfo(m, n, turn);
    }

    public void invest(peekCiryCrew crew, peekRegion region, Double amount) {
        int oldDeposit = regions[crew.positionM][crew.positionN].deposit();
        if(region.playerOwnerIndex == -1)   takeRegion(crew);
        regions[region.positionM][region.positionN].addDeposit(amount);
        // debug
        System.err.println("(" + region.positionM + ", " + region.positionN + ") deposit from " + oldDeposit + " to " + regions[region.positionM][region.positionN].deposit());
    }

    public void nextTurn(peekCiryCrew crew, int turn){
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
}

class region {
    private int playerOwnerIndex = 0;
    private Double deposit = 0.0;
    private Double maxDeposit = 0.0;
    private Double init_InterestRate = 0.0;
    private String type = "null";
    private int positionM = 0;
    private int positionN = 0;

    public int getOwner() { return playerOwnerIndex; }
    public int invest(player player, double amount) { return 0; }
    public void takeDeposit(Double amount) { deposit-=amount; }
    public int deposit() { return deposit.intValue(); }
    public int interest(player player, double amount) { return (int)(deposit * init_InterestRate / 100.0); }
    public String beAttack(Double amount) {
        deposit-=amount;
        if(deposit < 0){
            this.playerOwnerIndex = -1;
            this.deposit = 0.0;
            this.type = "empty";
            return "lostRegion";
        }
        return "lostDeposit";
    }

    public void take(peekCiryCrew crew){
        this.playerOwnerIndex = crew.crewOfPlayer;
    }

    public void returnOwner() {
        this.playerOwnerIndex = -1;
        this.type = "empty";
    }

    region(int M, int N, Double MaxDeposit, Double init_InterestRate) {
        this.positionM = M;
        this.positionN = N;
        this.playerOwnerIndex = -1;
        this.maxDeposit = MaxDeposit;
        this.init_InterestRate = init_InterestRate;
        this.type = "empty";
    }

    public peekRegion getInfo(int m, int n, int turn) {
//        System.err.println(turn);
        return new peekRegion(playerOwnerIndex, deposit, maxDeposit, calculateRealInterestRate(turn), type, m, n);
    }

    public void addDeposit(Double amount) {
        deposit +=amount;
    }

    public double calculateRealInterestRate(int turn) {
        // b * log10 d * ln t
//        System.err.println(init_InterestRate + " * " + Math.log10(deposit) + " * " + Math.log(turn));
        return init_InterestRate * Math.log10(deposit) * Math.log(turn);
    }

    public void calculateInterest(int turn){
        deposit+=(deposit* calculateRealInterestRate(turn) /100.0);
    }

    public void newCityCenter(peekCiryCrew crew, double init_deposit){
        this.playerOwnerIndex = crew.crewOfPlayer;
        this.type = "cityCenter";
        this.deposit = init_deposit;
    }
}

class peekRegion {
    public int playerOwnerIndex;
    public Double deposit;
    public Double MaxDeposit;
    public Double real_InterestRate;
    public String Type;
    public int positionM;
    public int positionN;
    peekRegion(int PlayerOwnerIndex, Double Deposit, Double MaxDeposit, Double real_InterestRate, String Type, int M, int N){
        this.playerOwnerIndex = PlayerOwnerIndex;
        this.deposit = Deposit;
        this.MaxDeposit = MaxDeposit;
        this.real_InterestRate = real_InterestRate;
        this.Type = Type;
        this.positionM = M;
        this.positionN = N;
    }
}