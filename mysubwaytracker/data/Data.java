package com.example.jiach.mysubwaytracker.data;

import android.util.Log;

import org.json.JSONObject;

import java.sql.Time;

/**
 * Created by jiach on 4/28/2018.
 */

public class Data implements JSONPopulator {
    private String arrivalTime;
    private String departureTime;
    @Override
    public void poupulate(JSONObject data) {
        Log.i("aaaaaa","1111");
        arrivalTime=data.optString("status");
        Log.i("aaaaaa","1111");
        departureTime=data.optString("departure_time");
        Log.i("aaaaaa","aaaa");
        //arrival_time
        //departure_time
    }

    public String getArrivalTime(){
        return arrivalTime;
    }

    public String getDepartureTime(){
        return departureTime;
    }
}
