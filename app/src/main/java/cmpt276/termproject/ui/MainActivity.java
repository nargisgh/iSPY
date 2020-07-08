package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import cmpt276.termproject.R;
import cmpt276.termproject.model.MusicManager;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout mm_Layout;
    public MusicManager musicManager;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEdit;
    String curr_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicManager = MusicManager.getInstance();
        musicManager.play();

        setupPlayButton();
        setupOptionButton();
        setupHelpButton();
        setupQuitButton();
        setupHighscoreButton();
        setBackground();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setBackground();
    }

    //Button setup for start , options and play
    private void setupPlayButton(){
        Button play_btn = findViewById(R.id.main_play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PlayActivity.makeIntent(MainActivity.this);
                musicManager.pause();
                startActivity(intent);
            }
        });
    }

    private void setupOptionButton(){
        Button option_btn = findViewById(R.id.main_option_btn);
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
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HelpActivity.makeIntent(MainActivity.this);
                musicManager.pause();
                startActivity(intent);
            }
        });
    }


    private void setupQuitButton(){
        Button qt_btn = findViewById(R.id.main_quit_btn);
        qt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.stop();
                finish();
            }
        });
    }


    private void setupHighscoreButton() {
        Button highscore = (Button) findViewById(R.id.main_hscore_btn);
        highscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HighScoreActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });


    }
    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }


    private void setBackground()
    {
        mm_Layout = findViewById(R.id.mm_Layout);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEdit = mPreferences.edit();
        curr_theme = mPreferences.getString("Theme", "Superheroes");

        if (curr_theme.equals("Hypnomob"))
        {
            mm_Layout.setBackgroundResource(R.drawable.bg_menu_hypno);
        }
        else
        {
            mm_Layout.setBackgroundResource(R.drawable.bg_menu_heroes);
        }

    }


}
