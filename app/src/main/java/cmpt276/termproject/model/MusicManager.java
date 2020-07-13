/*
Music manager that handles game background music and it's functions within the app.
 */
package cmpt276.termproject.model;
import android.content.Context;
import android.media.MediaPlayer;
import cmpt276.termproject.R;

public class MusicManager {
    MediaPlayer themeSong;
    private static MusicManager instance;

    public static MusicManager getInstance(){
        if (instance == null)
            instance = new MusicManager();
        return  instance;
    }
    public void setThemeSong(Context context){
        themeSong = MediaPlayer.create(context, R.raw.themesong);
    }
    public void play(){
        themeSong.start();
        themeSong.setLooping(true);
    }
    public void stop(){
        themeSong.release();
    }
    public void pause(){
        themeSong.pause();
    }
}