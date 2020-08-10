/*
Handles connection to the Flickr API.
*/
package cmpt276.termproject.model.FlickrGallery;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import java.util.List;

public class PollService extends IntentService {
    //Source: "Android Programming: The Big Nerd Ranch Guide 3rd edition" - Bill Philips, Chris Stewart, and Kristin Marsciano
    //Ch 25-29
    private static final String TAG = "PollService";

    /*IntentService
    * Bsck end, service will poll Flickr in the bg to preform newtorking in bg safely*/
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
            items = new FlickrFetchr().fetchRecentPhotos();
        }
        else {
            items = new FlickrFetchr().searchPhotos(query);
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
}