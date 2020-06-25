package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import cmpt276.termproject.R;
import cmpt276.termproject.model.Card;
import cmpt276.termproject.model.GameManager;

public class MainActivity extends AppCompatActivity {

    private GameManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup
        setup();

        //Temp function to show that all cards are created
        printCards();

    }

    private void setup(){
        manager = GameManager.getInstance();
    }


    private void printCards(){
        manager.createCards();

        TextView txt = findViewById(R.id.print_cards);
        String str = new String("");
        for (Card card : manager.getDrawPile()){
            str = str.concat(card.getImages().toString() + "\n");
            txt.setText(str);
        }


    }

}
