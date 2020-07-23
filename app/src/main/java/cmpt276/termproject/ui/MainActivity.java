/*
Main menu with 2 custom backgrounds that switch depending on theme selected. Player can start
game, open options, view high scores, view help menu or quit.
 */
package cmpt276.termproject.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import java.util.ArrayList;

import cmpt276.termproject.R;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.HighScores;
import cmpt276.termproject.model.MusicManager;

/* Main activity, buttons for different activities and
* updating bg for consistent theme */

public class MainActivity extends AppCompatActivity {

    public MusicManager musicManager;
    private GameManager gameManager;

    private HighScores highScores;
    private String[] default_scores;
    private TableLayout tableLayout;
    private Typeface face;
    private static boolean isInitialized = false;
    private static boolean init;
    private  SharedPreferences sp;
    private SharedPreferences.Editor editor;

    ArrayList<String> arr = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicManager = MusicManager.getInstance();
        gameManager = GameManager.getInstance(getApplicationContext());
        highScores = HighScores.getInstance();


        sp = PreferenceManager.getDefaultSharedPreferences(this);
        init = sp.getBoolean("bool",false);

        // initializing all options of DEF_array as false once, when app is installed
        //this is to identify if def scores were initialized for a specific option
        if(!init){
            highScores.set_initDEF_False(MainActivity.this);
            editor = sp.edit();
            editor.putBoolean("bool",true);
            editor.commit();
        }

        setupPlayButton();
        setupOptionButton();
        setupHelpButton();
        setupQuitButton();
        setupHighScoreButton();
        setTheme();
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        musicManager.play();
        setTheme();
    }

    //Button setup for start , options and play
    private void setupPlayButton(){
        Button play_btn = findViewById(R.id.main_play_btn);
        dynamicScaling(play_btn, 5, 6);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PlayActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupOptionButton(){
        Button option_btn = findViewById(R.id.main_option_btn);
        dynamicScaling(option_btn, 5, 8);

        option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionActivity.makeIntent(MainActivity.this);
                musicManager.pause();
                startActivity(intent);
            }
        });
    }

    private void setupHelpButton(){
        Button help_btn = findViewById(R.id.main_help_btn);
        dynamicScaling(help_btn, 5, 8);

        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HelpActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupQuitButton(){
        Button qt_btn = findViewById(R.id.main_quit_btn);
        dynamicScaling(qt_btn, 5, 8);

        qt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.stop();
                finish();
            }
        });
    }


    private void setupHighScoreButton() {
        Button hs_btn = (Button) findViewById(R.id.main_hscore_btn);
        dynamicScaling(hs_btn, 5, 8);

        hs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HighScoreActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

    private void setTheme()
    {
        ConstraintLayout mm_Layout = findViewById(R.id.mm_Layout);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEdit = mPreferences.edit();
        String curr_theme = mPreferences.getString("Theme", "Superheroes");

        if (curr_theme.equals("Hypnomob"))
        {
            mm_Layout.setBackgroundResource(R.drawable.bg_menu_hypno);
            gameManager.setTheme(1);
        }
        else
        {
            mm_Layout.setBackgroundResource(R.drawable.bg_menu_heroes);
            gameManager.setTheme(2);
        }

        mEdit.apply();
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
    public void onBackPressed() {
        super.onBackPressed();
        musicManager.stop();
        finishAffinity();
    }


}
