package nz.ac.ara.macklei.thetheseusandminotaur;

/**
 * Created by MegaMac on 21/06/17.
 */

public class ViewModel {
    GameModel game;

    public ViewModel() {
        this.game = new GameModel();
    }

    public void setLevel(int width, int depth) {
        game.setWidthAcross(width);
        game.setDepthDown(depth);
    }

    public int getLevelWidth() {
        return game.getWidthAcross();
    }

    public int getLevelDepth() {
        return game.getDepthDown();
    }

    public void setTheseus(Point point) {
        game.addTheseus(point);
    }

    public int getTheseusX() {
        return game.wheresTheseus().across();
    }

    public int getTheseusY() {
        return game.wheresTheseus().down();
    }


    public void moveTheseus(Directions directions) {
        game.moveTheseus(directions);
    }
}
