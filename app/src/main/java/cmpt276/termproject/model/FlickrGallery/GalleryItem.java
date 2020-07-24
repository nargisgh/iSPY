package cmpt276.termproject.model.FlickrGallery;

import android.net.Uri;

public class GalleryItem {

    private String mCaption;
    private String mId;
    private String mUrl;
    private String mOwner;

    public String getOwner() {
        return mOwner;
    }
    public void setOwner(String owner) {
        mOwner = owner;
    }
    public Uri getPhotoPageUri() {
        return Uri.parse("https://www.flickr.com/photos/").buildUpon()
                        .appendPath(mOwner).appendPath(mId).build();
    }

    @Override
    public String toString() {
        return mCaption;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setCaption(String title) {
        this.mCaption = title;
    }
    public String getCaption(){
        return mCaption;
    }

    public void setUrl(String url_s) {
        this.mUrl = url_s;
    }
    public String getUrl(){
        return mUrl;
    }
}
