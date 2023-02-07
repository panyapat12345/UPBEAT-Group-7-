package AST;
import Exception.*;

public class Dir implements Node{
    private String dir;
    private final String[] possibleDirs = {"up", "down", "upleft", "upright", "downleft", "downright"};

    public Dir(String dir) {
        this.dir = dir;
        check();
    }

    private void check() {
        for(String possibleDir : possibleDirs){
            if(dir.equals(possibleDir))
                return;
        }
        throw new IllegalDirectionException(dir);
    }

    public void prettyPrint(StringBuilder s) {
        s.append(dir);
        //s.append(" ");
    }

    @Override
    public String toString() {
        return dir;
    }
}
