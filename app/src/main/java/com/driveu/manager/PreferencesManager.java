package com.driveu.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.driveu.config.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Aishwarya on 4/20/2018.
 */

public class PreferencesManager  {

    private static final String LOCATION = "location";

    /************************************
     * PRIVATE STATIC FIELDS
     ************************************/
    private static volatile PreferencesManager instance;

    /************************************
     * PUBLIC METHODS
     ************************************/
    public static PreferencesManager getInstance() {
        if (instance == null) {
            synchronized (PreferencesManager.class) {
                if (instance == null) {
                    instance = new PreferencesManager();
                }
            }
        }
        return instance;
    }

    public void putValue(Context context, String key, String value) {
        getPreferencesEditor(context).putString(key,value).apply();
    }

    public String getValue(Context context, String key) {
        return getPreferences(context).getString(key,"DEFAULT");
    }

    public void putState(Context context, String key, int value) {
        getPreferencesEditor(context).putInt(key,value).apply();
    }

    public int getState(Context context, String key) {
        return getPreferences(context).getInt(key,-1);
    }

    public void removeData(Context context, String key) {
        Log.d("REMOVED","removed data");
        getPreferencesEditor(context).remove(key);
    }

    public void clearData(Context context) {
        getPreferencesEditor(context).clear();
    }

    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(LOCATION, MODE_PRIVATE);
    }

    private SharedPreferences.Editor getPreferencesEditor(Context context) {
        return getPreferences(context).edit();
    }
}
