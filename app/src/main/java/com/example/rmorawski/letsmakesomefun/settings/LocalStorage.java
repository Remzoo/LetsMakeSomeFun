package com.example.rmorawski.letsmakesomefun.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by r.morawski on 3/23/2018.
 */

public class LocalStorage {

    private Context context;
    private SharedPreferences sharedPreferences;

    public LocalStorage(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
    }

    public String getValue(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
