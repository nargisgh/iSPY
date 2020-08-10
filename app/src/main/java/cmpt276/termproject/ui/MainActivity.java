/*
Main menu with 2 custom backgrounds that switch depending on theme selected. Player can start
game, open options, view high scores, view help menu or quit.
 */
package cmpt276.termproject.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.List;
import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrGallery.FlickrImage;
import cmpt276.termproject.model.FlickrGallery.FlickrManager;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.HighScores;
import cmpt276.termproject.model.MusicManager;

/* Main activity, buttons for different activities and
* updating bg for consistent theme */

public class MainActivity extends AppCompatActivity {

    public MusicManager musicManager;
    private GameManager gameManager;
    private HighScores highScores;
    private  SharedPreferences sp;
    FlickrManager flickrManager;
    List<FlickrImage> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicManager = MusicManager.getInstance();
        gameManager = GameManager.getInstance(getApplicationContext());
        highScores = HighScores.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean init = sp.getBoolean("bool", false);

        // initializing all options of DEF_array as false once, when app is installed
        //this is to identify if def scores were initialized for a specific option
        if(!init){
            highScores.setInitDEFFalse(MainActivity.this);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("bool",true);
            editor.apply();
        }

        checkExternalStorageWritingPermission();
        setupPlayButton();
        setupOptionButton();
        setupHelpButton();
        setupQuitButton();
        setupHighScoreButton();
        setupFlickrButton();
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
        Button playBtn = findViewById(R.id.main_play_btn);
        dynamicScaling(playBtn, 5, 6);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getString("Theme", "Superheroes").equals( "FLICKR")) {
                    flickrManager = FlickrManager.getInstance();
                    imageList = flickrManager.getImageList(getApplicationContext());
                    int size = imageList.size();
                    String order = highScores.getOrder(getApplicationContext());
                    int remain;

                    if (order.equals("2") && (size < 7)) {
                        remain = 7-size;
                        Toast.makeText(getApplicationContext(), "Not enough images selected to play! You need " + remain+ " more image(s). Please go select more from Custom.", Toast.LENGTH_LONG).show();
                    } else if (order.equals("3") && (size < 13)) {
                        remain = 13-size;
                        Toast.makeText(getApplicationContext(), "Not enough images selected to play! You need " +remain+" more image(s). Please go select more from Custom.", Toast.LENGTH_LONG).show();
                    } else if (order.equals("5") && (size < 31)) {
                        remain = 31-size;
                        Toast.makeText(getApplicationContext(), "Not enough images selected to play! You need "+remain+" more images(s). Please go select more from Custom.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = PlayActivity.makeIntent(MainActivity.this);
                        startActivity(intent);
                    }
                }
                else {
                    Intent intent = PlayActivity.makeIntent(MainActivity.this);
                    startActivity(intent);
                }
            }
        });

    }

    private void setupOptionButton(){
        Button optionBtn = findViewById(R.id.main_option_btn);
        dynamicScaling(optionBtn, 5, 8);

        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionActivity.makeIntent(MainActivity.this);
                musicManager.pause();
                startActivity(intent);
            }
        });
    }

    private void setupHelpButton(){
        Button helpBtn = findViewById(R.id.main_help_btn);
        dynamicScaling(helpBtn, 5, 8);

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HelpActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupQuitButton(){
        Button quitBtn = findViewById(R.id.main_quit_btn);
        dynamicScaling(quitBtn, 5, 8);

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.stop();
                finish();
            }
        });
    }

    private void setupHighScoreButton() {
        Button hsBtn = findViewById(R.id.main_hscore_btn);
        dynamicScaling(hsBtn, 5, 8);

        hsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HighScoreActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupFlickrButton()
    {
        Button customBtn = findViewById(R.id.main_custom_btn);
        dynamicScaling(customBtn, 7, 6);

        customBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = FlickrGallery.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

    private void setTheme()
    {
        ConstraintLayout mmLayout = findViewById(R.id.mm_Layout);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEdit = mPreferences.edit();
        String currTheme = mPreferences.getString("Theme", "SUPERHEROES");

        if (currTheme.equals("LOGOGANG"))
        {
            //set Logogang BG
            mmLayout.setBackgroundResource(R.drawable.bg_menu_logo);
            gameManager.setTheme(1);
        }
        else if (currTheme.equals("SUPERHEROES"))
        {
            //set Superhero BG
            mmLayout.setBackgroundResource(R.drawable.bg_menu_heroes);
            gameManager.setTheme(2);
        }
        else
        {
            //make Generic BG
            mmLayout.setBackgroundResource(R.drawable.bg_menu_flickr);
            gameManager.setTheme(3);
        }

        mEdit.apply();
    }

    private void dynamicScaling (Button button, int width, int height)
    {
        ConstraintLayout.LayoutParams btnSize;
        btnSize = (ConstraintLayout.LayoutParams) button.getLayoutParams();
        btnSize.width = (getResources().getDisplayMetrics().widthPixels)/width;
        btnSize.height = (getResources().getDisplayMetrics().heightPixels)/height;
        button.setLayoutParams(btnSize);
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

    // Need to check permissions first in order to allow app to export images to files
    //https://stackoverflow.com/questions/8854359/exception-open-failed-eacces-permission-denied-on-android
    public void checkExternalStorageWritingPermission() {
        // if writing permission is not granted, user will be asked to deny/allow permission
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            this.requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );
        }
    }

}