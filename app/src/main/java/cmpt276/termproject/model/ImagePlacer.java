package cmpt276.termproject.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.List;

// Sub Class for placing Images on the cards, (probably better way to do this, I just used the
// default Extract Method in Android Studio)
public class ImagePlacer {
    private int pos_x;
    private int pos_y;

    private static final float RADIUS = 350f;
    public static final int IMG_WIDTH = 150;
    public static final int IMG_HEIGHT = 150;

    int getPosX() {
        return pos_x;
    }

    int getPosY() {
        return pos_y;
    }

     public Bitmap placeBitmap(float RADIUS ,Card card, float x, float y, int offset, int section_size, int i ,  List<Bitmap> bitmaps, int bitmap_index) {

        // Scale Randomizing
        float min = 0.6f;
        float max = 1.2f;
        double scale = min + Math.random() * (max - min);

        int img_rad = (int) (RADIUS / 2f);

        //Rotation randomizing
        int degree = (int) (Math.random() * 360);

         //Get Coordinates for placing bitmap within Circle
        float rad = (float) Math.toRadians( i * section_size + offset);
        int width = (int) (Math.cos(rad) * RADIUS * (0.45f));
        int height = (int) (Math.sin(rad) * RADIUS * (0.45f));


        //Create Bitmap scaled from list of bitmaps
        Bitmap placed_bitmap = Bitmap.createScaledBitmap(bitmaps.get(bitmap_index),
                (int) (img_rad * scale),
                (int) (img_rad * scale),
                true);

        pos_x = (int) (width + x - placed_bitmap.getWidth() / 2f);
        pos_y = (int) (height + y - placed_bitmap.getHeight() / 2f);

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        placed_bitmap = Bitmap.createBitmap(placed_bitmap,
                0, 0,
                (int) (img_rad * scale),
                (int) (img_rad * scale),
                matrix , true);


        // Set coordinates of the image for the designated image on the card
        card.setImageCoordinates(i ,new int[]{pos_x,pos_y});

        return placed_bitmap;
    }


}

