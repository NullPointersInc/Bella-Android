package com.example.android.Bella;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class DisasterActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static String TAG = PowergridActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    public String id;
    String info;
    int  l;
    String city;
    StickySwitch s1,s2,s3,s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        s1 = (StickySwitch)findViewById(R.id.toggleButton2);
        s2 = (StickySwitch)findViewById(R.id.toggleButton3);
        s3 = (StickySwitch)findViewById(R.id.toggleButton4);
        s4 = (StickySwitch)findViewById(R.id.toggleButton5);

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return ;
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return ;
            }
        });

        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return ;
            }
        });


        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return ;
            }
        });




        l = getIntent().getIntExtra("l",0);
        Log.d("l: ",Integer.toString(l));
        if(l==1) {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/Tsunami.json";
        } else if(l == 2) {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/cyclone.json";
        } else if(l == 4) {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/volcano.json";
        } else if(l == 3) {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/flood.json";
        } else {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/normal.json";
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisasterActivity.super.onBackPressed();
            }
        });
        city = "Bangalore";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        makeJsonObjectRequest();
    }

    private void makeJsonObjectRequest() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, info, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    city = response.getString("c_name");
                    if (city.equals("Hawaii")) {
                        notif("Warning! Volcano Alert","ETA: 1 hr");
                        s4.setSwitchColor(getResources().getColor(R.color.red));
                        s4.setDirection(StickySwitch.Direction.RIGHT);
                    } else if (city.equals("Miami")) {
                        notif("Warning! Cyclone Alert","ETA: 1 hr");
                        s2.setSwitchColor(getResources().getColor(R.color.red));
                        s2.setDirection(StickySwitch.Direction.RIGHT);
                    } else if (city.equals("Alaska")) {
                        notif("Warning! Tsunami Alert","ETA: 1 hr");
                        s1.setSwitchColor(getResources().getColor(R.color.red));
                        s1.setDirection(StickySwitch.Direction.RIGHT);
                    } else if (city.equals("North Carolina")) {
                        notif("Warning! Flood Alert","ETA: 1 hr");
                        s3.setSwitchColor(getResources().getColor(R.color.red));
                        s3.setDirection(StickySwitch.Direction.RIGHT);
                    } else {
                        notif("Mother Earth looks good","Have a nice day");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppCont.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void notif(String title, String notif) {
        int notifId=1;
        Intent resultIntent = new Intent(this, MainActivity.class);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.bella_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(notif);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationManager mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setContentIntent(resultPendingIntent);
        mNM.notify(notifId,mBuilder.build());
    }
}
