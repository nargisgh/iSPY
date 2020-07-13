/*
Handles storing and manipulating high score data to be displayed on the high score screen.
 */
package cmpt276.termproject.model;
import android.content.Context;
import android.content.SharedPreferences;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HighScores{

    private List<String> DEFAULT_SCORES = new ArrayList<>();
    ArrayList<String> arr = new ArrayList<>();

    //Signleton Stuff
    private static HighScores instance;
    public HighScores(){
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

        String[] temp_s = getCurrentScores(context).toArray(new String[0]);
        for (int i = 0; i < 5; i++) {
            if (entry.equals(temp_s[i])) {
                return;
            }
        }

        //Get All the highscores into an array
        List<String> scores = new ArrayList<>();
        for (int i = 0; i < 5; i ++){
            scores.add(sharedPreferences.getString("score" + (i + 1), ""));
        }

        // Check where to place new highscore
        double time = convert_time_to_double(getScore(entry));
        for (int i = 0; i < 5; i ++){
            if(time < convert_time_to_double(getScore(scores.get(i)))){
                scores.add(i,entry);
                break;
            }
        }
        //Reset the highscores preferences from the temp array
        for (int i = 0; i < 5; i ++){
            editor.putString("score" + (i+1), scores.get(i));
        }
        editor.apply();
    }

    // convert string time to secs for easier comparison
    public double convert_time_to_double(String str){
        String[] time = str.split("\\." );
        double sec = Integer.parseInt(time[0]);
        double ms = Double.parseDouble(time[1]) / 1000f;
        return sec + ms;
    }
}