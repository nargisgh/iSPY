package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        printDrawPile();
        tempDrawButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        printDrawPile();
    }

    private void setup(){
        manager = GameManager.getInstance();
        manager.createCards();
    }


    private void printDrawPile(){

        TextView txt = findViewById(R.id.draw_pile);
        String str = "";
        for (Card card : manager.getDrawPile()){
            str = str.concat(card.getImages().toString() + "\n");
        }
        txt.setText(str);
    }

    private void drawCard(){
        manager.drawCard();
        TextView txt = findViewById(R.id.discard_pile);
        String str = "";
        //Print Discard Pile
        for (Card card: manager.getDiscardPile()){
            str = str.concat(card.getImages().toString() + "\n");
        }
        txt.setText(str);
        //Update Draw Pile
        printDrawPile();
    }


    private void tempDrawButton(){
        Button btn = findViewById(R.id.draw_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCard();
            }
        });

    }


}
