package cmpt276.termproject.model.FlickrGallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlickrManager {

    private List<FlickrImage> imageList;
    private static FlickrManager instance;

    //Singleton stuff
    private FlickrManager(){
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

    }

    public List<FlickrImage> getImageList(Context context){
        if (imageList == null){
            createImageList(context);
        }
        return imageList;
    }

    public void removeImage(FlickrImage img, Context context){
        //Remove from internal Storage
        String filename = img.getImgID() + ".png";
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


        String filename = img.getImgID() + ".png";
        try (FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)){
                img.getImgBitmap().compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            }
        catch (Exception e){
            e.printStackTrace();
        }


    }



}
