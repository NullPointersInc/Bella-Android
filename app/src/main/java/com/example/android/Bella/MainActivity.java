package com.example.android.Bella;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Locale;
import android.media.MediaPlayer;
import java.text.DateFormat;
import java.util.Date;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private TextView txtText2;
    public TextView headText;
    public ToggleButton status;
    TextToSpeech tts;
    private SpeechRecognizer speech;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ((ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.RECORD_AUDIO}, 1);
        }

        txtText = (TextView) findViewById(R.id.txtText);
        txtText2 = (TextView) findViewById(R.id.txtText2);
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
                txtText.setHint("Listening...");
                btnSpeak.setImageResource(R.drawable.ic_listen);
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                speech.startListening(intent);
                try {

                    txtText.setText("");
                    txtText2.setText("");
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
        btnSpeak.setImageResource(R.drawable.ic_action_voice);
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
                    Toast.makeText(MainActivity.this, "Permission granted to record audio and internet", Toast.LENGTH_SHORT).show();
                    // permission was granted
                } else {

                    // permission denied, Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to record audio and internet", Toast.LENGTH_SHORT).show();
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
            txtText2.setText("You did not say anything!");
        } else if (txt.contains("connect")) {
            Intent i = new Intent(MainActivity.this, DeviceList.class);
            startActivity(i);
        } else if (txt.contains("creator") || txt.contains("devs") || txt.contains("developers") || txt.contains("father") || txt.contains("created") || txt.contains("built") || txt.contains("made")) {
            tts.speak("I was built from scratch by a bunch of developers, Here they are!!", TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("I was built from scratch by a bunch of developers, Here they are!!");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent (MainActivity.this,AboutActivity.class);
                    startActivity(i);
                }
            }, 2500);
        } else if (txt.contains("feeling")) {
            tts.speak("Never been better!", TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("Never been better!");
        } else if (txt.contains("weather")) {
            if(isNetworkAvailable()==0) {
                tts.speak("It seems like internet connection is unavailable so I am unable to fetch weather report", TextToSpeech.QUEUE_FLUSH, null);
                txtText2.setText("It seems like internet connection is unavailable so I am unable to fetch weather report");
                Intent i = new Intent(MainActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
            else {
                tts.speak("Here is the weather forecast", TextToSpeech.QUEUE_FLUSH, null);
                txtText2.setText("Here is the weather forecast");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainActivity.this, WeatherActivity.class);
                        startActivity(i);
                    }
                }, 1200);
            }
        } else if (txt.contains("date") && txt.contains("time")) {
            String datetime = DateFormat.getDateTimeInstance().format(new Date());
            tts.speak("It is"+datetime, TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("It is"+datetime);
        } else if (txt.contains("date")) {
            String date = DateFormat.getDateInstance().format(new Date());
            tts.speak("It is"+date, TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("It is"+date);
        } else if (txt.contains("time")) {
            String time = DateFormat.getTimeInstance().format(new Date());
            tts.speak("It is"+time, TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("It is"+time);
        } else if (txt.contains("what all can you play") || txt.contains("what all songs can you play") || txt.contains("supported songs")) {
            tts.speak("I can play. help me lose my mind by Disclosure. Lean On by Major Lazor. Closer by Chainsmokers. Ongoing things by 2SYL. We don't talk anymore by Charlie Puth. Soon I will support more songs.", TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("I can play the quoted songs");
        } else if (txt.contains("play")) {
            if(isNetworkAvailable()==0) {
                tts.speak("It seems like internet connection is unavailable so I am unable to play songs", TextToSpeech.QUEUE_FLUSH, null);
                txtText2.setText("It seems like internet connection is unavailable so I am unable to play songs");
            }
            else {
                if(txt.contains("help me lose my mind")) {
                    tts.speak("Playing help me lose my mind by Disclosure", TextToSpeech.QUEUE_FLUSH, null);
                    txtText2.setText("Playing help me lose my mind by Disclosure\"");
                    String path = "https://www.dropbox.com/s/bbdy28pwg6lwxp5/HelpMeLoseMyMind.mp3?dl=1";
                    play(path);
                } else if(txt.contains("lean on")) {
                    tts.speak("Playing Lean On by Major Lazor", TextToSpeech.QUEUE_FLUSH, null);
                    txtText2.setText("Playing Lean On by Major Lazor");
                    String path = "https://www.dropbox.com/s/cquqiauh204ml7x/LeanOn.mp3?dl=1";
                    play(path);
                } else if(txt.contains("closer")) {
                    tts.speak("Playing Closer by Chainsmokers", TextToSpeech.QUEUE_FLUSH, null);
                    txtText2.setText("Playing Closer by Chainsmokers");
                    String path = "https://www.dropbox.com/s/x428bu4lv3wd1qj/Closer.mp3?dl=1";
                    play(path);
                } else if(txt.contains("on going things") || txt.contains("ongoing things")) {
                    tts.speak("Playing Ongoing things by 2SYL", TextToSpeech.QUEUE_FLUSH, null);
                    txtText2.setText("Playing Ongoing things by 2SYL");
                    String path = "https://www.dropbox.com/s/r7hmbxxlw13nlsf/OngoingThings.mp3?dl=1";
                    play(path);
                } else if(txt.contains("we don't talk anymore")) {
                    tts.speak("Playing We don't talk anymore by Charlie Puth", TextToSpeech.QUEUE_FLUSH, null);
                    txtText2.setText("Playing We don't talk anymore by Charlie Puth");
                    String path = "https://www.dropbox.com/s/a1ahxsid403ebj3/lkAnyMore.mp3?dl=1";
                    play(path);
                } else if(txt.contentEquals("play")){
                    tts.speak("You have not specified any song!", TextToSpeech.QUEUE_FLUSH, null);
                    txtText2.setText("You have not specified any song!");
                } else {
                    tts.speak("Song not available at the moment or not specified! Sorry!", TextToSpeech.QUEUE_FLUSH, null);
                    txtText2.setText("Song not available at the moment or not specified! Sorry!");
                }
            }
        } else if (txt.contains("bored")) {
            tts.speak("Go, get a life!", TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("Go get a life!");
        } else if (txt.contains("Bella") || txt.contains("bella") || txt.contentEquals("who are you")) {
            tts.speak("Greetings! human, I am Bella! An assistant powered by Artificial Intelligence and machiene learning.", TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("Greetings! human, I am Bella! An assistant powered by Artificial Intelligence and machiene learning");
        } else if (txt.contentEquals("hey") || txt.contentEquals("hi") || txt.contentEquals("hello")) {
            tts.speak("Greetings! human, I am Bella! An assistant powered by Artificial Intelligence and machiene learning.", TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("Greetings! human, I am Bella! An assistant powered by Artificial Intelligence and machiene learning");
        } else if ((txt.contains("hey") || txt.contains("hi") || txt.contains("hello")) && ((!txt.contains("bella") || !txt.contains("Bella"))) ) {
            tts.speak("Sorry, Are you talking to me? You can call me bella", TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("Sorry, Are you talking to me? You can call me bella");
        } else {
            tts.speak("You have said something that I did not understand, Sorry, I will try to learn more as I grow up!", TextToSpeech.QUEUE_FLUSH, null);
            txtText2.setText("You have said something that I did not understand, Sorry, I will try to learn more as I grow up!");
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




    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                int id = item.getItemId();
            if (id == R.id.action_settings) {
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                return true;
            }
            return true;
        }
    });
        popup.show();
    }

    public int isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {
            return 1;
        }else{
            return 0;
        }
    }


}
