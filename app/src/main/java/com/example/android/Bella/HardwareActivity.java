package com.example.android.Bella;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class HardwareActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, RecognitionListener {

    protected static final int RESULT_SPEECH = 1;
    private ProgressBar progressBar;
    public ImageButton btnSpeak;
    private TextView txtText;
    public TextView headText;
    public ToggleButton status;
    String address = null;
    TextToSpeech tts;
    private SpeechRecognizer speech;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);

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
        //receive the address of the bluetooth device
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
        status.setEnabled(false);
        new ConnectBT().execute();

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
    public void onResults(Bundle data)
    {
        ArrayList<String> text = data.getStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION);
        progressBar.setVisibility(View.INVISIBLE);

        txtText.setText(text.get(0));

        String txt = txtText.getText().toString();
        if(txt.isEmpty()) {
            Toast.makeText(com.example.android.Bella.HardwareActivity.this, "You did not say anything!", Toast.LENGTH_SHORT).show();
        } else if(txt.contains("room") || txt.contains("Room")) {
            headText.setText("Room");
            if(txt.contains(" on ") && (txt.contains("light 1") || txt.contains("light one"))) {
                //turn on lights
                tts.speak("Turned On light 1!", TextToSpeech.QUEUE_FLUSH, null);
                command(000);
                status.setChecked(true);
            } else if(txt.contains(" off") && (txt.contains("light 1") || txt.contains("light one"))) {
                //turn off lights
                tts.speak("Turned Off light 1!", TextToSpeech.QUEUE_FLUSH, null);
                command(001);
                status.setChecked(false);
            } else if(txt.contains(" on ") && (txt.contains("light 2") || txt.contains("light two"))) {
                //turn off lights
                tts.speak("Turned On light 2!", TextToSpeech.QUEUE_FLUSH, null);
                command(010);
                status.setChecked(true);
            } else if(txt.contains(" off") && (txt.contains("light 2") || txt.contains("light two"))) {
                //turn off lights
                tts.speak("Turned Off light 2!", TextToSpeech.QUEUE_FLUSH, null);
                command(011);
                status.setChecked(false);
            } else {
                tts.speak("Sorry, Incorrect information!", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else if (txt.contains("kitchen") || txt.contains("Kitchen")) {
            headText.setText("Kitchen");
            if(txt.contains("status") || txt.contains("enough food") || txt.contains("supplies")) {
                //turn on lighs
                tts.speak("Status of the container!", TextToSpeech.QUEUE_FLUSH, null);
                command(100);

            } else {
                tts.speak("Sorry, I can only tell the status of container.", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else if (txt.contains("garden") || txt.contains("Garden")) {
            headText.setText("Garden");
            if(txt.contains(" on ")) {
                //turn on sprinklers
                tts.speak("Turned On sprinklers!", TextToSpeech.QUEUE_FLUSH, null);
                command(101);
                status.setChecked(true);
            } else if(txt.contains("off")) {
                //turn off sprinklers
                tts.speak("Turned Off sprinklers!", TextToSpeech.QUEUE_FLUSH, null);
                command(110);
                status.setChecked(false);
            } else if(txt.contains("moisture")) {
                //moisture
                tts.speak("Moisture in the soil!", TextToSpeech.QUEUE_FLUSH, null);
                command(111);
            } else {
                tts.speak("Sorry, Incorrect information.", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else if (txt.contains("disconnect")){
            tts.speak("Disconnecting.", TextToSpeech.QUEUE_FLUSH, null);
            Disconnect();
        } else if (txt.contains("creator") || txt.contains("devs") || txt.contains("developers") || txt.contains("father") || txt.contains("created") || txt.contains("built") || txt.contains("made")) {
            tts.speak("I was built from scratch by a bunch of developers, Here they are!!", TextToSpeech.QUEUE_FLUSH, null);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent (com.example.android.Bella.HardwareActivity.this,AboutActivity.class);
                    startActivity(i);
                }
            }, 2500);
        } else if (txt.contains("feeling")) {
            tts.speak("Never been better!", TextToSpeech.QUEUE_FLUSH, null);
        } else if (txt.contains("weather")) {
            if(isNetworkAvailable()==0) {
                tts.speak("It seems like internet connection is unavailable so I am unable to fetch weather report", TextToSpeech.QUEUE_FLUSH, null);
                Intent i = new Intent(com.example.android.Bella.HardwareActivity.this, com.example.android.Bella.MainActivity.class);
                startActivity(i);
                finish();
            }
            else {
                tts.speak("Here is the weather forecast", TextToSpeech.QUEUE_FLUSH, null);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(com.example.android.Bella.HardwareActivity.this, WeatherActivity.class);
                        startActivity(i);
                    }
                }, 1200);
            }
        } else if (txt.contains("date") && txt.contains("time")) {
            String datetime = DateFormat.getDateTimeInstance().format(new Date());
            tts.speak("It is"+datetime, TextToSpeech.QUEUE_FLUSH, null);
        } else if (txt.contains("date")) {
            String date = DateFormat.getDateInstance().format(new Date());
            tts.speak("It is"+date, TextToSpeech.QUEUE_FLUSH, null);
        } else if (txt.contains("time")) {
            String time = DateFormat.getTimeInstance().format(new Date());
            tts.speak("It is"+time, TextToSpeech.QUEUE_FLUSH, null);
        } else if (txt.contains("what all can you play") || txt.contains("what all songs can you play") || txt.contains("supported songs")) {
            tts.speak("I can play. help me lose my mind by Disclosure. Lean On by Major Lazor. Closer by Chainsmokers. Ongoing things by 2SYL. We don't talk anymore by Charlie Puth. Soon I will support more songs.", TextToSpeech.QUEUE_FLUSH, null);
        } else if (txt.contains("play")) {
            if(isNetworkAvailable()==0) {
                tts.speak("It seems like internet connection is unavailable so I am unable to play songs", TextToSpeech.QUEUE_FLUSH, null);
            }
            else {
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
            }
        } else if (txt.contains("Bella") || txt.contains("bella") || txt.contentEquals("who are you")) {
            tts.speak("Greetings! human, I am Bella! An assistant powered by Artificial Intelligence and machiene learning.", TextToSpeech.QUEUE_FLUSH, null);
        } else if (txt.contentEquals("hey") || txt.contentEquals("hi") || txt.contentEquals("hello")) {
            tts.speak("Greetings! human, I am Bella! An assistant powered by Artificial Intelligence and machiene learning.", TextToSpeech.QUEUE_FLUSH, null);
        } else if ((txt.contains("hey") || txt.contains("hi") || txt.contains("hello")) && ((!txt.contains("bella") || !txt.contains("Bella"))) ) {
            tts.speak("Sorry, Are you talking to me? You can call me bella", TextToSpeech.QUEUE_FLUSH, null);
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




    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                int id = item.getItemId();
                if (id == R.id.action_settings) {
                    Intent intent = new Intent(com.example.android.Bella.HardwareActivity.this,AboutActivity.class);
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

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            txtText.setText("Connecting...");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                txtText.setText("");
                finish();
            }
            else
            {
                msg("Connected.");
                txtText.setText("");
                isBtConnected = true;
            }
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout
    }

    private void command(int i)
    {
        if (btSocket!=null)
        {
            if (i == 000) {
                try
                {
                    btSocket.getOutputStream().write("RL1O:".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            } else if (i == 001) {
                try
                {
                    btSocket.getOutputStream().write("RL1F:".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            } else if (i == 010) {
                try
                {
                    btSocket.getOutputStream().write("RL2O:".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            } else if (i == 011) {
                try
                {
                    btSocket.getOutputStream().write("RL2F:".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            } else if (i == 100) {
                try
                {
                    btSocket.getOutputStream().write("KS:".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            } else if (i == 101) {
                try
                {
                    btSocket.getOutputStream().write("GSO:".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            } else if (i == 110) {
                try
                {
                    btSocket.getOutputStream().write("GSF:".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            } else if (i == 111) {
                try
                {
                    btSocket.getOutputStream().write("GSS:".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }
    }
}
