package com.example.android.Bella;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ashish Nayak on 07-Mar-17.
 */

public class NewsActivity extends AppCompatActivity {
    class news
    {
        String title, url, url_img;
    }
    news[] obj = new news[7];
    JSONArray articles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.newslayout);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.newscontainer, new NewsFragment())
                    .commit();
        }
        fetchNewsDetails();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }
    public void fetchNewsDetails() {
        final String TAG = NewsActivity.class.getSimpleName();
        String s = "https://newsapi.org/v1/articles?source=the-hindu&sortBy=top&apiKey=f27d729ed4ea4d4e8b17db1bb5df031a";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, s, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    articles= response.getJSONArray("articles");
                    for(int i=0; i<7;i++) {
                        obj[i] = new news();
                        JSONObject temp = articles.getJSONObject(i);
                       obj[i].title=temp.getString("title");
                       obj[i].url=temp.getString("url");
                       obj[i].url_img=temp.getString("urlToImage");
                    }
                    Log.e(obj[0].title,obj[0].url);


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
