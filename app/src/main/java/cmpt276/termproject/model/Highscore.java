package cmpt276.termproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Highscore {

        private  static final String DEFAULT_SCORE_1 = "default score1";
        private  static final String DEFAULT_SCORE_2 = "default_score2";
        private  static final String DEFAULT_SCORE_3 = "default_score3";
        private  static final String DEFAULT_SCORE_4 = "default_score4";
        private  static final String DEFAULT_SCORE_5 =  "default_score5";


        // default times for comparison
        private String time1;
        private String time2;
        private String time3;
        private String time4;
        private String time5;





        //populate default_scores
        public void set_default_scores(Context context){
            SharedPreferences sharedPreferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(DEFAULT_SCORE_1, "Name: player1  Date:Jun 3, 2020   Time: 2:30");
            editor.putString(DEFAULT_SCORE_2, "Name: player2  Date: Jun 1, 2020  Time: 3:00");
            editor.putString(DEFAULT_SCORE_3, "Name: player3  Date: May 25, 2020 Time: 3:10");
            editor.putString(DEFAULT_SCORE_4, "Name: player4  Date: May 30, 2020 Time: 3:30");
            editor.putString(DEFAULT_SCORE_5, "Name: player5  Date: Jun 2, 2020  Time: 3:40");
            editor.commit();

            // using default times for comparison
            time1 ="2:30";
            time2 = "3:00";
            time3 = "3:10";
            time4 = "3:30";
            time5 = "3:40";
        }

        // get default scores
        public String[] get_default_scores(Context context){


            SharedPreferences sharedPreferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
            String score1 = sharedPreferences.getString(DEFAULT_SCORE_1,"");
            String score2 = sharedPreferences.getString(DEFAULT_SCORE_2,"");
            String score3 = sharedPreferences.getString(DEFAULT_SCORE_3,"");
            String score4 = sharedPreferences.getString(DEFAULT_SCORE_4,"");
            String score5 = sharedPreferences.getString(DEFAULT_SCORE_5,"");

            String arr[] = {score1,score2,score3,score4,score5};
            return  arr;
        }


        // convert string time to secs for easier comparison
        public int convert_min_to_secs(String str){
            String[] time = str.split(":");
            int min = Integer.parseInt(time[0]);
            int sec = Integer.parseInt(time[1]);
            int result = (min*60) + sec;
            return result;
        }


        // use textview to access id and update correct textview
        //public void update_highscore(Context context, TextView highscore1, TextView highscore2, TextView highscore3, TextView highscore4, TextView highscore5 )
        // or can update using sharedpreference
        public void update_highscore(Context context){

           // int convert_time = convert_min_to_secs(time.getString("new time", ""));


            // using sharedpreference to update
            SharedPreferences sharedPreferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // comparison of times
            String[] default_time1 = time1.split(":");
            int min1 = Integer.parseInt(default_time1[0]);
            int sec1 = Integer.parseInt(default_time1[1]);

            String[] default_time2 = time2.split(":");
            int min2 = Integer.parseInt(default_time2[0]);
            int sec2 = Integer.parseInt(default_time2[1]);

            String[] default_time3 = time3.split(":");
            int min3 = Integer.parseInt(default_time3[0]);
            int sec3 = Integer.parseInt(default_time3[1]);


            String[] default_time4 = time4.split(":");
            int min4 = Integer.parseInt(default_time4[0]);
            int sec4 = Integer.parseInt(default_time4[1]);

            String[] default_time5 = time5.split(":");
            int min5 = Integer.parseInt(default_time5[0]);
            int sec5 = Integer.parseInt(default_time5[1]);


            // latest time comparison
            String[] latest_time = get_time(context).split(":");
            int latest_min = Integer.parseInt(latest_time[0]);
            int latest_sec = Integer.parseInt(latest_time[1]);




            if(latest_min < min1 || latest_sec < sec1){
                String temp = time1;
                time1 = get_time(context);
                time2 = temp;
               // highscore1.setText("Name:" + username.getString("new username", "") + "Date:" +   LocalDate.now()+"Time:" + time.getString("new time", ""));


                editor.putString(DEFAULT_SCORE_1,"Name:" + get_username(context) + "Date:" +  current_date()+"Time:" + get_time(context));
                editor.putString(DEFAULT_SCORE_2,"Name:" +get_username(context) + "Date:" +   current_date()+"Time:" + time2);
            }

            else if(latest_min < min2 || latest_sec < sec2){
                String temp = time2;
                time2 = get_time(context);
                time3 = temp;
                //highscore2.setText("Name:" + username.getString("new username", "") + "Date:" +  LocalDate.now()+ "Time:" + time.getString("new time", ""));




                editor.putString(DEFAULT_SCORE_2,"Name:" +get_username(context) + "Date:" +  current_date()+"Time:" + get_time(context));
                editor.putString(DEFAULT_SCORE_3,"Name:" + get_username(context)+ "Date:" +   current_date()+"Time:" + time3);
            }

            else if(latest_min < min3 || latest_sec < sec3){
                String temp = time3;
                time3 = get_time(context);
                time4 = temp;


                //highscore3.setText("Name:" + username.getString("new username", "") + "Date:" +  LocalDate.now()+ "Time:" + time.getString("new time", ""));


                editor.putString(DEFAULT_SCORE_3,"Name:" + get_username(context) + "Date:" +  current_date()+"Time:" + get_time(context));
                editor.putString(DEFAULT_SCORE_3,"Name:" + get_username(context)+ "Date:" +  current_date()+"Time:" + time4);

            }

            else if(latest_min < min4 || latest_sec < sec4){
                String temp = time4;
                time4 = get_time(context);
                time5 = temp;
                //highscore4.setText("Name:" + username.getString("new username", "") + "Date:" +  LocalDate.now()+ "Time:" + time.getString("new time", ""));

                // OR

                editor.putString(DEFAULT_SCORE_4,"Name:" + get_username(context) + "Date:" +  current_date()+"Time:" + get_time(context));
                editor.putString(DEFAULT_SCORE_5,"Name:" + get_username(context)+ "Date:" +  current_date()+"Time:" + time5);

            }

            else if(latest_min < min5 || latest_sec < sec5){

                time5 = get_time(context);
                //highscore5.setText("Name:" + username.getString("new username", "") + "Date:" + LocalDate.now()+"Time:" + time.getString("new time", ""));


                editor.putString(DEFAULT_SCORE_5,"Name:" + get_username(context) + "Date:" +  current_date()+"Time:" + get_time(context));


            }

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
        public void set_time(String str,Context context){
            SharedPreferences time = context.getSharedPreferences("time", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = time.edit();
            editor.putString("new time", str);
            editor.commit();

        }

        public String get_time(Context context){
            SharedPreferences time = context.getSharedPreferences("time", Context.MODE_PRIVATE);
            return time.getString("new time", "");

        }

        // getting current date
        public String current_date(){
            DateTimeFormatter date = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            LocalDateTime current = LocalDateTime.now();
            return date.format(current);
        }



    }


