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

public class HelpActivity extends AppCompatActivity {

    ConstraintLayout hs_Layout;
    ConstraintLayout.LayoutParams btn_size;
    ConstraintLayout.LayoutParams text_size;
    public MusicManager musicManager;
    TextView sources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        musicManager = MusicManager.getInstance();
        musicManager.play();

        setupBackButton();
        scaleText();

        hs_Layout = findViewById(R.id.hs_Layout);
        hs_Layout.setBackgroundResource(R.drawable.bg_help);
        sources = findViewById(R.id.help_src_text);
        sources.setMovementMethod(new LinkMovementMethod());
    }

    private void setupBackButton(){
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

    private void scaleText()
    {
        TextView help_text = findViewById(R.id.help_desc_text);
        text_size = (ConstraintLayout.LayoutParams) help_text.getLayoutParams();
        text_size.height = (getResources().getDisplayMetrics().heightPixels)/3;
        help_text.setLayoutParams(text_size);

        TextView src_text = findViewById(R.id.help_src_text);
        text_size = (ConstraintLayout.LayoutParams) src_text.getLayoutParams();
        text_size.height = (getResources().getDisplayMetrics().heightPixels)/7;
        src_text.setLayoutParams(text_size);
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