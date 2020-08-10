/*
Handles creating cards, setting card theme, drawing pictures on cards, etc.
 */
package cmpt276.termproject.model;
import android.annotation.SuppressLint;
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
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrGallery.FlickrImage;
import cmpt276.termproject.model.FlickrGallery.FlickrManager;

/* Initializing when cards are drawn, checking if pile is empty,
 * setting theme, saving card info, and custom listener for interactive game play */

public class CardDrawer extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder  = null;
    private Paint paint = null;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private Bitmap cardBitmap;
    private List<Bitmap> bitmaps;
    private List<String> itemNames;
    private Canvas canvas;
    private static  float RADIUS ;
    private static final int OFFSET = 20;
    private GameListener gameListener;
    private final GameManager gameManager;
    private int counter = 0;
    private final List<Bitmap> imgBitmap = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public interface GameListener {
        void onGameOver();
        void onGameStart();
        void onSfxPlay(boolean failed);
    }

    public void setGameListener(GameListener listener){
        this.gameListener = listener;
    }

    private boolean foundMatch = false;

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
        cardBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.card);
        cardBitmap = Bitmap.createScaledBitmap(cardBitmap,(int)RADIUS * 2 , (int)RADIUS * 2, true);
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        this.context = context;
    }

    // Set the Theme from the available 2 and create bitmap array
    public void setCardTheme(){
        FlickrManager flickrManager = FlickrManager.getInstance();
        bitmaps = new ArrayList<>();
        itemNames = new ArrayList<>();
        Log.e("Theme", " " + gameManager.getTheme());
        int theme = R.array.theme_1_images;
        if (gameManager.getTheme() == 2){
            theme = R.array.theme_2_images;
        }
        else if (gameManager.getTheme() == 3){
            List<FlickrImage> flickrImages = flickrManager.getImageList(getContext());
            Collections.shuffle(flickrImages);
            for (FlickrImage flickrImage: flickrImages){
                bitmaps.add(flickrImage.getImgBitmap());
                itemNames.add(flickrImage.getImgId());
            }
            return;
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
            itemNames.add(name);
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
        if (!gameOver){
            gameListener.onGameOver();
            gameOver = true;
            drawCards();
        }
    }

    public void gameStarted(){
        if (!gameStarted){
            gameListener.onGameStart();
            gameStarted = true;
        }
    }

    public void drawCards(){

        int numImages  = gameManager.getNumberImages();
        int sectionSize = 360 /numImages;

        // Lock Canvas for Drawing
        canvas = surfaceHolder.lockCanvas();

        int x  = (int) (getWidth() / 2f);
        int y = (int) (getHeight() / 2f);

        // RESET THE BOARD
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        RectPlacer rectPlacer = new RectPlacer();
        canvas.drawBitmap(cardBitmap, x + OFFSET, y - RADIUS, null);
        //Draw discard Cards
        for (int i = 0; i < numImages; i ++){
            saveCardInfo(gameManager.getTopDiscardCard(), i, rectPlacer, (int) (x + RADIUS + OFFSET), y , sectionSize);
        }

        if (gameManager.getDrawPile().size() !=  0 ) {
            canvas.drawBitmap(cardBitmap, x - (2 * RADIUS) - OFFSET , y - RADIUS, null);
            //Draw Draw Card
            for (int i = 0; i < numImages; i++) {
                saveCardInfo(gameManager.getTopDrawCard(), i, rectPlacer, (int) (x - RADIUS - OFFSET), y, sectionSize);
            }
        }

        //Draw Card Stacks
        //Draw Pile
        int cardPileOffset  = 24;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        Bitmap deckCard = BitmapFactory.decodeResource(getResources(), R.drawable.deck_card);

        Bitmap scaledCard = Bitmap.createScaledBitmap(deckCard,deckCard.getWidth()/8, deckCard.getHeight()/8,true);
        for (int i = 0; i < gameManager.getDrawPile().size(); i++){
            canvas.drawBitmap(scaledCard,x - (2 * RADIUS) - scaledCard.getWidth() - OFFSET, y + RADIUS - i * cardPileOffset, paint);
        }

        //Discard Pile
        for (int i = 0; i < gameManager.getDiscardPile().size(); i++){
            canvas.drawBitmap(scaledCard,x + (2 * RADIUS) + OFFSET, y + RADIUS - i * cardPileOffset, paint);
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
        Bitmap mergedBitmap = mergeBitmaps(gameManager.getTopDiscardCard(),numImages,sectionSize,rectPlacer);
        imgBitmap.add(mergedBitmap);

    }

    public void saveCardInfo(Card card, int i, RectPlacer rectPlacer, int x , int y , int sectionSize){

        int rotation = card.getRotation(i);
        double scale = card.getScale(i);
        int dataIndex = card.getImages().get(i);
        // Create Rect
        Rect rect = rectPlacer.placeRect( RADIUS, x, y, sectionSize,i, scale);

        card.setName(i, itemNames.get(dataIndex));
        float textSize = 3f/( 360f / sectionSize );

        //Set Paint settings
        Paint rectPaint = new Paint();

        rectPaint.setTextSize(48f * textSize);
        if (scale > 0) {
            rectPaint.setTextSize(52f * textSize * (float) scale);
        }

        rectPaint.setTextAlign(Paint.Align.CENTER);
        Bitmap bitmap = Bitmap.createBitmap(bitmaps.get(dataIndex));

        //Rotate canvas
        canvas.save();
        canvas.translate(rectPlacer.getPosX(), rectPlacer.getPosY());
        canvas.rotate(rotation);

        //Draw text/img
        if (card.getIsText(i)){
            canvas.drawText(card.getName(i), 0, 0, rectPaint);
        }
        else {
            rect.offset(-rectPlacer.getPosX(), -rectPlacer.getPosY());
            canvas.drawBitmap(bitmap, null, rect,null);
        }
        //Restore canvas after rotation
        canvas.restore();

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
            if (gameOver || gameManager.getDrawPile().size() == 0) {
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
                    Card discardCard = gameManager.getTopDiscardCard();

                    for (int discardImage : discardCard.getImages()) {
                        if (image == discardImage) {
                            // Image has been found, Allow for drawing of next card
                            foundMatch = true;
                            gameListener.onSfxPlay(false);
                        }
                    }
                    if (!foundMatch) {
                        gameListener.onSfxPlay(true);
                    }
                }
            }
        }

        if (action == MotionEvent.ACTION_UP){
            if (foundMatch){
                gameManager.drawCard();
                if (gameManager.getDrawPile().size() == 0) {
                    gameOver();
                }
                else {
                    drawCards();
                    foundMatch = false;
                }
                return true;
            }
        }
      return true;
    }

    // storing image into gallery
    // https://stackoverflow.com/questions/8560501/android-save-image-into-gallery
    public static void storeImage(Bitmap bitmap, int card_counter) {
        HighScores highScores = new HighScores();
        String path = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
        File directory = new File(path);
        directory.mkdirs();
        String time = highScores.getCurrentDateTime();
        String fileName = "Card" + card_counter + "_" + time + ".png";
        File file = new File(directory, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, null, null);

    }

    // https://stackoverflow.com/questions/31813638/how-to-merge-bitmaps-in-android
    public Bitmap mergeBitmaps(Card card, int numImages, int sectionSize, RectPlacer rectPlacer) {

        Bitmap bitmap = Bitmap.createBitmap(cardBitmap.getWidth(), cardBitmap.getHeight(), cardBitmap.getConfig());
        canvas = new Canvas(bitmap);
        canvas.drawBitmap(cardBitmap, 0, 0, null);

        for (int i = 0; i < numImages; i++) {
            saveCardInfo(card, i, rectPlacer, cardBitmap.getWidth() / 2, cardBitmap.getHeight() / 2, sectionSize);
        }
        return bitmap;
    }

    public void exportCardImgs(){
        for(int i = 0; i< imgBitmap.size(); i++) {
            storeImage(imgBitmap.get(i), counter);
            counter++;
        }

    }

    public void deleteCardImgs(){
        for(int i = 0; i< imgBitmap.size(); i++) {
            imgBitmap.get(i).recycle();
        }

    }
}