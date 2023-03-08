package GameProcess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

interface territoryInterface {
    peekRegion getCurrentRegionInfo(int m, int n);
    boolean isInBound(int M, int N);
    peekRegion getInfoOfRegion(int M, int N);
    String shoot(peekRegion region, Double amount);
    territoryDirectionIterator getTerritoryDirectionIterator(int direction, int interestM, int interestN);
    peekRegion getCurrentRegionInfo(peekCiryCrew crew);
    void invest(peekRegion region, Double amount);
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
        if ((M >= 0 && N >= 0) && (M < m && N < n)) return true;
        return false;
    }

    public boolean isInBound(peekRegion target) {
        return isInBound(target.positionM, target.positionN);
    }

    public peekRegion getInfoOfRegion(int M, int N) {
        if(isInBound(M, N)) return regions[M][N].getInfo();
        else return new peekRegion(-1, 0.0, 0.0, 0.0, "null", -1, -1);
    }

    public void collect(peekCiryCrew crew, Double amount){
        regions[crew.positionM][crew.positionN].takeDeposit(amount);
    }

    public String shoot(peekRegion region, Double amount){
        return regions[region.positionM][region.positionN].beAttack(amount);
    }

    public void takeRegion(peekCiryCrew crew){ regions[crew.positionM][crew.positionN].take(crew); }

    public void newCityCenter(peekCiryCrew crew){
        regions[crew.positionM][crew.positionN].newCityCenter(crew);
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

    public void relocate(peekRegion from, peekRegion to){
        regions[to.positionM][to.positionN] = regions[from.positionM][from.positionN];
        regions[from.positionM][from.positionN] = new region(from.positionM, from.positionN, Variables.get("max_dep"), Variables.get("interest_pct"));
    }

    public peekRegion getCurrentRegionInfo(peekCiryCrew crew) {
        return getCurrentRegionInfo(crew.positionM, crew.positionN);
    }

    @Override
    public peekRegion getCurrentRegionInfo(int m, int n) {
        return regions[m][n].getInfo();
    }

    public void invest(peekRegion region, Double amount) {
        regions[region.positionM][region.positionN].addDeposit(amount);
    }

    public void nextTurn(){
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                regions[i][j].calculateInterest();
            }
        }
    }
}

class region {
    private int playerOwnerIndex = 0;
    private Double deposit = 0.0;
    private Double maxDeposit = 0.0;
    private Double interestRate = 0.0;
    private String type = "null";
    private int positionM = 0;
    private int positionN = 0;

    public int getOwner() { return playerOwnerIndex; }
    public int invest(player player, double amount) { return 0; }
    public void takeDeposit(Double amount) { deposit-=amount; }
    public int deposit() { return deposit.intValue(); }
    public int interest(player player, double amount) { return (int)(deposit * interestRate / 100.0); }
    public String beAttack(Double amount) {
        deposit-=amount;
        if(deposit < 0){
            this.playerOwnerIndex = -1;
            this.type = "empty";
            return "lostRegion";
        }
        return "lostDeposit";
    }

    public void take(peekCiryCrew crew){
        this.playerOwnerIndex = crew.crewOfPlayer;
    }

    region(int M, int N, Double MaxDeposit, Double InterestRate) {
        this.positionM = M;
        this.positionN = N;
        this.playerOwnerIndex = -1;
        this.maxDeposit = MaxDeposit;
        this.interestRate = InterestRate;
        this.type = "empty";
    }

    public peekRegion getInfo() { return new peekRegion(playerOwnerIndex, deposit, maxDeposit, interestRate, type, positionM, positionN); }

    public void addDeposit(Double amount) {
        deposit +=amount;
    }

    public void calculateInterest(){
        deposit+=(deposit*interestRate/100.0);
    }

    public void newCityCenter(peekCiryCrew crew){
        this.playerOwnerIndex = crew.crewOfPlayer;
        this.type = "cityCenter";
    }
}

class peekRegion {
    public int playerOwnerIndex;
    public Double deposit;
    public Double MaxDeposit;
    public Double interestRate;
    public String Type;
    public int positionM;
    public int positionN;
    peekRegion(int PlayerOwnerIndex, Double Deposit, Double MaxDeposit, Double InterestRate, String Type, int M, int N){
        this.playerOwnerIndex = PlayerOwnerIndex;
        this.deposit = Deposit;
        this.MaxDeposit = MaxDeposit;
        this.interestRate = InterestRate;
        this.Type = Type;
        this.positionM = M;
        this.positionN = N;
    }
}