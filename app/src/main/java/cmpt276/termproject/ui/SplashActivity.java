/*
Skippable welcome screen with game title, authors and animations.
 */
package cmpt276.termproject.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import cmpt276.termproject.R;
import cmpt276.termproject.model.MusicManager;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIMER = 6000;
    public MusicManager musicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        musicManager = MusicManager.getInstance();
        musicManager.setThemeSong(SplashActivity.this);
        musicManager.play();
        ConstraintLayout ss_Layout;
        ss_Layout = findViewById(R.id.ss_Layout);
        ss_Layout.setBackgroundResource(R.drawable.bg_welcome);

        setupQuitBtn();
        setupSkipBtn();
        setupAnimation();
    }

    private void setupAnimation()
    {
        TextView title1 = findViewById(R.id.splash_title1_text);
        Animation title1_anim = AnimationUtils.loadAnimation(this, R.anim.splash_title1_animation);
        title1.startAnimation(title1_anim);

        TextView title2 = findViewById(R.id.splash_title2_text);
        Animation title2_anim = AnimationUtils.loadAnimation(this, R.anim.splash_title2_animation);
        title2.startAnimation(title2_anim);

        TextView skip = findViewById(R.id.splash_skip_btn);
        Animation skip_anim = AnimationUtils.loadAnimation(this, R.anim.splash_skip_animation);
        skip.startAnimation(skip_anim);

        // Multithreaded process that checks that the Animation is finished, waits for SPLASH_TIMER
        // and goes to next activity
        title1_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Thread timer = new Thread(){
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(SPLASH_TIMER);
                            }
                        }
                        catch (Exception ignored) {
                        }
                        finally {
                            //Check if process is being finished by the quit button, if not
                            // Make the next activity, otherwise exit
                            if (!isFinishing()) {
                                musicManager.pause();
                                Intent intent = MainActivity.makeIntent(SplashActivity.this);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                };
                timer.start();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void setupSkipBtn(){
        Button skip_btn = findViewById(R.id.splash_skip_btn);
        dynamicScaling(skip_btn, 1, 20);

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.makeIntent(SplashActivity.this);
                musicManager.pause();
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupQuitBtn(){
        Button quit_btn = findViewById(R.id.splash_quit_btn);
        dynamicScaling(quit_btn, 15, 20);

        quit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.stop();
                finish();
            }
        });
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
        super.onBackPressed();
        musicManager.stop();
        finishAffinity();
    }

}
