package nz.ac.ara.macklei.thetheseusandminotaur;

import android.content.Context;

/**
 * Created by MegaMac on 25/06/17.
 */

public class JakeViewModel extends ViewModel {
    protected JakesGame game;

    public JakeViewModel(Context context) {
        super(context);
        this.game = new JakesGame();
    }
}
