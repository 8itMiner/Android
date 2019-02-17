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
    private SharedPreferences preferences;

    public ReminderColours(SharedPreferences preferences) {
        // Gson
        Gson gson = new Gson();
        // Tag colours
        String tagColourJson = preferences.getString("tag-colours", "{}");

        // Convert json to hashmap
        Type stringStringMap = new TypeToken<HashMap<String, String>>(){}.getType();
        this.tagColours = gson.fromJson(tagColourJson, stringStringMap);

        // Store the prefs
        this.preferences = preferences;
    }


    /* createTag creates a tag from a given tag and colour
            @params;
                String tag
                String colour

     */
    public boolean createTag(String tag, String colour) {
        tagColours.put(tag, colour);
        return this.save();
    }


    /* save saves the current hashmap into the shared preferences
            @params;
               nil
     */
    private boolean save() {
        // Gson instances
        Gson gson = new Gson();
        // Convert existing hashmap into json for the shared prefs
        String hashMapString = gson.toJson(this.tagColours);

        // Save this into our shared prefs
        SharedPreferences.Editor editor = this.preferences.edit();
        // Push the data into the appropriate var
        editor.putString("tag-colours", hashMapString);
        editor.apply();

        return true;
    }

}
/*
    Usage: ReminderColours(Sharedprefs) x;
    x.createTag("x", "#colour")
 */
