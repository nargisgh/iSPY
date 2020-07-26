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

    ConstraintLayout hs_Layout;
    public MusicManager musicManager;
    TextView src_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        musicManager = MusicManager.getInstance();
        musicManager.play();

        setupBackButton();

        hs_Layout = findViewById(R.id.hs_Layout);
        hs_Layout.setBackgroundResource(R.drawable.bg_help);
        src_text = findViewById(R.id.help_src_text);
        src_text.setMovementMethod(new LinkMovementMethod());
        ConstraintLayout.LayoutParams text_size;
        text_size = (ConstraintLayout.LayoutParams) src_text.getLayoutParams();
        text_size.height = (getResources().getDisplayMetrics().heightPixels)/6;
        src_text.setLayoutParams(text_size);
    }

    private void setupBackButton(){
        ConstraintLayout.LayoutParams btn_size;
        Button back_btn = findViewById(R.id.help_back_btn);

        btn_size = (ConstraintLayout.LayoutParams) back_btn.getLayoutParams();
        btn_size.width = (getResources().getDisplayMetrics().widthPixels)/6;
        btn_size.height = (getResources().getDisplayMetrics().heightPixels)/8;
        back_btn.setLayoutParams(btn_size);

        back_btn.setOnClickListener(new View.OnClickListener() {
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

    }
}