 /*
Sub Class for placing images on the cards
 */
package cmpt276.termproject.model;
import android.graphics.Rect;

 /* Sub Class for placing Images on the cards, (probably better way to do this, I just used the
  default Extract Method in Android Studio) */
public class RectPlacer {
    private int posX;
    private int posY;

    int getPosX() {
        return posX;
    }

    int getPosY() {
        return posY;
    }

     public Rect placeRect( float RADIUS , float x, float y, int sectionSize, int i, double scale) {
        //Scale Factor
        double numImgs = 360f / sectionSize;
        double cardScaleFac = 3 / (numImgs + 0.5f);
        double cardCenterOffset = (1.25f / numImgs * numImgs);
        int imgRad = (int) (RADIUS / 3f) ;
        if (scale > 0){
            imgRad = (int)(imgRad * scale);
        }

         //Get Coordinates for placing bitmap within Circle
        float rads = (float) Math.toRadians( i * sectionSize );
        int width = (int) (Math.cos(rads) * RADIUS * (0.45f) );
        int height = (int) (Math.sin(rads) * RADIUS * (0.45f) );

        posX = (int) (width * cardCenterOffset + x );
        posY = (int) (height * cardCenterOffset + y );


         Rect rect = new Rect(posX - (int) (imgRad * cardScaleFac),
                 posY - (int) (imgRad * cardScaleFac),
                 posX + (int) (imgRad * cardScaleFac),
                 posY + (int) (imgRad * cardScaleFac));

         return rect;
    }

 }