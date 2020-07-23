package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.List;

import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrGallery.FlickrImage;
import cmpt276.termproject.model.FlickrGallery.FlickrManager;

public class CameraRoll extends AppCompatActivity {

    private FlickrManager flickrManager;
    private List<FlickrImage> imageList;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_roll);

        flickrManager = FlickrManager.getInstance();
        tableLayout = findViewById(R.id.camera_roll_table);

        showBitmaps();
    }

    public void showBitmaps(){
        imageList = flickrManager.getImageList(getApplicationContext());

        for (int i = 0; i < imageList.size(); i ++){

            TableRow tr = new TableRow(this);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(imageList.get(i).getImgBitmap());
            tr.addView(imageView);
            tableLayout.addView(tr);

        }
    }


    public static Intent makeIntent(Context context){
        return new Intent(context,CameraRoll.class);
    }
}