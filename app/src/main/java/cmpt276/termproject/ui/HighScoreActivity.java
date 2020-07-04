package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import cmpt276.termproject.R;
import cmpt276.termproject.model.HighScores;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class HighScoreActivity extends AppCompatActivity {

    private HighScores highscore;

    private List<TextView> scores = new ArrayList<>();

    private TextView time1;
    private String currentTime;
    TableLayout table;
    TableRow row;
    TextView username;
    TextView score;
    TextView Date_Time;
    private String[] default_scores;

    ArrayList<String> arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);


        highscore = HighScores.getInstance();

        default_scores = getResources().getStringArray(R.array.default_highscores);
        highscore.set_default_scores(HighScoreActivity.this, default_scores);
        arr = highscore.getNewscores(HighScoreActivity.this);
        populateScores();

    }

    private void populateScores() {
        table = findViewById(R.id.table);
        table.setColumnStretchable(0,true);
        table.setColumnStretchable(1,true);
        table.setColumnStretchable(2,true);
        for(int i = 0; i<arr.size();i++){
            row = new TableRow(this);
            username = new TextView(this);
            score = new TextView(this);
            Date_Time = new TextView(this);

            username.setText(arr.get(i).split(" ")[1]);
            username.setGravity(Gravity.CENTER);
            username.setTextSize(25);
            username.setTextColor(Color.WHITE);
            score.setText(arr.get(i).split(" ")[0]);
            score.setGravity(Gravity.CENTER);
            score.setTextSize(25);
            score.setTextColor(Color.WHITE);
            Date_Time.setText(arr.get(i).split(" ")[3]);
            Date_Time.setGravity(Gravity.CENTER);
            Date_Time.setTextSize(15);
            Date_Time.setTextColor(Color.WHITE);
            row.addView(username);
            row.addView(score);
            row.addView(Date_Time);
            table.addView(row);
        }

        setupResetbtn();
        //https://www.youtube.com/watch?v=iSCtFzbC7kA

    }

    /*private void populateScores() {

        LinearLayout layout = findViewById(R.id.highscores_layout);
        layout.removeAllViews();
        for (int i = 0 ; i < arr.size(); i ++) {
            TextView score_text = new TextView(this);
            score_text.setText(arr.get(i));
            score_text.setTextAppearance(R.style.highscore_text);
            layout.addView(score_text);
        }

        for (int i = 0 ; i < scores.size(); i ++){
            scores.get(i).setText(arr.get(i));
        }

        Toast.makeText(getApplicationContext(),highscore.getScore(),Toast.LENGTH_SHORT).show();

        /*time1 = findViewById(R.id.Time1);
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        time1.setText(currentTime);*/

       // setupResetbtn();
/*
    }*/


    // button to reset back to default scores
    private void setupResetbtn() {
        Button reset = findViewById(R.id.highscore_reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highscore.set_default_scores(HighScoreActivity.this,default_scores);
                arr = highscore.get_default_scores(HighScoreActivity.this);
                populateScores();
            }
        });
    }



    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, HighScoreActivity.class);
        return intent;
    }
}