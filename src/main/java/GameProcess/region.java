package GameProcess;

public class region {
    private int playerOwnerIndex = 0;
    private Double deposit = 0.0;
    private Double maxDeposit = 0.0;
    private Double init_InterestRate = 0.0;
    private String type = "null";
    private int positionM = 0;
    private int positionN = 0;

    public int getOwner() { return playerOwnerIndex; }
    public void takeDeposit(Double amount) { deposit-=amount; }
    public int deposit() { return deposit.intValue(); }
    public String beAttack(double amount) {
        deposit = Math.max(0, deposit - amount);
        if(deposit < 1){
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

    public region(int M, int N, double MaxDeposit, double init_InterestRate) {
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

    public void addDeposit(double amount) {
        deposit +=amount;
    }

    public double calculateRealInterestRate(int turn) {
        // b * log10 d * ln t
//        System.err.println(init_InterestRate + " * " + Math.log10(deposit) + " * " + Math.log(turn));
        return init_InterestRate * Math.log10(deposit) * Math.log(turn);
    }

    public double profit(int turn) { return (deposit* calculateRealInterestRate(turn) /100.0); }

    public void calculateInterest(int turn){
        double profit = profit(turn);
        double gap = maxDeposit - deposit;
        if(profit > gap){
            deposit += gap;
        } else {
            deposit += profit;
        }
    }

    public void newCityCenter(peekCiryCrew crew, double init_deposit){
        this.playerOwnerIndex = crew.crewOfPlayer;
        this.type = "cityCenter";
        this.deposit = init_deposit;
    }
}