package cmpt276.termproject.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import cmpt276.termproject.R;
import cmpt276.termproject.model.FlickrFetchr;

public class PhotoGallery extends AppCompatActivity {
    private static final String TAG =
            "PhotoGallery";
    private RecyclerView mPhotoRecyclerView;


    public static PhotoGallery newInstance()
    {
        return new PhotoGallery();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchItemsTask().execute();
   }


    private class FetchItemsTask extends
        AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            new FlickrFetchr().fetchItems();
            return null;
        }
    }
    public static Intent makeIntent(Context context){
        return new Intent(context,PhotoGallery.class);
    }
}


