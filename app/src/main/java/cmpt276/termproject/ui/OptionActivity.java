/*
Options screen allowing user to switch game themes.
 */
package cmpt276.termproject.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
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
    private RadioGroup theme_grp;
    private RadioGroup mode_grp;
    private RadioGroup size_grp;
    private RadioGroup order_grp;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEdit;

    private enum Theme{ SUPERHEROES, LOGOGANG, FLICKR }

    private enum Mode { TRUE, FALSE }

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

    private void storeOptions()
    {
        final RadioButton hero_rbtn = findViewById(R.id.themes_heroes_rbtn);
        final RadioButton logo_rbtn = findViewById(R.id.themes_logo_rbtn);
        final RadioButton flickr_rbtn = findViewById(R.id.themes_flickr_rbtn);
        final RadioButton enabled_rbtn = findViewById(R.id.mode_enabled_rbtn);
        final RadioButton disabled_rbtn = findViewById(R.id.mode_disabled_rbtn);
        final RadioButton size5_rbtn = findViewById(R.id.size_five_rbtn);
        final RadioButton size10_rbtn = findViewById(R.id.size_ten_rbtn);
        final RadioButton size15_rbtn = findViewById(R.id.size_fifteen_rbtn);
        final RadioButton size20_rbtn = findViewById(R.id.size_twenty_rbtn);
        final RadioButton sizeAll_rbtn = findViewById(R.id.size_all_rbtn);
        final RadioButton order2_rbtn = findViewById(R.id.order_two_rbtn);
        final RadioButton order3_rbtn = findViewById(R.id.order_three_rbtn);
        RadioButton order5_rbtn = findViewById(R.id.order_five_rbtn);
        final RadioButton[] size_rbtns = {size5_rbtn, size10_rbtn, size15_rbtn, size20_rbtn, sizeAll_rbtn};
        final RadioButton[] order_rbtns = {order2_rbtn, order3_rbtn, order5_rbtn};

        final RadioButton[] theme_rbtns = {hero_rbtn,logo_rbtn,flickr_rbtn};

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEdit = mPreferences.edit();
        //Store user options and defaults
        String curr_theme = mPreferences.getString("Theme", "SUPERHEROES");
        String curr_mode = mPreferences.getString("Mode", "FALSE");
        String curr_size = mPreferences.getString("Size", "0");
        String curr_order = mPreferences.getString("Order", "2");


        //Restore user options from last application run
        if (curr_theme.equals(String.valueOf(Theme.LOGOGANG))) {
            logo_rbtn.setChecked(true);
        }
        else if (curr_theme.equals(String.valueOf(Theme.FLICKR))) {
            flickr_rbtn.setChecked(true);
        }
        else {
            hero_rbtn.setChecked(true);
        }

        if (curr_mode.equals(String.valueOf(Mode.TRUE))) {
            enabled_rbtn.setChecked(true);
        }
        else {
            disabled_rbtn.setChecked(true);
        }


        for (int i = 0; i < 5; i++) {
            if (curr_size.equals("0")) {
                size_rbtns[4].setChecked(true);
            }
            else if (size_rbtns[i].getText().toString().equals(curr_size)) {
                size_rbtns[i].setChecked(true);
            }
        }

        for (int i = 0; i < 3; i++) {
            if (order_rbtns[i].getText().toString().equals(curr_order)) {
                order_rbtns[i].setChecked(true);
            }
        }

        //Disable invalid options based on restored options
        if (flickr_rbtn.isChecked()) {
            disableButton(enabled_rbtn);
        }
        if(enabled_rbtn.isChecked()) {
            disableButton(flickr_rbtn);
        }

        if (size10_rbtn.isChecked()) {
            disableButton(order2_rbtn);
        }
        if (size15_rbtn.isChecked() || size20_rbtn.isChecked()) {
            disableButton(order2_rbtn);
            disableButton(order3_rbtn);
        }

        if (order2_rbtn.isChecked()) {
            disableButton(size10_rbtn);
            disableButton(size15_rbtn);
            disableButton(size20_rbtn);
        }
        if (order3_rbtn.isChecked()) {
            disableButton(size15_rbtn);
            disableButton(size20_rbtn);
        }

        //From here to the end of the function handles on click events for the different set
        //of options which includes disabling invalid options based on user selections
        //and saving for future application runs

        theme_grp = findViewById(R.id.options_themes_rgroup);
        theme_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                View theme_btn = theme_grp.findViewById(checkedId);
                int index = theme_grp.indexOfChild(theme_btn);

                for (int i = 0; i < theme_rbtns.length; i ++ ){
                    enableButton(enabled_rbtn);
                }
                if (index == 3){
                    disableButton(enabled_rbtn);
                }
                String theme = String.valueOf(Theme.values()[index-1]);
                mEdit.putString("Theme", theme);
                mEdit.apply();
            }
        });

        mode_grp = findViewById(R.id.options_mode_rgroup);
        mode_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                View mode_btn = mode_grp.findViewById(checkedId);
                int index = mode_grp.indexOfChild(mode_btn);

                if (index == 1){
                    disableButton(flickr_rbtn);
                }
                else {
                    enableButton(flickr_rbtn);
                }
                String mode = String.valueOf(Mode.values()[index-1]);
                mEdit.putString("Mode", mode);
                mEdit.apply();

            }
        });


        size_grp = findViewById(R.id.options_size_rgroup);
        size_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                View size_btn = size_grp.findViewById(checkedId);
                int index = size_grp.indexOfChild(size_btn);

                //Array for storing values of the order buttons
                int[]  order_vals = getOrderButtonsValues(order_rbtns);
                //Get Value of clicked Size button
                int size_val = (size_rbtns[index-1].getText().equals("ALL")) ? 0 : Integer.parseInt((String) size_rbtns[index-1].getText());

                //Set order button availability and save in prefs
                for (int i = 0 ; i < order_vals.length; i ++) {
                    if( getNumCards(order_vals[i]) > size_val){
                        enableButton(order_rbtns[i]);
                    }
                    else{
                        disableButton(order_rbtns[i]);
                    }
                }
                String size = String.valueOf(size_val);
                mEdit.putString("Size", size);
                mEdit.apply();
            }
        });

        order_grp = findViewById(R.id.options_order_rgroup);
        order_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                View order_btn = order_grp.findViewById(checkedId);
                int index = order_grp.indexOfChild(order_btn);

                int[]  order_vals = getOrderButtonsValues(order_rbtns);

                for (RadioButton size_rbtn : size_rbtns) {
                    //get value of all the buttons
                    int size_val = (size_rbtn.getText().equals("ALL")) ? 0 : Integer.parseInt((String) size_rbtn.getText());
                    //Enable Buttons according to order
                    if (getNumCards(order_vals[index - 1]) > size_val) {
                        enableButton(size_rbtn);
                    } else {
                        disableButton(size_rbtn);
                    }
                }
                String order = String.valueOf(order_vals[index-1]);
                mEdit.putString("Order", order);
                mEdit.apply();
            }
        });
    }


    private void disableButton(RadioButton btn){
        btn.setEnabled(false);
        btn.setTextColor(Color.RED);
    }

    private void enableButton(RadioButton btn){
        btn.setEnabled(true);
        btn.setTextColor(Color.WHITE);
    }

    private int[] getOrderButtonsValues(RadioButton[] order_rbtns){
        int[] order_vals = new int [order_rbtns.length];
        for (int i = 0; i < order_rbtns.length; i ++ ){
            order_vals[i] = Integer.parseInt((String) order_rbtns[i].getText());
        }
        return order_vals;
    }

    private int getNumCards(int order){
        return ((order + 1) * order) + 1;
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
        storeOptions();
    }

    @Override
    public void onBackPressed() {}
}