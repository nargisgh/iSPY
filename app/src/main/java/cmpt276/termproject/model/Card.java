package cmpt276.termproject.model;

import java.util.ArrayList;
import java.util.List;

import cmpt276.termproject.R;

public class Card {

    private List<Integer> images;

    public Card(List<Integer> images){
        this.images = images;
    }

    public List<Integer> getImages(){
        return images;
    }

}
