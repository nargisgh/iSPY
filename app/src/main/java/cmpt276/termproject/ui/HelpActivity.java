/*
Help screen displaying brief game info/instructions and sources used for project.
 */
package cmpt276.termproject.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cmpt276.termproject.R;
import cmpt276.termproject.model.MusicManager;

/*Help Text and Sources provided in LinkMovement method*/
public class HelpActivity extends AppCompatActivity {

    ConstraintLayout hsLayout;
    public MusicManager musicManager;
    TextView srcText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        musicManager = MusicManager.getInstance();
        musicManager.play();
        setupBackButton();
        hsLayout = findViewById(R.id.hs_Layout);
        hsLayout.setBackgroundResource(R.drawable.bg_help);
        srcText = findViewById(R.id.help_src_text);
        srcText.setMovementMethod(new LinkMovementMethod());
        ConstraintLayout.LayoutParams textSize;
        textSize = (ConstraintLayout.LayoutParams) srcText.getLayoutParams();
        textSize.height = (getResources().getDisplayMetrics().heightPixels)/6;
        srcText.setLayoutParams(textSize);
    }

    private void setupBackButton(){
        ConstraintLayout.LayoutParams btnSize;
        Button backBtn = findViewById(R.id.help_back_btn);
        btnSize = (ConstraintLayout.LayoutParams) backBtn.getLayoutParams();
        btnSize.width = (getResources().getDisplayMetrics().widthPixels)/6;
        btnSize.height = (getResources().getDisplayMetrics().heightPixels)/8;
        backBtn.setLayoutParams(btnSize);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HelpActivity.class);
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
        finish();
    }
}