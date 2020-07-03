package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.print.PrinterId;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

import cmpt276.termproject.R;
import cmpt276.termproject.model.Card;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.HighScores;

public class PlayActivity extends AppCompatActivity {

    SharedPreferences preferences;
    private GameManager manager;
    int score;
    String name;
    String dateTime;
    String entry;
    String time;
    private Chronometer chronometer;
    EditText inputName;

    HighScores highscore = HighScores.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        inputName = findViewById(R.id.inputName);
        inputName.setFocusable(false);
        chronometer = findViewById(R.id.stopwatch);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
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


    //add entry of name etc for Highscores.
    private void addHighScore() {
        inputName.setFocusableInTouchMode(true);
        name = inputName.getText().toString();
        int elapsed = ((int)(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
        LocalTime score = LocalTime.ofSecondOfDay(elapsed);
        String time = score.toString();
        Toast.makeText(getApplicationContext(),time,Toast.LENGTH_SHORT).show();
        if(name.length()!=0){
            entry = (""+time+" "+name+" "+highscore.getCurrentDateTime());
            Toast.makeText(getApplicationContext(),entry,Toast.LENGTH_SHORT).show();
            highscore.setNewValues(PlayActivity.this, entry);
        }
        /*
         * name = getUser nickname;
         * current time = time user starts playing, call getCurrentTime();
         * score = what timer collects how long they play for, after finally finding all images ends
         *
         * entry = (score+ " "+name+ " " + highscore.getCurrentDate() +" at "+ currentTime);
         * highscore.setNewValues(PlayActivity.this, entry);
         *
         * */

    }


    private void setup(){
        manager = GameManager.getInstance();
        manager.createCards();
        setupBackButton();
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
        if(str == ""){
            chronometer.stop();
            addHighScore();
        }
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

    private void setupBackButton(){
        Button back_btn = findViewById(R.id.play_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputName.setFocusableInTouchMode(true);
                name = inputName.getText().toString();
                if(name.length()!=0){
                    int elapsed = ((int)(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
                    LocalTime score = LocalTime.ofSecondOfDay(elapsed);
                    time = score.toString();
                    dateTime = highscore.getCurrentDateTime();
                    Toast.makeText(getApplicationContext(),time,Toast.LENGTH_SHORT).show();
                    entry = (""+time+" "+name+" "+dateTime);
                    Toast.makeText(getApplicationContext(),entry,Toast.LENGTH_SHORT).show();
                    highscore.setNewValues(PlayActivity.this, entry);

                    Intent gameInfo = new Intent(PlayActivity.this, PopUp.class);
                    gameInfo.putExtra("name", name);
                    gameInfo.putExtra("dateTime", dateTime);
                    gameInfo.putExtra("time",time);
                    startActivity(gameInfo);
                }

                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, PlayActivity.class);
        return intent;
    }

}
