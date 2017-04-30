package com.example.android.Bella;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class SoilActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView t1,t2,t3,t4,t5,t6,t7;
    private static String TAG = SoilActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    public String id;
    public String info;
    int  link;
    String cityName,type ,farm,crop,cover,ssnS,ssnE ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        link = getIntent().getIntExtra("link",0);
        if(link==1) {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/goal1_1.json";
        } else  {
            info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/master/goal1_2.json";
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoilActivity.super.onBackPressed();
            }
        });
        t1 = (TextView)findViewById(R.id.city_name);
        t2 = (TextView)findViewById(R.id.soil_type);
        t3 = (TextView)findViewById(R.id.farm_type);
        t4 = (TextView)findViewById(R.id.crop_type);
        t5 = (TextView)findViewById(R.id.cover_crop);
        t6 = (TextView)findViewById(R.id.starts);
        t7 = (TextView)findViewById(R.id.ends);

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
                    cityName = response.getString("c_name");
                    Log.d("City: ",cityName);
                    type = response.getString("s_type");
                    farm= response.getString("s_farm");
                    crop = response.getString("s_crop1");
                    cover = response.getString("s_cover");
                    ssnS = response.getString("ssn_s");
                    ssnE = response.getString("ssn_e");
                    t1.setText("City: "+cityName);
                    t2.setText("Soil Type: "+type);
                    t3.setText("Farm Type: "+farm);
                    t4.setText("Crop: "+crop);
                    t5.setText("Crop Cover: "+cover);
                    t6.setText("Start Season: "+ssnS);
                    t7.setText("End Season:"+ssnE);

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

}
