package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrinterId;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cmpt276.termproject.R;
import cmpt276.termproject.model.Card;
import cmpt276.termproject.model.CardDrawer;
import cmpt276.termproject.model.GameManager;

public class PlayActivity extends AppCompatActivity {

    private GameManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        setup();

        //Temp function to show that all cards are created
        printDrawPile();

    }

    @Override
    protected void onResume() {
        super.onResume();
        printDrawPile();
    }



    private void setup(){
        manager = GameManager.getInstance();
        manager.createCards();
        setupBackButton();
    }



    private void printDrawPile(){
        String str = "";
        for (Card card : manager.getDrawPile()){
            if (card == manager.getDrawPile().get(0)) {
                str = str.concat(card.getImages().toString() + "\n");
            }
            else {
                str = str.concat("[x, x, x]\n");
            }
        }
    }

    private void drawCard(){
        manager.drawCard();
        String str = "";
        //Print Discard Pile
        for (Card card: manager.getDiscardPile()){
            if (card == manager.getDiscardPile().get(0)){
                str = str.concat(card.getImages().toString() + "\n");
            }
            else {
                str = str.concat("[x, x, x]\n");
            }
        }
        //Update Draw Pile
        printDrawPile();
    }





    private void setupBackButton(){
        Button back_btn = findViewById(R.id.play_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, PlayActivity.class);
        return intent;
    }

}
