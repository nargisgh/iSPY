/*
High score screen displaying top 5 high scores and button to reset high scores to default.
 */
package cmpt276.termproject.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import cmpt276.termproject.R;
import cmpt276.termproject.model.HighScores;
import cmpt276.termproject.model.MusicManager;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/*Has functions to populate and update high score table
* Using Singleton method and Shared Preferences to pass data,
* interactively retrieving and passing data to HS java class
* TableLayout set up*/

public class HighScoreActivity extends AppCompatActivity {

    private HighScores highScores;
    ConstraintLayout hs_Layout;
    ConstraintLayout.LayoutParams btn_size;
    ConstraintLayout.LayoutParams table_size;
    private HighScores highscore;
    private List<TextView> scores = new ArrayList<>();

    private MusicManager musicManager;
    private TableRow row;
    private String[] default_scores;
    private TableLayout tableLayout;
    private Typeface face;
    private static boolean isInitialized = false;
    ArrayList<String> arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        highScores = HighScores.getInstance();
        hs_Layout = findViewById(R.id.hs_Layout);
        hs_Layout.setBackgroundResource(R.drawable.bg_hscore);
        musicManager = MusicManager.getInstance();
        highscore = HighScores.getInstance();

        default_scores = getResources().getStringArray(R.array.default_highscores);
        initializeScores();

        setupResetBtn();
        setupBackBtn();
    }

    public void initializeScores() {
        SharedPreferences entry_new = getSharedPreferences("entry", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = entry_new.edit();
        int counter = entry_new.getInt("counter", 0);

        String input = entry_new.getString("new entry"+ 1, null);

        if (input != null) {
            //adding this otherwise wont populate on first game play*
            isInitialized = true;
        }
        // initializing default scores once when app starts
        if (!isInitialized) {
            highScores.set_default_scores(HighScoreActivity.this, default_scores);
            arr = highScores.get_default_scores(HighScoreActivity.this);
            populateScores();
            isInitialized = true;
        }
        else {
            for (int i = 0; i <= counter; i ++ ) {
                input = entry_new.getString("new entry" + i, null);
                arr = highScores.getCurrentScores(HighScoreActivity.this);
                populateScores();
                if (input != null) {
                    highScores.update(input, HighScoreActivity.this);
                }
                updated_table();
            }
        }
        editor.putInt("counter", 0);
        editor.apply();
    }

    private void populate(String[] entry){

        row = new TableRow(this);
        TextView username = new TextView(this);
        TextView date = new TextView(this);
        TextView score = new TextView(this);

        setEntry(entry, score, 0);
        setEntry(entry, username, 1);
        setEntry(entry, date, 2);
    }

    private void populateScores() {

        tableLayout = findViewById(R.id.highscore_table);
        tableLayout.removeAllViews();
        setHeadings();

        for(int i = 0; i<arr.size();i++){
            String[] entry = default_scores[i].split("/");
            populate(entry);
            tableLayout.addView(row);
        }
        //https://www.youtube.com/watch?v=iSCtFzbC7kA
    }

    // adding new highscore to table and updating
    private void updated_table(){
        tableLayout = findViewById(R.id.highscore_table);
        tableLayout.removeAllViews();
        setHeadings();

        for(int i = 1; i<=arr.size();i++){
            SharedPreferences sharedPreferences = getSharedPreferences("updated scores", Context.MODE_PRIVATE);
            String[] entry = sharedPreferences.getString("score"+i,"").split("/");
            populate(entry);
            tableLayout.addView(row);
        }
    }

    private void setEntry(String[] entry, TextView score, int index) {
        score.setText(entry[index]);
        score.setGravity(Gravity.CENTER);
        score.setTextSize(25);
        score.setTextColor(Color.WHITE);
        row.addView(score);
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

        tableLayout.addView(row);
    }

    private void HeadingName(TextView hd, String id) {
        hd.setText(id);
        hd.setTypeface(face);
        hd.setTextSize(36);
        hd.setTextColor(Color.WHITE);
        hd.setGravity(Gravity.CENTER);
        row.addView(hd);
    }

    // button to reset back to default scores
    private void setupResetBtn() {
        Button reset_btn = findViewById(R.id.highscore_reset_btn);

        btn_size = (ConstraintLayout.LayoutParams) reset_btn.getLayoutParams();
        btn_size.width = (getResources().getDisplayMetrics().widthPixels)/6;
        btn_size.height = (getResources().getDisplayMetrics().heightPixels)/8;
        reset_btn.setLayoutParams(btn_size);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.clear();
                highScores.set_default_scores(HighScoreActivity.this,default_scores);
                arr = highScores.get_default_scores(HighScoreActivity.this);
                populateScores();
                SharedPreferences entry_new = getSharedPreferences("entry", Context.MODE_PRIVATE);
                entry_new.edit().clear().apply();
            }
        });
    }


    private void setupBackBtn() {
        Button back = findViewById(R.id.highscore_back_btn);
        btn_size = (ConstraintLayout.LayoutParams) back.getLayoutParams();
        btn_size.width = (getResources().getDisplayMetrics().widthPixels)/6;
        btn_size.height = (getResources().getDisplayMetrics().heightPixels)/8;
        back.setLayoutParams(btn_size);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HighScoreActivity.class);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        musicManager.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        musicManager.play();
    }
}