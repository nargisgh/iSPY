package cmpt276.termproject.model.FlickrGallery;

import android.content.Context;
import android.preference.PreferenceManager;

import cmpt276.termproject.model.GameManager;


public class QueryPrefs {
    //singleton stuff
    private static QueryPrefs instance;


    private static final String PREF_SEARCH_QUERY = "searchQuery";
    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }
    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(PREF_SEARCH_QUERY, query).apply();
    }
}
