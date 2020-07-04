package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import cmpt276.termproject.R;
import cmpt276.termproject.model.HighScores;
import cmpt276.termproject.model.Highscore;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class HighScoreActivity extends AppCompatActivity {

    private HighScores highscore;

    private Highscore hs=  new Highscore();

    private List<TextView> scores = new ArrayList<>();

    TableRow row;
    TextView username;
    TextView Score;
    TextView Date;
    private String[] default_scores;
    TableLayout tableLayout;

    Typeface face;

    String test_input;

    ArrayList<String> arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);




        highscore = HighScores.getInstance();

        default_scores = getResources().getStringArray(R.array.default_highscores);
        highscore.set_default_scores(HighScoreActivity.this, default_scores);
        arr = highscore.getNewscores(HighScoreActivity.this);

        hs.set_default_scores(HighScoreActivity.this,default_scores);



        //populateScores();
        setupResetbtn();

        test_input = "1:10/ testplayer / Jul 4 at 15:30";
        hs.update(test_input,HighScoreActivity.this);

        updated_table();




    }


    private void populateScores() {

        tableLayout = findViewById(R.id.table);
        tableLayout.removeAllViews();
        setHeadings();

        for(int i = 0; i<arr.size();i++){

            row = new TableRow(this);
            username = new TextView(this);
            Date = new TextView(this);
            Score = new TextView(this);

            String[] entry = default_scores[i].split("/");

            setEntry(entry, Score, 0);
            setEntry(entry, username, 1);
            setEntry(entry, Date, 2);


            row.addView(Score);
            row.addView(username);
            row.addView(Date);
            tableLayout.addView(row);

        }

        //https://www.youtube.com/watch?v=iSCtFzbC7kA

    }

    private void updated_table(){
        tableLayout = findViewById(R.id.table);
        tableLayout.removeAllViews();
        setHeadings();

        for(int i = 0; i<arr.size();i++){

            if(i==0){
            row = new TableRow(this);
            username = new TextView(this);
            Date = new TextView(this);
            Score = new TextView(this);

            SharedPreferences sharedPreferences = getSharedPreferences("default scores", Context.MODE_PRIVATE);

            String[] entry = sharedPreferences.getString("score1","").split("/");

            setEntry(entry, Score, 0);
            setEntry(entry, username, 1);
            setEntry(entry, Date, 2);


            row.addView(Score);
            row.addView(username);
            row.addView(Date);
            tableLayout.addView(row);
            }


        else if(i==1){
            row = new TableRow(this);
            username = new TextView(this);
            Date = new TextView(this);
            Score = new TextView(this);

            SharedPreferences sharedPreferences = getSharedPreferences("default scores", Context.MODE_PRIVATE);

            String[] entry = sharedPreferences.getString("score2","").split("/");

            setEntry(entry, Score, 0);
            setEntry(entry, username, 1);
            setEntry(entry, Date, 2);


            row.addView(Score);
            row.addView(username);
            row.addView(Date);
            tableLayout.addView(row);}

            else if(i==2){
                row = new TableRow(this);
                username = new TextView(this);
                Date = new TextView(this);
                Score = new TextView(this);

                SharedPreferences sharedPreferences = getSharedPreferences("default scores", Context.MODE_PRIVATE);

                String[] entry = sharedPreferences.getString("score3","").split("/");

                setEntry(entry, Score, 0);
                setEntry(entry, username, 1);
                setEntry(entry, Date, 2);


                row.addView(Score);
                row.addView(username);
                row.addView(Date);
                tableLayout.addView(row);}

            else if(i==3){
                row = new TableRow(this);
                username = new TextView(this);
                Date = new TextView(this);
                Score = new TextView(this);

                SharedPreferences sharedPreferences = getSharedPreferences("default scores", Context.MODE_PRIVATE);

                String[] entry = sharedPreferences.getString("score4","").split("/");

                setEntry(entry, Score, 0);
                setEntry(entry, username, 1);
                setEntry(entry, Date, 2);


                row.addView(Score);
                row.addView(username);
                row.addView(Date);
                tableLayout.addView(row);}

            else if(i==4){
                row = new TableRow(this);
                username = new TextView(this);
                Date = new TextView(this);
                Score = new TextView(this);

                SharedPreferences sharedPreferences = getSharedPreferences("default scores", Context.MODE_PRIVATE);

                String[] entry = sharedPreferences.getString("score5","").split("/");

                setEntry(entry, Score, 0);
                setEntry(entry, username, 1);
                setEntry(entry, Date, 2);


                row.addView(Score);
                row.addView(username);
                row.addView(Date);
                tableLayout.addView(row);}

        }


        }









    private void setEntry(String[] entry, TextView score, int index) {
        score.setText(entry[index]);
        score.setGravity(Gravity.CENTER);
        score.setTextSize(25);
        score.setTextColor(Color.WHITE);
    }

    private void setHeadings() {
        TextView name_hd;
        TextView score_hd;
        TextView dateT_hd;

        face = ResourcesCompat.getFont(this, R.font.faster_one);
        row = new TableRow(this);
        name_hd = new TextView(this);
        score_hd = new TextView(this);
        dateT_hd = new TextView(this);

        HeadingName(name_hd, "Score");

        HeadingName(score_hd, "Username");

        HeadingName(dateT_hd, "Date/Time");

        row.addView(name_hd);
        row.addView(score_hd);
        row.addView(dateT_hd);
        tableLayout.addView(row);

    }

    private void HeadingName(TextView hd, String id) {
        hd.setText(id);
        hd.setTypeface(face);
        hd.setTextSize(36);
        hd.setTextColor(Color.WHITE);
        hd.setGravity(Gravity.CENTER);
    }


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