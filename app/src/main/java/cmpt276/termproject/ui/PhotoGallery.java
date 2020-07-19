package cmpt276.termproject.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrFetchr;
import cmpt276.termproject.model.GalleryItem;

public class PhotoGallery extends AppCompatActivity {
    private static final String TAG = "PhotoGallery";
    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();


    public static PhotoGallery newInstance()
    {
        return new PhotoGallery();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        mPhotoRecyclerView = findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(PhotoGallery.this,5));

        new FetchItemsTask().execute();
        setupAdapter();
   }

    public void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    private boolean isAdded() {
        return mItems != null;
    }


    private class PhotoHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        public PhotoHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }
        public void bindGalleryItem(GalleryItem item){
            mTitleTextView.setText(item.toString());
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
            TextView textView = new TextView(PhotoGallery.this);
            return new PhotoHolder(textView);
        }
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            photoHolder.bindGalleryItem(galleryItem); }
        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class FetchItemsTask extends AsyncTask<Void,Void,List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            return new FlickrFetchr().fetchItems();
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
}


