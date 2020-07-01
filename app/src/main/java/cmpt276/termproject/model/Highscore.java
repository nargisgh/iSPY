package cmpt276.termproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.time.LocalDate;

public class Highscore {

        private  static final String DEFAULT_SCORE_1 = "default score1";
        private  static final String DEFAULT_SCORE_2 = "default_score2";
        private  static final String DEFAULT_SCORE_3 = "default_score3";
        private  static final String DEFAULT_SCORE_4 = "default_score4";
        private  static final String DEFAULT_SCORE_5 =  "default_score5";


        // default times
        private int time1;
        private int time2;
        private int time3;
        private int time4;
        private int time5;



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
        // or can update using sharedpreference
        public void update_highscore(Context context, TextView highscore1, TextView highscore2, TextView highscore3, TextView highscore4, TextView highscore5 ){

            // using default times for comparison
            time1 = convert_min_to_secs("2:30");
            time2 = convert_min_to_secs("3:00");
            time3 = convert_min_to_secs("3:10");
            time4 = convert_min_to_secs("3:30");
            time5 = convert_min_to_secs("3:40");
            SharedPreferences username = context.getSharedPreferences("username", Context.MODE_PRIVATE);
            SharedPreferences time = context.getSharedPreferences("time", Context.MODE_PRIVATE);

            int latest_time = convert_min_to_secs(time.getString("new time", ""));

            // if using sharedpreference to update
            SharedPreferences sharedPreferences = context.getSharedPreferences("default scores", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();


            if(latest_time < time1){
                time1 = latest_time;
                highscore1.setText("Name:" + username.getString("new username", "") + "Date:" +   LocalDate.now()+"Time:" + time.getString("new time", ""));

                // OR

                editor.putString(DEFAULT_SCORE_1,"Name:" + username.getString("new username", "") + "Date:" +   LocalDate.now()+"Time:" + time.getString("new time", ""));



            }

            else if(latest_time < time2){
                time2 = latest_time;
                highscore2.setText("Name:" + username.getString("new username", "") + "Date:" +  LocalDate.now()+ "Time:" + time.getString("new time", ""));


                // OR

                editor.putString(DEFAULT_SCORE_2,"Name:" + username.getString("new username", "") + "Date:" +   LocalDate.now()+"Time:" + time.getString("new time", ""));

            }

            else if(latest_time < time3){
                time3 = latest_time;
                highscore3.setText("Name:" + username.getString("new username", "") + "Date:" +  LocalDate.now()+ "Time:" + time.getString("new time", ""));

                // OR

                editor.putString(DEFAULT_SCORE_3,"Name:" + username.getString("new username", "") + "Date:" +   LocalDate.now()+"Time:" + time.getString("new time", ""));


            }

            else if(latest_time < time4){
                time4 = latest_time;
                highscore4.setText("Name:" + username.getString("new username", "") + "Date:" +  LocalDate.now()+ "Time:" + time.getString("new time", ""));

                // OR

                editor.putString(DEFAULT_SCORE_4,"Name:" + username.getString("new username", "") + "Date:" +   LocalDate.now()+"Time:" + time.getString("new time", ""));


            }

            else if(latest_time < time5){
                highscore5.setText("Name:" + username.getString("new username", "") + "Date:" + LocalDate.now()+"Time:" + time.getString("new time", ""));

                // OR

                editor.putString(DEFAULT_SCORE_5,"Name:" + username.getString("new username", "") + "Date:" +   LocalDate.now()+"Time:" + time.getString("new time", ""));


            }




        }

        // save name of player
        public void set_username(String str, Context context){
            SharedPreferences username = context.getSharedPreferences("username", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = username.edit();
            editor.putString("new username", str);
            editor.commit();

        }

        // save time used to play game
        public void set_time(String str,Context context){
            SharedPreferences time = context.getSharedPreferences("time", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = time.edit();
            editor.putString("new time", str);
            editor.commit();

        }








    }


