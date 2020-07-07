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


    private String currentDateTime;



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
    public ArrayList<String> get_default_scores(Context context){
        //getScore();
        arr = new ArrayList<>();
        SharedPreferences shared_Preferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
        for (int i = 0; i < DEFAULT_SCORES.size(); i ++){
            arr.add(shared_Preferences.getString(DEFAULT_SCORES.get(i), ""));
        }
        return arr;
    }


    public String getCurrentDateTime(){
        Date date = new Date();
        return currentDateTime = DateFormat.getDateTimeInstance().format(date);
    }

    // This is the format I am thinking of so that we can split string with "/"
    // format : score / name/ currentdate , so all entries should look like this

    public void AddEntry(String name, String score){
        String entry = (score+ "/ "+name+ "/ " + getCurrentDateTime());
    }


    public String getName(String entry){
        String[] arr = entry.split("/");
        return arr[1];

    }

    public String getScore(String entry){
        String[] arr = entry.split("/");
        return arr[0];

    }

    public String getDate_time(String entry){
        String[] arr = entry.split("/");
        return arr[2];

    }



    public ArrayList<String> getNewscores(Context context){
        SharedPreferences shared_Preferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
        String newScore= shared_Preferences.getString("New Score","");
        if(newScore.length() == 0){
            return get_default_scores(context);
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

    }





    // Here we will input the default array just to setup the global string variables with default entries
// save variables with string entries
    public void initialize_default_scores(Context context, String[] default_scores){
        SharedPreferences sharedPreferences = context.getSharedPreferences("updated scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString("score1", default_scores[0]);
        editor.putString("score2", default_scores[1]);
        editor.putString("score3", default_scores[2]);
        editor.putString("score4", default_scores[3]);
        editor.putString("score5", default_scores[4]);


        score1 = sharedPreferences.getString("score1","");
        score2 = sharedPreferences.getString("score2","");
        score3 = sharedPreferences.getString("score3","");
        score4 = sharedPreferences.getString("score4","");
        score5 = sharedPreferences.getString("score5","");


        editor.apply();
    }

    // Here all the times are converted  to secs for easier comparison
    // As each time is compared, sharedpreferences are updated and every entry is moved down the list
    // so when we are updating rows on tablelayout we just need to call the sharedpreferences
    //  https://www.youtube.com/watch?v=_cV7cgQFDo0

    public void update(String entry, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("updated scores", Context.MODE_PRIVATE);

        int latest_time = convert_min_to_secs(getScore(entry));

        int compare1 = convert_min_to_secs(getScore(sharedPreferences.getString("score1","")));
        int compare2 = convert_min_to_secs(getScore(sharedPreferences.getString("score2","")));
        int compare3 = convert_min_to_secs(getScore(sharedPreferences.getString("score3","")));
        int compare4 = convert_min_to_secs(getScore(sharedPreferences.getString("score4","")));
        int compare5 = convert_min_to_secs(getScore(sharedPreferences.getString("score5","")));

        // score1 comparison
        if(latest_time < compare1){

            String temp = sharedPreferences.getString("score1","");
            score1 = entry;
            score2 = temp;

            // shifting down the list
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score2", score2);
            editor.putString("score1",score1);
            editor.apply();



        }

        //score2 comparison
        else if(latest_time < compare2){
            String temp = sharedPreferences.getString("score2","");
            score2 = entry;
            score3 = temp;


            // shifting down the list
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score3", score3);
            editor.putString("score2",score2);
            editor.apply();



        }

        //score3 comparison
        else if(latest_time < compare3){
            String temp = sharedPreferences.getString("score3","");;
            score3 = entry;
            score4 = temp;


            // shifting down the list
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score4", score4);
            editor.putString("score3",score3);
            editor.apply();



        }

        //score4 comparison
        else if(latest_time < compare4){
            String temp = sharedPreferences.getString("score4","");;
            score4 = entry;
            score5 = temp;

            // shifting down the list
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score5", score5);
            editor.putString("score4",score4);
            editor.apply();



        }

        //score5 comparison
        else if(latest_time < compare5){
            score5 = entry;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score5", score5);
            editor.apply();


        }




    }

    // convert string time to secs for easier comparison
    public int convert_min_to_secs(String str){
        String[] time = str.split(":");
        int hr = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
        int sec = Integer.parseInt(time[2]);
        int result = (hr*3600)+(min*60) + sec;
        return result;
    }








}
