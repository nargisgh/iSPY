package cmpt276.termproject.model;

import java.util.ArrayList;
import java.util.List;

import cmpt276.termproject.R;

public class Card {

    private List<Integer> images;
    private List<int[]> image_coordinates = new ArrayList<>();

    public Card(List<Integer> images){
        this.images = images;
        image_coordinates.add(new int[] {0,0});
    }

    public void setImageCoordinates(int image_idx, int[] coordinates){
        this.image_coordinates.add(image_idx, coordinates);
    }

    public List<int[]> getImageCoordinates(){
        return image_coordinates;
    }

    public List<Integer> getImages(){
        return images;
    }

}
