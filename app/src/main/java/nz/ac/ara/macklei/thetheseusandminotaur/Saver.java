package nz.ac.ara.macklei.thetheseusandminotaur;

/**
 * Created by MegaMac on 20/06/17.
 */

public interface Saver {
    void save( Game game );
    void save( Game game, String fileName );
    void save( Game game, String fileName, String levelName );
}
