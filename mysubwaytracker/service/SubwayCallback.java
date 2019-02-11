package com.example.jiach.mysubwaytracker.service;

import com.example.jiach.mysubwaytracker.data.Data;

import java.nio.channels.Channel;

/**
 * Created by jiach on 4/28/2018.
 */

public interface SubwayCallback {
    void sericeSuccess(Data data);
    void serviceFailure(Exception ex);
}
