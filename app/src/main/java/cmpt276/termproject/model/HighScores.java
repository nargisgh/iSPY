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

    private String name_1;
    private String name_2;
    private String name_3;
    private String name_4;
    private String name_5;
    private String currentDateTime;

    int score_1 = 5;
    int score_2 = 4;
    int score_3 = 3;
    int score_4 = 2;
    int score_5 = 1;


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

        SharedPreferences sharedPreferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < DEFAULT_SCORES.size(); i ++){
            editor.putString(DEFAULT_SCORES.get(i), default_scores[i]);
        }
        editor.apply();

        //reinitialize the array to so we dont add more than 5 elements
        arr = new ArrayList<>();
        SharedPreferences shared_Preferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
        for (int i = 0; i < DEFAULT_SCORES.size(); i ++){
            arr.add(shared_Preferences.getString(DEFAULT_SCORES.get(i), ""));
        }
    }


    // get default scores
    public ArrayList<String> get_default_scores(){
        //getScore();
        return arr;
    }


    public String getCurrentDateTime(){
        Date date = new Date();
        return currentDateTime = DateFormat.getDateTimeInstance().format(date);
    }

    public void AddEntry(String name, String score, String time){
        String entry = (score+ " "+name+ " " + getCurrentDateTime());
    }

    public ArrayList<String> getNewscores(Context context){
        SharedPreferences shared_Preferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
        String newScore= shared_Preferences.getString("New Score","");
        if(newScore.length() == 0){
            return get_default_scores();
        }
        else {
            arr.add(newScore);
            return arr;
        }
    }


    public void setNewValues(Context context, String entry){
        SharedPreferences new_sharedPreferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = new_sharedPreferences.edit();
        editor.putString("New Score", entry);
        editor.apply();

        //updateScores();
    }


    public String getScore(){
        String[] temp = new String[arr.size()];
        for (int i=0;i<arr.size();i++){
            temp[i] = (arr.get(i).split(" ")[0]);
            //once i have scores to work with I can convert to integer and compare.
        }
        //compare scores of each place and then arr.remove(index of lowest score)
        //swap and populate new values

        return temp[0];
    }
}
