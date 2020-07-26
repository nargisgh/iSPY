package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.List;

import cmpt276.termproject.R;
import cmpt276.termproject.model.flickrGallery.FlickrImage;
import cmpt276.termproject.model.flickrGallery.FlickrManager;
/* Interacts with FlickrManager to display added photos from API
*  Can Remove or Add */
public class CameraRoll extends AppCompatActivity {
    //Source: "Android Programming: The Big Nerd Ranch Guide 3rd edition" - Bill Philips, Chris Stewart, and Kristin Marsciano
    //Ch. 25-29
    private FlickrManager flickrManager;
    private List<FlickrImage> imageList;
    private RecyclerView photoRecyclerView;
    private List<FlickrImage> removeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_roll);

        setUpBack();
        setUpDelete();

        flickrManager = FlickrManager.getInstance();
        showBitmaps();
        photoRecyclerView = findViewById(R.id.camroll);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(CameraRoll.this,5));
        photoRecyclerView.setAdapter(new PhotoAdapter());
    }

    private void setUpBack() {
        Button back = findViewById(R.id.cam_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpDelete(){
        Button delete_btn = findViewById(R.id.delete);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flickrManager.getRemoveList() == null){
                    return;
                }
                removeList = flickrManager.getRemoveList();
                for (int i = 0 ; i < removeList.size(); i ++){
                    flickrManager.removeImage(removeList.get(i),getApplicationContext());
                }
                photoRecyclerView.setAdapter(new PhotoAdapter());

            }
        });

    }


    private static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView image;
        private final CheckBox checkBox;
        private final List<FlickrImage> imageList;
        private final FlickrManager flickrManager;
        public PhotoHolder(View itemView, List<FlickrImage> imageList) {
            super(itemView);
            flickrManager = FlickrManager.getInstance();

            image = itemView.findViewById(R.id.item_image_view);
            checkBox = itemView.findViewById(R.id.grid_item_checkbox);

            itemView.setOnClickListener(this);
            this.imageList = imageList;
        }

        @Override
        public void onClick(View view){
            Bitmap img_bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            if (!checkBox.isChecked()) {
                for (int i = 0; i < imageList.size(); i++) {
                    if (img_bitmap == imageList.get(i).getImgBitmap()) {
                        flickrManager.addToRemoveList(imageList.get(i));
                    }
                }
                checkBox.setChecked(true);
            }
            else{
                for (int i = 0; i < imageList.size(); i++) {
                    if (img_bitmap == imageList.get(i).getImgBitmap()) {
                        flickrManager.removeFromRemoveList(imageList.get(i));
                    }
                }
                checkBox.setChecked(false);
            }
        }

    }

    //Adapter to display Images in layout
    public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>{
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(CameraRoll.this);
            View view = inflater.inflate(R.layout.list_item_gallery, parent, false);

            return new PhotoHolder(view, imageList);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            holder.image.setImageBitmap(imageList.get(position).getImgBitmap());
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }

    public void showBitmaps(){
        imageList = flickrManager.getImageList(getApplicationContext());
    }

    public static Intent makeIntent(Context context){
        return new Intent(context,CameraRoll.class);
    }

    @Override
    public void onBackPressed() {
        //Disable back button
    }
}