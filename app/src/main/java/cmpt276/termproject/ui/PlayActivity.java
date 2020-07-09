package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

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

    private boolean dialog_open = false;
    private double game_start_time;

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


    private void setup(){
        //Setup Game Manager Class
        gameManager = GameManager.getInstance();
        gameManager.createCards();

        setupBackButton();
    }


    //TODO: GAME OVER POPUP
    //TODO: TIMER
    // *@Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            if (gameManager.getDiscardPile().size() == 2) {
                game_start_time = System.currentTimeMillis();
            }
        }
        if (gameManager.getDrawPile().size() == 0 && !dialog_open){
            //PLACE CODE FOR THE GAME OVER POPUP IN HERE
            int elapsed = ((int)(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
            chronometer.stop();
            LocalTime score = LocalTime.ofSecondOfDay(elapsed);
            time = score.toString();
            dateTime = highscore.getCurrentDateTime();

            double elapsed_time = System.currentTimeMillis() - game_start_time;
            Log.e("Time", String.valueOf(elapsed_time));

            popup(dateTime,time);
            dialog_open = true;
        }
        return super.onTouchEvent(event);
    }
    //If I use the touch feature the pop up occurs twice.





    private void popup(final String dateTime,final String time){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_pop_up);

        Button save = dialog.findViewById(R.id.saveBtn);
        final EditText userId = dialog.findViewById(R.id.userId);
        final TextView score_p = dialog.findViewById(R.id.score);
        final TextView dateTime_p = dialog.findViewById(R.id.dateTime);
        score_p.setText(time);
        dateTime_p.setText(dateTime);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userId.getText().toString();
                userId.setText(name);

                if(userId.getText().toString().length()!=0) {
                    userId.setClickable(false);
                    userId.setFocusableInTouchMode(false);
                    userId.setEnabled(false);
                    String entry = ("" + time + "/ " + name + "/" + dateTime);

                    SharedPreferences entry_new = getSharedPreferences("entry", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = entry_new.edit();
                    editor.putString("new entry", entry);
                    editor.apply();
                    dialog.dismiss();
                    finish();
                }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }



    private void setupBackButton(){
        Button back_btn = findViewById(R.id.play_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (gameManager.getDrawPile().size() == 0){
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
                else{finish();}*/
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, PlayActivity.class);
    }


}
