 /*
Handles the general game play: draw pile, discard pile, cards, etc.
 */
package cmpt276.termproject.model;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private static GameManager instance;
    private List<Card> draw_pile;
    private List<Card> discard_pile;
    private int draw_pile_size;
    private boolean imgs_text_mode;
    private int order = 2;
    private int theme = 1;
    private List<int[]> draw;
    private int difficultyMode;

    // Singleton for the Manager
    private GameManager (Context context) {
        imgs_text_mode = true;
        this.context = context;
        setupGameSettings();
    }

    public static GameManager getInstance(Context context){
        if (instance == null)
            instance = new GameManager(context);
        return  instance;
    }

    public void setupGameSettings(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        order = Integer.parseInt(sharedPreferences.getString("Order", "2"));
        draw_pile_size = Integer.parseInt(sharedPreferences.getString("Size", "0"));
        imgs_text_mode = Boolean.parseBoolean(sharedPreferences.getString("Mode", "False"));
        Log.e("Order", order + " " + draw_pile_size + " " + imgs_text_mode);
        difficultyMode = sharedPreferences.getInt("Difficulty", 0);

    }

    public boolean getMode(){
        return imgs_text_mode;
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
        draw_pile = new ArrayList<>();
        discard_pile = new ArrayList<>();
        for (int[] imgs : draw) {
            List<Integer> img_list = new ArrayList<>();
            for (int img : imgs) {
                img_list.add(img);
            }
            draw_pile.add(new Card(img_list,difficultyMode));
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

    public int getDraw_pile_size(){return draw_pile_size;}
}