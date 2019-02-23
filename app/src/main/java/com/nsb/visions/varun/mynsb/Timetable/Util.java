package com.nsb.visions.varun.mynsb.Timetable;


import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.amirs.JSON;

// Special functions used by the timetable class
public class Util {

    // getClassName takes a class code like: "10SCI.C" and Spits out its full name: "Science"
    static String getClassName(String classCode, Context context) throws IOException {

        // Regex to parse class code, see: https://regex101.com/r/QUG50E/1/ to test our the regex
        Pattern getCode = Pattern.compile("(\\d+)([a-zA-z]+\\d?)(.*)");
        Matcher matcher = getCode.matcher(classCode);
        String subjectCode = "";

        // This is a quick hack that deals with SRC roll-calls
        if (Objects.equals(classCode, "SRC RC")) {
            subjectCode = "RC";
        }
        // Read the regex group into the subject code var
        while(matcher.find()) {
            subjectCode = matcher.group(2);
            // Determine if the class code contains RC, we compare against the class code and not subject because some students are SRC members and as such their SRC roll call doesn't get
            if (subjectCode.contains("RC")) {
                subjectCode = "RC";
            }
        }

        InputStream classCodeJson = context.getAssets().open("class_codes.json");
        int size = classCodeJson.available();
        byte[] buffer = new byte[size];
        classCodeJson.read(buffer);
        classCodeJson.close();
        // Get the raw json
        String rawJson = new String(buffer, "UTF-8");
        // Parse this json
        JSON json = new JSON(rawJson);

        return json.key(subjectCode).stringValue();
    }
}
