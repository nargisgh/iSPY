package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;

import cmpt276.termproject.R;
import cmpt276.termproject.model.HighScores;

public class PopUp extends AppCompatActivity {

    public static final int X_PARAM = 0;
    public static final int Y_PARAM = -20;
    EditText userId;
    TextView score;
    TextView dateTime;
    String name;
    String entry;
    HighScores highScores;
    Button save;
    Intent gameInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        gameInfo = getIntent();
        highScores = HighScores.getInstance();
        populate();
        setUpDisplay();
        setUpSaveBtn();


    }

    private void setUpSaveBtn() {
        save = findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userId.getText().toString();
                userId.setText(name);
                if(userId.getText().toString().length()!=0) {
                    userId.setClickable(false);
                    userId.setFocusableInTouchMode(false);
                    userId.setEnabled(false);
                    entry = ("" + (gameInfo.getStringExtra("time")) + "/ " + name + "/" + (gameInfo.getStringExtra("dateTime")));
                    highScores.setNewValues(PopUp.this, entry);
                }
                finish();
            }
        });
    }

    private void setUpDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.85),(int)(height*.7));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = X_PARAM;
        params.y = Y_PARAM;
        getWindow().setAttributes(params);
    }

    private void populate() {
        userId = findViewById(R.id.userId);
        score = findViewById(R.id.score);
        dateTime = findViewById(R.id.dateTime);
        score.setText((gameInfo.getStringExtra("time")));
        dateTime.setText((gameInfo.getStringExtra("dateTime")));


    }
}