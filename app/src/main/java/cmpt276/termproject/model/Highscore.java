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


//    // default times for comparison
//    private int time1 = convert_min_to_secs("2:30");
//    private int time2 = convert_min_to_secs("3:00");
//    private int time3 = convert_min_to_secs("3:10");
//    private int time4 = convert_min_to_secs("3:30");
//    private int time5 = convert_min_to_secs("3:40");





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

        // getting current date
        public String current_date(){
            DateTimeFormatter date = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            LocalDateTime current = LocalDateTime.now();
            return date.format(current);
        }


        // adding time to an arraylist where all the time is stored
    // sorts list with the added time and get the index of added time
    // so that at that index we can add the current username, date and time
    public int add_time(int time, ArrayList list) {
        String time_str = String.valueOf(time);
        int index = 0;

        list.add(time);
        Collections.sort(list);
        for (int i = 0; i < 5; i++) {
            if (list.get(i).toString() == time_str) {
                index = i;

            }

        }
        return index;
    }



    public String convert_sec_to_timeformat(int secs){
        int hour = secs/3600;
        int minute = (secs % 3600)/60;
        int seconds = secs % 60;
        return String.format("%02d:%02d", minute,seconds);
    }

    }


