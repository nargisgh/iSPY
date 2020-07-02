package cmpt276.termproject.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cmpt276.termproject.ui.HighScoreActivity;

public class HighScores{

    private String name_1;
    private String name_2;
    private String name_3;
    private String name_4;
    private String name_5;
    String currentTime;
    String currentDate;

    int score_1 = 5;
    int score_2 = 4;
    int score_3 = 3;
    int score_4 = 2;
    int score_5 = 1;

    private static final String DEFAULT_SCORE_1 = "default score1";
    private static final String DEFAULT_SCORE_2 = "default_score2";
    private static final String DEFAULT_SCORE_3 = "default_score3";
    private static final String DEFAULT_SCORE_4 = "default_score4";
    private static final String DEFAULT_SCORE_5 = "default_score5";

    ArrayList<String> arr = new ArrayList<>();

    private static HighScores instance;
    public static HighScores getInstance(){
        if (instance == null){
            instance = new HighScores();
        }
        return instance;
    }

    //populate default_scores
    public void set_default_scores(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_SCORE_1, "Score1 Flash Jun 3, 2020 at 14:30");
        editor.putString(DEFAULT_SCORE_2, "Score2 BigBrain Jun 1, 2020 at 10:00");
        editor.putString(DEFAULT_SCORE_3, "Score3 SuperMan was here May 25, 2020 at 12:00");
        editor.putString(DEFAULT_SCORE_4, "Score4 ... May 30, 2020 at 15:32");
        editor.putString(DEFAULT_SCORE_5, "Score5 MrSlow Jun 2, 2020 at 1:37");
        editor.apply();

        SharedPreferences shared_Preferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
        String score1 = shared_Preferences.getString(DEFAULT_SCORE_1,"");
        String score2 = shared_Preferences.getString(DEFAULT_SCORE_2,"");
        String score3 = shared_Preferences.getString(DEFAULT_SCORE_3,"");
        String score4 = shared_Preferences.getString(DEFAULT_SCORE_4,"");
        String score5 = shared_Preferences.getString(DEFAULT_SCORE_5,"");
        arr.add(score1);
        arr.add(score2);
        arr.add(score3);
        arr.add(score4);
        arr.add(score5);
    }

    // get default scores
    public ArrayList<String> get_default_scores(Context context){
        //getScore();
        return arr;
    }

    public String getCurrentTime() {
        return currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }
    public String getCurrentDate(){
        return currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public void AddEntry(String name, String score, String time){
        String entry = (score+ " "+name+ " " + getCurrentDate() +" at "+ time);
    }

    public ArrayList<String> getNewscores(Context context){
        SharedPreferences shared_Preferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
        String newScore= shared_Preferences.getString("New Score","");
        if(newScore.length() == 0){
            return get_default_scores(context);

        }
        else {
            arr.remove(4);
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
