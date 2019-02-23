package com.nsb.visions.varun.mynsb.Common;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by varun on 22/01/2018.
 */


// ReminderColours is simply a class that allows users to set and edit the colours for specific types of reminders
public class ReminderColours {

    public HashMap<String, String> tagColours;
    private SharedPreferences preferences;




    // Constructor
    public ReminderColours(SharedPreferences preferences) {
        Gson jsonDecoder = new Gson();
        String tagColourJson = preferences.getString("tag-colours", "{}");

        // Convert json to hashmap
        Type stringStringMap = new TypeToken<HashMap<String, String>>(){}.getType();
        this.tagColours = jsonDecoder.fromJson(tagColourJson, stringStringMap);
        this.preferences = preferences;
    }




    // createTag creates a tag from a given tag and colour
    public boolean createTag(String tag, String colour) {
        tagColours.put(tag, colour);
        return this.save();
    }




    // save saves the current colour hashmap into the shared preferences for the application
    private boolean save() {
        Gson jsonEncoder = new Gson();
        String jsonEncoded = jsonEncoder.toJson(this.tagColours);

        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString("tag-colours", jsonEncoded);
        editor.apply();

        return true;
    }

}
/*
    Usage: ReminderColours(Sharedprefs) x;
    x.createTag("x", "#colour")
    x.save()
 */
