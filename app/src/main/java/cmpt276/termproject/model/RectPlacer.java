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


    int getPosX() {
        return pos_x;
    }

    int getPosY() {
        return pos_y;
    }

     public Rect placeRect(float RADIUS , float x, float y, int offset, int section_size, int i) {
        //Scale Factor
        double num_imgs = 360f / section_size;
        double card_scale_fac = 3 / (num_imgs + 0.5f);

        double card_center_offset = (1.25f / num_imgs * num_imgs);

        int img_rad = (int) (RADIUS / 3f);

        //Rotation randomizing
        int degree = (int) (Math.random() * 360);

         //Get Coordinates for placing bitmap within Circle
        float rads = (float) Math.toRadians( i * section_size + offset);
        int width = (int) (Math.cos(rads) * RADIUS * (0.45f));
        int height = (int) (Math.sin(rads) * RADIUS * (0.45f));

        pos_x = (int) (width * card_center_offset + x );
        pos_y = (int) (height * card_center_offset + y );

        Rect rect = new Rect(pos_x - (int)(img_rad * card_scale_fac),
                pos_y - (int)(img_rad * card_scale_fac),
                pos_x + (int)(img_rad * card_scale_fac),
                pos_y + (int)(img_rad * card_scale_fac));

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        return rect;
    }
}