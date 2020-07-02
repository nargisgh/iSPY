package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import cmpt276.termproject.R;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_TIMER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        setupQuitBtn();
        setupSkipBtn();

        setupAnimation();

    }


    private void setupAnimation(){
        TextView txt = findViewById(R.id.splash_title);
        //Place Holder Splash Screen Animation
        AlphaAnimation text_anim = new AlphaAnimation(0.2f, 1.0F);
        text_anim.setDuration(2000);
        txt.setAnimation(text_anim);
        text_anim.start();


        // Multithreaded process that checks that the Animation is finished, waits for SPLASH_TIMER
        // and goes to next activity
        text_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

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
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    private void setupSkipBtn(){
        Button btn = findViewById(R.id.splash_skip_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.makeIntent(SplashActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }


    private void setupQuitBtn(){
        Button btn = findViewById(R.id.splash_quit_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }





}
