package nz.ac.ara.macklei.thetheseusandminotaur;

/**
 * Created by MegaMac on 20/06/17.
 */

public interface Saveable {
    int getWidthAcross();
    int getDepthDown();
    Wall whatsAbove(Point where);
    Wall whatsLeft(Point where);
    Point wheresTheseus();
    Point wheresMinotaur();
    Point wheresExit();
}
