package com.nsb.visions.varun.mynsb.Common;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by varun on 22/01/2018. Coz varun is awesome as hell :)
 */

public class ReminderColours {

    public HashMap<String, String> tagColours;

    public ReminderColours(SharedPreferences preferences) {
        // Gson
        Gson gson = new Gson();
        // Tag colours
        String tagColourJson = preferences.getString("tag-colours", "{}");
        // Convert json to hashmap
        Type stringStringMap = new TypeToken<HashMap<String, String>>(){}.getType();
        this.tagColours = gson.fromJson(tagColourJson, stringStringMap);
    }

}
