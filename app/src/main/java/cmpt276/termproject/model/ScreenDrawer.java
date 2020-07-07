package cmpt276.termproject.model;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cmpt276.termproject.R;

public class ScreenDrawer extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder  = null;
    private Paint paint = null;
    private GameManager gameManager;

    private List<Bitmap> bitmaps;
    Canvas canvas;

    private static final float RADIUS = 350f;

    //TODO : Draw the Draw and Discard Piles




    public ScreenDrawer(Context context) {
        super(context);
        //setFocusable(true);

        if (surfaceHolder == null){
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
        }

        if (paint == null){
            paint = new Paint();
            paint.setColor(Color.DKGRAY);
            paint.setStyle(Paint.Style.FILL);
        }

        gameManager = GameManager.getInstance();

        setCardTheme();

        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }


    // Set the Theme from the available 2 and create bitmap array
    public void setCardTheme(){
        int theme = gameManager.getTheme();
        bitmaps = new ArrayList<>();

        TypedArray typedArray = getResources().obtainTypedArray(R.array.theme_1_images);
        if (gameManager.getTheme() == 2){
            typedArray = getResources().obtainTypedArray(R.array.theme_2_images);
        }
        for (int i = 0;  i < typedArray.length(); i ++) {
            Bitmap decoded_bitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(i, -1));
            bitmaps.add(decoded_bitmap);
        }

        typedArray.recycle();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawCards();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // THIS IS WHERE ANIMATIONS HAPPEN !!!!
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    public void drawCards(){
        Card topDiscardCard = gameManager.getTopDiscardCard();
        Card topDrawCard = gameManager.getTopDrawCard();

        int num_images  = gameManager.getTopDiscardCard().getImages().size();
        int section_size = 360 /num_images;

        // Lock Canvas for Drawing
        canvas = surfaceHolder.lockCanvas();

        int x  = (int) (getWidth() / 2f);
        int y = (int) (getHeight() / 2f);

        canvas.drawCircle(x + RADIUS, y, RADIUS, paint);
        canvas.drawCircle( x - RADIUS , y, RADIUS, paint );

        ImagePlacer imagePlacer= new ImagePlacer();

        //Draw discard Cards
        for (int i = 0; i <num_images; i ++){
            int bitmap_index = gameManager.getTopDiscardCard().getImages().get(i);
            Log.e("Disard index", String.valueOf(bitmap_index));
            Bitmap bitmap = imagePlacer.placeBitmap(x - RADIUS, y, section_size, i, bitmaps,bitmap_index);
            canvas.drawBitmap(bitmap, imagePlacer.getPosX(),imagePlacer.getPosY(),null);
        }

        //Draw Draw Card
        for (int i = 0; i <num_images; i ++){
            int bitmap_index = gameManager.getTopDrawCard().getImages().get(i);
            Log.e("Draw index", String.valueOf(bitmap_index));
            Bitmap bitmap = imagePlacer.placeBitmap(x + RADIUS, y, section_size, i, bitmaps,bitmap_index);
            canvas.drawBitmap(bitmap, imagePlacer.getPosX(),imagePlacer.getPosY(),null);
        }



        surfaceHolder.unlockCanvasAndPost(canvas);
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (action == MotionEvent.ACTION_DOWN) {

        // Get the Image touched
//                if (x > pos_x && x < pos_x + width && y > pos_y && y < pos_y + height) {
//
//
//
//                }
        }
        return true;
    }




}
