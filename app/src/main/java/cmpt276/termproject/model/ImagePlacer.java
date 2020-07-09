package cmpt276.termproject.model;

import android.graphics.Bitmap;

import java.util.List;

// Sub Class for placing Images on the cards, (probably better way to do this, I just used the
// default Extract Method in Android Studio)
public class ImagePlacer {
    private int pos_x;
    private int pos_y;

    private GameManager gameManager = GameManager.getInstance();

    private static final float RADIUS = 350f;
    public static final int IMG_WIDTH = 150;
    public static final int IMG_HEIGHT = 150;

    int getPosX() {
        return pos_x;
    }

    int getPosY() {
        return pos_y;
    }

     public Bitmap placeBitmap(Card card, float x, float y, int offset, int section_size, int i ,  List<Bitmap> bitmaps, int bitmap_index) {
        // TODO: Randomize the initial degree for placing the items so its not so obvious
        // TODO: Randomize the x and y coord offsets a bit

         float min = 0.65f;
         float max = 1.3f;
         double scale = min + Math.random() * (max - min);

         //Get Coordinates for placing bitmap within Circle
        float rad = (float) Math.toRadians( i * section_size + offset);
        int width = (int) (Math.cos(rad) * RADIUS * (0.5f));
        int height = (int) (Math.sin(rad) * RADIUS * (0.5f));


        //Create Bitmap scaled from list of bitmaps
        Bitmap placed_bitmap = Bitmap.createScaledBitmap(bitmaps.get(bitmap_index),
                (int) (IMG_WIDTH * scale),
                (int) (IMG_HEIGHT * scale),
                true);



        // Return Bitmap?

        pos_x = (int) (width + x - placed_bitmap.getWidth() / 2f);
        pos_y = (int) (height + y - placed_bitmap.getHeight() / 2f);

        // Set coordinates of the image for the designated image on the card
        card.setImageCoordinates(i ,new int[]{pos_x,pos_y});

        return placed_bitmap;
    }
}

