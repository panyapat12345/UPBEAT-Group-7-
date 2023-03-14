package GameProcess;

public class peekRegion {
    public int playerOwnerIndex;
    public Double deposit;
    public Double MaxDeposit;
    public Double real_InterestRate;
    public String Type;
    public int positionM;
    public int positionN;
    public peekRegion(int PlayerOwnerIndex, Double Deposit, Double MaxDeposit, Double real_InterestRate, String Type, int M, int N){
        this.playerOwnerIndex = PlayerOwnerIndex;
        this.deposit = Deposit;
        this.MaxDeposit = MaxDeposit;
        this.real_InterestRate = real_InterestRate;
        this.Type = Type;
        this.positionM = M;
        this.positionN = N;
    }
}
