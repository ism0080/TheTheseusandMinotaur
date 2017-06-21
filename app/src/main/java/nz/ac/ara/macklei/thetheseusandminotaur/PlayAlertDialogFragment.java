package nz.ac.ara.macklei.thetheseusandminotaur;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by MegaMac on 20/06/17.
 */

public class PlayAlertDialogFragment extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] levels = {"Maze 1", "Maze 2", "Maze 3"};
        builder.setTitle(R.string.pick_size)
                .setItems(levels, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        // Where the intent is to be sent
                        Intent game = new Intent(getActivity(), GameCustomActivity.class);

                        String message = levels[which];
                        game.putExtra(MainActivity.EXTRA_MESSAGE, message);
                        startActivity(game);
                    }
                });
        return builder.create();
    }
}
