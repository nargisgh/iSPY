package cmpt276.termproject.model.FlickrGallery;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

    public PollService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }
        String query = QueryPrefs.getStoredQuery(this);
        String lastResultId = QueryPrefs.getLastResultId(this);
        List<GalleryItem> items;
        if (query == null) {
            items = new DownloadGalleryItems().fetchRecentPhotos();
        }
        else {
            items = new DownloadGalleryItems().searchPhotos(query);
        }
        if (items.size() == 0) {
            return;
        }
        String resultId = items.get(0).getId();
        if (resultId.equals(lastResultId)) {
            Log.i(TAG, "Got an old result: " + resultId);
        }
        else {
            Log.i(TAG, "Got a new result: " + resultId);
        }
        QueryPrefs.setLastResultId(this, resultId);
    }
    private boolean isNetworkAvailableAndConnected() {
        /* checking network availability */

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert cm != null;
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
    }

    /*public static void setServiceAlarm(Context context, boolean isOn) {
        //System service that will send intents for us

        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        }
        else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        QueryPrefs.setAlarmOn(context, isOn);
    }*/


}
