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

/*Has functions to populate and update high score table
* Using Singleton method and Shared Preferences to pass data,
* interactively retrieving and passing data to HS java class
* TableLayout set up*/

public class HighScoreActivity extends AppCompatActivity {

    private HighScores highScores;


    private MusicManager musicManager;
    private TableRow row;
    private String[] default_scores;
    private TableLayout tableLayout;
    private Typeface face;
    private static boolean isInitialized = false;
    ArrayList<String> arr = new ArrayList<>();
    private static String order;
    private static String draw;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        highScores = HighScores.getInstance();
        ConstraintLayout hs_Layout;
        hs_Layout = findViewById(R.id.hs_Layout);
        hs_Layout.setBackgroundResource(R.drawable.bg_hscore);
        musicManager = MusicManager.getInstance();


// get order and drawsize to get correct default array
        order = highScores.getOrder(HighScoreActivity.this);
        draw = highScores.getDrawPile_size(HighScoreActivity.this);

        name = highScores.getFileName(order,draw);
        setOptionsTitle(order,draw);

        default_scores = highScores.getDEF_array(order,draw,HighScoreActivity.this);
        isInitialized = highScores.get_initDEF_Bool(HighScoreActivity.this,order,draw);
        if(!isInitialized) {
            showDEF_scores();
        }

        initializeScores();


        setupResetBtn();
        setupBackBtn();

    }

    private void setOptionsTitle(String order, String draw){

        if( order.equals("2") && draw.equals("0")){
            draw = "7";
        }
        else if(order.equals("3") && draw.equals("0")){
            draw = "13";
        }
        else if(order.equals("5") && draw.equals("0")){
            draw = "31";
        }
        TextView textView = (TextView)findViewById(R.id.option_info);
        textView.setText("Order: "+ order+ "    Draw Pile Size: "+draw);
    }

    public void showDEF_scores(){


        highScores.set_default_scores(HighScoreActivity.this, default_scores,name);
        arr = highScores.get_default_scores(HighScoreActivity.this,name);
        populateScores();
        isInitialized = true;
        highScores.set_initDEF_Bool(HighScoreActivity.this,order,draw,true);

    }


    public void initializeScores() {

        SharedPreferences entry_new = getSharedPreferences("entry"+name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = entry_new.edit();
        int counter = entry_new.getInt("counter", 0);

            for (int i = 0; i <= counter; i ++ ) {
             String input = entry_new.getString("new entry" + i, null);
                arr = highScores.getCurrentScores(HighScoreActivity.this,name);
                populateScores();
                if (input != null) {
                    highScores.update(input, HighScoreActivity.this,name);
                }
                updated_table();
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
            SharedPreferences sharedPreferences = getSharedPreferences("updated scores"+name, Context.MODE_PRIVATE);
            String[] entry = sharedPreferences.getString("score"+i,"").split("/");
            populate(entry);
            tableLayout.addView(row);
        }
    }

    private void setEntry(String[] entry, TextView score, int index) {
        if (index >= entry.length) {return;}
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
        dynamicScaling(reset_btn, 6, 8);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.clear();
                highScores.set_default_scores(HighScoreActivity.this,default_scores,name);
                arr = highScores.get_default_scores(HighScoreActivity.this,name);
                populateScores();
                SharedPreferences entry_new = getSharedPreferences("entry", Context.MODE_PRIVATE);
                entry_new.edit().clear().apply();

                highScores.set_initDEF_Bool(HighScoreActivity.this,order,draw,false);
            }
        });
    }


    private void setupBackBtn() {
        Button back_btn = findViewById(R.id.highscore_back_btn);
        dynamicScaling(back_btn, 6, 8);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HighScoreActivity.class);
    }

    private void dynamicScaling (Button button, int width, int height)
    {
        ConstraintLayout.LayoutParams btn_size;
        btn_size = (ConstraintLayout.LayoutParams) button.getLayoutParams();
        btn_size.width = (getResources().getDisplayMetrics().widthPixels)/width;
        btn_size.height = (getResources().getDisplayMetrics().heightPixels)/height;
        button.setLayoutParams(btn_size);
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

    @Override
    public void onBackPressed() {

    }
}