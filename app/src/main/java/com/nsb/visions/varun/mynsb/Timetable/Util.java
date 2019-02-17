package com.nsb.visions.varun.mynsb.Timetable;


import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.amirs.JSON;

// Special functions used by the timetable class
public class Util {


    public static String getClassName(String classCode, Context context) throws IOException {

        Log.d("Timetable-ClassCode: ", "h " + classCode);

        // This is because some classcodes have numbers which we want to get rid of, eg. 10MAT will become MAT
        // Get the class code regex
        Pattern getCode = Pattern.compile("(\\d+)([a-zA-z]+\\d?)(.*)");
        // Determine the code for the class, (2nd matching group)
        Matcher matcher = getCode.matcher(classCode);


        String subjectCode = "";
        // This is just to test if they are an SRC student, this part is a bit of a hack but I'm really CBBS RN
        if (Objects.equals(classCode, "SRC RC")) {
            subjectCode = "RC";
        }
        // Push stuff into the matches list
        while(matcher.find()) {
            subjectCode = matcher.group(2);

            // Determine if the class code contains RC, we compare against the class code and not subject because some students are SRC members and as such their SRC roll call doesn't get
            // regex matched
            if (subjectCode.contains("RC")) {
                subjectCode = "RC";
            }
        }



        // Parse this and determine the full name of the class by reading the class_codes.json file
        InputStream is = context.getAssets().open("class_codes.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        // Get the raw json
        String rawJson = new String(buffer, "UTF-8");

        // Parse this json
        JSON json = new JSON(rawJson);

        return json.key(subjectCode).stringValue();
    }

}
