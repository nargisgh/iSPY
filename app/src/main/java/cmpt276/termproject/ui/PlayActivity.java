package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import cmpt276.termproject.R;
import cmpt276.termproject.model.CardDrawer;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.HighScores;
import cmpt276.termproject.model.MusicManager;

/* Game Play, displays cards drawn and to match.
Setting Game start and over with custom listener.
Pop Up dialog when game is over */

public class PlayActivity extends AppCompatActivity  {

    ConstraintLayout ps_Layout;
    ConstraintLayout.LayoutParams btn_size;
    private GameManager gameManager;

    String name;
    String dateTime;

    private Chronometer chronometer;

    private double game_start_time;
    public MusicManager musicManager;
    public HighScores highScore;

    private CardDrawer cardDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        highScore = HighScores.getInstance();
        musicManager = MusicManager.getInstance();

        ps_Layout = findViewById(R.id.root);
        ps_Layout.setBackgroundResource(R.drawable.bg_play);

        setup();

        chronometer = findViewById(R.id.stopwatch);
    }


    private void setup(){
        //Setup Game Manager Class
        gameManager = GameManager.getInstance();
        gameManager.createCards();

        FrameLayout frameLayout = findViewById(R.id.frame);

        cardDrawer = new CardDrawer(getApplicationContext());

        setupGameOver();

        frameLayout.addView(cardDrawer);

        setupBackButton();
    }

    public void setupGameOver(){

        cardDrawer.setGameListener(new CardDrawer.GameListener() {
            @Override
            public void onGameOver() {
                int elapsed = ((int)(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
                chronometer.stop();

                dateTime = highScore.getCurrentDateTime();
                double elapsed_time_ms = System.currentTimeMillis() - game_start_time;

                //ms = String.valueOf((int)elapsed_time % 1000 / 10);
                double time = elapsed_time_ms/1000;
                //Ex format: 8.5

                popup(dateTime, String.valueOf(time));
            }

            @Override
            public void onGameStart(){
                game_start_time = System.currentTimeMillis();
                Log.e("Chrono", "started");
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });
    }

    private void popup(final String dateTime,final String time){
        musicManager.play();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_pop_up);
        Button save = dialog.findViewById(R.id.saveBtn);
        Button cancel = dialog.findViewById(R.id.cancelBtn);
        final EditText userId = dialog.findViewById(R.id.userId);
        final TextView score_p = dialog.findViewById(R.id.score);
        final TextView dateTime_p = dialog.findViewById(R.id.dateTime);
        score_p.setText(time);
        dateTime_p.setText(dateTime);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.pause();
                dialog.dismiss();
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userId.getText().toString();
                userId.setText(name);

                if(userId.getText().toString().length()!=0) {
                    userId.setClickable(false);
                    userId.setFocusableInTouchMode(false);
                    userId.setEnabled(false);
                    String entry = (time + "/ " + name + "/" + dateTime);

                    SharedPreferences entry_new = getSharedPreferences("entry", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = entry_new.edit();
                    int counter = entry_new.getInt("counter", 0);
                    counter ++;
                    editor.putString("new entry"+counter, entry);
                    editor.putInt("counter",counter);

                    editor.apply();
                    musicManager.pause();
                    dialog.dismiss();
                    finish();
                }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
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
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, PlayActivity.class);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        musicManager.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(musicManager ==null){
            musicManager.play();
        }
    }
}
