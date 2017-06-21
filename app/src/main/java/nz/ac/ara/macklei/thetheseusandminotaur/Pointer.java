package nz.ac.ara.macklei.thetheseusandminotaur;

/**
 * Created by MegaMac on 20/06/17.
 */

public class Pointer implements Point {
    private int across;
    private int down;

    public Pointer(int x, int y){
        this.across = x;
        this.down = y;
    }

    @Override
    public int across() {
        return this.across;
    }

    @Override
    public int down() {
        return this.down;
    }
}
