package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.time.LocalTime;

import cmpt276.termproject.R;
import cmpt276.termproject.model.CardDrawer;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.HighScores;

public class PlayActivity extends AppCompatActivity  {


    SharedPreferences preferences;
    private GameManager gameManager;
    CardDrawer cardDrawerCanvas;

    private GameManager manager;
    int score;
    String name;
    String dateTime;
    String time;
    private Chronometer chronometer;

    public HighScores highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        highscore = HighScores.getInstance();
        chronometer = findViewById(R.id.stopwatch);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        setup();

        cardDrawerCanvas = findViewById(R.id.card_canvas);


    }



    //add entry of name etc for Highscores.



    private void setup(){
        //Setup Game Manager Class
        gameManager = GameManager.getInstance();
        gameManager.createCards();

        setupBackButton();
    }


    //TODO: GAME OVER POPUP
    //TODO: TIMER
    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameManager.getDrawPile().size() == 0){
            //PLACE CODE FOR THE GAME OVER POPUP IN HERE
            int elapsed = ((int)(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
            chronometer.stop();
            LocalTime score = LocalTime.ofSecondOfDay(elapsed);
            time = score.toString();
            dateTime = highscore.getCurrentDateTime();

            Intent gameInfo = new Intent(PlayActivity.this, PopUp.class);
            gameInfo.putExtra("name", name);
            gameInfo.putExtra("dateTime", dateTime);
            gameInfo.putExtra("time",time);
            startActivity(gameInfo);
            finish();

        }
        return super.onTouchEvent(event);

    }*/
    //If I use the touch feature the pop up occurs twice.




    private void setupBackButton(){
        Button back_btn = findViewById(R.id.play_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameManager.getDrawPile().size() == 0){
                    //PLACE CODE FOR THE GAME OVER POPUP IN HERE
                    chronometer.stop();
                    int elapsed = ((int)(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
                   // chronometer.stop();

                    LocalTime score = LocalTime.ofSecondOfDay(elapsed);
                    time = score.toString();
                    dateTime = highscore.getCurrentDateTime();


                    Intent gameInfo = new Intent(PlayActivity.this, PopUp.class);

                    gameInfo.putExtra("name", name);
                    gameInfo.putExtra("dateTime", dateTime);
                    gameInfo.putExtra("time",time);

                    startActivity(gameInfo);
                    finish();



                }

            }
        });
    }





    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, PlayActivity.class);
        return intent;
    }


}
