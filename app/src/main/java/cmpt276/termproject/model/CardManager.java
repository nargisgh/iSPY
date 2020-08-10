package cmpt276.termproject.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Generating card pile, shuffling of cards
 and passing info through singleton method */

/// Translated into Java from the python file that was provided at :
/// https://github.com/WRadigan/pySpot-It

public class CardManager {

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
        int minFactor = 2;

        for (;  minFactor < 1 + (int)(Math.pow(p, 0.5f)); minFactor ++ ){
            if (p % minFactor == 0) {
                break;
            }
        }

        if (minFactor == 1 + (int)(Math.pow(p, 0.5f))){
            minFactor = p;
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

        for (int i = 0 ; i < minFactor; i ++){
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

        int[] temp = new int[minFactor + 1];
        for (int i = 0 ; i < minFactor + 1; i ++){
            temp[i] = p * p + i;
        }
        cards.add(temp);

        // Shuffle the cards before returning them
        Collections.shuffle(cards);

        return cards;
    }
}