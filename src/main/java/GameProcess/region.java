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