package GameProcess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

interface territoryInterface {
    peekRegion getCurrentRegionInfo(int m, int n);
    void attackRegion(player player, region Target);
    void move(peekRegion target);
    void invest(player player, region region);
    void collect(player player, region region);
    void shoot(player player, int direction, region region);
    void relocate(region From, region To);
    int opponent(region currentRegion);
    int nearby(cityCrew cityCrew);
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
    private int m;
    private int n;
    private region[][] regions = null;
    HashMap<String, Double> Variables;

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

    public int opponent(cityCrew cityCrew) {
        peekCiryCrew crew = cityCrew.getCityCrewInfo();
        int crewOf = crew.crewOfPlayer;
        int CurrentM = crew.positionM;
        int CurrentN = crew.positionN;
        return 0;
    }

    public void shoot(player player, int direction){
        peekCiryCrew crew = player.getCityCrewInfo();
        peekRegion peekRegion = getInfoOfRegion(crew.positionM, crew.positionM);
        regions[peekRegion.positionM][peekRegion.positionN].beAttack(crew.budGet);
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

    @Override
    public void attackRegion(player player, region Target) {

    }

    @Override
    public void move(peekRegion target) {

    }

    public void invest(peekRegion region, Double amount) {
        regions[region.positionM][region.positionN].addDeposit(amount);
    }

    @Override
    public void collect(player player, region region) {

    }

    @Override
    public void shoot(player player, int direction, region region) {

    }

    @Override
    public void relocate(region From, region To) {

    }

    @Override
    public int opponent(region currentRegion) {
        return 0;
    }

    @Override
    public int nearby(cityCrew cityCrew) {
        return 0;
    }
}

class region {
    private int playerOwnerIndex;
    private Double deposit;
    private Double maxDeposit;
    private Double interestRate;
    private String type;
    private int positionM;
    private int positionN;

    public int getOwner() { return playerOwnerIndex; }
    public int invest(player player, double Amount) { return 0; }
    public int deposit() { return deposit.intValue(); }
    public int interest(player player, double Amount) { return (int)(deposit * interestRate / 100.0); }
    public int beAttack(Double Amount) { return 0; }

    region(int M, int N, Double MaxDeposit, Double InterestRate) {
        this.positionM = M;
        this.positionN = N;
        this.playerOwnerIndex = -1;
        this.maxDeposit = MaxDeposit;
        this.interestRate = InterestRate;
        this.type = "Empty";
    }

    public peekRegion getInfo() { return new peekRegion(playerOwnerIndex, deposit, maxDeposit, interestRate, type, positionM, positionN); }

    public void addDeposit(Double amount) {
        deposit +=amount;
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