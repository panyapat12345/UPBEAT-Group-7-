package GameProcess;

import java.util.HashMap;

interface TerritoryInterface{
    Region GetCurrentRegionInfo(int m, int n);
}

class Territory {
    private int m;
    private int n;
    private Region[][] Regions = null;

    Territory(HashMap<String, Double> Variables) {
        this.m = Variables.get("m").intValue();
        this.n = Variables.get("n").intValue();
        Regions = new Region[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Regions[i][j] = new Region(Variables.get("max_dep"), Variables.get("interest_pct"));
            }
        }
    }

    public PeekRegion GetInfoOfRegion(int M, int N) {
        if ((M >= 0 && N >= 0) && (M < m && N < n)) return Regions[M][N].GetInfo();
        else return new PeekRegion(-1, 0.0, 0.0, 0.0, "null");
    }

    public int opponent(int PlayerIndex, int CurrentM, int CurrentN) {
        return 0;
    }
}

class Region {
    private int PlayerOwnerIndex;
    private Double Deposit;
    private Double MaxDeposit;
    private Double InterestRate;
    private String Type;

    public int GetOwner() { return PlayerOwnerIndex; }
    public int deposit() { return Deposit.intValue(); }
    public int interest() { return (int)(Deposit * InterestRate / 100.0); }

    Region(Double MaxDeposit, Double InterestRate) {
        this.PlayerOwnerIndex = -1;
        this.MaxDeposit = MaxDeposit;
        this.InterestRate = InterestRate;
        this.Type = "Empty";
    }

    public PeekRegion GetInfo() { return new PeekRegion(PlayerOwnerIndex, Deposit, MaxDeposit, InterestRate, Type); }
}

class PeekRegion {
    public int PlayerOwnerIndex;
    public Double Deposit;
    public Double MaxDeposit;
    public Double InterestRate;
    public String Type;
    PeekRegion(int PlayerOwnerIndex, Double Deposit, Double MaxDeposit, Double InterestRate, String Type){
        this.PlayerOwnerIndex = PlayerOwnerIndex;
        this.Deposit = Deposit;
        this.MaxDeposit = MaxDeposit;
        this.InterestRate = InterestRate;
        this.Type = Type;
    }
}