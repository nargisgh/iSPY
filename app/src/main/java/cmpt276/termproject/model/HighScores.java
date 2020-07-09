package cmpt276.termproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cmpt276.termproject.ui.HighScoreActivity;

public class HighScores{

// global variables for updating scores
    String score1;
    String score2;
    String score3;
    String score4;
    String score5;

    private List<String> DEFAULT_SCORES = new ArrayList<>();


    ArrayList<String> arr = new ArrayList<>();

    //Signleton Stuff
    private static HighScores instance;
    private HighScores (){
        init();
    }
    public static HighScores getInstance(){
        if (instance == null){
            instance = new HighScores();
        }
        return instance;
    }


    //Initialise DEF_Array
    private void init(){
        for (int i = 0; i < 5; i ++){
            DEFAULT_SCORES.add("default_score" + i);
        }
    }

    //populate default_scores
    public void set_default_scores(Context context, String[] default_scores){

        SharedPreferences sharedPreferences = context.getSharedPreferences("updated scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(int i=0;i<default_scores.length;i++){
            int j = i+1;
            editor.putString("score"+j, default_scores[i]);
        }
        score1 = sharedPreferences.getString("score1","");
        score2 = sharedPreferences.getString("score2","");
        score3 = sharedPreferences.getString("score3","");
        score4 = sharedPreferences.getString("score4","");
        score5 = sharedPreferences.getString("score5","");
        editor.apply();

    }


    // get default scores
    public ArrayList<String> get_default_scores(Context context){
        arr = new ArrayList<>();
        for (int i = 0; i < DEFAULT_SCORES.size(); i ++){
            SharedPreferences shared_Preferences = context.getSharedPreferences("scores"+i, Context.MODE_PRIVATE);
            arr.add(shared_Preferences.getString("score"+i, ""));
        }
        return arr;
    }
    public ArrayList<String> getCurrentScores(Context context){
        arr = new ArrayList<>();

        for (int i = 1; i <= 5; i ++){
            SharedPreferences shared_Preferences = context.getSharedPreferences("updated scores", Context.MODE_PRIVATE);
            arr.add(shared_Preferences.getString("score"+i, ""));
        }
        return arr;
    }


    public String getCurrentDateTime(){
        Date date = new Date();
        return DateFormat.getDateTimeInstance().format(date);
    }

    public String getScore(String entry){
        String[] arr = entry.split("/");
        return arr[0];

    }

    // Here all the times are converted  to secs for easier comparison
    // As each time is compared, sharedpreferences are updated and every entry is moved down the list
    // so when we are updating rows on tablelayout we just need to call the sharedpreferences
    //  https://www.youtube.com/watch?v=_cV7cgQFDo0

    public void update(String entry, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("updated scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int latest_time = convert_min_to_secs(getScore(entry));

        int compare1 = convert_min_to_secs(getScore(sharedPreferences.getString("score1","")));
        int compare2 = convert_min_to_secs(getScore(sharedPreferences.getString("score2","")));
        int compare3 = convert_min_to_secs(getScore(sharedPreferences.getString("score3","")));
        int compare4 = convert_min_to_secs(getScore(sharedPreferences.getString("score4","")));
        int compare5 = convert_min_to_secs(getScore(sharedPreferences.getString("score5","")));

        String[] temp_s = getCurrentScores(context).toArray(new String[0]);

        for(int i=0;i<5;i++){
            if(entry.equals(temp_s[i])){
                return;
            }
        }
        // score1 comparison
        if(latest_time < compare1){
            String temp1 = sharedPreferences.getString("score1","");
            String temp2 = sharedPreferences.getString("score2","");
            String temp3 = sharedPreferences.getString("score3","");
            String temp4 = sharedPreferences.getString("score4","");
            score1 = entry;
            score2 = temp1;
            score3 = temp2;
            score4 = temp3;
            score5= temp4;

            // shifting down the list
            applySwap(sharedPreferences,"score2",score2);
            applySwap(sharedPreferences,"score3",score3);
            applySwap(sharedPreferences,"score4",score4);
            applySwap(sharedPreferences,"score5",score5);
            applySwap(sharedPreferences,"score1",score1);



        }

        //score2 comparison
        else if(latest_time < compare2){
            String temp2 = sharedPreferences.getString("score2","");
            String temp3 = sharedPreferences.getString("score3","");
            String temp4 = sharedPreferences.getString("score4","");
            score2 = entry;
            score3 = temp2;
            score4 = temp3;
            score5 = temp4;
            // shifting down the list
            applySwap(sharedPreferences,"score3",score3);
            applySwap(sharedPreferences,"score4",score4);
            applySwap(sharedPreferences,"score5",score5);
            applySwap(sharedPreferences,"score2",score2);



        }

        //score3 comparison
        else if(latest_time < compare3){
            String temp3 = sharedPreferences.getString("score3","");
            String temp4 = sharedPreferences.getString("score4","");
            score3 = entry;
            score4 = temp3;
            score5 = temp4;

            // shifting down the list
            applySwap(sharedPreferences,"score4",score4);
            applySwap(sharedPreferences,"score5",score5);
            applySwap(sharedPreferences,"score3",score3);

        }

        //score4 comparison
        else if(latest_time < compare4){
            String temp4 = sharedPreferences.getString("score4","");
            score4 = entry;
            score5 = temp4;
            // shifting down the list
            applySwap(sharedPreferences,"score5",score5);
            applySwap(sharedPreferences,"score4",score4);

        }

        //score5 comparison
        else if(latest_time < compare5){
            score5 = entry;
            applySwap(sharedPreferences,"score5",score5);

        }
    }
    private void applySwap(SharedPreferences sharedPreferences, String score_key, String score_position){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(score_key, score_position);
        editor.apply();
    }
    // convert string time to secs for easier comparison
    public int convert_min_to_secs(String str){
        String[] time = str.split(":");
        int hr = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
        int sec = Integer.parseInt(time[2]);
        return (hr * 3600) + (min * 60) + sec;
    }
}
