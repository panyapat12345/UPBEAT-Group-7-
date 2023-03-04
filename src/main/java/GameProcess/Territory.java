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
        return isInBound(target.PositionM, target.PositionN);
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
        regions[peekRegion.PositionM][peekRegion.PositionN].beAttack(crew.budGet);
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
        regions[to.PositionM][to.PositionN] = regions[from.PositionM][from.PositionN];
        regions[from.PositionM][from.PositionN] = new region(from.PositionM, from.PositionN, Variables.get("max_dep"), Variables.get("interest_pct"));
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

    @Override
    public void invest(player player, region region) {

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
    private int PlayerOwnerIndex;
    private Double Deposit;
    private Double MaxDeposit;
    private Double InterestRate;
    private String Type;
    private int PositionM;
    private int PositionN;

    public int GetOwner() { return PlayerOwnerIndex; }
    public int invest(player player, double Amount) { return 0; }
    public int deposit() { return Deposit.intValue(); }
    public int interest(player player, double Amount) { return (int)(Deposit * InterestRate / 100.0); }
    public int beAttack(Double Amount) { return 0; }

    region(int M, int N, Double MaxDeposit, Double InterestRate) {
        this.PositionM = M;
        this.PositionN = N;
        this.PlayerOwnerIndex = -1;
        this.MaxDeposit = MaxDeposit;
        this.InterestRate = InterestRate;
        this.Type = "Empty";
    }

    public peekRegion getInfo() { return new peekRegion(PlayerOwnerIndex, Deposit, MaxDeposit, InterestRate, Type, PositionM, PositionN); }
}

class peekRegion {
    public int PlayerOwnerIndex;
    public Double Deposit;
    public Double MaxDeposit;
    public Double InterestRate;
    public String Type;
    public int PositionM;
    public int PositionN;
    peekRegion(int PlayerOwnerIndex, Double Deposit, Double MaxDeposit, Double InterestRate, String Type, int M, int N){
        this.PlayerOwnerIndex = PlayerOwnerIndex;
        this.Deposit = Deposit;
        this.MaxDeposit = MaxDeposit;
        this.InterestRate = InterestRate;
        this.Type = Type;
        this.PositionM = M;
        this.PositionN = N;
    }
}