package GameProcess;

import java.util.List;

public class territoryDirectionIterator{
    List<peekRegion> listRegion = null;
    int index = 0;

    public territoryDirectionIterator(List<peekRegion> listRegion) {
        this.listRegion = listRegion;
    }

    public peekRegion next(){
        if(index < listRegion.size()){
            peekRegion current = listRegion.get(index);
            index++;
            return current;
        }
        else return new peekRegion(-1, 0.0, 0.0, 0.0, "null", -1, -1);
    }
}
