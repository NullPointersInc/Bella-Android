package com.example.android.Bella;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class SuggestionActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestion_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuggestionActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mainListView = (ListView) findViewById(R.id.list_item_suggestion);
        final String[] queries = { "How's the weather today?", "Play me some song", "What is 15 + 20?", "Fetch me latest news","Connect to my home","Turn On light1","Turn On Sprinklers","Moisture status",
                "What date is it today?","What time is it now?","Is this is a good song?","When is your birthday?","What is 50 * 12","Play the song Closer"};

        ArrayList<String> suggestionList = new ArrayList<String>();
        suggestionList.addAll( Arrays.asList(queries) );
        listAdapter = new ArrayAdapter<String>(this, R.layout.list_item_suggestion, suggestionList);
        mainListView.setAdapter( listAdapter );
    }
}
