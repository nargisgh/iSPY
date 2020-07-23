 /*
Sub Class for placing images on the cards
 */
package cmpt276.termproject.model;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import java.util.List;

 /* Sub Class for placing Images on the cards, (probably better way to do this, I just used the
  default Extract Method in Android Studio) */
public class RectPlacer {
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

     public Rect placeRect(float RADIUS , float x, float y, int offset, int section_size, int i) {
        // Scale Randomizing
        float min = 0.6f;
        float max = 1.2f;
        double scale = min + Math.random() * (max - min);

        int img_rad = (int) (RADIUS / 3f);

        //Rotation randomizing
        int degree = (int) (Math.random() * 360);

         //Get Coordinates for placing bitmap within Circle
        float rads = (float) Math.toRadians( i * section_size + offset);
        int width = (int) (Math.cos(rads) * RADIUS * (0.45f));
        int height = (int) (Math.sin(rads) * RADIUS * (0.45f));



         pos_x = (int) (width + x );
         pos_y = (int) (height + y );


        Rect rect = new Rect(pos_x - img_rad,
                pos_y - img_rad,
                pos_x + img_rad,
                pos_y + img_rad);

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);



        //FOR REMOVAL
//        //Create Bitmap scaled from list of bitmaps
//        Bitmap placed_bitmap = Bitmap.createScaledBitmap(bitmaps.get(bitmap_index),
//                (int) (img_rad * scale),
//                (int) (img_rad * scale),
//                true);

//        pos_x = (int) (width + x - placed_bitmap.getWidth() / 2f);
//        pos_y = (int) (height + y - placed_bitmap.getHeight() / 2f);

//        placed_bitmap = Bitmap.createBitmap(placed_bitmap,
//                0, 0,
//                (int) (img_rad * scale),
//                (int) (img_rad * scale),
//                matrix , true);



        return rect;
    }
}