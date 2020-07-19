/*
Handles card properties and sets the size, orientation and position of images on the cards.
 */
package cmpt276.termproject.model;
import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;

/* Setting up Card attributes - Images, Coordinates, Size */

public class Card {


    private List<CardItem> cardItems = new ArrayList<>();

    public Card(List<Integer> images){
        for (int img: images){
            cardItems.add(new CardItem(img));
        }
    }

    public void setImageCoordinates(int image_idx, int[] coordinates){
        cardItems.get(image_idx).setItemCoords(coordinates);
    }

    public int getImageX(int index){
        return cardItems.get(index).getItemX();
    }

    public int getImageY(int index){
        return cardItems.get(index).getItexY();
    }

    public List<Integer> getImages(){
        List<Integer> items = new ArrayList<>();
        for (CardItem cardItem: cardItems){
            items.add(cardItem.getItem());
        }
        return items;
    }

    public void setImageBitmaps(int index, Bitmap bitmap){
        cardItems.get(index).setBitmap(bitmap);
    }

    public List<Bitmap> getImageBitmaps(){
        List<Bitmap> bitmaps = new ArrayList<>();
        for (CardItem cardItem: cardItems){
            bitmaps.add(cardItem.getItemBitmaps());
        }
        return bitmaps;
    }
}