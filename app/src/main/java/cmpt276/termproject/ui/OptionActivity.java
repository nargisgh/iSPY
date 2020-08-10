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
    private RadioGroup themeGrp;
    private RadioGroup modeGrp;
    private RadioGroup sizeGrp;
    private RadioGroup orderGrp;
    private SharedPreferences.Editor mEdit;
    private RadioGroup difficultyGroup;
    private enum Theme{ SUPERHEROES, LOGOGANG, FLICKR }
    private enum Mode { TRUE, FALSE }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        ConstraintLayout osLayout;
        musicManager = MusicManager.getInstance();
        musicManager.play();
        osLayout = findViewById(R.id.os_Layout);
        osLayout.setBackgroundResource(R.drawable.bg_options);
        setupBackButton();
        storeOptions();
    }

    private void setupBackButton() {
        ConstraintLayout.LayoutParams btnSize;
        Button backBtn = findViewById(R.id.options_back_btn);
        btnSize = (ConstraintLayout.LayoutParams) backBtn.getLayoutParams();
        btnSize.width = (getResources().getDisplayMetrics().widthPixels) / 6;
        btnSize.height = (getResources().getDisplayMetrics().heightPixels) / 8;
        backBtn.setLayoutParams(btnSize);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void storeOptions()
    {
        final RadioButton heroRbtn = findViewById(R.id.themes_heroes_rbtn);
        final RadioButton logoRbtn = findViewById(R.id.themes_logo_rbtn);
        final RadioButton flickrRbtn = findViewById(R.id.themes_flickr_rbtn);
        final RadioButton enabledRbtn = findViewById(R.id.mode_enabled_rbtn);
        final RadioButton disabledRbtn = findViewById(R.id.mode_disabled_rbtn);
        final RadioButton size5Rbtn = findViewById(R.id.size_five_rbtn);
        final RadioButton size10Rbtn = findViewById(R.id.size_ten_rbtn);
        final RadioButton size15Rbtn = findViewById(R.id.size_fifteen_rbtn);
        final RadioButton size20Rbtn = findViewById(R.id.size_twenty_rbtn);
        final RadioButton sizeAllRbtn = findViewById(R.id.size_all_rbtn);
        final RadioButton order2Rbtn = findViewById(R.id.order_two_rbtn);
        final RadioButton order3Rbtn = findViewById(R.id.order_three_rbtn);
        RadioButton order5Rbtn = findViewById(R.id.order_five_rbtn);
        final RadioButton[] sizeRbtns = {size5Rbtn, size10Rbtn, size15Rbtn, size20Rbtn, sizeAllRbtn};
        final RadioButton[] orderRbtns = {order2Rbtn, order3Rbtn, order5Rbtn};
        final RadioButton[] themeRbtns = {heroRbtn,logoRbtn,flickrRbtn};
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEdit = mPreferences.edit();
        //Store user options and defaults
        String currTheme = mPreferences.getString("Theme", "SUPERHEROES");
        String currMode = mPreferences.getString("Mode", "FALSE");
        String currSize = mPreferences.getString("Size", "0");
        String currOrder = mPreferences.getString("Order", "2");

        //Difficulty stuff
        final RadioButton[] difficultyRbtns = {findViewById(R.id.difficulty_easy_rb),
                findViewById(R.id.difficulty_med_rb),
                findViewById(R.id.difficulty_hard_rb)};

        int difficulty = mPreferences.getInt("Difficulty", 0);
        difficultyRbtns[difficulty].setChecked(true);

        //Restore user options from last application run
        if (currTheme.equals(String.valueOf(Theme.LOGOGANG))) {
            logoRbtn.setChecked(true);
        }
        else if (currTheme.equals(String.valueOf(Theme.FLICKR))) {
            flickrRbtn.setChecked(true);
        }
        else {
            heroRbtn.setChecked(true);
        }

        if (currMode.equals(String.valueOf(Mode.TRUE))) {
            enabledRbtn.setChecked(true);
        }
        else {
            disabledRbtn.setChecked(true);
        }

        for (int i = 0; i < 5; i++) {
            if (currSize.equals("0")) {
                sizeRbtns[4].setChecked(true);
            }
            else if (sizeRbtns[i].getText().toString().equals(currSize)) {
                sizeRbtns[i].setChecked(true);
            }
        }

        for (int i = 0; i < 3; i++) {
            if (orderRbtns[i].getText().toString().equals(currOrder)) {
                orderRbtns[i].setChecked(true);
            }
        }

        //Disable invalid options based on restored options
        if (flickrRbtn.isChecked()) {
            disableButton(enabledRbtn);
        }
        if(enabledRbtn.isChecked()) {
            disableButton(flickrRbtn);
        }

        if (size10Rbtn.isChecked()) {
            disableButton(order2Rbtn);
        }
        if (size15Rbtn.isChecked() || size20Rbtn.isChecked()) {
            disableButton(order2Rbtn);
            disableButton(order3Rbtn);
        }

        if (order2Rbtn.isChecked()) {
            disableButton(size10Rbtn);
            disableButton(size15Rbtn);
            disableButton(size20Rbtn);
        }
        if (order3Rbtn.isChecked()) {
            disableButton(size15Rbtn);
            disableButton(size20Rbtn);
        }

        //From here to the end of the function handles on click events for the different set
        //of options which includes disabling invalid options based on user selections
        //and saving for future application runs

        themeGrp = findViewById(R.id.options_themes_rgroup);
        themeGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                View themeBtn = themeGrp.findViewById(checkedId);
                int index = themeGrp.indexOfChild(themeBtn);

                for (int i = 0; i < themeRbtns.length; i ++ ){
                    enableButton(enabledRbtn);
                }
                if (index == 3){
                    disableButton(enabledRbtn);
                }
                String theme = String.valueOf(Theme.values()[index-1]);
                mEdit.putString("Theme", theme);
                mEdit.apply();
            }
        });

        modeGrp = findViewById(R.id.options_mode_rgroup);
        modeGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                View modeBtn = modeGrp.findViewById(checkedId);
                int index = modeGrp.indexOfChild(modeBtn);

                if (index == 1){
                    disableButton(flickrRbtn);
                }
                else {
                    enableButton(flickrRbtn);
                }
                String mode = String.valueOf(Mode.values()[index-1]);
                mEdit.putString("Mode", mode);
                mEdit.apply();

            }
        });


        sizeGrp = findViewById(R.id.options_size_rgroup);
        sizeGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                View sizeBtn = sizeGrp.findViewById(checkedId);
                int index = sizeGrp.indexOfChild(sizeBtn);

                //Array for storing values of the order buttons
                int[]  orderVals = getOrderButtonsValues(orderRbtns);
                //Get Value of clicked Size button
                int sizeVal = (sizeRbtns[index-1].getText().equals("ALL")) ? 0 : Integer.parseInt((String) sizeRbtns[index-1].getText());

                //Set order button availability and save in prefs
                for (int i = 0 ; i < orderVals.length; i ++) {
                    if( getNumCards(orderVals[i]) > sizeVal){
                        enableButton(orderRbtns[i]);
                    }
                    else{
                        disableButton(orderRbtns[i]);
                    }
                }
                String size = String.valueOf(sizeVal);
                mEdit.putString("Size", size);
                mEdit.apply();
            }
        });

        orderGrp = findViewById(R.id.options_order_rgroup);
        orderGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                View orderBtn = orderGrp.findViewById(checkedId);
                int index = orderGrp.indexOfChild(orderBtn);

                int[]  orderVals = getOrderButtonsValues(orderRbtns);

                for (RadioButton sizeRbtn : sizeRbtns) {
                    //get value of all the buttons
                    int sizeVal = (sizeRbtn.getText().equals("ALL")) ? 0 : Integer.parseInt((String) sizeRbtn.getText());
                    //Enable Buttons according to order
                    if (getNumCards(orderVals[index - 1]) > sizeVal) {
                        enableButton(sizeRbtn);
                    } else {
                        disableButton(sizeRbtn);
                    }
                }
                String order = String.valueOf(orderVals[index-1]);
                mEdit.putString("Order", order);
                mEdit.apply();
            }
        });

        difficultyGroup = findViewById(R.id.difficulty_radio_group);
        difficultyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View difficultyBtn = difficultyGroup.findViewById(checkedId);
                int index = difficultyGroup.indexOfChild(difficultyBtn) - 1;

                mEdit.putInt("Difficulty", index);
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

    private int[] getOrderButtonsValues(RadioButton[] orderRbtns){
        int[] orderVals = new int [orderRbtns.length];
        for (int i = 0; i < orderRbtns.length; i ++ ){
            orderVals[i] = Integer.parseInt((String) orderRbtns[i].getText());
        }
        return orderVals;
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}