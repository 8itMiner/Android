package com.nsb.visions.varun.mynsb.Timetable;

import android.content.Context;

import com.nsb.visions.varun.mynsb.Common.Loader;

import java.util.List;

import eu.amirs.JSON;
import okhttp3.Response;

/**
 * Created by varun on 31/01/2018. Coz varun is awesome as hell :)
 */

public class Timetables extends Loader<Subject> {

    public Timetables(Context context) {
        super(context);
    }

    @Override
    public Response sendRequest() throws Exception {

    }

    @Override
    public Subject parseJson(JSON json) throws Exception {
        return null;
    }

    @Override
    public void getAdapterInstance(List<Subject> subjects) {

    }
}
