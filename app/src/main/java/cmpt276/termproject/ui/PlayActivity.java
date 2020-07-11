package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.invoke.ConstantCallSite;
import java.time.LocalTime;

import cmpt276.termproject.R;
import cmpt276.termproject.model.CardDrawer;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.MusicManager;
import cmpt276.termproject.model.HighScores;

public class PlayActivity extends AppCompatActivity  {

    private FrameLayout frameLayout;

    ConstraintLayout ps_Layout;
    ConstraintLayout.LayoutParams btn_size;
    private GameManager gameManager;

    String name;
    String dateTime;
    String entry;
    String time;
    private Chronometer chronometer;

    HighScores highscore = HighScores.getInstance();
    public MusicManager musicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        ps_Layout = findViewById(R.id.root);
        ps_Layout.setBackgroundResource(R.drawable.bg_play);


        chronometer = findViewById(R.id.stopwatch);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        musicManager = MusicManager.getInstance();
        setup();


    }





    private void setup(){
        //Setup Game Manager Class
        gameManager = GameManager.getInstance();
        gameManager.createCards();

        frameLayout = findViewById(R.id.frame);

        CardDrawer surfaceView = new CardDrawer(getApplicationContext());

        frameLayout.addView(surfaceView);

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

        btn_size = (ConstraintLayout.LayoutParams) back_btn.getLayoutParams();
        btn_size.width = (getResources().getDisplayMetrics().widthPixels)/5;
        btn_size.height = (getResources().getDisplayMetrics().heightPixels)/8;
        back_btn.setLayoutParams(btn_size);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.play();
                //inputName.setFocusableInTouchMode(true);
                //name = inputName.getText().toString();
                /*if(name.length()!=0){
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
                }*/

                finish();
            }
        });
    }




    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, PlayActivity.class);
        return intent;
    }



}
