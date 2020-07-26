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

import java.util.List;

import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrGallery.FlickrImage;
import cmpt276.termproject.model.FlickrGallery.FlickrManager;
import cmpt276.termproject.model.MusicManager;

/* Select theme and allowing the selection to
be used to update theme throughout the entire app*/

public class OptionActivity extends AppCompatActivity {
    public MusicManager musicManager;
    RadioGroup theme_grp;
    RadioGroup mode_grp;
    RadioGroup size_grp;
    RadioGroup order_grp;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEdit;
    FlickrManager flickrManager;
    List<FlickrImage> image_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        //pull img list
        flickrManager = FlickrManager.getInstance();
        image_list = flickrManager.getImageList(getApplicationContext());


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
        RadioButton hero_rbtn = findViewById(R.id.themes_heroes_rbtn);
        RadioButton logo_rbtn = findViewById(R.id.themes_logo_rbtn);
        final RadioButton flickr_rbtn = findViewById(R.id.themes_flickr_rbtn);
        final RadioButton enabled_rbtn = findViewById(R.id.mode_enabled_rbtn);
        RadioButton disabled_rbtn = findViewById(R.id.mode_disabled_rbtn);
        final RadioButton size5_rbtn = findViewById(R.id.size_five_rbtn);
        final RadioButton size10_rbtn = findViewById(R.id.size_ten_rbtn);
        final RadioButton size15_rbtn = findViewById(R.id.size_fifteen_rbtn);
        final RadioButton size20_rbtn = findViewById(R.id.size_twenty_rbtn);
        final RadioButton sizeAll_rbtn = findViewById(R.id.size_all_rbtn);
        final RadioButton order2_rbtn = findViewById(R.id.order_two_rbtn);
        final RadioButton order3_rbtn = findViewById(R.id.order_three_rbtn);
        RadioButton order5_rbtn = findViewById(R.id.order_five_rbtn);
        RadioButton[] size_rbtns = {size5_rbtn, size10_rbtn, size15_rbtn, size20_rbtn, sizeAll_rbtn};
        RadioButton[] order_rbtns = {order2_rbtn, order3_rbtn, order5_rbtn};
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEdit = mPreferences.edit();
        String curr_theme = mPreferences.getString("Theme", "Superheroes");
        String curr_mode = mPreferences.getString("Mode", "False");
        String curr_size = mPreferences.getString("Size", "5");
        String curr_order = mPreferences.getString("Order", "2");

        if (curr_theme.equals("Logogang"))
        {
            logo_rbtn.setChecked(true);
        }
        else if (curr_theme.equals("Flickr"))
        {
            flickr_rbtn.setChecked(true);
        }
        else
        {
            hero_rbtn.setChecked(true);
        }

        if (curr_mode.equals("True"))
        {
            enabled_rbtn.setChecked(true);
        }
        else
        {
            disabled_rbtn.setChecked(true);
        }

        for (int i = 0; i < 5; i++)
        {
            if (curr_size.equals("0"))
            {
                size_rbtns[4].setChecked(true);
            }
            else if (size_rbtns[i].getText().toString().equals(curr_size))
            {
                size_rbtns[i].setChecked(true);
            }
        }

        for (int i = 0; i < 3; i++)
        {
            if (order_rbtns[i].getText().toString().equals(curr_order))
            {
                order_rbtns[i].setChecked(true);
            }
        }

        if (flickr_rbtn.isChecked())
        {
            enabled_rbtn.setEnabled(false);
            enabled_rbtn.setTextColor(Color.RED);
        }
        if(enabled_rbtn.isChecked())
        {
            flickr_rbtn.setEnabled(false);
            flickr_rbtn.setTextColor(Color.RED);
        }
        if (size10_rbtn.isChecked())
        {
            order2_rbtn.setEnabled(false);
            order2_rbtn.setTextColor(Color.RED);
        }
        if (size15_rbtn.isChecked() || size20_rbtn.isChecked())
        {
            order2_rbtn.setEnabled(false);
            order2_rbtn.setTextColor(Color.RED);
            order3_rbtn.setEnabled(false);
            order3_rbtn.setTextColor(Color.RED);
        }
        if (order2_rbtn.isChecked())
        {
            size10_rbtn.setEnabled(false);
            size10_rbtn.setTextColor(Color.RED);
            size15_rbtn.setEnabled(false);
            size15_rbtn.setTextColor(Color.RED);
            size20_rbtn.setEnabled(false);
            size20_rbtn.setTextColor(Color.RED);
        }
        if (order3_rbtn.isChecked())
        {
            size15_rbtn.setEnabled(false);
            size15_rbtn.setTextColor(Color.RED);
            size20_rbtn.setEnabled(false);
            size20_rbtn.setTextColor(Color.RED);
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
                        enabled_rbtn.setEnabled(true);
                        enabled_rbtn.setTextColor(Color.WHITE);
                        break;

                    case 2: //Logogang theme chosen
                        mEdit.putString("Theme", "Logogang");
                        mEdit.apply();
                        enabled_rbtn.setEnabled(true);
                        enabled_rbtn.setTextColor(Color.WHITE);
                        break;

                    case 3: //Custom Flickr theme chosen
                        mEdit.putString("Theme","Flickr");
                        mEdit.apply();
                        enabled_rbtn.setEnabled(false);
                        enabled_rbtn.setTextColor(Color.RED);
                        break;
                }
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

