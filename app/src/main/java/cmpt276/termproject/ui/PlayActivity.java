package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.time.LocalTime;

import cmpt276.termproject.R;
import cmpt276.termproject.model.CardDrawer;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.MusicManager;
import cmpt276.termproject.model.HighScores;

public class PlayActivity extends AppCompatActivity  {

    private FrameLayout frameLayout;

    private GameManager gameManager;
    CardDrawer cardDrawerCanvas;

    String name;
    String dateTime;
    String entry;
    String time;
    private Chronometer chronometer;
    EditText inputName;

    HighScores highscore = HighScores.getInstance();
    public MusicManager musicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        inputName = findViewById(R.id.inputName);
        inputName.setFocusable(false);
        chronometer = findViewById(R.id.stopwatch);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        musicManager = MusicManager.getInstance();
        setup();



        //cardDrawerCanvas = findViewById(R.id.card_canvas);

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
        //Setup Game Manager Class
        gameManager = GameManager.getInstance();
        gameManager.createCards();

        frameLayout = findViewById(R.id.frame);

        CardDrawer surfaceView = new CardDrawer(getApplicationContext());

        frameLayout.addView(surfaceView);
        //surfaceView.setOnTouchListener((View.OnTouchListener) this);


        setupBackButton();

    }


    //TODO: GAME OVER POPUP
    //TODO: TIMER
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameManager.getDrawPile().size() == 0){
            //PLACE CODE FOR THE GAME OVER POPUP IN HERE
            Toast.makeText(getApplicationContext(), "GAME OVER", Toast.LENGTH_SHORT).show();
        }
        return super.onTouchEvent(event);
    }




    private void setupBackButton(){
        Button back_btn = findViewById(R.id.play_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.play();
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
