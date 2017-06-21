package nz.ac.ara.macklei.thetheseusandminotaur;

/**
 * Created by MegaMac on 20/06/17.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameModel implements Saveable, Loadable, Game{

    private List<List<Cell>> level = new ArrayList<List<Cell>>();
    private int width = 0;
    private int depth = 0;
    private int theseusMove = 0;
    private int minotaurMove = 0;

    @Override
    public void moveTheseus(Directions direction) {
        Point theseusAt = this.wheresTheseus();
        Point theseusTo = new Pointer(theseusAt.across() + direction.acrossAdjust, theseusAt.down() + direction.downAdjust);

        if (direction == Directions.SPACE) {
            this.moveMinotaur();
            this.moveMinotaur();
        }else {
            if (!this.isBlocked(direction, theseusAt, theseusTo)) {
                this.setCell(theseusAt, Part.NOTHING, "character");
                this.addTheseus(theseusTo);
                this.gameWinState(this.wheresTheseus());
                this.theseusMove++;
            } else {
                System.out.println("BLOCKED");
            }
        }

    }

    @Override
    public void moveMinotaur() {
        Point theseusAt = this.wheresTheseus();
        Point minotaurAt = this.wheresMinotaur();
        Point minotaurTo;

        Directions horizDir = this.findDirection(theseusAt, minotaurAt, "horizontal");
        Directions vertDir = this.findDirection(theseusAt, minotaurAt, "vertical");
        if (horizDir != null
                && !this.isBlocked(horizDir, minotaurAt,
                minotaurTo = new Pointer(
                        minotaurAt.across() + horizDir.acrossAdjust,
                        minotaurAt.down() + horizDir.downAdjust))) {
            this.setCell(minotaurAt, Part.NOTHING, "character");
            this.addMinotaur(minotaurTo);
            this.minotaurMove++;
        } else if (vertDir != null
                && !this.isBlocked(vertDir, minotaurAt,
                minotaurTo = new Pointer(
                        minotaurAt.across() + vertDir.acrossAdjust,
                        minotaurAt.down() + vertDir.downAdjust))) {
            this.setCell(minotaurAt, Part.NOTHING, "character");
            this.addMinotaur(minotaurTo);
            this.minotaurMove++;
        }
        this.gameLossState(theseusAt);
    }

    private boolean isBlocked(Directions direction, Point current, Point destination) {
        HashMap<Directions, Boolean> isBlocked = new HashMap<Directions, Boolean>();
        isBlocked.put(Directions.LEFT, this.wallAt(current, "left"));
        isBlocked.put(Directions.RIGHT, this.wallAt(destination, "left"));
        isBlocked.put(Directions.UP, this.wallAt(current, "top"));
        isBlocked.put(Directions.DOWN, this.wallAt(destination, "top"));
//		isBlocked.put(Directions.SPACE, false);

        return isBlocked.get(direction);
    }

    private boolean wallAt(Point position, String value) {
        if ((Wall) this.getCell(position).get(value) == Wall.SOMETHING) {
            return true;
        }
        return false;
    }

    private Directions findDirection(Point theseus, Point minotaur, String flag) {
        HashMap<String, Directions> findDirection = new HashMap<String, Directions>();
        findDirection.put("horizontal", findDirectionToTheseusHorizontal(theseus, minotaur));
        findDirection.put("vertical", findDirectionToTheseusVertical(theseus, minotaur));

        return findDirection.get(flag);
    }

    private Directions findDirectionToTheseusVertical(Point theseus, Point minotaur) {
        Directions result = null;
        if (theseus.down() > minotaur.down()) {
            result = Directions.DOWN;
        } else if (theseus.down() < minotaur.down()) {
            result = Directions.UP;
        }
        return result;
    }

    private Directions findDirectionToTheseusHorizontal(Point theseus, Point minotaur) {
        Directions result = null;
        if (theseus.across() > minotaur.across()) {
            result = Directions.RIGHT;
        } else if (theseus.across() < minotaur.across()) {
            result = Directions.LEFT;
        }
        return result;
    }

    private void gameLossState(Point theseusAt) {
        Cell cell = this.getCell(theseusAt);
        if (cell.get("character") != Part.THESEUS){
            System.out.println("LOSER");
        }
    }

    private void gameWinState(Point theseusTo) {
        Point exit = this.wheresExit();
        if (theseusTo.across() == exit.across()
                && theseusTo.down() == exit.down()){
            System.out.println("WINNER");
        }
    }

    @Override
    public int setWidthAcross(int widthAcross) {
        this.width = widthAcross;
        if (this.depth > 0 && this.width > 0) {
            this.levelBuilder();
        }
        return this.width;
    }

    @Override
    public int setDepthDown(int depthDown) {
        this.depth = depthDown;
        if (this.width > 0 && this.depth > 0) {
            this.levelBuilder();
        }
        return this.depth;
    }

    private void levelBuilder() {
        for (int i = 0; i < this.depth; i++) {
            List<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < this.width; j++) {
                row.add(new Cell());
            }
            this.level.add(row);
        }
    }

    private Cell getCell(Point where) {
        List<Cell> row = level.get(where.down());
        Cell cell = row.get(where.across());
        return cell;
    }

    private void setCell(Point where, Object object, String key) {
        int across = where.across();
        int down = where.down();

        List<Cell> row = level.get(down);
        Cell cell = row.get(across);

        cell.set(key, object);

        row.set(across, cell);
        this.level.set(down, row);
    }

    private Point getObjectLocation(Object object, String key) {
        Point result = null;

        for (int i = 0; i < this.depth; i++) {
            for (int j = 0; j < this.width; j++) {
                Point here = new Pointer(j,i);
                if (this.getCell(here).get(key) == object) {
                    result = here;
                }
            }
        }
        return result;
    }

    @Override
    public void addWallAbove(Point where) {
        this.setCell(where, Wall.SOMETHING, "top");

    }

    @Override
    public void addWallLeft(Point where) {
        this.setCell(where, Wall.SOMETHING, "left");

    }

    @Override
    public void addTheseus(Point where) {
        this.setCell(where, Part.THESEUS, "character");
    }

    @Override
    public void addMinotaur(Point where) {
        this.setCell(where, Part.MINOTAUR, "character");

    }

    @Override
    public void addExit(Point where) {
        this.setCell(where, Part.EXIT, "objective");

    }

    @Override
    public int getWidthAcross() {
        return this.width;
    }

    @Override
    public int getDepthDown() {
        return this.depth;
    }

    @Override
    public Wall whatsAbove(Point where) {
        return (Wall) this.getCell(where).get("top");
    }

    @Override
    public Wall whatsLeft(Point where) {
        return (Wall) this.getCell(where).get("left");
    }

    @Override
    public Point wheresTheseus() {
        return this.getObjectLocation(Part.THESEUS, "character");
    }

    @Override
    public Point wheresMinotaur() {
        return this.getObjectLocation(Part.MINOTAUR, "character");
    }

    @Override
    public Point wheresExit() {
        return this.getObjectLocation(Part.EXIT, "objective");
    }
}
