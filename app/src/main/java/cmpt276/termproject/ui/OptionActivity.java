/*
Options screen allowing user to switch game themes.
 */
package cmpt276.termproject.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import cmpt276.termproject.R;
import cmpt276.termproject.model.GameManager;
import cmpt276.termproject.model.MusicManager;

public class OptionActivity extends AppCompatActivity {

    ConstraintLayout os_Layout;
    ConstraintLayout.LayoutParams btn_size;
    RadioGroup theme_grp;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEdit;
    RadioButton hero_rbtn;
    RadioButton hypno_rbtn;
    public MusicManager musicManager;

    private GameManager gameManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        gameManager = GameManager.getInstance();
        musicManager = MusicManager.getInstance();
        musicManager.play();
        os_Layout = findViewById(R.id.os_Layout);
        os_Layout.setBackgroundResource(R.drawable.bg_options);
        hero_rbtn = findViewById(R.id.options_heroes_rbtn);
        hypno_rbtn = findViewById(R.id.options_hypno_rbtn);

        setupBackButton();
        storeOptions();
    }

    private void setupBackButton(){
        Button back_btn = findViewById(R.id.options_back_btn);

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

    private void storeOptions()
    {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEdit = mPreferences.edit();
        String curr_theme = mPreferences.getString("Theme", "Superheroes");

        if (curr_theme.equals("Hypnomob"))
        {
            hypno_rbtn.setChecked(true);
        }
        else
        {
            hero_rbtn.setChecked(true);
        }

        theme_grp = findViewById(R.id.options_themes_rgroup);
        theme_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                View theme_btn = theme_grp.findViewById(checkedId);
                int index = theme_grp.indexOfChild(theme_btn);

                switch (index)
                {
                    case 1: //Superhero theme chosen

                        mEdit.putString("Theme", "Superheroes");
                        mEdit.apply();
                        Toast.makeText(getApplicationContext(), "Superhero theme applied!", Toast.LENGTH_SHORT).show();
                        break;

                    case 2: //Hypnomob theme chosen

                        mEdit.putString("Theme", "Hypnomob");
                        mEdit.apply();
                        Toast.makeText(getApplicationContext(), "Hypnomob theme applied!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context,OptionActivity.class);
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
