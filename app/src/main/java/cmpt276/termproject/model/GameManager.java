/*
Handles the general game play: draw pile, discard pile, cards, etc.
 */
package cmpt276.termproject.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameManager {

    /* Getters for the Card pile
    * Updating the cards from the drawn pile
    * Setting a consistent
    */
    private static GameManager instance;
    private List<Card> draw_pile;
    private List<Card> discard_pile;

    private int draw_pile_size = 3;
    private boolean imgs_text_mode;
    private int order = 2;
    private int theme = 1;
    private List<int[]> draw;

    // Singleton for the Manager
    private GameManager () {
        //TODO: Get text/img mode here
        imgs_text_mode = true;
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

        //Keeps only draw_pile_size elements in the list
        Collections.shuffle(draw);
        if (draw_pile_size != 0) {
            int new_size = draw.size() - draw_pile_size;
            for (int i = 0; i < new_size ; i ++ ){
                draw.remove(draw.size()- 1);
            }
        }

        updateCards();

        //Set the Card Mode to Text Randomly
        if (imgs_text_mode) {
            for (Card card : draw_pile) {
                for (int i = 0; i < card.getImages().size(); i++) {
                    Random bool = new Random();
                    card.setIsText(i, bool.nextBoolean());
                }
            }
        }

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
