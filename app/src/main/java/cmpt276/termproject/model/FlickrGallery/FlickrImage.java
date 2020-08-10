/*
Image object for the Flickr Images, stores ID and bitmap so they can be referenced.
*/
package cmpt276.termproject.model.FlickrGallery;
import android.graphics.Bitmap;

public class FlickrImage {

    private final String imgId;
    private final Bitmap imgBitmap;

    public FlickrImage(String ID, Bitmap bitmap){
        imgId = ID;
        imgBitmap = bitmap;
    }

    public String getImgId(){
        return imgId;
    }

    public Bitmap getImgBitmap(){
        return imgBitmap;
    }
}