package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import cmpt276.termproject.R;

import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HighScoreActivity extends AppCompatActivity {


    public static final String DEFAULT_SCORE_1 = "default score1";
    public static final String DEFAULT_SCORE_2 = "default_score2";
    private static final String DEFAULT_SCORE_3 = "default_score3";
    private static final String DEFAULT_SCORE_4 = "default_score4";
    private static final String DEFAULT_SCORE_5 =  "default_score5";

    TextView score1;
    TextView score2;
    TextView score3;
    TextView score4;
    TextView score5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        set_default_scores();
        String arr[] = get_default_scores();
        score1 = (TextView) findViewById(R.id.highscore1);
        score2 = (TextView) findViewById(R.id.highscore2);
        score3 = (TextView) findViewById(R.id.highscore3);
        score4 = (TextView) findViewById(R.id.highscore4);
        score5 = (TextView) findViewById(R.id.highscore5);
        score1.setText(arr[0]);
        score2.setText(arr[1]);
        score3.setText(arr[2]);
        score4.setText(arr[3]);
        score5.setText(arr[4]);

        setupResetbtn();


    }


    // populate default_scores
    public void set_default_scores(){
        SharedPreferences sharedPreferences = getSharedPreferences("default scores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_SCORE_1, "Name: player1 " + " Date:Jun 3, 2020 "+ " Time: 2:30");
        editor.putString(DEFAULT_SCORE_2, "Name: player2 " + " Date: Jun 1, 2020 "+ " Time: 3:00");
        editor.putString(DEFAULT_SCORE_3, "Name: player3 " + " Date: May 25, 2020 "+ " Time: 3:10");
        editor.putString(DEFAULT_SCORE_4, "Name: player4 " + " Date: May 30, 2020 "+ " Time: 3:30");
        editor.putString(DEFAULT_SCORE_5, "Name: player5 " + " Date: Jun 2, 2020 "+ " Time: 3:40");
        editor.commit();
    }

    // get default scores
    public String[] get_default_scores(){
        SharedPreferences sharedPreferences = getSharedPreferences("default scores", MODE_PRIVATE);
       String score1 = sharedPreferences.getString(DEFAULT_SCORE_1, "" );
       String score2 = sharedPreferences.getString(DEFAULT_SCORE_2, "" );
       String score3 = sharedPreferences.getString(DEFAULT_SCORE_3, "" );
       String score4 = sharedPreferences.getString(DEFAULT_SCORE_4, "" );
       String score5 = sharedPreferences.getString(DEFAULT_SCORE_5, "" );
       String arr[] = {score1,score2,score3,score4,score5};
       return arr;
    }


    // reset to default scores
    public void reset_default_scores(){
        String arr[] = get_default_scores();
        score1.setText(arr[0]);
        score2.setText(arr[1]);
        score3.setText(arr[2]);
        score4.setText(arr[3]);
        score5.setText(arr[4]);
    }


// button to reset to default scores
    private void setupResetbtn() {
        Button reset = (Button) findViewById(R.id.highscore_reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_default_scores();
            }
        });
    }


    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, HighScoreActivity.class);
        return intent;
    }
}