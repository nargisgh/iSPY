/*
Handles storing and manipulating high score data to be displayed on the high score screen.
 */
package cmpt276.termproject.model;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cmpt276.termproject.R;

/* Functions to set default scores, save and update new scores.
 * Using Singleton Method and Shared Preferences to pass data
 */

public class HighScores{

    private final List<String> DEFAULT_SCORES = new ArrayList<>();
    ArrayList<String> arr = new ArrayList<>();

    //Singleton Stuff
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

    //populate defaultScores
    public void setDefaultScores(Context context, String[] defaultScores, String filename){
        SharedPreferences sharedPreferences = context.getSharedPreferences("updated scores"+filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int i=0;i<defaultScores.length;i++){
            int j = i+1;
            editor.putString("score"+j, defaultScores[i]);
        }
        editor.apply();
    }

    // get default scores
    public ArrayList<String> getDefaultScores(Context context, String filename){
        arr = new ArrayList<>();
        for (int i = 1; i <= 5; i ++){
            SharedPreferences sharedPreferences = context.getSharedPreferences("updated scores"+filename, Context.MODE_PRIVATE);
            arr.add(sharedPreferences.getString("score"+i, ""));
        }
        return arr;
    }

    public ArrayList<String> getCurrentScores(Context context, String filename){
        arr = new ArrayList<>();
        for (int i = 1; i <= 5; i ++){
            SharedPreferences  sharedPreferences = context.getSharedPreferences("updated scores"+filename, Context.MODE_PRIVATE);

            arr.add(sharedPreferences.getString("score"+i, ""));
        }
        return arr;
    }

    public String getCurrentDateTime(){
        Date date = new Date();
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT).format(date);
    }

    public String getScore(String entry){
        String[] arr = entry.split("/");
        return arr[0];
    }

    // Here all the times are converted  to secs for easier comparison
    // As each time is compared, shared preferences are updated and every entry is moved down the list
    // so when we are updating rows on table layout we just need to call the sharedpreferences
    //  https://www.youtube.com/watch?v=_cV7cgQFDo0

    public void update(String entry, Context context,String filename) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("updated scores"+filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String[] tempS = getCurrentScores(context,filename).toArray(new String[0]);

        for (int i = 0; i < 5; i++) {
            if (entry.equals(tempS[i])) {
                return;
            }
        }

        //Get All the high scores into an array
        List<String> scores = new ArrayList<>();
        for (int i = 0; i < 5; i ++){
            scores.add(sharedPreferences.getString("score" + (i + 1), ""));
        }

        // Check where to place new high score
        double time = convertTimeToDouble(getScore(entry));
        for (int i = 0; i < 5; i ++){
            if(time < convertTimeToDouble(getScore(scores.get(i)))){
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
    public double convertTimeToDouble(String str){
        String[] time = str.split("\\." );
        double sec = Integer.parseInt(time[0]);
        double ms = Double.parseDouble(time[1]) / 1000f;
        return sec + ms;
    }

    public void setInitDEFFalse(Context context){
        String[] arr = context.getResources().getStringArray(R.array.initialized);
        SharedPreferences sp = context.getSharedPreferences("initialized", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        for (int i = 0; i < arr.length; i++)
            editor.putBoolean(arr[i],false);

        editor.apply();
    }

    public boolean getInitDEFBool(Context context, String order, String draw){
        String name = "order_"+order+"_draw_"+draw;
        SharedPreferences sp = context.getSharedPreferences("initialized", Context.MODE_PRIVATE);
        return sp.getBoolean(name,false);
    }

    public void setInitDEFBool(Context context, String order, String draw, Boolean bool){
        String name = "order_"+order+"_draw_"+draw;
        SharedPreferences sp = context.getSharedPreferences("initialized", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name,bool);
        editor.apply();
    }

    // to identify which option highscores is updating
    public String getFileName(String order, String draw){
        return "order_"+order+"_draw"+draw;
    }

    // getting order from options activity
    public String getOrder(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("Order","2");
    }

    // getting draw pile size from options activity
    public String getDrawPileSize(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("Size","0");
    }
    //retrieve DEF array of specific option
    public String[] getDEFArray(String order, String drawPileSize, Context context){

        String arrayName = "default_highscores_"+order+"_"+drawPileSize;
        int id = context.getResources().getIdentifier(arrayName, "array",context.getPackageName());
        return context.getResources().getStringArray(id);
    }
}