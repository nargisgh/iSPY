package cmpt276.termproject.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameManager {

    private static GameManager instance;

    private List<Card> draw_pile;

    private List<Card> discard_pile;

    int order = 2;

    private List<int[]> draw;
    //private int[][] draw = new int[][]{ {0,1,2}, {0,2,5}, {0,3,6}, {1,2,6},
                                       // {1,3,5}, {2,3,5}, {4,5,6}};

    // Singleton for the Manager
    private GameManager (){
    }

    public static GameManager getInstance(){
        if (instance == null)
            instance = new GameManager();
        return  instance;
    }

    public List<Card> getDrawPile(){
        return draw_pile;
    }

    public List<Card> getDiscardPile(){
        return discard_pile;
    }



    public void updateCards(){
        //Function for updating  the cards from the draw configuration that is given,
        // Since the draw is of order 2 currently, we can have the int[][] be static,
        // May have to change the draw array to be fetched from a JSON so we don't
        // have to worry about generating it.

        draw_pile = new ArrayList<>();
        discard_pile = new ArrayList<>();
        for (int[] imgs : draw) {
            List<Integer> img_list = new ArrayList<>();
            for (int img : imgs) {
                img_list.add(img);
            }
            draw_pile.add(new Card(img_list));
        }
    }


    public void createCards(){

        DrawGenerator card_generator = new DrawGenerator();
        draw = card_generator.generateCards(order);

        updateCards();


        // Shuffle the draw pile so
        Collections.shuffle(draw_pile);
    }




    public void  drawCard() {
        // Draw a card if the draw pile is has cards left and place it into the discard pile
        // and remove the card from the draw pile

        if (draw_pile.size() > 0) {
            discard_pile.add(0,draw_pile.get(0));
            draw_pile.remove(0);
        }



    }

    public boolean matchItemOnCards(int item){
        // Loop through all the items in the topmost discard_pile card and check if the
        // Click item is on that card => Return True if it is found, other wise return false

        for (int discard_item: discard_pile.get(0).getImages()){
            if (item == discard_item){
                return true;
            }
        }
        return false;
    }



}
