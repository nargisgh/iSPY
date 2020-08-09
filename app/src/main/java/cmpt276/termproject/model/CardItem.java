package cmpt276.termproject.model;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

//Setting card image mode with different size or rotation/position of the images

public class CardItem {

    private final int item;
    private int[] item_coords = new int[2];
    private String name;
    private boolean isText;
    private Rect rect;
    private int rotationAngle;
    private double itemScale;

    public CardItem(int item, int difficulty ){
        this.item = item;
        setDifficulty(difficulty);
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


    public void setDifficulty(int difficulty){
        if (difficulty == 1) {
            rotationAngle = (int) (Math.random() * 360);
        }
        if (difficulty == 2){
            rotationAngle = (int) (Math.random() * 360);
            itemScale = (Math.random() * (1.5f - 0.75f)) + 0.75f;
        }
        Log.e("Rotation", difficulty + "");
    }

    public void setRotationAngle(int angle){
        rotationAngle = angle;
    }

    public int getRotationAngle(){
        return rotationAngle;
    }

    public void setItemScale(double scale){
        itemScale = scale;
    }

    public double getItemScale(){
        return itemScale;
    }

}