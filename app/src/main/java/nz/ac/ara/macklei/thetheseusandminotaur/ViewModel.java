package nz.ac.ara.macklei.thetheseusandminotaur;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MegaMac on 21/06/17.
 */

public class ViewModel {
    protected GameModel game;
    protected Context context;
    private String level;

    public ViewModel(Context context) {
        this.game = new GameModel();
        this.context = context;

    }

    public void setLevel(int width, int depth) {
        this.game.setWidthAcross(width);
        this.game.setDepthDown(depth);
    }

    public int getLevelWidth() {
        return this.game.getWidthAcross();
    }

    public int getLevelDepth() {
        return this.game.getDepthDown();
    }

    public void setPlayers(Point thes, Point min, Point exit) {
        this.game.addTheseus(thes);
        this.game.addMinotaur(min);
        this.game.addExit(exit);
    }

    public Point getTheseus() {
        return game.wheresTheseus();
    }

    public Point getMinotaur() {
        return this.game.wheresMinotaur();
    }

    public void setWallAbove(Point where) {
        this.game.addWallAbove(where);
    }

    public void setWallLeft(Point where) {
        this.game.addWallLeft(where);
    }

    public void move(String string) {
        HashMap<String, Directions> move = new HashMap<String, Directions>();
        move.put("left", Directions.LEFT);
        move.put("right", Directions.RIGHT);
        move.put("up", Directions.UP);
        move.put("down", Directions.DOWN);
        this.game.moveTheseus(move.get(string));
        this.gameWinState();
    }

    public void moveMinotaur() {
        this.game.moveMinotaur();
        this.gameLossState();
    }

    public void getLevelNumber(String level) {
        this.level = level;
    }

    private void gameWinState() {
        Point exit = this.game.wheresExit();
        Point theseusTo = this.getTheseus();
        if (theseusTo.across() == exit.across()
                && theseusTo.down() == exit.down()){
            this.gameEndMessage("WINNER");
            MediaPlayer mediaPlayer = MediaPlayer.create(this.context, R.raw.win);
            mediaPlayer.start();
        }
    }

    private void gameLossState() {
        Point theseus = this.getTheseus();
        Point minotaur = this.getMinotaur();
        if (theseus.across() == minotaur.across()
                && theseus.down() == minotaur.down()){
            this.gameEndMessage("LOSER");
            MediaPlayer mediaPlayer = MediaPlayer.create(this.context, R.raw.lose);
            mediaPlayer.start();
        }
    }

    private void gameEndMessage(String title) {
        FragmentManager fragmentManager = ((Activity) this.context).getFragmentManager();
        EndGameAlertDialogFragment endGameAlertDialogFragment = new EndGameAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("level",this.level);
        endGameAlertDialogFragment.setArguments(args);
        endGameAlertDialogFragment.show(fragmentManager, "title");
    }
}
