package cmpt276.termproject.model;

import java.util.ArrayList;
import java.util.List;

/// Translated into Java from the python file that was provided at :
/// https://github.com/WRadigan/pySpot-It


public class DrawGenerator {


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

        List<int[]> cards = new ArrayList<int[]>();
        for (int i = 0; i < p; i ++){
            int[] temp = new int [p + 1];
            int j = 0;
            for (j = 0; j < p ; j ++){
                temp[j] = i * p + j;
            }
            temp[j] = p*p;
            cards.add(temp);
        }
        for (int i = 0 ; i < min_factor; i ++){
            int j = 0;
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
        return cards;
    }

}
