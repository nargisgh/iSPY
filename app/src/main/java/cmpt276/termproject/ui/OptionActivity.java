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
import cmpt276.termproject.R;
import cmpt276.termproject.model.MusicManager;

/* Select theme and allowing the selection to
be used to update theme throughout the entire app*/

public class OptionActivity extends AppCompatActivity {
    public MusicManager musicManager;
    RadioGroup theme_grp = findViewById(R.id.options_themes_rgroup);
    RadioGroup mode_grp = findViewById(R.id.options_mode_rgroup);
    RadioGroup size_grp = findViewById(R.id.options_size_rgroup);
    RadioGroup order_grp = findViewById(R.id.options_order_rgroup);
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        ConstraintLayout os_Layout;
        musicManager = MusicManager.getInstance();
        musicManager.play();
        os_Layout = findViewById(R.id.os_Layout);
        os_Layout.setBackgroundResource(R.drawable.bg_options);
        setupBackButton();
        storeOptions();
    }

    private void setupBackButton() {
        ConstraintLayout.LayoutParams btn_size;
        Button back_btn = findViewById(R.id.options_back_btn);

        btn_size = (ConstraintLayout.LayoutParams) back_btn.getLayoutParams();
        btn_size.width = (getResources().getDisplayMetrics().widthPixels) / 6;
        btn_size.height = (getResources().getDisplayMetrics().heightPixels) / 8;
        back_btn.setLayoutParams(btn_size);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void storeOptions() {
        RadioButton hero_rbtn = findViewById(R.id.themes_heroes_rbtn);
        RadioButton hypno_rbtn = findViewById(R.id.themes_hypno_rbtn);
        RadioButton enabled_rbtn = findViewById(R.id.mode_enabled_rbtn);
        RadioButton disabled_rbtn = findViewById(R.id.mode_disabled_rbtn);
        RadioButton size5_rbtn = findViewById(R.id.size_five_rbtn);
        RadioButton size10_rbtn = findViewById(R.id.size_ten_rbtn);
        RadioButton size15_rbtn = findViewById(R.id.size_fifteen_rbtn);
        RadioButton size20_rbtn = findViewById(R.id.size_twenty_rbtn);
        RadioButton sizeAll_rbtn = findViewById(R.id.size_all_rbtn);
        RadioButton order2_rbtn = findViewById(R.id.order_two_rbtn);
        RadioButton order3_rbtn = findViewById(R.id.order_three_rbtn);
        RadioButton order5_rbtn = findViewById(R.id.order_five_rbtn);
        RadioButton[] size_rbtns = {size5_rbtn, size10_rbtn, size15_rbtn, size20_rbtn, sizeAll_rbtn};
        RadioButton[] order_rbtns = {order2_rbtn, order3_rbtn, order5_rbtn};
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEdit = mPreferences.edit();
        String curr_theme = mPreferences.getString("Theme", "Superheroes");
        String curr_mode = mPreferences.getString("Mode", "Disabled");
        String curr_size = mPreferences.getString("Size", "5");
        String curr_order = mPreferences.getString("Order", "2");

        if (curr_theme.equals("Hypnomob")) {
            hypno_rbtn.setChecked(true);
        } else {
            hero_rbtn.setChecked(true);
        }
        if (curr_mode.equals("Enabled")) {
            enabled_rbtn.setChecked(true);
        } else {
            disabled_rbtn.setChecked(true);
        }

        for (int i = 0; i < 5; i++) {
            if (size_rbtns[i].getText().toString().equals(curr_size)) {
                size_rbtns[i].setChecked(true);
            }
        }

        for (int i = 0; i < 3; i++) {
            if (order_rbtns[i].getText().toString().equals(curr_order)) {
                order_rbtns[i].setChecked(true);
            }
        }

        theme_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View theme_btn = theme_grp.findViewById(checkedId);
                int index = theme_grp.indexOfChild(theme_btn);

                switch (index)
                {
                    case 1: //Superhero theme chosen

                        mEdit.putString("Theme", "Superheroes");
                        mEdit.apply();
                        //Toast.makeText(getApplicationContext(), "Superhero theme applied!", Toast.LENGTH_SHORT).show();
                        break;

                    case 2: //Hypnomob theme chosen

                        mEdit.putString("Theme", "Hypnomob");
                        mEdit.apply();
                        //Toast.makeText(getApplicationContext(), "Hypnomob theme applied!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mode_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View mode_btn = mode_grp.findViewById(checkedId);
                int index = mode_grp.indexOfChild(mode_btn);

                switch (index)
                {
                    case 1: //Enabled word and image mode
                        mEdit.putString("Mode", "Enabled");
                        mEdit.apply();
                        break;

                    case 2: //Disabled word and image mode
                        mEdit.putString("Mode", "Disabled");
                        mEdit.apply();
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
