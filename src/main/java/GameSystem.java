public interface GameSystem {
    // static GameSystem instance();

    int rows();
    int cols();
    int currow();
    int curcol();
    int budget();
    int deposit();
    int interest();
    int maxDeposit();
    int random();
    int opponent();
    int nearby();
}
