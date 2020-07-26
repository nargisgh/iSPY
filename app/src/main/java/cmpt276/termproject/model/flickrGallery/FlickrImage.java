package cmpt276.termproject.model.flickrGallery;

import android.graphics.Bitmap;

public class FlickrImage {

    private final String imgID;
    private final Bitmap imgBitmap;

    public FlickrImage(String ID, Bitmap bitmap){
        imgID = ID;
        imgBitmap = bitmap;
    }

    public String getImgID(){
        return imgID;
    }

    public Bitmap getImgBitmap(){
        return imgBitmap;
    }
}
