package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;

import cmpt276.termproject.R;
import cmpt276.termproject.model.Card;
import cmpt276.termproject.model.GameManager;

public class MainActivity extends AppCompatActivity {

    private GameManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            if (card == manager.getDrawPile().get(0)) {
                str = str.concat(card.getImages().toString() + "\n");
            }
            else {
                str = str.concat("[x, x, x]\n");
            }
        }
        txt.setText(str);
    }

    private void drawCard(){
        manager.drawCard();
        TextView txt = findViewById(R.id.discard_pile);
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
