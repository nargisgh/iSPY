package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cmpt276.termproject.R;
import cmpt276.termproject.model.MusicManager;

public class HelpActivity extends AppCompatActivity {

    ConstraintLayout hs_Layout;
    public MusicManager musicManager;
    TextView sources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        musicManager = MusicManager.getInstance();
        musicManager.play();

        setupBackButton();

        hs_Layout = findViewById(R.id.hs_Layout);
        hs_Layout.setBackgroundResource(R.drawable.bg_help);
        sources = findViewById(R.id.sources);
        sources.setMovementMethod(new LinkMovementMethod());
    }


    private void setupBackButton(){
        Button back_btn = findViewById(R.id.help_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, HelpActivity.class);
        return intent;
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
