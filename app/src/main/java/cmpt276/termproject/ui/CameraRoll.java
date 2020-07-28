package cmpt276.termproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import java.util.List;

import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrGallery.FlickrImage;
import cmpt276.termproject.model.FlickrGallery.FlickrManager;
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
        photoRecyclerView = findViewById(R.id.camroll_items_view);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(CameraRoll.this,5));
        photoRecyclerView.setAdapter(new PhotoAdapter());

        updateImageText();

    }

    @SuppressLint("SetTextI18n")
    private void updateImageText() {
        TextView imageSize = findViewById(R.id.cam_roll_num_images);
        imageSize.setText(""+ imageList.size()+ getString(R.string.images));
    }

    private void setUpBack() {
        Button back = findViewById(R.id.cam_roll_back_btn);
        dynamicScaling(back,4,10);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpDelete(){
        Button delete_btn = findViewById(R.id.cam_roll_delete_btn);
        dynamicScaling(delete_btn,4,10);

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
                updateImageText();

            }
        });

    }


    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView image;
        private final CheckBox checkBox;
        private final List<FlickrImage> imageList;
        private final FlickrManager flickrManager;
        public PhotoHolder(View itemView, List<FlickrImage> imageList) {
            super(itemView);
            flickrManager = FlickrManager.getInstance();

            image = itemView.findViewById(R.id.item_image_view);
            checkBox = itemView.findViewById(R.id.grid_item_checkbox);
            checkBox.setEnabled(false);

            itemView.setOnClickListener(this);
            this.imageList = imageList;
        }

        @Override
        public void onClick(View view){
            checkBox.setEnabled(true);
            Bitmap img_bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            if (!checkBox.isChecked()) {
                for (int i = 0; i < imageList.size(); i++) {
                    if (img_bitmap == imageList.get(i).getImgBitmap()) {
                        flickrManager.addToRemoveList(imageList.get(i));
                    }
                }
                checkBox.setButtonTintList(CameraRoll.this.getColorStateList(R.color.colorAccent));
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
            checkBox.setEnabled(false);
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
    private void dynamicScaling (Button button, int width, int height)
    {
        ConstraintLayout.LayoutParams btn_size;
        btn_size = (ConstraintLayout.LayoutParams) button.getLayoutParams();
        btn_size.width = (getResources().getDisplayMetrics().widthPixels)/width;
        btn_size.height = (getResources().getDisplayMetrics().heightPixels)/height;
        button.setLayoutParams(btn_size);
    }
}