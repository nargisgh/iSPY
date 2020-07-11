package cmpt276.termproject.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import cmpt276.termproject.R;

public class Card {

    private List<Integer> images;
    private List<int[]> image_coordinates = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();

    public Card(List<Integer> images){
        this.images = images;
    }

    public void setImageCoordinates(int image_idx, int[] coordinates){
        this.image_coordinates.add(image_idx, coordinates);
    }


    public int getImageX(int index){
        return image_coordinates.get(index)[0];
    }

    public int getImageY(int index){
        return image_coordinates.get(index)[1];
    }

    public List<int[]> getImageCoordinates(){
        return image_coordinates;
    }

    public List<Integer> getImages(){
        return images;
    }

    public void setImageBitmaps(int index, Bitmap bitmap){
        bitmaps.add(index, bitmap);
    }

    public List<Bitmap> getImageBitmaps(){
        return bitmaps;
    }

}
