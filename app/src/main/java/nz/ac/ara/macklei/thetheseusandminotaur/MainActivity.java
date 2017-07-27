package nz.ac.ara.macklei.thetheseusandminotaur;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "nz.ac.ara.macklei.thetheseusandminotaur.MESSAGE";

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String message;
        if (intent != null) {
            message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
            if (message != null) {
                if (message.equals("play")) {
                    this.sendMessagePlay();
                }
            }
        }
    }

    public void sendMessagePlay(View view) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        PlayAlertDialogFragment playAlertDialogFragment = new PlayAlertDialogFragment();
        playAlertDialogFragment.show(getSupportFragmentManager(), "Game");

    }

    public void sendMessagePlay() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        PlayAlertDialogFragment playAlertDialogFragment = new PlayAlertDialogFragment();
        playAlertDialogFragment.show(getSupportFragmentManager(), "Game");

    }
}
