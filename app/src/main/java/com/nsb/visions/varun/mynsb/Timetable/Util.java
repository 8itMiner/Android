package com.nsb.visions.varun.mynsb.Timetable;


import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.amirs.JSON;

// Special functions used by the timetable class
public class Util {


    public static String getClassName(String classCode, Context context) throws IOException {
        // Get the class code regex
        Pattern getCode = Pattern.compile("\\d+([a-zA-z]+)");

        // Determine the code for the class, (2nd matching group)
        Matcher matcher = getCode.matcher(classCode);

        // Parse this and determine the full name of the class by reading the class_codes.json file
        InputStream is = context.getAssets().open("class_codes.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read();
        is.close();

        // Get the raw json
        String rawJson = new String(buffer, "UTF-8");

        // Parse this json
        JSON json = new JSON(rawJson);

        return json.key(matcher.group(2)).stringValue();
    }

}
