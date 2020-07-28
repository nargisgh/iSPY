package cmpt276.termproject.model;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* Generating card pile, shuffling of cards
 and passing info through singleton method */

/// Translated into Java from the python file that was provided at :
/// https://github.com/WRadigan/pySpot-It

public class CardManager {

    private List<int[]> card_list;
    private static CardManager instance;

    //Use singleton to share the data between classes
    private CardManager(){
    }
    public static CardManager getInstance(){
        if (instance == null){
            instance = new CardManager();
        }
        return instance;
    }

    public List<int[]> generateCards(int p){
        int min_factor = 2;

        for (;  min_factor < 1 + (int)(Math.pow(p, 0.5f)); min_factor ++ ){
            if (p % min_factor == 0) {
                break;
            }
        }

        if (min_factor == 1 + (int)(Math.pow(p, 0.5f))){
            min_factor = p;
        }

        List<int[]> cards = new ArrayList<>();
        for (int i = 0; i < p; i ++){
            int[] temp = new int [p + 1];
            int j;
            for (j = 0; j < p ; j ++){
                temp[j] = i * p + j;
            }
            temp[j] = p*p;
            cards.add(temp);
        }

        for (int i = 0 ; i < min_factor; i ++){
            int j;
            for (j = 0; j < p ; j ++){
                int[] temp = new int [p + 1 ];
                for (int k = 0; k < p; k ++){
                    temp[k] = k * p + (j + i * k ) % p ;
                }
                temp[p] = (p * p) + 1 + i;
                cards.add(temp);
            }
        }

        int[] temp = new int[min_factor + 1];
        for (int i = 0 ; i < min_factor + 1; i ++){
            temp[i] = p * p + i;
        }
        cards.add(temp);
        card_list = cards;

        // Shuffle the cards before returning them
        Collections.shuffle(card_list);

        return card_list;
    }
}