package com.example.android.Bella;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Timer;

import io.ghyeok.stickyswitch.widget.StickySwitch;


public class PowergridActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static String TAG = PowergridActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    public String urll,id;
    public int lim,pro,usage;
    String info;
    int  link;
    StickySwitch s1,s2,s3,s4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_grid);
        link = getIntent().getIntExtra("link",0);
        if(link==1) {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/goal2_1.json";
        } else if(link==2) {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/goal2_2.json";
        } else {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/goal2_3.json";
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        s1 = (StickySwitch)findViewById(R.id.toggleButton2);
        s2 = (StickySwitch)findViewById(R.id.toggleButton3);
        s3 = (StickySwitch)findViewById(R.id.toggleButton4);
        s4 = (StickySwitch)findViewById(R.id.toggleButton5);
        Log.d("link: ",Integer.toString(link));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        makeJsonObjectRequest();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowergridActivity.super.onBackPressed();
            }
        });
        fuckyou();
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
                    int name = response.getInt("id1");
                    int usg = response.getInt("curr_usg");
                    int pro= response.getInt("production");
                    int lim = response.getInt("g_limit");
                    // String donate = response.getString("donat");
                    //String about = response.getString("about_post");
                    /*Respadd=response.getString("add");
                    Respemail=response.getString("email");
                    phn=response.getString("phn");
                    Respname = name;
                    Respabout= about + "\n";
                    Respdonate = donate + "\n";


                    sname.setText(Respname);
                    content.setText(Respabout);*/

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
    private void fuckyou() {
        if(link == 1) {
            new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    notif("High Energy Usage","House");
                    s1.setSwitchColor(getResources().getColor(R.color.red));
                    s1.setDirection(StickySwitch.Direction.RIGHT);
                }
            }.start();
        } else if(link == 2) {
            new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    notif("High Energy Usage","Industry");
                    s2.setSwitchColor(getResources().getColor(R.color.red));
                    s2.setDirection(StickySwitch.Direction.RIGHT);
                }
            }.start();
        } else {
            new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    notif("High Energy Usage","Farming");
                    s3.setSwitchColor(getResources().getColor(R.color.red));
                    s3.setDirection(StickySwitch.Direction.RIGHT);
                }
            }.start();
        }
    }

    private void notif(String title, String notif) {
        int notifId=1;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.bella_launcher);
        mBuilder.setContentTitle("Warning! "+title);
        mBuilder.setContentText(notif);
        NotificationManager mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNM.notify(notifId,mBuilder.build());
    }
    }


