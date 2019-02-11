package com.example.jiach.mysubwaytracker.service;

import android.os.AsyncTask;
import android.util.Log;

import com.example.jiach.mysubwaytracker.SubwayTrackerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jiach on 4/28/2018.
 */

public class SubwayService {
    private  SubwayCallback callback;
    private String location1;
    private Exception error;

    public SubwayService(SubwayCallback callback){
        this.callback=callback;
    }

    public String getLocation(){
        return  location1;
    }

    public void refreshSubway(final String location){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                location1=location;
                try {
                    URL url =new URL("https://api-v3.mbta.com/predictions?filter%5Bstop%5D=place-ccmnl&filter%5Bdirection_id%5D=" + location);
                            /*("https://api-v3.mbta.com/predictions?"
                    + "filter[stop]=${ccmnl#origin}&"
                    + "filter[direction_id]=${0}&"
                    + "filter[route]=Orange&"
                    + "sort=departure_time&"
                    + "page[limit]=5&"
                    + "api_key=${af310295e7234d658d69cc63ebdef6b4}");*/

                    URLConnection connection=url.openConnection();

                    InputStream inputStream=connection.getInputStream();

                    BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        result.append(line);
                    }

                    return result.toString();

                } catch (Exception e) {
                    error =e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s){
                if (s==null && error !=null){
                    return;
                }

                try {

                    JSONObject root=new JSONObject(s);
                    JSONArray array=root.getJSONArray("data");

                    JSONObject object=array.getJSONObject(0);
                    if(location.equals("0")) {
                        SubwayTrackerActivity.nextToFTextView.setText("Next train to Forest Hills : "+subtractTime(object.optJSONObject("attributes").optString("departure_time")));
                    }
                    else
                        SubwayTrackerActivity.nextToOTextView.setText("Next train to Oak Grove : "+subtractTime(object.optJSONObject("attributes").optString("arrival_time")));

                    object=array.getJSONObject(1);
                    if(location.equals("0"))
                        SubwayTrackerActivity.secToFTextView.setText("2nd train to Forest Hills : "+subtractTime(object.optJSONObject("attributes").optString("departure_time")));
                    else
                        SubwayTrackerActivity.secToOTextView.setText("2nd train to Oak Grove : "+subtractTime(object.optJSONObject("attributes").optString("arrival_time")));

                    /*
                    JSONObject data=new JSONObject(s);

                    Log.i("aaaaaa","aaaa");
                    JSONObject dataResults=data.optJSONObject("data");

                    Log.i("aaaaaa","aaaa");
                    Data result=new Data();
                    result.poupulate(data.optJSONObject("attributes"));
                    Log.i("aaaaaa","aaaa");
                    callback.sericeSuccess(result);*/


                } catch (JSONException e) {
                    callback.serviceFailure(e);
                }
            }

        }.execute(location);
    }

    public String subtractTime(String time){
        Calendar c=Calendar.getInstance(TimeZone.getTimeZone("America/Boston"));
        SimpleDateFormat formatter=new SimpleDateFormat("mm");
        String strTime=formatter.format(c.getTime());

        String d1=time.substring(14,16);

        int d1Num=Integer.parseInt(d1);
        int strTimeNum=Integer.parseInt(strTime);

        int min=d1Num-strTimeNum;

        if(min<0)
            min+=60;

        if(min==0)
            return "Arriving";
        return min+"";
    }
}
