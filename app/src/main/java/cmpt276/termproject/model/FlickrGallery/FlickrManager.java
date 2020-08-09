/*
Manager for the Flickr images, creates the bitmaps list, allows for storing and
removing the flickr Images.
*/
package cmpt276.termproject.model.FlickrGallery;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlickrManager {

    private List<FlickrImage> imageList;
    private static FlickrManager instance;
    private final List<FlickrImage> removeList;

    //Singleton stuff
    private FlickrManager(){
        removeList = new ArrayList<>();
    }
    public static FlickrManager getInstance(){
        if (instance == null){
            instance = new FlickrManager();
        }
        return instance;
    }

    public void createImageList(Context context) {
        imageList = new ArrayList<>();
        String[] files = context.fileList();
        for (String file: files){
            try {
                FileInputStream fileInputStream = context.openFileInput(file);
                Bitmap bitmap =  BitmapFactory.decodeStream(fileInputStream);

                String[] tokens = file.split("\\.(?=[^.]+$)");
                FlickrImage img = new FlickrImage(tokens[0], bitmap);
                imageList.add(img);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Collections.shuffle(imageList);
    }

    public void addToRemoveList(FlickrImage img){
        removeList.add(img);
    }

    public void removeFromRemoveList(FlickrImage img){
        removeList.remove(img);
    }

    public List<FlickrImage> getRemoveList(){
        return removeList;
    }

    public List<FlickrImage> getImageList(Context context){
        if (imageList == null){
            createImageList(context);
        }
        return imageList;
    }

    public void removeImage(FlickrImage img, Context context){
        //Remove from internal Storage
        String filename = img.getImgId() + ".png";
        String[] files = context.fileList();

        for (String file: files){
            if (filename.equals(file)){
                context.deleteFile(file);
            }
        }
        //Remove bitmap from list of bmps
        imageList.remove(img);
    }

    public void saveImage(FlickrImage img, Context context){
        if(imageList!= null && imageList.size()>1) {
            for (int i = 0; i < imageList.size(); i++) {
                if (img.getImgBitmap().sameAs(imageList.get(i).getImgBitmap())) {
                    return;
                }
            }
        }
        String filename = img.getImgId() + ".png";
        try (FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)){
                img.getImgBitmap().compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}