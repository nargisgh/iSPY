package cmpt276.termproject.model.FlickrGallery;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* Assembling a bg thread
* */

public class ThumbnailDownloader<T> extends HandlerThread {
    //Source: "Android Programming: The Big Nerd Ranch Guide 3rd edition" - Bill Philips, Chris Stewart, and Kristin Marsciano
    //Ch 25-29
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;
    private boolean hasQuit = false;

    private Handler requestHandler;
    private final ConcurrentMap<T,String> requestMap = new ConcurrentHashMap<>();

    private final Handler responseHandler;
    private ThumbnailDownloadListener<T> thumbnailDownloadListener;
    //<T> type is a generic argument rather than locking the user
    // into a specific type of object as the identifier, more flexible

    public interface ThumbnailDownloadListener<T> {
        //be called when img has been fully downloaded and is ready to be added to UI
        // listener delegates the responsibility of what to do with the img

        // doing so separates downloading task from UI
        // updating so thumbnailDownloader can be used for other View objects needed

        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener listener) {
        /*Will be called when image has been fully downloaded and is ready o be added to the UI*/
        thumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        this.responseHandler = responseHandler;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        /*Called before the Looper checks the queue for the first time
        * Good place to create Handler implementation*/
        requestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + requestMap.get(target));
                    handleRequest(target);
                }
            }};
    }

    @Override
    public boolean quit() {
        hasQuit = true;
        return super.quit();
    }
    public void queueThumbnail(T target, String url)
    {
        //expects an object of type T to use as identifier
        // for the download and a string URL for the download (Photo adapter will call this)
        Log.i(TAG, "Got a URL: " + url);
        if (url == null) {
            requestMap.remove(target);
        }
        else {
            requestMap.put(target, url);
            requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
        }

    }
    public void clearQueue() {
        requestHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

    private void handleRequest(final T target) {
        /*Helper method where downloading happens
        * Checking existence of URL, then pass Url to instance of FlickrFetchr
        * Using BitmapFactory to construct a bitmap with the array of bytes returned from */
        try {
            final String url = requestMap.get(target);
            if (url == null) {
                return;
            }
            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            //Downloading and displaying images
            responseHandler.post(new Runnable() {
                public void run() {
                    //all code inside run will be executed on main thread
                    if (!Objects.equals(requestMap.get(target), url) || hasQuit) {
                        return;
                    }
                    requestMap.remove(target);
                    //setting bitmap on target PhotoAdapter
                    thumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
                } });
        }
        catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }
}