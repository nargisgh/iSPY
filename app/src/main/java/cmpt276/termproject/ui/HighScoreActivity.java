package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cmpt276.termproject.R;
import cmpt276.termproject.model.HighScores;
import cmpt276.termproject.model.Highscore;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class HighScoreActivity extends AppCompatActivity {

    HighScores highscore = new HighScores();

    TextView score1;
    TextView score2;
    TextView score3;
    TextView score4;
    TextView score5;

    TextView time1;
    String currentTime;

    ArrayList<String> arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        highscore.set_default_scores(HighScoreActivity.this);

        score1 = findViewById(R.id.highscore1);
        score2 = findViewById(R.id.highscore2);
        score3 = findViewById(R.id.highscore3);
        score4 = findViewById(R.id.highscore4);
        score5 = findViewById(R.id.highscore5);

        populateScores();

    }

    private void populateScores() {
        arr = highscore.getNewscores(HighScoreActivity.this);

        score1.setText(arr.get(0));
        score2.setText(arr.get(1));
        score3.setText(arr.get(2));
        score4.setText(arr.get(3));
        score5.setText(arr.get(4));
        Toast.makeText(getApplicationContext(),highscore.getScore(),Toast.LENGTH_SHORT).show();

        /*time1 = findViewById(R.id.Time1);
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        time1.setText(currentTime);*/

        setupResetbtn();

    }


    // button to reset back to default scores
    private void setupResetbtn() {
        Button reset = findViewById(R.id.highscore_reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highscore.set_default_scores(HighScoreActivity.this);
                populateScores();

            }
        });
    }



    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, HighScoreActivity.class);
        return intent;
    }
}