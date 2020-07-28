package cmpt276.termproject.model.FlickrGallery;
import android.net.Uri;

public class GalleryItem {
    //Source: "Android Programming: The Big Nerd Ranch Guide 3rd edition" - Bill Philips, Chris Stewart, and Kristin Marsciano
    //Ch 25-29
    /*
    Holds the information about each Item in the flickr gallery
     */

    private String Caption;
    private String ID;
    private String Url;
    private String Owner;

    public String getOwner() {
        return Owner;
    }
    public void setOwner(String owner) {
        Owner = owner;
    }
    public Uri getPhotoPageUri() {
        return Uri.parse("https://www.flickr.com/photos/").buildUpon()
                        .appendPath(Owner).appendPath(ID).build();
    }

    @Override
    public String toString() {
        return Caption;
    }

    public void setId(String id) {
        this.ID = id;
    }

    public String getId() {
        return ID;
    }

    public void setCaption(String title) {
        this.Caption = title;
    }
    public String getCaption(){
        return Caption;
    }

    public void setUrl(String url_s) {
        this.Url = url_s;
    }
    public String getUrl(){
        return Url;
    }
}