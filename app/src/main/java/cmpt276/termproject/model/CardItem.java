package cmpt276.termproject.model;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class CardItem {

    private final int item;
    private int[] item_coords = new int[2];
    private Bitmap bitmap;
    private String name;
    private boolean isText;
    private Rect rect;

    public CardItem(int item){
        this.item = item;
    }

    public void setMode(boolean isText){
        this.isText = isText;
    }

    public boolean getMode(){
        return this.isText;
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

    public void setRect(Rect rect){
        this.rect = rect;
    }

    public Rect getRect(){
        return rect;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
