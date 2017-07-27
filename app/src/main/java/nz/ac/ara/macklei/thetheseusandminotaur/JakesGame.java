package nz.ac.ara.macklei.thetheseusandminotaur;

import java.util.ArrayList;
import java.util.List;

public class JakesGame implements Saveable, Loadable, Game {
    private int depth = 0;
    private int width = 0;
//    private Loader gameLoad;
//    private Saver gameSave;

    private List<List<JakeCell>> level = new ArrayList<List<JakeCell>>();

//    public JakesGame(Loader gameLoad, Saver gameSave){
//        this.gameLoad = gameLoad;
//        this.gameSave = gameSave;
//    }

    private void setLevel(){
        if(this.depth > 0 && this.width > 0){
            this.buildLevel();
        }
    }

    private void buildLevel(){
        for( int i = 0; i < depth; i++){
            List<JakeCell> row = new ArrayList<JakeCell>();
            for(int j = 0; j < width; j++){
                row.add(new JakeCell());
            }
            level.add(row);
        }
    }

    private JakeCell getCell(Point where){
        int x = where.across();
        int y = where.down();

        List<JakeCell> row = level.get(y);
        return row.get(x);
    }

    private void setCell(JakeCell cell, Point where){
        int x = where.across();
        int y = where.down();

        List<JakeCell> row = level.get(y);
        row.set(x, cell);
    }

    private Point getActor(Part actor){
        Point place = null;

        for(int i = 0; i < this.depth; i ++){
            for(int j = 0; j < this.width; j++){
                Point point = new Pointer(j, i);
                JakeCell cell = getCell(point);
                if (cell.actor == actor){
                    place = point;
                }
            }
        }

        return place;
    }

    private Directions findVertical(Point currentThes, Point currentMin){
        Directions result = null;
        int minY = currentMin.down();
        int thesY = currentThes.down();

        if(thesY > minY){
            result = Directions.DOWN;
        }
        else if(thesY < minY){
            result = Directions.UP;
        }
        return result;
    }

    private Directions findHorizontal(Point currentThes, Point currentMin){
        Directions result = null;
        int minX = currentMin.across();
        int thesX = currentThes.across();

        if(thesX > minX){
            result = Directions.RIGHT;
        }
        else if(thesX < minX){
            result = Directions.LEFT;
        }
        return result;
    }

    private boolean isBlocked(Point curr, Point dest, Directions dir){
        boolean result = false;

        JakeCell destination = this.getCell(dest);
        JakeCell current = this.getCell(curr);

        if(dir == Directions.DOWN){
            if(destination.top == Wall.SOMETHING){
                result = true;
            }
        }
        if(dir == Directions.RIGHT){
            if(destination.left == Wall.SOMETHING){
                result = true;
            }
        }
        if(dir == Directions.LEFT){
            if(current.left == Wall.SOMETHING){
                result = true;
            }
        }
        if(dir == Directions.UP){
            if(current.top == Wall.SOMETHING){
                result = true;
            }
        }
        return result;
    }

    private boolean checkWithinGrid(Point where){
        boolean result = true;
        if(where.across() > width){
            result = false;
            throw new IllegalArgumentException("outside of grid width");
        }
        if(where.down() > depth){
            result = false;
            throw new IllegalArgumentException("outside of grid height");
        }
        return result;
    }

    @Override
    public void moveTheseus(Directions direction) {
        Point currentThes = this.wheresTheseus();
        Point destination = new Pointer(
                currentThes.across() + direction.acrossAdjust,
                currentThes.down() + direction.downAdjust);

        if(!this.isBlocked(currentThes, destination, direction)){
            if(this.getCell(destination).actor == Part.MINOTAUR){
                this.addEmpty(currentThes);
            }else{
                this.addEmpty(currentThes);
                this.addTheseus(destination);
            }
        }
    }

    @Override
    public void moveMinotaur() {
        int moves =2;
        while(moves > 0){
            Point currentMin = this.wheresMinotaur();
            Point currentThes = this.wheresTheseus();
            Point destination;

            Directions horizontal = this.findHorizontal(currentThes, currentMin);
            Directions vertical = this.findVertical(currentThes, currentMin);

            if(horizontal != null && !this.isBlocked(currentMin,
                    destination = new Pointer(
                            currentMin.across() + horizontal.acrossAdjust,
                            currentMin.down() + horizontal.downAdjust), horizontal)
                    && this.getCell(destination).actor != Part.EXIT){

                this.addMinotaur(destination);
                this.addEmpty(currentMin);
            }
            else if(vertical != null && !this.isBlocked(currentMin,
                    destination = new Pointer(
                            currentMin.across() + vertical.acrossAdjust,
                            currentMin.down() + vertical.downAdjust), vertical)
                    && this.getCell(destination).actor != Part.EXIT){

                this.addMinotaur(destination);
                this.addEmpty(currentMin);
            }
            moves--;
        }
    }

    @Override
    public int setWidthAcross(int widthAcross) {
        if(widthAcross < 4){
            throw new IllegalArgumentException("Minimum width of 4");
        }
        this.width = widthAcross;
        this.setLevel();
        return widthAcross;
    }

    @Override
    public int setDepthDown(int depthDown) {
        if(depthDown < 4){
            throw new IllegalArgumentException("Minimum height of 4");
        }
        this.depth = depthDown;
        this.setLevel();
        return depthDown;
    }

    @Override
    public void addWallAbove(Point where) {
        if(this.checkWithinGrid(where)){
            JakeCell cell = this.getCell(where);
            cell.top = Wall.SOMETHING;

            this.setCell(cell, where);
        }

    }

    @Override
    public void addWallLeft(Point where) {
        if(this.checkWithinGrid(where)){

        }
        JakeCell cell = this.getCell(where);
        cell.left = Wall.SOMETHING;

        this.setCell(cell, where);
    }

    @Override
    public void addTheseus(Point where) {
        if(this.checkWithinGrid(where)){
            Point currentThes = this.getActor(Part.THESEUS);

            if(currentThes != null){
                this.addEmpty(currentThes);
            }

            JakeCell cell = this.getCell(where);
            cell.actor = Part.THESEUS;

            this.setCell(cell, where);
        }
    }

    @Override
    public void addMinotaur(Point where) {
        if(this.checkWithinGrid(where)){
            Point currentMin = this.getActor(Part.MINOTAUR);

            if(currentMin != null){
                this.addEmpty(currentMin);
            }

            JakeCell cell = this.getCell(where);
            cell.actor = Part.MINOTAUR;

            this.setCell(cell, where);
        }
    }

    @Override
    public void addExit(Point where) {
        if(this.checkWithinGrid(where)){
            Point currentExit = this.getActor(Part.EXIT);

            if(currentExit != null){
                this.addEmpty(currentExit);
            }

            JakeCell cell = this.getCell(where);
            cell.actor = Part.EXIT;

            this.setCell(cell, where);
        }
    }

    private void addEmpty(Point where){
        JakeCell cell = this.getCell(where);
        cell.actor = Part.NOTHING;

        this.setCell(cell, where);
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
        return this.getCell(where).top;
    }

    @Override
    public Wall whatsLeft(Point where) {
        return this.getCell(where).left;
    }

    @Override
    public Point wheresTheseus() {
        return this.getActor(Part.THESEUS);
    }

    @Override
    public Point wheresMinotaur() {
        return this.getActor(Part.MINOTAUR);
    }

    @Override
    public Point wheresExit() {
        return this.getActor(Part.EXIT);
    }
}
