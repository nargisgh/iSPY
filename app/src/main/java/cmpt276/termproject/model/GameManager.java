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
    private List<Card> drawPile;
    private List<Card> discardPile;
    private int drawPileSize;
    private boolean imgsTextMode;
    private int order = 2;
    private int theme = 1;
    private List<int[]> draw;
    private int difficultyMode;

    // Singleton for the Manager
    private GameManager (Context context) {
        imgsTextMode = true;
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
        drawPileSize = Integer.parseInt(sharedPreferences.getString("Size", "0"));
        imgsTextMode = Boolean.parseBoolean(sharedPreferences.getString("Mode", "False"));
        Log.e("Order", order + " " + drawPileSize + " " + imgsTextMode);
        difficultyMode = sharedPreferences.getInt("Difficulty", 0);

    }

    public boolean getMode(){
        return imgsTextMode;
    }

    public Card getTopDrawCard(){
        return drawPile.get(0);
    }

    public Card getTopDiscardCard(){
        return discardPile.get(0);
    }

    public List<Card> getDrawPile(){
        return drawPile;
    }

    public List<Card> getDiscardPile(){
        return discardPile;
    }

    public int getNumberImages(){
        return discardPile.get(0).getImages().size();
    }

    public void updateCards(){
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        for (int[] imgs : draw) {
            List<Integer> imgList = new ArrayList<>();
            for (int img : imgs) {
                imgList.add(img);
            }
            drawPile.add(new Card(imgList,difficultyMode));
        }
    }

    public void createCards(){
        CardManager cardGenerator = CardManager.getInstance();
        draw = cardGenerator.generateCards(order);

        //Keeps only draw_pile_size elements in the list
        Collections.shuffle(draw);
        if (drawPileSize != 0) {
            int newSize = draw.size() - drawPileSize;
            for (int i = 0; i < newSize ; i ++ ){
                draw.remove(draw.size()- 1);
            }
        }

        updateCards();

        //Set the Card Mode to Text Randomly
        if (imgsTextMode) {
            for (Card card : drawPile) {
                for (int i = 0; i < card.getImages().size(); i++) {
                    Random bool = new Random();
                    card.setIsText(i, bool.nextBoolean());
                }
            }
        }

        //Shuffle Images on the cards
        for (Card card: drawPile){
            Collections.shuffle(card.getImages());
        }
        //Draw first card at start
        drawCard();
    }

    public void  drawCard() {
        // Draw a card if the draw pile is has cards left and place it into the discard pile
        // and remove the card from the draw pile
        if (drawPile.size() > 0) {
            discardPile.add(0, drawPile.get(0));
            drawPile.remove(0);
        }
    }

    public void setTheme(int theme){
        this.theme = theme;
    }

    public int getTheme() {
        return theme;
    }

    public int getDrawPileSize(){return drawPileSize;}
}