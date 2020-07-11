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

import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import cmpt276.termproject.R;

public class CardDrawer extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder  = null;
    private Paint paint = null;
    private GameManager gameManager;
    private boolean game_over = false;

    private Bitmap card_bitmap;
    private List<Bitmap> bitmaps;
    Canvas canvas;

    private MediaPlayer mp = new MediaPlayer();

    private static final float RADIUS = 350f;
    private static final int OFFSET = 20;


    //TODO : Draw the Draw and Discard Piles


    private boolean found_match = false;


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

        card_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.card);
        card_bitmap = Bitmap.createScaledBitmap(card_bitmap,(int)RADIUS * 2 , (int)RADIUS * 2, true);

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


        // RESET THE BOARD
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        //Draw Card Stacks
        //Draw Pile
        int card_pile_offset  = 30;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        for(int i = 0; i < gameManager.getDrawPile().size(); i++){
            canvas.drawBitmap(card_bitmap,x - (2 * RADIUS) - OFFSET, y - RADIUS - i * card_pile_offset, paint);

        }

        //Discard Pile
        for (int i = 0; i < gameManager.getDiscardPile().size(); i++){
            canvas.drawBitmap(card_bitmap,x + OFFSET, y - RADIUS - i * card_pile_offset, paint);
        }

        ImagePlacer imagePlacer= new ImagePlacer();
        canvas.drawBitmap(card_bitmap, x + OFFSET, y - RADIUS, null);
        //Draw discard Cards
        int offset = (int) (Math.random() * 90);
        for (int i = 0; i <num_images; i ++){
            saveCardInfo(gameManager.getTopDiscardCard(), i, imagePlacer, (int) (x + RADIUS + OFFSET), y, offset , section_size);
        }


        if (gameManager.getDrawPile().size() !=  0 ) {
            canvas.drawBitmap(card_bitmap, x - (2 * RADIUS) - OFFSET , y - RADIUS, null);
            //Draw Draw Card
            offset = (int) (Math.random() * 90);
            for (int i = 0; i < num_images; i++) {
                saveCardInfo(gameManager.getTopDrawCard(), i, imagePlacer, (int) (x - RADIUS - OFFSET), y, offset, section_size);

            }
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void saveCardInfo(Card card, int i, ImagePlacer imagePlacer , int x , int y , int offset,  int section_size){
        int bitmap_index = card.getImages().get(i);
        Bitmap bitmap = imagePlacer.placeBitmap(card, x, y, offset, section_size,i, bitmaps,bitmap_index);
        canvas.drawBitmap(bitmap, imagePlacer.getPosX(),imagePlacer.getPosY(),null);
        card.setImageCoordinates(i, new int[]{imagePlacer.getPosX(),imagePlacer.getPosY()});
        card.setImageBitmaps(i, bitmap);
    }

    public void playClickSound(boolean failed){
        mp = MediaPlayer.create(getContext(),R.raw.fail);
        mp.seekTo(715);
        if (!failed){
            mp = MediaPlayer.create(getContext(), R.raw.success);
            mp.seekTo(290);
        }
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
            }
        });
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            if (game_over || gameManager.getDrawPile().size() == 0) {
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
                            // Image has been found, Allow for drawing of next card
                            found_match = true;
                            playClickSound(false);
                        }
                    }
                    if (!found_match) {
                       playClickSound(true);
                    }
                }
            }

        }

        if (action == MotionEvent.ACTION_UP){
            if (found_match ){
                gameManager.drawCard();
                if (gameManager.getDrawPile().size() == 0) {
                    gameOver();
                }
                else {
                    drawCards();
                    found_match = false;
                }
                return true;
            }
        }
      return true;

    }





}
