package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
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

    Highscore hs;

    private List<TextView> scores = new ArrayList<>();

    TableRow row;
    TextView username;
    TextView Score;
    TextView Date;
    private String[] default_scores;
    TableLayout tableLayout;

    Typeface face;

    // default values
    String[] name_arr = {"Flash", "BigBrain", "SuperMan", "BatMan","MrSlow"};
    String[] date_arr = {"Jun 3, 2020", "Jun 1, 2020", "May 25, 2020","May 30, 2020","Jun 2, 2020"};
    String[] time_arr = {"2:30","3:00","3:10","3:30", "3:40"};

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
        setupResetbtn();


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

        HeadingName(name_hd, "Username");

        HeadingName(score_hd, "Score");

        HeadingName(dateT_hd, "Date/Time");

        row.addView(name_hd);
        row.addView(score_hd);
        row.addView(dateT_hd);
        tableLayout.addView(row);

    }

    private void HeadingName(TextView name_hd, String id) {
        name_hd.setText(id);
        name_hd.setTypeface(face);
        name_hd.setTextSize(36);
        name_hd.setTextColor(Color.WHITE);
        name_hd.setGravity(Gravity.CENTER);
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