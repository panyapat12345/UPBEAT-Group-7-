package GameProcess;

public interface territoryInterface {
    peekRegion getCurrentRegionInfo(int m, int n);
    boolean isInBound(int M, int N);
    peekRegion getInfoOfRegion(int M, int N);
    String shoot(peekRegion region, double amount);
    territoryDirectionIterator getTerritoryDirectionIterator(int direction, int interestM, int interestN);
    peekRegion getCurrentRegionInfo(peekCiryCrew crew);
    void invest(peekCiryCrew crew, peekRegion region, double amount);
}
