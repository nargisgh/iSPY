package cmpt276.termproject.model;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameManager {

    private static GameManager instance;

    private List<Card> draw_pile;

    private List<Card> discard_pile;

    int order = 2;
    int theme = 1;

    private List<int[]> draw;


    // Singleton for the Manager
    private GameManager (){
    }

    public static GameManager getInstance(){
        if (instance == null)
            instance = new GameManager();
        return  instance;
    }


    public Card getTopDrawCard(){
        return draw_pile.get(0);
    }

    public Card getTopDiscardCard(){
        return discard_pile.get(0);
    }

    public List<Card> getDrawPile(){
        return draw_pile;
    }

    public List<Card> getDiscardPile(){
        return discard_pile;
    }

    public int getNumberImages(){
        return discard_pile.get(0).getImages().size();
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
        CardManager card_generator = CardManager.getInstance();
        draw = card_generator.generateCards(order);

        updateCards();
        // Shuffle the draw pile
        Collections.shuffle(draw_pile);
        //Shuffle Images on the cards
        for (Card card: draw_pile){
            Collections.shuffle(card.getImages());
        }
        //Draw first card at start
        drawCard();
    }


    public void  drawCard() {
        // Draw a card if the draw pile is has cards left and place it into the discard pile
        // and remove the card from the draw pile
        if (draw_pile.size() > 0) {
            discard_pile.add(0,draw_pile.get(0));
            draw_pile.remove(0);
        }
    }


    public void setTheme(int theme){
        this.theme = theme;
    }

    public int getTheme() {
        return theme;
    }

}
