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
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;
    private boolean mHasQuit = false;

    private Handler mRequestHandler;
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();

    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;
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
        mThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }};
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }
    public void queueThumbnail(T target, String url)
    {
        //expects an object of type T to use as identifier
        // for the download and a string URL for the download (Photo adapter will call this)
        Log.i(TAG, "Got a URL: " + url);
        if (url == null) {
            mRequestMap.remove(target);
        }
        else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
        }

    }
    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRequestMap.clear();
    }

    private void handleRequest(final T target) {
        try {
            final String url = mRequestMap.get(target);
            if (url == null) {
                return;
            }
            byte[] bitmapBytes = new DownloadGalleryItems().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            //Downloading and displaying images
            mResponseHandler.post(new Runnable() {
                public void run() {
                    //all code inside run will be executed on main thread
                    if (!Objects.equals(mRequestMap.get(target), url) || mHasQuit) {
                        return;
                    }
                    mRequestMap.remove(target);
                    //setting bitmap on target PhotoAdapter
                    mThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
                } });
        }
        catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }
}
