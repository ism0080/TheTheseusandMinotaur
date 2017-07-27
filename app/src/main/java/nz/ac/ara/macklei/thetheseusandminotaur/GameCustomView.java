package nz.ac.ara.macklei.thetheseusandminotaur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.height;
import static android.R.attr.width;
import static android.graphics.Color.rgb;

/**
 * Created by MegaMac on 20/06/17.
 */

class GameCustomView extends View {
    private static final String LOGTAG = "MOTIONS";
    protected int theseusX;
    protected int theseusY;
    protected int minotaurX;
    protected int minotaurY;
    public int moves = 0;
    public String levelNumber;
    protected Bitmap theseusBitmap;
    protected Bitmap minotaurBitmap;
    private GestureDetectorCompat mDetector;
    private ViewModel viewModel;
    private int screenSize;
    private Point exitPos;
    private Point thesPos;
    private Point minPos;
    private Pointer[] pointArrayTop;
    private Pointer[] pointArrayLeft;

    public GameCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDetector = new GestureDetectorCompat(getContext(), new SwipeGestureDetector());
//        this.viewModel = new ViewModel(context);
        this.viewModel = new JakeViewModel(context);
    }

    protected void mazeType(String string) {
        this.levelNumber = string;
        Log.d("MAZE TYPE", "" + string);
        if (string.equals("Maze 1")) {
            this.pointArrayTop = new Pointer[]{new Pointer(1,1), new Pointer(2,1), new Pointer(3,1), new Pointer(4,1), new Pointer(5,1), new Pointer(6,1),
                                                new Pointer(2,4), new Pointer(3,4), new Pointer(4,4), new Pointer(5,4), new Pointer(7,4), new Pointer(2,5),
                                                new Pointer(3,5), new Pointer(4,5),new Pointer(5,5), new Pointer(7,5), new Pointer(1,9), new Pointer(2,9),
                                                new Pointer(3,9), new Pointer(4,9), new Pointer(5,9), new Pointer(6,9) };
            this.pointArrayLeft = new Pointer[]{new Pointer(1,1), new Pointer(7,1), new Pointer(1,2), new Pointer(7,2), new Pointer(1,3), new Pointer(7,3),
                                                new Pointer(1,4), new Pointer(6,4), new Pointer(1,5), new Pointer(7,5), new Pointer(1,6), new Pointer(7,6),
                                                new Pointer(1,7), new Pointer(7,7), new Pointer(1,8), new Pointer(7,8)};
            this.exitPos = new Pointer(7, 4);
            this.thesPos = new Pointer(3, 5);
            this.minPos = new Pointer(3, 3);
        }
        if (string.equals("Maze 2")) {
            this.pointArrayTop = new Pointer[]{new Pointer(1,1), new Pointer(2,1), new Pointer(3,1), new Pointer(4,1), new Pointer(6,1), new Pointer(1,2),
                                    new Pointer(2,3), new Pointer(5,3), new Pointer(3,4), new Pointer(5,4), new Pointer(3,5), new Pointer(1,7), new Pointer(2,7),
                                    new Pointer(3,7), new Pointer(4,7), new Pointer(5,7), new Pointer(6,7) };
            this.pointArrayLeft = new Pointer[]{new Pointer(5,0), new Pointer(6,0), new Pointer(1,1), new Pointer(6,1), new Pointer(7,1), new Pointer(1,2),
                                    new Pointer(3,2),new Pointer(5,2), new Pointer(6,2), new Pointer(7,2), new Pointer(1,3), new Pointer(2,3), new Pointer(3,3),
                                    new Pointer(4,3), new Pointer(7,3), new Pointer(1,4), new Pointer(2,4), new Pointer(5,4), new Pointer(7,4), new Pointer(1,5),
                                    new Pointer(2,5), new Pointer(3,5), new Pointer(7,5), new Pointer(1,6), new Pointer(7,6)};
            this.exitPos = new Pointer(5, 0);
            this.thesPos = new Pointer(1, 1);
            this.minPos = new Pointer(5, 3);
        }
        if (string.equals("Maze 3")) {
            this.pointArrayTop = new Pointer[]{new Pointer(1,1), new Pointer(2,1), new Pointer(3,1), new Pointer(4,1), new Pointer(5,1), new Pointer(6,1), new Pointer(3,4),
                                new Pointer(7,4), new Pointer(7,5), new Pointer(2,6), new Pointer(1,7), new Pointer(2,7), new Pointer(3,7), new Pointer(4,7), new Pointer(5,7),
                                new Pointer(6,7) };
            this.pointArrayLeft = new Pointer[]{ new Pointer(1,1), new Pointer(7,1), new Pointer(1,2), new Pointer(7,2), new Pointer(1,3), new Pointer(7,3), new Pointer(1,4),
                    new Pointer(1,5), new Pointer(2,5), new Pointer(3,5), new Pointer(7,5), new Pointer(1,6), new Pointer(7,6)};
            this.exitPos = new Pointer(7, 4);
            this.thesPos = new Pointer(3, 4);
            this.minPos = new Pointer(3, 3);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Call the super class.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Set the dimensions of this view.
        this.setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        Log.d("MAZE SIZE", "Width:" + getMeasuredWidth() + "Height:" + getMeasuredHeight());
        // Width: 864, Height: 1020
        this.screenSize = getMeasuredWidth();
        int width = (int)(getMeasuredWidth() / 100); // 8
        int depth = (int)(getMeasuredHeight() / 100); // 10
        Log.d("MAZE SIZE", "Width: " + width + " Height: " + depth);
        this.viewModel.setLevel(width, depth);
        this.viewModel.setPlayers(thesPos, minPos, exitPos);
    }

    protected void wallBuilderTop(Canvas canvas, Pointer point) {
        Bitmap wallBitmap = MainActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.wall, 120, 120);
        this.viewModel.setWallAbove(point);
        int x = this.levelGrid(point.across());
        int y = this.levelGrid(point.down());
        canvas.drawBitmap(wallBitmap, new Rect(0, 0, wallBitmap.getWidth(), wallBitmap.getHeight()), new Rect(x, y, x + 120, y + 20), null);
    }

    protected void wallBuilderLeft(Canvas canvas, Pointer point) {
        Bitmap wallBitmap = MainActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.wall, 120, 120);
        this.viewModel.setWallLeft(point);
        int x = this.levelGrid(point.across());
        int y = this.levelGrid(point.down());
        canvas.drawBitmap(wallBitmap, new Rect(0, 0, wallBitmap.getWidth(), wallBitmap.getHeight()), new Rect(x, y, x + 20, y + 120), null);
    }

    protected void wallBuilder(Canvas canvas) {
        for (int i = 0; i < this.pointArrayTop.length; i++) {
            this.wallBuilderTop(canvas, this.pointArrayTop[i]);
        }

        for (int i = 0; i < this.pointArrayLeft.length; i++) {
            this.wallBuilderLeft(canvas, this.pointArrayLeft[i]);
        }
    }

    protected void exitBuilder(Canvas canvas) {
        Bitmap exitBitmap = MainActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.goal, 100, 100);
        int x = this.levelGrid(exitPos.across());
        int y = this.levelGrid(exitPos.down());
        canvas.drawBitmap(exitBitmap, new Rect(0, 0, exitBitmap.getWidth(), exitBitmap.getHeight()), new Rect(x, y, x + 100, y + 100), null);
    }

    protected void moveCounter(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        canvas.drawText("Move Count: " + this.moves, 10, 25, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.drawTheseus(canvas);
        this.drawMinotaur(canvas);
        this.wallBuilder(canvas);
        this.exitBuilder(canvas);
        this.moveCounter(canvas);
        this.viewModel.getLevelNumber(this.levelNumber);

    }

    protected int levelGrid(int coordinate) {
//        int screenWidth = getMeasuredWidth();
//        double levelArea = screenWidth * 0.9;
//        double cellSize = levelArea / game.getWidthAcross();
//        return cellSize;
        double levelArea = this.screenSize * 0.9;
        double cellSize = levelArea / this.viewModel.getLevelWidth();
        return (int) (cellSize * coordinate);
    }

    protected void drawTheseus(Canvas canvas) {
        this.theseusPointerConversion();
        Log.d("THESEUS XY", "X: " + this.theseusX + " Y: " + this.theseusY);
        this.theseusBitmap = MainActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.theseus, 100, 100);
        canvas.drawBitmap(this.theseusBitmap, new Rect(0, 0, this.theseusBitmap.getWidth(), this.theseusBitmap.getHeight()),
                new Rect(this.theseusX, this.theseusY, this.theseusX + 100, this.theseusY + 100), null);
    }

    private void theseusPointerConversion() {
        this.theseusX = this.levelGrid(this.viewModel.getTheseus().across());
        this.theseusY = this.levelGrid(this.viewModel.getTheseus().down());
    }

    protected void drawMinotaur(Canvas canvas) {
        this.minotaurPointerConversion();
        this.minotaurBitmap = MainActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.minotaur, 100, 100);
        canvas.drawBitmap(this.minotaurBitmap, new Rect(0, 0, this.minotaurBitmap.getWidth(), this.minotaurBitmap.getHeight()),
                new Rect(this.minotaurX, this.minotaurY, this.minotaurX + 100, this.minotaurY + 100), null);
    }

    private void minotaurPointerConversion() {
        this.minotaurX = this.levelGrid(this.viewModel.getMinotaur().across());
        this.minotaurY = this.levelGrid(this.viewModel.getMinotaur().down());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return true;
    }

    private void moveMin(){
        viewModel.moveMinotaur();
        invalidate();
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewModel.moveMinotaur();
                    invalidate();
                }
            }, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            moveMin();
//            Toast.makeText(getContext(), "PAUSE", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            moves += 1;
            switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
                case 1:
                    Log.d(LOGTAG, "up");
                    viewModel.move("up");
                    invalidate();
                    moveMin();
//                    Toast.makeText(getContext(), "up", Toast.LENGTH_SHORT).show();
                    return true;
                case 2:
                    Log.d(LOGTAG, "left");
                    viewModel.move("left");
//                    Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
                    invalidate();
                    moveMin();
                    return true;
                case 3:
                    Log.d(LOGTAG, "down");
                    viewModel.move("down");
//                    Toast.makeText(getContext(), "down", Toast.LENGTH_SHORT).show();
                    invalidate();
                    moveMin();
                    return true;
                case 4:
                    Log.d(LOGTAG, "right");
                    viewModel.move("right");
//                    Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
                    invalidate();
                    moveMin();
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
