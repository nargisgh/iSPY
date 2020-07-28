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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrGallery.FlickrFetchr;
import cmpt276.termproject.model.FlickrGallery.FlickrImage;
import cmpt276.termproject.model.FlickrGallery.FlickrManager;
import cmpt276.termproject.model.FlickrGallery.GalleryItem;
import cmpt276.termproject.model.FlickrGallery.PollService;
import cmpt276.termproject.model.FlickrGallery.QueryPrefs;
import cmpt276.termproject.model.FlickrGallery.ThumbnailDownloader;

public class FlickrGallery extends AppCompatActivity  {
    //Source: "Android Programming: The Big Nerd Ranch Guide 3rd edition" - Bill Philips, Chris Stewart, and Kristin Marsciano
    //Ch 25-29
    private static final String TAG = "FlickrGallery";
    private RecyclerView photoRecyclerView;
    private List<GalleryItem> Items = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> thumbnailDownloader;
    public String query;
    private Context context;
    private FlickrManager flickrManager;
    private ProgressBar progressBar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_gallery);

        photoRecyclerView = findViewById(R.id.photo_recycler_view);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(FlickrGallery.this,5));

        flickrManager = FlickrManager.getInstance();
        context = getApplicationContext();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        setUpThread();
        setUpSearchAndClear();
        setUpBack();
        setUpCameraRoll();
        updateItems();

        Intent i = PollService.newIntent(FlickrGallery.this);
        FlickrGallery.this.startService(i);
        setupAdapter();
   }

    private void setUpThread() {
        Handler responseHandler = new Handler();
        thumbnailDownloader = new ThumbnailDownloader<>(responseHandler);

        thumbnailDownloader.setThumbnailDownloadListener(new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>(){
            //Photoholder makes for a convenient
            // identifier as it is also the target where the downloaded images will eventually go
            @Override
            public void onThumbnailDownloaded(PhotoHolder photoHolder, Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                photoHolder.bindDrawable(drawable);
            }
        });
        thumbnailDownloader.start();
        thumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }

    private void setUpCameraRoll() {
        Button camroll_btn = findViewById(R.id.gallery_camera_roll_btn);
        dynamicScaling(camroll_btn, 4, 10);
        camroll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CameraRoll.makeIntent(FlickrGallery.this);
                startActivity(i);
            }
        });
    }



    private void setUpSearchAndClear() {
        final SearchView searchItem = findViewById(R.id.gallery_search_bar);
        Button clear_btn = findViewById(R.id.gallery_clear_btn);
        dynamicScaling(clear_btn, 4, 10);
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "QueryTextSubmit: " + query);
                QueryPrefs.setStoredQuery(FlickrGallery.this, query);
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
                //Updating stored query whenever user submits new query
                query = QueryPrefs.getStoredQuery(FlickrGallery.this);
                searchItem.setQuery(query, false); }
        });

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clearing stored query
                QueryPrefs.setStoredQuery(FlickrGallery.this, null);
                updateItems();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thumbnailDownloader.interrupt();
        thumbnailDownloader.clearQueue();
        thumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");

    }


    private void updateItems() {
        //Will start AsyncTask
        query = QueryPrefs.getStoredQuery(FlickrGallery.this);
        new FetchItemsTask(query).execute();
    }

    private void setUpBack() {
        Button back_btn = findViewById(R.id.gallery_back_btn);
        dynamicScaling(back_btn, 4, 10);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbnailDownloader.quitSafely();
                finish();
            }
        });
    }

    public void setupAdapter() {
        if (isAdded()) {
            photoRecyclerView.setAdapter(new PhotoAdapter(Items));
        }
    }

    private boolean isAdded() {
        return Items != null;
    }

    //when you click each photo
    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GalleryItem galleryItem;
        private final ImageView itemImageView;
        private final CheckBox checkBox;
        public PhotoHolder(View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.item_image_view);
            checkBox = itemView.findViewById(R.id.grid_item_checkbox);
            checkBox.setEnabled(false);
            itemView.setOnClickListener(this);
            //https://dzone.com/articles/grid-images-and-checkboxes
        }
        public void bindDrawable(Drawable drawable){
            itemImageView.setImageDrawable(drawable);
        }
        public void bindGalleryItem(GalleryItem galleryItem) {
            this.galleryItem = galleryItem;
        }


        //Click Image Override
        @Override
        public void onClick(View v) {
            checkBox.setEnabled(true);
            Toast.makeText(FlickrGallery.this,"You saved "+ galleryItem.getCaption(),Toast.LENGTH_SHORT).show();

            Thread saveImg = new Thread(){
                public void run() {
                    try {

                        byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(galleryItem.getUrl());
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

                        FlickrImage clicked_img = new FlickrImage(galleryItem.getId(), bitmap);
                        if (checkBox.isChecked()){
                            flickrManager.removeImage(clicked_img,context);
                            checkBox.setChecked(false);
                        }
                        else{
                            flickrManager.saveImage(clicked_img , context);
                            checkBox.setButtonTintList(FlickrGallery.this.getColorStateList(R.color.colorAccent));
                            checkBox.setChecked(true);
                        }
                        checkBox.setEnabled(false);
                        flickrManager.createImageList(context);

                    }
                    catch (Exception ignored) {
                    }

                }
            };
            saveImg.start();

        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        /*Adapter to provide PhotoHolders as needed based on list of GalleryItems*/
        private final List<GalleryItem> galleryItems;
        public PhotoAdapter(List<GalleryItem> galleryItems) {
            this.galleryItems = galleryItems;
        }
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(FlickrGallery.this);
            View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
            return new PhotoHolder(view);
        }
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            //call the thread's queue thumbnail method and pass the target
            // photoholder where img will be placed in galleryitem's url to download from

            GalleryItem galleryItem = galleryItems.get(position);
            photoHolder.bindGalleryItem(galleryItem);
            Drawable placeholder = ContextCompat.getDrawable(FlickrGallery.this,R.drawable.loading_spinner);
            photoHolder.bindDrawable(placeholder);
            thumbnailDownloader.queueThumbnail(photoHolder, galleryItem.getUrl());

        }
        @Override
        public int getItemCount() {
            return galleryItems.size();
        }
    }

    //Thread Handling
    @SuppressLint("StaticFieldLeak")
    private class FetchItemsTask extends AsyncTask<Void,Void,List<GalleryItem>> {
        /*Easiest way to work with a bg thread to use utility class AsyncTask
        * Creates bg thread and runs in code doInBg()*/
        private final String Query;
        public FetchItemsTask(String query) {
            Query = query;
        }


        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            /*Get data from the Website and log it*/
            if (Query == null) {
                return new FlickrFetchr().searchPhotos("cloud");
                //return new FlickrFetchr().fetchRecentPhotos();
            }
            else {
                return new FlickrFetchr().searchPhotos(Query);
            }
        }
        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            Items = items;
            setupAdapter();
            progressBar.setVisibility(View.GONE);
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, FlickrGallery.class);
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

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        updateItems();
    }
}


