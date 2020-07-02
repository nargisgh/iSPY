package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import cmpt276.termproject.R;

public class PopUp extends AppCompatActivity {

    public static final int X_PARAM = 0;
    public static final int Y_PARAM = -20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);


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
}