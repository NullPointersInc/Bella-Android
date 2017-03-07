package com.example.android.Bella;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsFragment extends Fragment {
    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        NewsTask newsTask = new NewsTask();
        newsTask.execute();
    }


    public ArrayAdapter<String> mNewsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newsfragment, container, false);
        String newsArray[] = {};
        List<String> news = new ArrayList(Arrays.asList(newsArray));
        mNewsAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_news,
                R.id.list_item_news_textview,
               news);
        ListView listview = (ListView) rootView.findViewById(R.id.listview_news);
        listview.setAdapter(mNewsAdapter);
        return rootView;
    }


    public class NewsTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = NewsTask.class.getSimpleName();



        public String[]getNewsData(String newsJsonStr)
                throws JSONException {



            JSONObject news = new JSONObject(newsJsonStr);
            JSONArray NewsArray = news.getJSONArray("articles");
            String[] resultStrs = new String[7];
            for (int i = 0; i < 7; i++) {

                JSONObject newsList = NewsArray.getJSONObject(i);
                String res;
                res= newsList.getString("title");
                resultStrs[i] = res;
            }

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "News entry: " + s);
            }
            return resultStrs;
        }

        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String newsJsonStr = null;

            try {

                // Credits to https://newsapi.org
                URL url = new URL("https://newsapi.org/v1/articles?source=the-hindu&sortBy=top&apiKey=f27d729ed4ea4d4e8b17db1bb5df031a");
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
        protected void onPostExecute(String[] result) {
            if (result!=null){
                mNewsAdapter.clear();
                for ( String newsStr : result){
                    mNewsAdapter.add(newsStr);
                }

            }
        }
    }
}





