/*
Handles card properties and sets the size, orientation and position of images on the cards.
 */
package cmpt276.termproject.model;
import android.graphics.Bitmap;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.List;

/* Setting up Card attributes - Images, Coordinates, Size */

public class Card {

    private final List<CardItem> cardItems = new ArrayList<>();

    public Card(List<Integer> images){
        for (int img: images){
            cardItems.add(new CardItem(img));
        }
    }

    public void setItemCoordinates(int image_idx, int[] coordinates){
        cardItems.get(image_idx).setItemCoords(coordinates);
    }

    public int getItemX(int index){
        return cardItems.get(index).getItemX();
    }

    public int getItemY(int index){
        return cardItems.get(index).getItexY();
    }

    public List<Integer> getImages(){
        List<Integer> items = new ArrayList<>();
        for (CardItem cardItem: cardItems){
            items.add(cardItem.getItem());
        }
        return items;
    }

    public void setItemRect(int index, Rect rect){
        cardItems.get(index).setRect(rect);
    }

    public Rect getItemRect(int index){
       return cardItems.get(index).getRect();
    }

    public void setImageBitmaps(int index, Bitmap bitmap){
        cardItems.get(index).setBitmap(bitmap);
    }

    public void setIsText(int idx, boolean isText){
        cardItems.get(idx).setMode(isText);
    }

    public boolean getIsText(int idx){
        return cardItems.get(idx).getMode();
    }

    public void setName(int idx, String name) {
        cardItems.get(idx).setName(name);
    }

    public String getName (int idx){
        return cardItems.get(idx).getName();
    }
}