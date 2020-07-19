package cmpt276.termproject.model;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;

public class CardItem {

    private int item;
    private int[] item_coords = new int[2];
    private Bitmap bitmap;
    private boolean isText;

    public CardItem(int item){
        this.item = item;
    }

    public void setItemCoords(int [] coords) {
        item_coords = coords;
    }

    public int getItemX(){
        return item_coords[0];
    }

    public int getItexY(){
        return item_coords[1];
    }

    public int getItem(){
        return  item;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getItemBitmaps(){
        return this.bitmap;
    }
}
