package com.example.android.simpleai;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Locale;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.R.id.input;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    protected static final int RESULT_SPEECH = 1;
    private ProgressBar progressBar;
    private ImageButton btnSpeak;
    private TextView txtText;
    TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.RECORD_AUDIO},1);

        txtText = (TextView) findViewById(R.id.txtText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        //Initially progressbar is invisible
        progressBar.setVisibility(View.INVISIBLE);
        tts = new TextToSpeech(this, this);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    txtText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

    }
    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            Locale English = tts.getLanguage();
            int result = tts.setLanguage(English);
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","this language is not supported");
            } else {

            }
        } else {
            Log.e("TTS","Initialization failed!!");
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission granted to record audio", Toast.LENGTH_SHORT).show();
                    // permission was granted
                } else {

                    // permission denied, Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to record audio", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {
                    progressBar.setVisibility(View.INVISIBLE);
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    txtText.setText(text.get(0));

                    String txt = txtText.getText().toString();
                    if(txt.isEmpty())
                    {
                        Toast.makeText(MainActivity.this, "You did not say anything!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                break;
            }

        }

    }
}
