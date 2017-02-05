package com.example.android.simpleai;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Locale;
import android.media.MediaPlayer;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;
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
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, RecognitionListener {

    protected static final int RESULT_SPEECH = 1;
    private ProgressBar progressBar;
    public ImageButton btnSpeak;
    private TextView txtText;
    public TextView headText;
    public ToggleButton status;
    TextToSpeech tts;
    private SpeechRecognizer speech;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO},1);

        txtText = (TextView) findViewById(R.id.txtText);
        headText = (TextView) findViewById(R.id.textView2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        status = (ToggleButton) findViewById(R.id.toggleButton);
        //Initially progressbar is invisible
        progressBar.setVisibility(View.INVISIBLE);
        tts = new TextToSpeech(this, this);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                speech.startListening(intent);
                try {

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
    public void onBeginningOfSpeech()
    {
    }

    @Override
    public void onBufferReceived(byte[] arg0)
    {
    }

    @Override
    public void onEndOfSpeech()
    {
    }

    @Override
    public void onError(int e)
    {
    }

    @Override
    public void onEvent(int arg0, Bundle arg1)
    {
    }

    @Override
    public void onPartialResults(Bundle arg0)
    {
    }

    @Override
    public void onReadyForSpeech(Bundle arg0)
    {
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
                    Toast.makeText(MainActivity.this, "Permission granted to record audio,internet and storage", Toast.LENGTH_SHORT).show();
                    // permission was granted
                } else {

                    // permission denied, Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to record audio,internet and storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onResults(Bundle data)
    {
        ArrayList<String> text = data.getStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION);
        progressBar.setVisibility(View.INVISIBLE);

        txtText.setText(text.get(0));

        String txt = txtText.getText().toString();
        if(txt.isEmpty()) {
            Toast.makeText(MainActivity.this, "You did not say anything!", Toast.LENGTH_SHORT).show();
        } else if (txt.contains("connect")) {
            if(txt.contains("room") || txt.contains("Room")) {
                headText.setText("Room");
                if(txt.contains(" on ") && (txt.contains("light 1") || txt.contains("light one"))) {
                    //turn on lights
                    tts.speak("Turned On light 1!", TextToSpeech.QUEUE_FLUSH, null);
                    status.setChecked(true);
                } else if(txt.contains(" off") && (txt.contains("light 1") || txt.contains("light one"))) {
                    //turn off lights
                    tts.speak("Turned Off light 1!", TextToSpeech.QUEUE_FLUSH, null);
                    status.setChecked(false);
                } else if(txt.contains(" on ") && (txt.contains("light 2") || txt.contains("light two"))) {
                    //turn off lights
                    tts.speak("Turned On light 2!", TextToSpeech.QUEUE_FLUSH, null);
                    status.setChecked(true);
                } else if(txt.contains(" off") && (txt.contains("light 2") || txt.contains("light two"))) {
                    //turn off lights
                    tts.speak("Turned Off light 2!", TextToSpeech.QUEUE_FLUSH, null);
                    status.setChecked(false);
                } else {
                    tts.speak("Sorry, Incorrect information!", TextToSpeech.QUEUE_FLUSH, null);
                }
            } else if (txt.contains("kitchen") || txt.contains("Kitchen")) {
                headText.setText("Kitchen");
                if(txt.contains("status") || txt.contains("enough food") || txt.contains("supplies")) {
                    //turn on lighs
                    tts.speak("Status of the container!", TextToSpeech.QUEUE_FLUSH, null);

                } else {
                    tts.speak("Sorry, I can only tell the status of container.", TextToSpeech.QUEUE_FLUSH, null);
                }
            } else if (txt.contains("garden") || txt.contains("Garden")) {
                headText.setText("Garden");
                if(txt.contains(" on ")) {
                    //turn on sprinklers
                    tts.speak("Turned On sprinklers!", TextToSpeech.QUEUE_FLUSH, null);
                    status.setChecked(true);
                } else if(txt.contains("off")) {
                    //turn off sprinklers
                    tts.speak("Turned Off sprinklers!", TextToSpeech.QUEUE_FLUSH, null);
                    status.setChecked(false);
                } else if(txt.contains("moisture")) {
                    //moisture
                    tts.speak("Moisture in the soil!", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    tts.speak("Sorry, Incorrect information.", TextToSpeech.QUEUE_FLUSH, null);
                }
            } else {
                tts.speak("I currently support room, kitchen and garden, but dont you worry, I will support more devices soon", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else if (txt.contains("feeling")) {
            tts.speak("Never been better!", TextToSpeech.QUEUE_FLUSH, null);
        } else if (txt.contains("weather")) {
            Intent i = new Intent (MainActivity.this,weatherActivity.class);
           // tts.speak("Weather in bangalore is very pleasant with little humidity", TextToSpeech.QUEUE_FLUSH, null);
            startActivity(i);
        } else if (txt.contains("date") && txt.contains("time")) {
            String datetime = DateFormat.getDateTimeInstance().format(new Date());
            tts.speak("It is"+datetime, TextToSpeech.QUEUE_FLUSH, null);
        } else if (txt.contains("date")) {
            String date = DateFormat.getDateInstance().format(new Date());
            tts.speak("It is"+date, TextToSpeech.QUEUE_FLUSH, null);
        } else if (txt.contains("time")) {
            String time = DateFormat.getTimeInstance().format(new Date());
            tts.speak("It is"+time, TextToSpeech.QUEUE_FLUSH, null);
        }  else if (txt.contains("play")) {
            if(txt.contains("help me lose my mind")) {
                tts.speak("Playing help me lose my mind by Disclosure", TextToSpeech.QUEUE_FLUSH, null);
                String path = "https://www.dropbox.com/s/bbdy28pwg6lwxp5/HelpMeLoseMyMind.mp3?dl=1";
                    play(path);
            } else if(txt.contains("lean on")) {
                tts.speak("Playing Lean On by Major Lazor", TextToSpeech.QUEUE_FLUSH, null);
                String path = "https://www.dropbox.com/s/cquqiauh204ml7x/LeanOn.mp3?dl=1";
                play(path);
            } else if(txt.contains("closer")) {
                tts.speak("Playing Closer by Chainsmokers", TextToSpeech.QUEUE_FLUSH, null);
                String path = "https://www.dropbox.com/s/x428bu4lv3wd1qj/Closer.mp3?dl=1";
                play(path);
            } else if(txt.contains("on going things") || txt.contains("ongoing things")) {
                tts.speak("Playing Ongoing things by 2SYL", TextToSpeech.QUEUE_FLUSH, null);
                String path = "https://www.dropbox.com/s/r7hmbxxlw13nlsf/OngoingThings.mp3?dl=1";
                play(path);
            } else if(txt.contains("we don't talk anymore")) {
                tts.speak("Playing We don't talk anymore by Charlie Puth", TextToSpeech.QUEUE_FLUSH, null);
                String path = "https://www.dropbox.com/s/a1ahxsid403ebj3/lkAnyMore.mp3?dl=1";
                play(path);
            } else if(txt.contentEquals("play")){
                tts.speak("You have not specified any song!", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                tts.speak("Song not available at the moment or not specified! Sorry!", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else if (txt.contains("Siri")) {
            tts.speak("Hello! developers, how may I help u?", TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak("You have said something that I did not understand, Sorry, I will try to learn more as I grow up!", TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    public void onRmsChanged(float arg0)
    {
    }

    public void play (String path) {
        //set up MediaPlayer
        final MediaPlayer mp = new MediaPlayer();
                try {
                    mp.setDataSource(path);
                    mp.prepare();
                    mp.start();
                    btnSpeak.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            mp.stop();
                            return true;
                        }
                    });

                } catch (Exception e) {
                    tts.speak("Sorry! Song not found!", TextToSpeech.QUEUE_FLUSH, null);
                }
    }
}
