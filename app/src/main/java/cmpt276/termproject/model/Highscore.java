package cmpt276.termproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Highscore {

   ArrayList arr = new ArrayList();



   String score1;
    String score2;
    String score3;
    String score4;
    String score5;


// Here we will input the default array just to setup the global string variables with default entries
// save variables with string entries
    public void set_default_scores(Context context, String[] default_scores){
        SharedPreferences sharedPreferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
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
    // cite: https://www.youtube.com/watch?v=_cV7cgQFDo0

    public void update(String entry, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);

        int latest_time = convert_min_to_secs(getScore(entry));

        int compare1 = convert_min_to_secs(getScore(sharedPreferences.getString("score1","")));
        int compare2 = convert_min_to_secs(getScore(sharedPreferences.getString("score2","")));
        int compare3 = convert_min_to_secs(getScore(sharedPreferences.getString("score3","")));
        int compare4 = convert_min_to_secs(getScore(sharedPreferences.getString("score4","")));
        int compare5 = convert_min_to_secs(getScore(sharedPreferences.getString("score5","")));

        // score1 comparison
        if(latest_time < compare1){


            String temp = score1;
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
            String temp = score2;
            score2 = entry;
            score3 = temp;


            // shifting down the list
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score2", score2);
            editor.putString("score3",score3);
            editor.apply();



        }

        //score3 comparison
        else if(latest_time < compare3){
            String temp = score3;
            score3 = entry;
            score4 = temp;


            // shifting down the list
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score3", score3);
            editor.putString("score4",score4);
            editor.apply();



        }

        //score4 comparison
        else if(latest_time < compare4){
            String temp = score4;
            score4 = entry;
            score5 = temp;


            // shifting down the list
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score4", score4);
            editor.putString("score5",score5);
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



        // convert string time to secs for easier comparison
        public int convert_min_to_secs(String str){
            String[] time = str.split(":");
            int min = Integer.parseInt(time[0]);
            int sec = Integer.parseInt(time[1]);
            int result = (min*60) + sec;
            return result;
        }



        // save name of player
        public void set_username(String str, Context context){
            SharedPreferences username = context.getSharedPreferences("username", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = username.edit();
            editor.putString("new username", str);
            editor.commit();

        }

        public String get_username(Context context){
            SharedPreferences username = context.getSharedPreferences("username", Context.MODE_PRIVATE);
            return username.getString("new username", "");
        }

        // save time used to play game
        public void set_time(int num,Context context){
            SharedPreferences time = context.getSharedPreferences("time", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = time.edit();
            editor.putInt("new time", num);
            editor.commit();

        }

        public int get_time(Context context){
            SharedPreferences time = context.getSharedPreferences("time", Context.MODE_PRIVATE);
            return time.getInt("new time", 0);

        }

        // getting current date and time
        public String current_date(){
            DateTimeFormatter date = DateTimeFormatter.ofPattern("MMMM dd, yyyy at HH:mm:ss");
            LocalDateTime current = LocalDateTime.now();
            return date.format(current);
        }



    public String convert_sec_to_timeformat(int secs){
        int hour = secs/3600;
        int minute = (secs % 3600)/60;
        int seconds = secs % 60;
        return String.format("%02d:%02d", minute,seconds);
    }

    }


