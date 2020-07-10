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
import cmpt276.termproject.model.MusicManager;

public class PlayActivity extends AppCompatActivity  {

    private GameManager gameManager;
    CardDrawer cardDrawerCanvas;

    String name;
    String dateTime;
    String time;
    String ms;
    float new_time;
    String format;

    private Chronometer chronometer;

    private boolean dialog_open = false;
    private double game_start_time;
    public MusicManager musicManager;
    public HighScores highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        highscore = HighScores.getInstance();

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
                chronometer = findViewById(R.id.stopwatch);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        }
        if (gameManager.getDrawPile().size() == 0 && !dialog_open){
            int elapsed = ((int)(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
            chronometer.stop();

            LocalTime score = LocalTime.ofSecondOfDay(elapsed);
            time = score.toString();
            dateTime = highscore.getCurrentDateTime();
            double elapsed_time = System.currentTimeMillis() - game_start_time;


            ms = String.valueOf((int)elapsed_time % 1000 / 10);
            String seconds = String.valueOf((int)elapsed_time/1000);

            //Ex format: 8.5

            format = seconds + "."+ ms;
            new_time = Float.parseFloat(format);

            popup(dateTime, String.valueOf(new_time));
            dialog_open = true;
        }
        return super.onTouchEvent(event);
    }





    private void popup(final String dateTime,final String time){
        musicManager = MusicManager.getInstance();
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
                    editor.putString("new entry", entry);
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
        //<a href='https://www.freepik.com/free-photos-vectors/background'>Background vector created by starline - www.freepik.com</a>

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
        return new Intent(context, PlayActivity.class);
    }


}
