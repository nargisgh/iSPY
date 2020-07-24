package cmpt276.termproject.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrGallery.DownloadGalleryItems;
import cmpt276.termproject.model.FlickrGallery.FlickrImage;
import cmpt276.termproject.model.FlickrGallery.FlickrManager;
import cmpt276.termproject.model.FlickrGallery.GalleryItem;
import cmpt276.termproject.model.FlickrGallery.PollService;
import cmpt276.termproject.model.FlickrGallery.QueryPrefs;
import cmpt276.termproject.model.FlickrGallery.ThumbnailDownloader;

public class PhotoGallery extends AppCompatActivity  {
    private static final String TAG = "PhotoGallery";
    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    public String query;
    private Context context;
    private FlickrManager flickrManager;
    public CheckBox checkBox;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        mPhotoRecyclerView = findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(PhotoGallery.this,5));

        flickrManager = FlickrManager.getInstance();
        context = getApplicationContext();


        setUpThread();
        setUpSearchAndClear();
        setUpBack();
        setUpCameraRoll();
        updateItems();

        Intent i = PollService.newIntent(PhotoGallery.this);
        PhotoGallery.this.startService(i);
        //PollService.setServiceAlarm(PhotoGallery.this,true);
        setupAdapter();
   }

    private void setUpThread() {
        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);

        mThumbnailDownloader.setThumbnailDownloadListener(new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>(){
            //Photoholder makes for a convenient
            // identifier as it is also the target where the downloaded images will eventually go
            @Override
            public void onThumbnailDownloaded(PhotoHolder photoHolder, Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                photoHolder.bindDrawable(drawable);
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }

    private void setUpCameraRoll() {
        Button camroll_btn = findViewById(R.id.CameraRoll);
        dynamicScaling(camroll_btn, 4, 10);
        camroll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CameraRoll.makeIntent(PhotoGallery.this);
                startActivity(i);
            }
        });
    }



    private void setUpSearchAndClear() {
        final SearchView searchItem = findViewById(R.id.search_pics);
        Button clear_btn = findViewById(R.id.clear);
        dynamicScaling(clear_btn, 4, 10);
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "QueryTextSubmit: " + query);
                QueryPrefs.setStoredQuery(PhotoGallery.this, query);
                updateItems();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "QueryTextChange: " + newText);
                return false;
            }
        });
        searchItem.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = QueryPrefs.getStoredQuery(PhotoGallery.this);
                searchItem.setQuery(query, false); }
        });

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QueryPrefs.setStoredQuery(PhotoGallery.this, null);
                updateItems();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.interrupt();
        mThumbnailDownloader.clearQueue();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");

    }


    private void updateItems() {

        query = QueryPrefs.getStoredQuery(PhotoGallery.this);
        new FetchItemsTask(query).execute();
    }

    private void setUpBack() {
        Button back_btn = findViewById(R.id.flickr_back);
        dynamicScaling(back_btn, 4, 10);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThumbnailDownloader.quitSafely();
                finish();
            }
        });
    }

    public void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));

        }
    }

    private boolean isAdded() {
        return mItems != null;
    }


    //when you click each photo
    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GalleryItem mGalleryItem;
        private ImageView mItemImageView;
        private CheckBox checkBox;
        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = itemView.findViewById(R.id.item_image_view);
            checkBox = itemView.findViewById(R.id.grid_item_checkbox);
            itemView.setOnClickListener(this);
        }
        public void bindDrawable(Drawable drawable){
            mItemImageView.setImageDrawable(drawable);
        }
        public void bindGalleryItem(GalleryItem galleryItem) {
            mGalleryItem = galleryItem;
        }


        //Click Image Override
        @Override
        public void onClick(View v) {
            Toast.makeText(PhotoGallery.this,"You clicked "+mGalleryItem.getCaption(),Toast.LENGTH_LONG).show();

            Thread saveImg = new Thread(){
                public void run() {
                    try {

                        byte[] bitmapBytes = new DownloadGalleryItems().getUrlBytes(mGalleryItem.getUrl());
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

                        FlickrImage clicked_img = new FlickrImage(mGalleryItem.getId(), bitmap);
                        if (checkBox.isChecked()){
                            flickrManager.removeImage(clicked_img,context);
                            checkBox.setChecked(false);
                        }
                        else{
                            flickrManager.saveImage(clicked_img , context);
                            checkBox.setChecked(true);
                        }
                        flickrManager.createImageList(context);

                    }
                    catch (Exception ignored) {
                    }

                }
            };
            saveImg.start();

            //Intent i = new Intent(Intent.ACTION_VIEW, mGalleryItem.getPhotoPageUri());
            //startActivity(i);
        }
    }





    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItems;
        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(PhotoGallery.this);
            View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
            return new PhotoHolder(view);
        }
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            //call the thread's queue thumbnail method and pass the target
            // photoholder where img will be placed in galleryitem's url to download from

            GalleryItem galleryItem = mGalleryItems.get(position);
            photoHolder.bindGalleryItem(galleryItem);
            Drawable placeholder = ContextCompat.getDrawable(PhotoGallery.this,R.drawable.loading_spinner);
            photoHolder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(photoHolder, galleryItem.getUrl());

        }
        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    //Thread Handling
    @SuppressLint("StaticFieldLeak")
    private class FetchItemsTask extends AsyncTask<Void,Void,List<GalleryItem>> {
        private String mQuery;
        public FetchItemsTask(String query) {
            mQuery = query;
        }


        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            if (mQuery == null) {
                return new DownloadGalleryItems().searchPhotos("cloud");
                //return new DownloadGalleryItems().fetchRecentPhotos();
            }
            else {
                return new DownloadGalleryItems().searchPhotos(mQuery);
            }
        }
        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context,PhotoGallery.class);
    }

    private void dynamicScaling (Button button, int width, int height)
    {
        ConstraintLayout.LayoutParams btn_size;
        btn_size = (ConstraintLayout.LayoutParams) button.getLayoutParams();
        btn_size.width = (getResources().getDisplayMetrics().widthPixels)/width;
        btn_size.height = (getResources().getDisplayMetrics().heightPixels)/height;
        button.setLayoutParams(btn_size);
    }

    @Override
    public void onBackPressed() {

    }
}


