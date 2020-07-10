package cmpt276.termproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HighScores{

    private List<String> DEFAULT_SCORES = new ArrayList<>();

    String[] arr2 = new String[5];


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

    public void update(String entry, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("updated scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<String> scores = new ArrayList<>();
        for (int i = 0; i < 5; i ++){
            scores.add(sharedPreferences.getString("score" + (i + 1), ""));
            Log.e("Scores", scores.get(i));
        }

        int time = convert_time_to_int(getScore(entry));
        for (int i = 0; i < 5; i ++){
            if(time < convert_time_to_int(getScore(scores.get(i)))){
                scores.add(i,entry);
                break;
            }
        }
        for (int i = 0; i < 5; i ++){
            editor.putString("score" + (i+1), scores.get(i));
        }
        editor.apply();

//        String temp1 = sharedPreferences.getString("score1","");
//        String temp2 = sharedPreferences.getString("score2","");
//        String temp3 = sharedPreferences.getString("score3","");
//        String temp4 = sharedPreferences.getString("score4","");
//
//        String[] temp_s = getCurrentScores(context).toArray(new String[0]);
//
//        for (int i = 0; i < 5; i++) {
//            if (entry.equals(temp_s[i])) {
//                return;
//            }
//        }
//
//        int latest_time = convert_time_to_int(getScore(entry));
//
//            if(latest_time < convert_time_to_int(getScore(sharedPreferences.getString("score1", "")))){
//            // shifting down the list
//            applySwap(sharedPreferences,"score2",temp1);
//            applySwap(sharedPreferences,"score3",temp2);
//            applySwap(sharedPreferences,"score4",temp3);
//            applySwap(sharedPreferences,"score5",temp4);
//            applySwap(sharedPreferences,"score1",entry);
//
//            }
//            else if(latest_time < convert_time_to_int(getScore(sharedPreferences.getString("score2", "")))){
//            // shifting down the list
//            applySwap(sharedPreferences,"score3",temp2);
//            applySwap(sharedPreferences,"score4",temp3);
//            applySwap(sharedPreferences,"score5",temp4);
//            applySwap(sharedPreferences,"score2",entry);
//
//
//            }
//            else if(latest_time < convert_time_to_int(getScore(sharedPreferences.getString("score3", "")))){
//            // shifting down the list
//            applySwap(sharedPreferences,"score4",temp3);
//            applySwap(sharedPreferences,"score5",temp4);
//            applySwap(sharedPreferences,"score3",entry);
//
//            }
//            else if(latest_time < convert_time_to_int(getScore(sharedPreferences.getString("score4", "")))){
//            // shifting down the list
//            applySwap(sharedPreferences,"score5",temp4);
//            applySwap(sharedPreferences,"score4",entry);
//
//            }
//            else if(latest_time < convert_time_to_int(getScore(sharedPreferences.getString("score5", "")))){
//                // shifting down the list
//            applySwap(sharedPreferences,"score5",entry);
//
//            }
//
    }
//    private void applySwap(SharedPreferences sharedPreferences, String score_key, String score_position){
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(score_key, score_position);
//        editor.apply();
//    }
    // convert string time to secs for easier comparison
    public int convert_time_to_int(String str){
        String[] time = str.split("\\." );

        int sec = Integer.parseInt(time[0]);
        int ms = Integer.parseInt(time[1]);

        return sec + ms;
    }




}
