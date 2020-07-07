package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import cmpt276.termproject.R;

public class PopUp extends AppCompatActivity {

    public static final int X_PARAM = 0;
    public static final int Y_PARAM = -20;
    TextView userId;
    TextView score;
    TextView dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        populate();
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
        userId = findViewById(R.id.pop_userid_text);
        score = findViewById(R.id.pop_uscore_text);
        dateTime = findViewById(R.id.pop_udate_text);
        Intent gameInfo = getIntent();
        userId.setText((gameInfo.getStringExtra("name")));
        score.setText((gameInfo.getStringExtra("time")));
        dateTime.setText((gameInfo.getStringExtra("dateTime")));

    }
}