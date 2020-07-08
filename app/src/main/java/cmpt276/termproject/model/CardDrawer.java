package cmpt276.termproject.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cmpt276.termproject.R;

public class CardDrawer extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder  = null;
    private Paint paint = null;
    private GameManager gameManager;
    private boolean game_over = false;

    private List<Bitmap> bitmaps;
    Canvas canvas;

    private static final float RADIUS = 350f;

    //TODO : Draw the Draw and Discard Piles




    public CardDrawer(Context context) {
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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    public void gameOver(){
        //Update Canvas on Game Over
        if (!game_over){
            game_over = true;
            drawCards();
        }
    }


    public void drawCards(){

        int num_images  = gameManager.getNumberImages();
        int section_size = 360 /num_images;

        // Lock Canvas for Drawing
        canvas = surfaceHolder.lockCanvas();

        int x  = (int) (getWidth() / 2f);
        int y = (int) (getHeight() / 2f);




        paint.setColor(Color.DKGRAY);

        ImagePlacer imagePlacer= new ImagePlacer();

        canvas.drawCircle(x + RADIUS, y, RADIUS, paint);
        //Draw discard Cards
        for (int i = 0; i <num_images; i ++){
            saveCardInfo(gameManager.getTopDiscardCard(), i, imagePlacer, (int) (x + RADIUS), y , section_size);
        }

        //When Game is over paint over old circle
        if (game_over){
            paint.setColor(Color.TRANSPARENT);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            canvas.drawCircle(x - RADIUS,y, RADIUS, paint );
        }

        if (gameManager.getDrawPile().size() !=  0 ) {
            canvas.drawCircle( x - RADIUS , y, RADIUS, paint );
            //Draw Draw Card
            for (int i = 0; i < num_images; i++) {
                saveCardInfo(gameManager.getTopDrawCard(), i, imagePlacer, (int) (x - RADIUS), y, section_size);

            }
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void saveCardInfo(Card card, int i, ImagePlacer imagePlacer , int x , int y , int section_size){
        int bitmap_index = card.getImages().get(i);
        Bitmap bitmap = imagePlacer.placeBitmap(card, x, y, section_size, i, bitmaps,bitmap_index);
        canvas.drawBitmap(bitmap, imagePlacer.getPosX(),imagePlacer.getPosY(),null);
        card.setImageCoordinates(i, new int[]{imagePlacer.getPosX(),imagePlacer.getPosY()});
        card.setImageBitmaps(i, bitmap);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            if ( game_over){
                return true;
            }
            Card card = gameManager.getTopDrawCard();

            for (int i = 0; i < gameManager.getNumberImages(); i ++){
                Bitmap bitmap = card.getImageBitmaps().get(i);
                int pos_x = card.getImageX(i);
                int pos_y = card.getImageY(i);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                int image = card.getImages().get(i);
                //Check if click an Image
                if (x > pos_x && x < pos_x + width && y > pos_y && y < pos_y + height) {
                    Card discard_card = gameManager.getTopDiscardCard();
                    for (int discard_image : discard_card.getImages()) {
                        if (image == discard_image) {
                            // Image has been found, draw next card
                            gameManager.drawCard();
                            drawCards();
                        }
                    }
                }
            }

        }

        if (action == MotionEvent.ACTION_UP){
            if (gameManager.getDrawPile().size() == 0) {
                gameOver();
                return true;
            }
        }
        return true;
    }




}
