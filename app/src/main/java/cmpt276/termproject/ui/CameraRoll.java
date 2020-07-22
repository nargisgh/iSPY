package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cmpt276.termproject.R;

public class CameraRoll extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_roll);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context,CameraRoll.class);
    }
}