package cmpt276.termproject.model;

public class GalleryItem {

    private String mCaption;
    private String mId;
    private String mUrl;

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
