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

import java.util.ArrayList;
import java.util.List;


public class HighScoreActivity extends AppCompatActivity {

    private HighScores highscore;

    private Highscore hs;

    private List<TextView> scores = new ArrayList<>();

    TableRow row;
    TextView username;
    TextView Score;
    TextView Date;
    String[] default_scores;
    TableLayout tableLayout;

    Typeface face;

    String test_input;

private static boolean isreset = false;

    private static boolean isinitialized = false;

    ArrayList<String> arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);




       highscore = HighScores.getInstance();

       hs = Highscore.getInstance();



        default_scores = getResources().getStringArray(R.array.default_highscores);

        //hs.set_default_scores(HighScoreActivity.this, default_scores);
        //populateScores();

//        highscore.set_default_scores(HighScoreActivity.this,default_scores);
//        arr = highscore.get_default_scores(HighScoreActivity.this);
        //populateScores();

       // highscore.set_default_scores(HighScoreActivity.this, default_scores);
        //arr = highscore.getNewscores(HighScoreActivity.this);

//        if(!initialized) {
//
//            highscore.set_default_scores(HighScoreActivity.this,default_scores);
//            arr = highscore.get_default_scores(HighScoreActivity.this);
//            hs.set_default_scores(HighScoreActivity.this, default_scores);
//            populateScores();
//        }
//        else {
//
//            int time = getIntent().getIntExtra("time", 0);
//            String name = getIntent().getStringExtra("name");
//            String date_time = getIntent().getStringExtra("dateTime");
//
//            test_input = hs.convert_sec_to_timeformat(70) + "/ name / Jul 4,2020";
//            hs.update(test_input, HighScoreActivity.this);
//
//
//            updated_table();
//        }


        //hs.set_default_scores(HighScoreActivity.this, default_scores);

        if(!isinitialized) {
            // run your one time code here

            highscore.set_default_scores(HighScoreActivity.this,default_scores);
            arr = highscore.get_default_scores(HighScoreActivity.this);
            hs.set_default_scores(HighScoreActivity.this, default_scores);
            populateScores();

            isinitialized = true;


//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("firstTime", false);
//            editor.commit();
        }





//        if(!initialized) {
//
//            highscore.set_default_scores(HighScoreActivity.this,default_scores);
//            arr = highscore.get_default_scores(HighScoreActivity.this);
//            hs.set_default_scores(HighScoreActivity.this, default_scores);
//            populateScores();
//        }

//        else {
//            String time = getIntent().getStringExtra("time");
//            String name = getIntent().getStringExtra("name");
//            String date_time = getIntent().getStringExtra("dateTime");
//
//            int convert_time = Integer.parseInt(time);
//
//            test_input = hs.convert_sec_to_timeformat(convert_time) + "/" + name + "/" + date_time;
//            hs.update(test_input, HighScoreActivity.this);
//
//
//            updated_table();
//
//        }

        else if (isinitialized) {

//
            highscore.set_default_scores(HighScoreActivity.this,default_scores);
            arr = highscore.get_default_scores(HighScoreActivity.this);
            //hs.set_default_scores(HighScoreActivity.this, default_scores);
            populateScores();
//
            //test_input = "1:10" + "/ name / Jul 4,2020";
//            String input2 = hs.convert_sec_to_timeformat(71) + "/ name / Jul 4,2020";
//            String input3 = hs.convert_sec_to_timeformat(72) + "/ name / Jul 4,2020";
            //hs.update(test_input, HighScoreActivity.this);
//            hs.update(input2, HighScoreActivity.this);
//            hs.update(input3, HighScoreActivity.this);


//            String time = getIntent().getStringExtra("time1");
//            String name = getIntent().getStringExtra("name1");
//            String date_time = getIntent().getStringExtra("dateTime1");

            //String convert_time = hs.convert_sec_to_timeformat(time);

            //test_input = time + "/" + name + "/" + hs.current_date();

            SharedPreferences entry_new = getSharedPreferences("entry", Context.MODE_PRIVATE);
            test_input = entry_new.getString("new entry",null);
            if(test_input != null) {
                hs.update(test_input, HighScoreActivity.this);
                updated_table();
            }




//            if(isreset){
//                highscore.set_default_scores(HighScoreActivity.this,default_scores);
//                arr = highscore.get_default_scores(HighScoreActivity.this);
//                hs.set_default_scores(HighScoreActivity.this, default_scores);
//                populateScores();
//                isreset = false;
//
//            }


        }

            setupResetbtn();
            setupBackbtn();


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

            SharedPreferences sharedPreferences = getSharedPreferences("default scores1", Context.MODE_PRIVATE);

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

            SharedPreferences sharedPreferences = getSharedPreferences("default scores1", Context.MODE_PRIVATE);

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

                SharedPreferences sharedPreferences = getSharedPreferences("default scores1", Context.MODE_PRIVATE);

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

                SharedPreferences sharedPreferences = getSharedPreferences("default scores1", Context.MODE_PRIVATE);

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

                SharedPreferences sharedPreferences = getSharedPreferences("default scores1", Context.MODE_PRIVATE);

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
                isreset = true;
                SharedPreferences entry_new = getSharedPreferences("entry", Context.MODE_PRIVATE);
                entry_new.edit().clear().apply();


            }
        });
    }

    private void setupBackbtn() {
        Button back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }



    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, HighScoreActivity.class);
        return intent;
    }
}