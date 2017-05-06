package com.example.android.Bella;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GarbageActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircularProgressBar circularProgressBar,circularProgressBar2,circularProgressBar3,circularProgressBar4,circularProgressBar5,circularProgressBar6;
    TextView t1,t2,t3,t4,t5,t6;
    int temp[] = new int[6];
    String s = "http://api.thingspeak.com/channels/265517/feed/last.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage);
        circularProgressBar = (CircularProgressBar)findViewById(R.id.circle_progress1);
        circularProgressBar2 = (CircularProgressBar)findViewById(R.id.circle_progress2);
        circularProgressBar3 = (CircularProgressBar)findViewById(R.id.circle_progress3);
        circularProgressBar4 = (CircularProgressBar)findViewById(R.id.circle_progress4);
        circularProgressBar5 = (CircularProgressBar)findViewById(R.id.circle_progress5);
        circularProgressBar6 = (CircularProgressBar)findViewById(R.id.circle_progress6);
        t1=(TextView)findViewById(R.id.textView24);
        t2=(TextView)findViewById(R.id.textView25);
        t3=(TextView)findViewById(R.id.textView26);
        t4=(TextView)findViewById(R.id.textView27);
        t5=(TextView)findViewById(R.id.textView28);
        t6=(TextView)findViewById(R.id.textView29);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GarbageActivity.super.onBackPressed();
            }
        });

        fetchGarbageDetails();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                showDetails();
            }
        }, 2000);

    }

    public void showDetails() {
        int animationDuration = 1500; // 2500ms = 2,5s
        if (temp[0]<50) {
            circularProgressBar.setColor(getResources().getColor(R.color.green));
        }
        else if (temp[0] >= 50 && temp[0] <70) {
            circularProgressBar.setColor(getResources().getColor(R.color.yellow_600));
        }
        else {
            circularProgressBar.setColor(getResources().getColor(R.color.red));
        }
        circularProgressBar.setProgressWithAnimation(temp[0], animationDuration);
        t1.setText("    " + temp[0]+"%");
        if (temp[1]<50) {
            circularProgressBar2.setColor(getResources().getColor(R.color.green));
        }
        else if (temp[1] >= 50 && temp[1] <70) {
            circularProgressBar2.setColor(getResources().getColor(R.color.yellow_600));
        }
        else {
            circularProgressBar2.setColor(getResources().getColor(R.color.red));
        }
        circularProgressBar2.setProgressWithAnimation(temp[1], animationDuration);
        t2.setText("   " + temp[1]+"%");
        if (temp[2]<50) {
            circularProgressBar3.setColor(getResources().getColor(R.color.green));
        }
        else if (temp[2] >= 50 && temp[2] <70) {
            circularProgressBar3.setColor(getResources().getColor(R.color.yellow_600));
        }
        else {
            circularProgressBar3.setProgress(getResources().getColor(R.color.red));
        }
        circularProgressBar3.setProgressWithAnimation(temp[2], animationDuration);
        t3.setText("" + temp[2]+"%");

        if (temp[3]<50) {
            circularProgressBar4.setColor(getResources().getColor(R.color.green));
        }
        else if (temp[3] >= 50 && temp[3] <70) {
            circularProgressBar4.setColor(getResources().getColor(R.color.yellow_600));
        }
        else {
            circularProgressBar4.setColor(getResources().getColor(R.color.red));
        }
        circularProgressBar4.setProgressWithAnimation(temp[3], animationDuration);
        t4.setText("   " + temp[3]+"%");

        if (temp[4]<50) {
            circularProgressBar5.setColor(getResources().getColor(R.color.green));
        }
        else if (temp[4] >= 50 && temp[4] <70) {
            circularProgressBar5.setColor(getResources().getColor(R.color.yellow_600));
        }
        else {
            circularProgressBar5.setColor(getResources().getColor(R.color.red));
        }

        circularProgressBar5.setProgressWithAnimation(temp[4], animationDuration);
        t5.setText(" " + temp[4]+"%");

        if (temp[5]<50) {
            circularProgressBar6.setColor(getResources().getColor(R.color.green));
        }
        else if (temp[5] >= 50 && temp[5] <70) {
            circularProgressBar6.setColor(getResources().getColor(R.color.yellow_600));
        }
        else {
            circularProgressBar6.setColor(getResources().getColor(R.color.red));
        }
        circularProgressBar6.setProgressWithAnimation(temp[5], animationDuration);
        t6.setText("   " + temp[5]+"%");
    }


    public void fetchGarbageDetails() {
        final String TAG = GarbageActivity.class.getSimpleName();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, s, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    temp[0] = response.getInt("field1");
                    temp[1] = response.getInt("field2");
                    temp[2] = response.getInt("field3");
                    temp[3] = response.getInt("field4");
                    temp[4] = response.getInt("field5");
                    temp[5] = response.getInt("field6");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });
        // Adding request to request queue
        AppCont.getInstance().addToRequestQueue(jsonObjReq);
    }
}
