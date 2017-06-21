package nz.ac.ara.macklei.thetheseusandminotaur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import static android.R.attr.height;
import static android.R.attr.width;
import static android.graphics.Color.rgb;

/**
 * Created by MegaMac on 20/06/17.
 */

class GameCustomView extends View {
    private static final String LOGTAG = "MOTIONS";
    public static int x = 0;
    public static int y = 0;
    public int theseusX;
    public int theseusY;
    public static int minotaurX = 200;
    public static int minotaurY = 200;
    public Bitmap theseusBitmap;
    public Bitmap minotaurBitmap;
    private GestureDetectorCompat mDetector;
    private ViewModel view;

    public GameCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDetector = new GestureDetectorCompat(getContext(), new SwipeGestureDetector());
        this.view = new ViewModel();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Call the super class.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Set the dimensions of this view.
        this.setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        Log.d("MAZE SIZE", "Width:" + getMeasuredWidth() + "Height:" + getMeasuredHeight());
        // Width: 864, Height: 1020

        int width = (int)(getMeasuredWidth() / 100); // 8
        int depth = (int)(getMeasuredHeight() / 100); // 10
        Log.d("MAZE SIZE", "Width:" + width + " Height:" + depth);
//        game.setWidthAcross(width);
//        game.setDepthDown(depth);
        this.view.setLevel(width, depth);
        Log.d("Level Size", "Width:" + this.view.getLevelWidth() + " Depth:" + this.view.getLevelDepth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.view.setTheseus(new Pointer(2, 4));
        Log.d("Theseus POINT", "Across: " + this.view.getTheseusX() + " Down: " + this.view.getTheseusY());
        Log.d("THESEUS XY", "X: " + this.theseusX + " Y: " + this.theseusY);
        this.drawTheseus(canvas);
//        this.drawMinotaur(canvas);

    }

//    protected double levelGrid() {
//        int screenWidth = getMeasuredWidth();
//        double levelArea = screenWidth * 0.9;
//        double cellSize = levelArea / game.getWidthAcross();
//        return cellSize;
//    }



    protected void drawTheseus(Canvas canvas) {
        this.theseusX = view.getTheseusX();
        this.theseusY = view.getTheseusY();
        Log.d("THESEUS XY", "X: " + this.theseusX + " Y: " + this.theseusY);
//        theseusBitmap = MainActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.theseus, 120, 120);
//        canvas.drawBitmap(theseusBitmap, new Rect(0, 0, theseusBitmap.getWidth(), theseusBitmap.getHeight()), new Rect(theseusX, theseusY, theseusX + 120, theseusY + 120), null);

        int width = 100;
        int height = 100;
        int orange = rgb(255, 165, 0);
        Paint paint = new Paint();
        Rect rect = new Rect(
                this.theseusX,
                this.theseusY,
                this.theseusX + width,
                this.theseusY + height);

        paint.setColor(orange);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(rect, paint);
    }

    protected void drawMinotaur(Canvas canvas) {
        minotaurBitmap = MainActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.minotaur, 120, 120);
        canvas.drawBitmap(minotaurBitmap, new Rect(0, 0, minotaurBitmap.getWidth(), minotaurBitmap.getHeight()), new Rect(minotaurX, minotaurY, minotaurX + 120, minotaurY + 120), null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
//        super.onTouchEvent(event);
        return true;
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {

            switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
                case 1:
                    Log.d(LOGTAG, "up");
//                    GameCustomView.theseusY -= 20;
                    invalidate();
                    return true;
                case 2:
                    Log.d(LOGTAG, "left");
//                    GameCustomView.minotaurX -= 20;
                    invalidate();
                    return true;
                case 3:
                    Log.d(LOGTAG, "down");
                    invalidate();
                    return true;
                case 4:
                    Log.d(LOGTAG, "right");
//                    GameCustomView.minotaurX += 20;
                    view.moveTheseus(Directions.RIGHT);
                    invalidate();
                    return true;
            }
            return false;
        }

        private int getSlope(float x1, float y1, float x2, float y2) {
            Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
            if (angle > 45 && angle <= 135)
                // top
                return 1;
            if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
                // left
                return 2;
            if (angle < -45 && angle >= -135)
                // down
                return 3;
            if (angle > -45 && angle <= 45)
                // right
                return 4;
            return 0;
        }
    }

}
