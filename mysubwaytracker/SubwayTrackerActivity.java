package com.example.jiach.mysubwaytracker;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jiach.mysubwaytracker.data.Data;
import com.example.jiach.mysubwaytracker.service.SubwayCallback;
import com.example.jiach.mysubwaytracker.service.SubwayService;

import java.nio.channels.Channel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class SubwayTrackerActivity extends AppCompatActivity implements SubwayCallback {


    public static TextView nextToFTextView,secToFTextView,nextToOTextView,secToOTextView,timeTextView,textView;
    private Button startBtn,stopBtn,exitBtn;
    private Timer timer;
    MyTimerTask myTimerTask;

    private SubwayService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_tracker);

        nextToFTextView=(TextView)findViewById(R.id.NextToFTextView);
        secToFTextView=(TextView)findViewById(R.id.secToFTextView);
        nextToOTextView=(TextView)findViewById(R.id.NextToOTextView);
        secToOTextView=(TextView)findViewById(R.id.secToOTextView);
        timeTextView=(TextView)findViewById(R.id.TimeTextView);
        startBtn=(Button)findViewById(R.id.StartBtn);
        stopBtn=(Button)findViewById(R.id.StopBtn);
        exitBtn=(Button)findViewById(R.id.ExitBtn);
        textView=(TextView)findViewById(R.id.copyright);
        textView.setText("Copyright 2018 JiachaoChen\ncurrent API level: " + Integer.valueOf(android.os.Build.VERSION.SDK));

        service=new SubwayService(this);
        service.refreshSubway("0");
        service.refreshSubway("1");

        Calendar c=Calendar.getInstance(TimeZone.getTimeZone("America/Boston"));
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat time=new SimpleDateFormat("h:mm:ss a");
        String strTime=time.format(c.getTime());
        timeTextView.setText(strTime);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer!=null)
                    timer.cancel();
                timer=new Timer();
                myTimerTask=new MyTimerTask();
                timer.schedule(myTimerTask,1000,1000);
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer!=null){
                    timer.cancel();
                    timer=null;
                }
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void update(){
        service.refreshSubway("0");
        service.refreshSubway("1");
        Calendar c=Calendar.getInstance(TimeZone.getTimeZone("America/Boston"));
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat time=new SimpleDateFormat("h:mm:ss a");
        String strTime=time.format(c.getTime());
        timeTextView.setText(strTime);
    }

    @Override
    public void sericeSuccess(Data data) {

    }

    @Override
    public void serviceFailure(Exception ex) {

    }

    class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }
}
