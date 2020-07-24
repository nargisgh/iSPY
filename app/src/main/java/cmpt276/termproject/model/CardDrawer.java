/*
Handles creating cards, setting card theme, drawing pictures on cards, etc.
 */
package cmpt276.termproject.model;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import cmpt276.termproject.R;

/* Initializing when cards are drawn, checking if pile is empty,
 * setting theme, saving card info, and custom listener for interactive game play */

public class CardDrawer extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder  = null;
    private Paint paint = null;
    private boolean game_over = false;
    private boolean game_started = false;

    private Bitmap card_bitmap;
    private List<Bitmap> bitmaps;
    private List<String> item_names;


    private Canvas canvas;

    private static  float RADIUS ;
    private static final int OFFSET = 20;

    private GameListener gameListener;
    private GameManager gameManager;

    public interface GameListener {
        void onGameOver();
        void onGameStart();
        void onSfxPlay(boolean failed);
    }

    public void setGameListener(GameListener listener){
        this.gameListener = listener;
    }

    private boolean found_match = false;

    public CardDrawer(Context context) {
        super(context);
        //setFocusable(true);
        this.gameListener = null;

        if (surfaceHolder == null){
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
        }

        if (paint == null){
            paint = new Paint();
            paint.setColor(Color.DKGRAY);
            paint.setStyle(Paint.Style.FILL);
        }

        gameManager = GameManager.getInstance(context);
        setCardTheme();
        RADIUS = context.getResources().getDisplayMetrics().heightPixels / 3.5f;
        card_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.card);
        card_bitmap = Bitmap.createScaledBitmap(card_bitmap,(int)RADIUS * 2 , (int)RADIUS * 2, true);
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    // Set the Theme from the available 2 and create bitmap array
    public void setCardTheme(){
        bitmaps = new ArrayList<>();
        item_names = new ArrayList<>();
        int theme = R.array.theme_1_images;
        if (gameManager.getTheme() == 2){
            theme = R.array.theme_2_images;
        }
        Resources res = getResources();
        List<String> themes = Arrays.asList(res.getStringArray(theme));
        Collections.shuffle(themes);

        //Set bitmaps and text
        for (int i = 0;  i < themes.size(); i ++) {
            String name = themes.get(i);
            int bitmap_id = getResources().getIdentifier( name, "drawable", getContext().getPackageName());
            Bitmap decoded_bitmap = BitmapFactory.decodeResource(res, bitmap_id);
            bitmaps.add(decoded_bitmap);
            item_names.add(name);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawCards();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    public void gameOver(){
        //Update Canvas on Game Over
        if (!game_over){
            gameListener.onGameOver();
            game_over = true;
            drawCards();
        }
    }

    public void gameStarted(){
        if (!game_started){
            gameListener.onGameStart();
            game_started = true;
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



        RectPlacer rectPlacer = new RectPlacer();
        canvas.drawBitmap(card_bitmap, x + OFFSET, y - RADIUS, null);
        //Draw discard Cards
        int offset = (int) (Math.random() * 90);
        for (int i = 0; i <num_images; i ++){
            saveCardInfo(gameManager.getTopDiscardCard(), i, rectPlacer, (int) (x + RADIUS + OFFSET), y, offset , section_size);
        }

        if (gameManager.getDrawPile().size() !=  0 ) {
            canvas.drawBitmap(card_bitmap, x - (2 * RADIUS) - OFFSET , y - RADIUS, null);
            //Draw Draw Card
            offset = (int) (Math.random() * 90);
            for (int i = 0; i < num_images; i++) {
                saveCardInfo(gameManager.getTopDrawCard(), i, rectPlacer, (int) (x - RADIUS - OFFSET), y, offset, section_size);
            }
        }

        //Draw Card Stacks
        //Draw Pile
        int card_pile_offset  = 30;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        for (int i = 0; i < gameManager.getDrawPile().size(); i++){
            canvas.drawBitmap(card_bitmap,x - (2 * RADIUS) - OFFSET, y - RADIUS - i * card_pile_offset, paint);
        }

        //Discard Pile
        for (int i = 0; i < gameManager.getDiscardPile().size(); i++){
            canvas.drawBitmap(card_bitmap,x + OFFSET, y - RADIUS - i * card_pile_offset, paint);
        }


        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void saveCardInfo(Card card, int i, RectPlacer rectPlacer, int x , int y , int offset, int section_size){
        int data_index = card.getImages().get(i);
        Rect rect = rectPlacer.placeRect( RADIUS, x, y, offset, section_size,i);

        card.setName(i, item_names.get(data_index));

        Paint rect_paint = new Paint();
        rect_paint.setTextSize(48f);
        rect_paint.setTextAlign(Paint.Align.CENTER);
        //rect_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        Bitmap bitmap = Bitmap.createBitmap(bitmaps.get(data_index));

        if (card.getIsText(i)){
            canvas.drawText(card.getName(i), rectPlacer.getPosX(), rectPlacer.getPosY(), rect_paint);
            //canvas.drawBitmap(bitmap, null, rect,null);
        }
        else {
            canvas.drawBitmap(bitmap, null, rect,null);
        }
        card.setItemRect(i, rect);
        card.setImageBitmaps(i, bitmap);
        card.setItemCoordinates(i, new int[]{rectPlacer.getPosX(), rectPlacer.getPosY()});

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
                Rect rect = card.getItemRect(i);

                int width = rect.width();
                int height = rect.height();
                //GetItem gives center of rect , subtract half of width to compensate
                int pos_x = card.getItemX(i) - width / 2;
                int pos_y = card.getItemY(i) - height / 2;




                int image = card.getImages().get(i);
                //Check if click an Image
                if (x > pos_x && x < pos_x + width && y > pos_y && y < pos_y + height) {
                    //Log.e("Coords", pos_x+ " " + (pos_x+width) + " " + pos_y + " "  +  (pos_y+height) );

                    if (gameManager.getDiscardPile().size() == 1){
                        gameStarted();
                    }
                    Card discard_card = gameManager.getTopDiscardCard();
                    Log.e("Item", card.getName(i) + " " + card.getIsText(i));

                    for (int discard_image : discard_card.getImages()) {
                        if (image == discard_image) {
                            // Image has been found, Allow for drawing of next card
                            found_match = true;
                            gameListener.onSfxPlay(false);
                        }
                    }
                    if (!found_match) {
                        gameListener.onSfxPlay(true);
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