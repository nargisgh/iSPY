package cmpt276.termproject.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;

import java.util.List;

import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrGallery.FlickrImage;
import cmpt276.termproject.model.FlickrGallery.FlickrManager;

public class CameraRoll extends AppCompatActivity {

    private FlickrManager flickrManager;
    private List<FlickrImage> imageList;
    private RecyclerView mPhotoRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_roll);

        setUpBack();

        flickrManager = FlickrManager.getInstance();
        showBitmaps();
        mPhotoRecyclerView = findViewById(R.id.camroll);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(CameraRoll.this,5));
        mPhotoRecyclerView.setAdapter(new PhotoHolder());

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

    public class PhotoHolder extends RecyclerView.Adapter<ImageViewHolder>{

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(CameraRoll.this);
            View view = inflater.inflate(R.layout.list_item_gallery, parent, false);
            ImageViewHolder imageViewHolder = new ImageViewHolder(view);

            return imageViewHolder;
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
            holder.image.setImageBitmap(imageList.get(position).getImgBitmap());

        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        CheckBox checkBox;
        public ImageViewHolder( View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image_view);
            checkBox = itemView.findViewById(R.id.grid_item_checkbox);

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

    }
}