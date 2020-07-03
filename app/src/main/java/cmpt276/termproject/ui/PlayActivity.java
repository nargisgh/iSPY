package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cmpt276.termproject.R;
import cmpt276.termproject.model.Card;
import cmpt276.termproject.model.CardDrawer;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.MusicManager;

public class PlayActivity extends AppCompatActivity  {

    private GameManager gameManager;
    CardDrawer cardDrawerCanvas;

    private GameManager manager;
    public MusicManager musicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        musicManager = MusicManager.getInstance();
        setup();

        cardDrawerCanvas = findViewById(R.id.card_canvas);

    }




    private void setup(){
        //Setup Game Manager Class
        gameManager = GameManager.getInstance();
        gameManager.createCards();

        setupBackButton();
    }


    //TODO: GAME OVER POPUP
    //TODO: TIMER
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameManager.getDrawPile().size() == 0){
            //PLACE CODE FOR THE GAME OVER POPUP IN HERE
            Toast.makeText(getApplicationContext(), "GAME OVER", Toast.LENGTH_SHORT).show();
        }
        return super.onTouchEvent(event);
    }




    private void setupBackButton(){
        Button back_btn = findViewById(R.id.play_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.play();
                finish();
            }
        });
    }




    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, PlayActivity.class);
        return intent;
    }


}
