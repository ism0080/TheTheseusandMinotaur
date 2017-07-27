package nz.ac.ara.macklei.thetheseusandminotaur;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Switch;

/**
 * Created by MegaMac on 24/06/17.
 */

public class EndGameAlertDialogFragment extends DialogFragment {
    MainActivity main = new MainActivity();
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] options = {"Home", "Restart", "Select Level"};
        builder.setTitle(getArguments().getString("title"))
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        // Where the intent is to be sent
                        String selection = options[which];
                        Intent intent = null;
                        String extra = null;

                        switch (selection) {
                            case "Home":
                                intent = new Intent(getActivity(), MainActivity.class);
                                break;
                            case "Restart":
                                intent = new Intent(getActivity(), GameCustomActivity.class);
                                extra = getArguments().getString("level");
                                intent.putExtra(MainActivity.EXTRA_MESSAGE, extra);
                                break;
                            case "Select Level":
                                intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra(MainActivity.EXTRA_MESSAGE, "play");
                                break;
                        }
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
