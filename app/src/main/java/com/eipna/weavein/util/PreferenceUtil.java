package com.eipna.weavein.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class PreferenceUtil {

    private final SharedPreferences sharedPreferences;

    public PreferenceUtil(@NonNull Context context) {
        this.sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public void setUserID(long userID) {
        sharedPreferences.edit().putLong("user_id", userID).apply();
    }

    public long getUserID() {
        return sharedPreferences.getLong("user_id", -1);
    }
}