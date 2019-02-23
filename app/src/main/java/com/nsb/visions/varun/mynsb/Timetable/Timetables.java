package com.nsb.visions.varun.mynsb.Timetable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.ArrayList;
import java.util.List;

import eu.amirs.JSON;
import okhttp3.Response;

/**
 */

public class Timetables extends Loader<Subject> {

    private int day = 0;
    private boolean expandOrNot;
    private String adapterTitle;
    private String dayStr;
    private String week;
    public JSON belltimes;


    // Constructor
    public Timetables(Context context, Boolean expandOrNot) {
        super(context);
        String week = Util.weekAorB(this.context);
        this.day = Util.calculateDay(week);
        // Convert the returned day to a dayOfWeek, e.g 6 = 1 = Monday, 7 = 2 = Tuesday
        int dayOfWeek = day > 5 ? day - 5 : day;
        this.week = week;
        // This is flag that deals with Weeks A and B, e.g. if its day 6 then the week flag is B, if its 1 then its A
        if (this.day > 5) {
            week = "B";
        }

        this.adapterTitle = Util.intToDaystr(dayOfWeek + 1) + " " + week;
        this.dayStr = Util.intToDaystr(dayOfWeek + 1);
        this.expandOrNot = expandOrNot;
        this.belltimes = new JSON(getBelltimes(context)).key("Body").index(0).key(dayStr);
    }




    @Override
    public Response sendRequest() {
        try {
            HTTP httpClient = new HTTP(this.context);
            return httpClient.performRequest(
                httpClient.buildRequest(HTTP.GET, "/timetables/get", new String[]{"day="+String.valueOf(this.day)}),false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    @Override
    public Subject parseJson(JSON jsonRaw, int position) throws Exception {
        JSON json = jsonRaw.index(position);
        int period = json.key("Period").intValue();
        return new Subject(json.key("Subject").stringValue(), json.key("ClassRoom").stringValue(),
            json.key("Teacher").stringValue(), String.valueOf(period), this.belltimes.key(String.valueOf(period)).stringValue());
    }




    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Subject> subjects) {
        // This tells us if we should expand the timetables or not, expanded timetables list every single timetable the student has. see the extended package
        if (this.expandOrNot) {
            this.adapterTitle = null;
        }
        return new TimetableAdapter(subjects, this.adapterTitle, context);
    }




    @Override
    public List<Subject> getModels() {
        return addFreePeriods(super.getModels());
    }




    // Generate list generates a list of numbers given a begining index and an end index
    private List<Integer> generateList(int start, int end) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < end - start + 1; i++) {
            integerList.add(1 + i);
        }
        return integerList;
    }




    // addFreePeriods adds free periods to a students timetable, we need to add this as the API doesn't include a specific "free period" period.
    private List<Subject> addFreePeriods(List<Subject> base) {
        // Iterate over json array and push it into the models list
        // Generate an array of "ideals" this is the model array size
        int lastPeriod = Integer.parseInt(base.get(base.size() - 1).period);
        List<Integer> contentMapping = generateList(1, lastPeriod);
        List<Subject> copy = new ArrayList<>();
        List<Integer> actualMapping = new ArrayList<>();

        // Copy the actual base into our copy array
        for (Subject s: base) {
            copy.add(Integer.parseInt(s.period)-1, s);
        }
        // Generate a mapping for what we actually have with the body array
        for (int i = 0; i < base.size()-1; i++) {
            if (base.get(i).period.isEmpty()) {
                continue;
            }
            int periodNumber = Integer.parseInt(base.get(i).period);
            actualMapping.add(periodNumber);
        }
        // Compare these two lists to get a list of missing periods
        // The free periods are now in the content mapping list
        contentMapping.removeAll(actualMapping);
        for(int i: contentMapping) {
            copy.add(i-1, new Subject(
                "10FREE",
                "LAB3",
                "Mr Heath", String.valueOf(i), this.belltimes.key(String.valueOf(i)).stringValue()));
        }
        return copy;
    }




    // Little helper function that will allow us to set a different url to retrieve our timetables from
    public void setCurrDay(int day) {this.day = day;}
    public void setDayAndUpdateBellTimes(String dayStr) {
        this.dayStr = dayStr;
        this.belltimes = new JSON(getBelltimes(context)).key("Body").index(0).key(dayStr);
    }
    public void setWeek(String week) {this.week = week;}
    public String getWeek(){return this.week;}
    public String getDayStr() {return this.dayStr;}




    // getBelltimes retrieves the belltimes from the API
    private static String getBelltimes(Context context) {
        HTTP http = new HTTP(context);
        try {
            return new JSON(
                http.performRequest(
                    http.buildRequest(HTTP.GET, "/reminders/get", null), false).body().string()).key("Message").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}