                switch (index)
                {
                    case 1: //Enabled word and image mode
                        mEdit.putString("Mode", "True");
                        mEdit.apply();
                        flickr_rbtn.setEnabled(false);
                        flickr_rbtn.setTextColor(Color.RED);
                        break;

                    case 2: //Disabled word and image mode
                        mEdit.putString("Mode", "False");
                        mEdit.apply();
                        flickr_rbtn.setEnabled(true);
                        flickr_rbtn.setTextColor(Color.WHITE);
                        break;
                }
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

                switch (index)
                {
                    case 1: //Set deck size to 5
                        mEdit.putString("Size", "5");
                        mEdit.apply();
                        order2_rbtn.setEnabled(true);
                        order2_rbtn.setTextColor(Color.WHITE);
                        order3_rbtn.setEnabled(true);
                        order3_rbtn.setTextColor(Color.WHITE);
                        break;

                    case 2: //Set deck size to 10
                        mEdit.putString("Size", "10");
                        mEdit.apply();
                        order2_rbtn.setEnabled(false);
                        order2_rbtn.setTextColor(Color.RED);
                        order3_rbtn.setEnabled(true);
                        order3_rbtn.setTextColor(Color.WHITE);
                        break;

                    case 3: //Set deck size to 15
                        mEdit.putString("Size", "15");
                        mEdit.apply();
                        order2_rbtn.setEnabled(false);
                        order2_rbtn.setTextColor(Color.RED);
                        order3_rbtn.setEnabled(false);
                        order3_rbtn.setTextColor(Color.RED);
                        break;

                    case 4: //Set deck size to 20
                        mEdit.putString("Size", "20");
                        mEdit.apply();
                        order2_rbtn.setEnabled(false);
                        order2_rbtn.setTextColor(Color.RED);
                        order3_rbtn.setEnabled(false);
                        order3_rbtn.setTextColor(Color.RED);
                        break;

                    case 5: //Set deck size to ALL
                        mEdit.putString("Size", "0");
                        mEdit.apply();
                        order2_rbtn.setEnabled(true);
                        order2_rbtn.setTextColor(Color.WHITE);
                        order3_rbtn.setEnabled(true);
                        order3_rbtn.setTextColor(Color.WHITE);
                        break;
                }
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

                switch (index)
                {
                    case 1: //Set order complexity to 2
                        mEdit.putString("Order", "2");
                        mEdit.apply();
                        size10_rbtn.setEnabled(false);
                        size10_rbtn.setTextColor(Color.RED);
                        size15_rbtn.setEnabled(false);
                        size15_rbtn.setTextColor(Color.RED);
                        size20_rbtn.setEnabled(false);
                        size20_rbtn.setTextColor(Color.RED);
                        break;

                    case 2: //Set order complexity to 3
                        mEdit.putString("Order", "3");
                        mEdit.apply();
                        size10_rbtn.setEnabled(true);
                        size10_rbtn.setTextColor(Color.WHITE);
                        size15_rbtn.setEnabled(false);
                        size15_rbtn.setTextColor(Color.RED);
                        size20_rbtn.setEnabled(false);
                        size20_rbtn.setTextColor(Color.RED);
                        break;

                    case 3: //Set order complexity to 5
                        mEdit.putString("Order", "5");
                        mEdit.apply();
                        size10_rbtn.setEnabled(true);
                        size10_rbtn.setTextColor(Color.WHITE);
                        size15_rbtn.setEnabled(true);
                        size15_rbtn.setTextColor(Color.WHITE);
                        size20_rbtn.setEnabled(true);
                        size20_rbtn.setTextColor(Color.WHITE);
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
        storeOptions();
    }
    @Override
    public void onBackPressed() {

    }
}