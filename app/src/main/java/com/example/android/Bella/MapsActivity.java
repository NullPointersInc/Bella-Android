package com.example.android.Bella;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    JSONObject articles;
    public String[] pt = new String[4];
    public LatLng start,bin1,bin2,bin3,bin4,bin5,bin6;

    Marker now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //fetchGarbageDetails();
       NewsTask news = new NewsTask();
        news.execute();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Start and move the camera
         start = new LatLng(13.114448, 77.634688);
         bin1 = new LatLng(13.114558, 77.636095);
         bin2 = new LatLng(13.115770, 77.636642);
         bin3 = new LatLng(13.117358, 77.634496);
         bin4 = new LatLng(13.112750, 77.634206);
         bin5 = new LatLng(13.113731, 77.634762);
         bin6 = new LatLng(13.116319, 77.634947);
        mMap.addMarker(new MarkerOptions().position(start).title("Marker in S2 Lab").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        float zoomLevel = 18; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, zoomLevel));
    }

    public void onLocationChange() {
        if (now != null) {
            now.remove();
        }
    }


   /* public void fetchGarbageDetails() {
        final String TAG = MapsActivity.class.getSimpleName();
        String s = "http://api.thingspeak.com/channels/265517/feed/last.json";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, s, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object



                    String s = response.getString("field8");
                    String[] pt = new String[4];
                    pt[0] = s.substring(0, 1);
                    pt[1] = s.substring(1, 2);
                    pt[2] = s.substring(2, 3);
                    pt[3] = s.substring(3, 4);
                    Log.e(pt[0],pt[1]);





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
     //   AppCont.getInstance().addToRequestQueue(jsonObjReq);
    }*/


    /*// Getting latitude of the current location
    double latitude = location.getLatitude();

    // Getting longitude of the current location
    double longitude = location.getLongitude();

    // Creating a LatLng object for the current location
    LatLng latLng = new LatLng(latitude, longitude);
    now = mMap.addMarker(new MarkerOptions().position(latLng)));
    // Showing the current location in Google Map
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    // Zoom in the Google Map
    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));*/
    public class NewsTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = NewsTask.class.getSimpleName();


        public String[] getNewsData(String newsJsonStr)
                throws JSONException {


            JSONObject articles = new JSONObject(newsJsonStr);

            String s = articles.getString("field8");

            pt[0] = s.substring(0, 1);
            pt[1] = s.substring(1, 2);
            pt[2] = s.substring(2, 3);
            pt[3] = s.substring(3, 4);
            return pt;
        }

        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String newsJsonStr = null;

            try {


                // Credits to https://newsapi.org
                URL url = new URL("http://api.thingspeak.com/channels/265517/feed/last.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                newsJsonStr = buffer.toString();
                Log.v(LOG_TAG, "News JSON string" + newsJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // if no data
                return null;
            } finally

            {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getNewsData(newsJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {

              switch(result[1]){
                  case "1":  mMap.addMarker(new MarkerOptions().position(bin1).title("Bin 1"));
                                break;
                  case "2":  mMap.addMarker(new MarkerOptions().position(bin2).title("Bin 2"));
                                break;
                  case "3":  mMap.addMarker(new MarkerOptions().position(bin3).title("Bin 3"));
                      break;
                  case "4":  mMap.addMarker(new MarkerOptions().position(bin4).title("Bin 4"));
                        break;
                  case "5":  mMap.addMarker(new MarkerOptions().position(bin5).title("Bin 5"));
                        break;
                  case "6":  mMap.addMarker(new MarkerOptions().position(bin6).title("Bin 6"));
                        break;

              }
                switch(result[2]){
                    case "1":  mMap.addMarker(new MarkerOptions().position(bin1).title("Bin 1"));
                        break;
                    case "2":  mMap.addMarker(new MarkerOptions().position(bin2).title("Bin 2"));
                        break;
                    case "3":  mMap.addMarker(new MarkerOptions().position(bin3).title("Bin 3"));
                        break;
                    case "4":  mMap.addMarker(new MarkerOptions().position(bin4).title("Bin 4"));
                        break;
                    case "5":  mMap.addMarker(new MarkerOptions().position(bin5).title("Bin 5"));
                        break;
                    case "6":  mMap.addMarker(new MarkerOptions().position(bin6).title("Bin 6"));
                        break;

                }
                switch(result[3]){
                    case "1":  mMap.addMarker(new MarkerOptions().position(bin1).title("Bin 1").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        break;
                    case "2":  mMap.addMarker(new MarkerOptions().position(bin2).title("Bin 2").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        break;
                    case "3":  mMap.addMarker(new MarkerOptions().position(bin3).title("Bin 3").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        break;
                    case "4":  mMap.addMarker(new MarkerOptions().position(bin4).title("Bin 4").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        break;
                    case "5":  mMap.addMarker(new MarkerOptions().position(bin5).title("Bin 5").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        break;
                    case "6":  mMap.addMarker(new MarkerOptions().position(bin6).title("Bin 6").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        break;

                }

            }
        }
    }

}



