package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cmpt276.termproject.R;
import cmpt276.termproject.model.Highscore;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class HighScoreActivity extends AppCompatActivity {

    Highscore highscore = new Highscore();

    TextView score1;
    TextView score2;
    TextView score3;
    TextView score4;
    TextView score5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        highscore.set_default_scores(HighScoreActivity.this);

        score1 = (TextView) findViewById(R.id.highscore1);
        score2 = (TextView) findViewById(R.id.highscore2);
        score3 = (TextView) findViewById(R.id.highscore3);
        score4 = (TextView) findViewById(R.id.highscore4);
        score5 = (TextView) findViewById(R.id.highscore5);

      String arr[] = highscore.get_default_scores(HighScoreActivity.this);

        score1.setText(arr[0]);
        score2.setText(arr[1]);
        score3.setText(arr[2]);
        score4.setText(arr[3]);
        score5.setText(arr[4]);


        setupResetbtn();



    }





// button to reset back to default scores
    private void setupResetbtn() {
        Button reset = (Button) findViewById(R.id.highscore_reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String arr[] = highscore.get_default_scores(HighScoreActivity.this);

                score1.setText(arr[0]);
                score2.setText(arr[1]);
                score3.setText(arr[2]);
                score4.setText(arr[3]);
                score5.setText(arr[4]);

            }
        });
    }






    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, HighScoreActivity.class);
        return intent;
    }
}