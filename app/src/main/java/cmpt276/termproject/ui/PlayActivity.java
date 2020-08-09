/*
Play screen where cards are drawn, timer is started and game space is set up for user to play
the game.
 */
package cmpt276.termproject.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import cmpt276.termproject.R;
import cmpt276.termproject.model.CardDrawer;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.HighScores;
import cmpt276.termproject.model.MusicManager;

/* Game Play, displays cards drawn and to match.
Setting Game start and over with custom listener.
Pop Up dialog when game is over */

public class PlayActivity extends AppCompatActivity  {

    private MediaPlayer sfxPlayer = new MediaPlayer();
    private String name;
    private String dateTime;
    private Chronometer chronometer;
    private double gameStartTime;
    public MusicManager musicManager;
    public HighScores highScore;
    private CardDrawer cardDrawer;
    boolean isPlaying = false;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        highScore = HighScores.getInstance();
        musicManager = MusicManager.getInstance();
        String order = highScore.getOrder(PlayActivity.this);
        String draw = highScore.getDrawPileSize(PlayActivity.this);
        filename = highScore.getFileName(order, draw);
        ConstraintLayout psLayout = findViewById(R.id.root);
        psLayout.setBackgroundResource(R.drawable.bg_play);
        setup();
        chronometer = findViewById(R.id.stopwatch);
    }

    private void setup(){
        //Setup Game Manager Class
        GameManager gameManager = GameManager.getInstance(getApplicationContext());
        gameManager.setupGameSettings();
        gameManager.createCards();
        FrameLayout frameLayout = findViewById(R.id.frame);
        cardDrawer = new CardDrawer(getApplicationContext());
        setupGameListener();
        frameLayout.addView(cardDrawer);
        setupBackButton();
    }

    public void setupGameListener(){

        cardDrawer.setGameListener(new CardDrawer.GameListener() {
            @Override
            public void onGameOver() {
                int elapsed = ((int)(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
                chronometer.stop();

                dateTime = highScore.getCurrentDateTime();
                double elapsedTimeMs = System.currentTimeMillis() - gameStartTime;
                double time = elapsedTimeMs/1000;
                //Ex format: 8.5

                popup(dateTime, String.valueOf(time));
            }

            @Override
            public void onGameStart(){
                gameStartTime = System.currentTimeMillis();
                Log.e("Chrono", "started");
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }

            @Override
            public void onSfxPlay(boolean failed) {
                sfxPlayer.release();
                sfxPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fail);
                sfxPlayer.seekTo(715);
                if (!failed){
                    sfxPlayer = MediaPlayer.create(getApplicationContext(),R.raw.success);
                    sfxPlayer.seekTo(310);
                }
                sfxPlayer.start();
            }
        });

        sfxPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                sfxPlayer.reset();
                sfxPlayer.release();
            }
        });

    }

    private void popup(final String dateTime,final String time){
        musicManager.play();
        isPlaying = true;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_pop_up);

        Button saveBtn = dialog.findViewById(R.id.pop_save_btn);
        dynamicScaling(saveBtn, 6, 8);

        Button doneBtn = dialog.findViewById(R.id.pop_done_btn);
        dynamicScaling(doneBtn, 6, 8);

        final Button exportBtn = dialog.findViewById(R.id.pop_export_btn);
        dynamicScaling(exportBtn, 6, 8);

        final EditText userId = dialog.findViewById(R.id.userId);
        final TextView scoreP = dialog.findViewById(R.id.score);
        final TextView dateTimeP = dialog.findViewById(R.id.dateTime);
        scoreP.setText(time);
        dateTimeP.setText(dateTime);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.pause();
                cardDrawer.deleteCardImgs();
                dialog.dismiss();
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userId.getText().toString();
                userId.setText(name);

                if(userId.getText().toString().length()>10){
                    userId.setError(""+userId.getText().toString().length() +"/10 characters");
                }
                else if(userId.getText().toString().length()!=0) {
                    userId.setClickable(false);
                    userId.setFocusableInTouchMode(false);
                    userId.setEnabled(false);
                    String entry = (time + "/ " + name + "/" + dateTime);

                    // put string stored in filename var into sharpref file name
                    SharedPreferences entryNew = getSharedPreferences("entry"+filename, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = entryNew.edit();
                    int counter = entryNew.getInt("counter", 0);
                    counter ++;
                    editor.putString("new entry"+counter, entry);
                    editor.putInt("counter",counter);
                    editor.apply();
                    musicManager.pause();
                    Toast.makeText(getApplicationContext(), "Username and score are saved!", Toast.LENGTH_SHORT).show();
                }

                else{
                    userId.setError("Invalid Username");
                }

            }
        });

        exportBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cardDrawer.exportCardImgs();
                Toast.makeText(getApplicationContext(), "Game cards saved to gallery!", Toast.LENGTH_SHORT).show();
                cardDrawer.deleteCardImgs();

            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                musicManager.pause();
                cardDrawer.deleteCardImgs();
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    private void setupBackButton(){
        Button backBtn = findViewById(R.id.play_back_btn);
        dynamicScaling(backBtn, 5, 8);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, PlayActivity.class);
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
    public void onResume() {
        super.onResume();
        if(isPlaying){
            musicManager.play();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}