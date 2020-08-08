/*
Main menu with 2 custom backgrounds that switch depending on theme selected. Player can start
game, open options, view high scores, view help menu or quit.
 */
package cmpt276.termproject.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
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
    List<FlickrImage> image_list;
    private static String[] Permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

        //User can't save imgs w/o storage permission
        checkExternalStoragePermission();
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
        Button play_btn = findViewById(R.id.main_play_btn);
        dynamicScaling(play_btn, 5, 6);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getString("Theme", "Superheroes").equals( "FLICKR")) {
                    flickrManager = FlickrManager.getInstance();
                    image_list = flickrManager.getImageList(getApplicationContext());
                    int size = image_list.size();
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
        Button hs_btn = findViewById(R.id.main_hscore_btn);
        dynamicScaling(hs_btn, 5, 8);

        hs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HighScoreActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupFlickrButton()
    {
        Button custom_btn = findViewById(R.id.main_custom_btn);
        dynamicScaling(custom_btn, 7, 6);

        custom_btn.setOnClickListener(new View.OnClickListener()
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
        ConstraintLayout mm_Layout = findViewById(R.id.mm_Layout);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEdit = mPreferences.edit();
        String curr_theme = mPreferences.getString("Theme", "SUPERHEROES");

        if (curr_theme.equals("LOGOGANG"))
        {
            //set Logogang BG
            mm_Layout.setBackgroundResource(R.drawable.bg_menu_logo);
            gameManager.setTheme(1);
        }
        else if (curr_theme.equals("SUPERHEROES"))
        {
            //set Superhero BG
            mm_Layout.setBackgroundResource(R.drawable.bg_menu_heroes);
            gameManager.setTheme(2);
        }
        else
        {
            //make Generic BG
            mm_Layout.setBackgroundResource(R.drawable.bg_menu_flickr);
            gameManager.setTheme(3);
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

    // Need to check permissions first in order to allow app to export images to files
    //https://stackoverflow.com/questions/8854359/exception-open-failed-eacces-permission-denied-on-android
    public void checkExternalStoragePermission() {
        int writing_permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // if writing permission is not granted, user will be asked to deny/allow permission
        if (writing_permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    Permission,
                    1
            );
        }
    }

}