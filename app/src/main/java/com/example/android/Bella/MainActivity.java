    package com.example.android.Bella;

    import android.Manifest;
    import android.animation.Animator;
    import android.app.DownloadManager;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.bluetooth.BluetoothAdapter;
    import android.bluetooth.BluetoothDevice;
    import android.bluetooth.BluetoothSocket;
    import android.content.ComponentName;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;
    import android.graphics.Typeface;
    import android.graphics.drawable.Drawable;
    import android.net.ConnectivityManager;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.os.Build;
    import android.os.CountDownTimer;
    import android.os.Environment;
    import android.os.Handler;
    import android.os.Vibrator;
    import android.provider.MediaStore;
    import android.speech.RecognitionListener;
    import android.speech.SpeechRecognizer;
    import android.support.v4.app.ActivityCompat;
    import android.support.v4.app.ActivityOptionsCompat;
    import android.support.v4.app.NotificationCompat;
    import android.support.v4.content.ContextCompat;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;

    import java.io.IOException;
    import java.io.InputStream;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.HashMap;
    import java.util.Locale;
    import android.media.MediaPlayer;
    import java.text.DateFormat;
    import java.util.Date;
    import java.util.UUID;

    import android.speech.RecognizerIntent;
    import android.speech.tts.TextToSpeech;
    import android.content.ActivityNotFoundException;
    import android.content.Intent;
    import android.support.v7.widget.PopupMenu;
    import android.text.SpannableString;
    import android.text.format.Time;
    import android.text.style.StyleSpan;
    import android.text.style.UnderlineSpan;
    import android.transition.Explode;
    import android.transition.Fade;
    import android.transition.Transition;
    import android.transition.TransitionInflater;
    import android.util.Log;
    import android.view.Display;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.ViewAnimationUtils;
    import android.widget.Button;
    import android.widget.ImageButton;
    import android.widget.ProgressBar;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.android.volley.Request;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.VolleyLog;
    import com.android.volley.toolbox.JsonObjectRequest;
    import com.getkeepsafe.taptargetview.TapTarget;
    import com.getkeepsafe.taptargetview.TapTargetSequence;
    import com.getkeepsafe.taptargetview.TapTargetView;
    import com.github.javiersantos.appupdater.AppUpdater;
    import com.github.javiersantos.appupdater.enums.UpdateFrom;
    import com.tapadoo.alerter.Alerter;
    import com.thefinestartist.finestwebview.FinestWebView;

    import org.jetbrains.annotations.NotNull;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;
    import org.w3c.dom.Text;

    import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
    import io.ghyeok.stickyswitch.widget.StickySwitch;

    import static com.example.android.Bella.SoilActivity.info;


    public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, RecognitionListener {

        protected static final int RESULT_SPEECH = 1;
        private ProgressBar progressBar;
        public ImageButton btnSpeak;
        private TextView txtText;
        public TextView headText;
        public StickySwitch status;
        TextToSpeech tts;
        boolean doubleBackToExitPressedOnce;
        TransitionInflater tf;
        public static String type_name, soil_type, start_month, end_month, temp_crop, temp_crop2,city;

        private SpeechRecognizer speech;
        public Vibrator myVib;
        private com.tuyenmonkey.mkloader.MKLoader loader;
        public static boolean hardware = false;
        String address = null;
        String song = null;
        boolean queryStatus = false;

        String max;
        String min;
        String c="bangalore";

        //News
        public static class news
        {
            String title, url, url_img,desc;
        }
        public static news[] obj = new news[12];
        JSONArray articles;

        JSONArray list;

        //Bluetooth Stuff
        BluetoothAdapter myBluetooth = null;
        BluetoothSocket btSocket = null;
        private boolean isBtConnected = false;

        //bluetooth receive initializer
        InputStream mmInputStream;
        volatile boolean stopWorker;
        Thread workerThread;

        byte[] readBuffer;
        int readBufferPosition;
        public String updateStatus;

        //SPP UUID. Look for it
        static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        HashMap<String,String> contexts = new HashMap<>();

        //Notification
        NotificationCompat.Builder mBuilder;


        //NASA Space Apps
        int energyUsed = 0;
        CountDownTimer t1,t2,t3;
        int link=1;

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            checkForUpdate();

            /*try {
                tf = TransitionInflater.from(this);
                Transition t = tf.inflateTransition(R.transition.transactivity);
                getWindow().setExitTransition(t);
            } catch (Exception e) {

            }*/
            //custom app crash
            CustomActivityOnCrash.setShowErrorDetails(false);
            CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.drawable.bellacoffee);
            CustomActivityOnCrash.install(this);

            Fade s = new Fade();
            s.setDuration(1000);
            getWindow().setReturnTransition(s);

            txtText = (TextView) findViewById(R.id.txtText);
            headText = (TextView) findViewById(R.id.textView2);
            progressBar = (ProgressBar) findViewById(R.id.progressBar1);
            btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
            status = (StickySwitch) findViewById(R.id.toggleButton);
            status.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            loader = (com.tuyenmonkey.mkloader.MKLoader) findViewById(R.id.listen);
            //Initially progressbar is invisible
            progressBar.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.INVISIBLE);
            tts = new TextToSpeech(this, this);
            speech = SpeechRecognizer.createSpeechRecognizer(this);
            speech.setRecognitionListener(this);
            myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
            btnSpeak.setVisibility(View.INVISIBLE);
            status.setVisibility(View.INVISIBLE);
            txtText.setVisibility(View.INVISIBLE);

            status.setClickable(false);
            status.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
                @Override
                public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                    direction = status.getDirection();
                    status.setDirection(direction);
                }
            });

            updateStatus = "FFFFFF";
            if(hardware) {
                //receive the address of the bluetooth device
                Intent newint = getIntent();
                address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
                status.setEnabled(false);
                new ConnectBT().execute();
            }

            btnSpeak.postDelayed(new Runnable() {
                @Override
                public void run() {
                    revealEffect(btnSpeak);
                }
            },900);

            status.postDelayed(new Runnable() {
                @Override
                public void run() {
                    revealEffect(status);
                }
            },300);

            status.postDelayed(new Runnable() {
                @Override
                public void run() {
                    revealEffect(txtText);
                }
            },600);

            SharedPreferences sharedPref = this.getSharedPreferences("SEQUENCE_TAP_TARGET", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();
            // We load a drawable and create a location to show a tap target here
            // We need the display to get the width and height at this point in time
            final Display display = getWindowManager().getDefaultDisplay();
            // Load our little droid guy
            final Drawable droid = ContextCompat.getDrawable(this, R.drawable.ic_face);

            final SpannableString sassyDesc = new SpannableString("This will tell you which activity you are currently in");
            sassyDesc.setSpan(new StyleSpan(Typeface.ITALIC), sassyDesc.length() - "sometimes".length(), sassyDesc.length(), 0);

            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_action_off)
                    .setContentTitle("The bulb seems to be switched ON for too long!")
                    .setContentText("Tap to switch off the bulb");

            // We have a sequence of targets, so lets build it!
            final TapTargetSequence sequence = new TapTargetSequence(this)
                    .targets(
                            // This tap target will target the back button, we just need to pass its containing toolbar
                            TapTarget.forView(findViewById(R.id.textView2), "This is the current screen", sassyDesc).id(1)
                                    .dimColor(android.R.color.black)
                                    .outerCircleColor(R.color.target)
                                    .targetCircleColor(android.R.color.black)
                                    .transparentTarget(true)
                                    .textColor(android.R.color.black),
                            // Likewise, this tap target will target the search button
                            TapTarget.forView(findViewById(R.id.toggleButton), "Status Indicator", "As Bella can connect to hardware, it can tell the current status")
                                    .dimColor(android.R.color.black)
                                    .outerCircleColor(R.color.target)
                                    .targetCircleColor(android.R.color.black)
                                    .transparentTarget(true)
                                    .textColor(android.R.color.black)
                                    .id(2),
                            // You can also target the overflow button in your toolbar
                            TapTarget.forView(findViewById(R.id.txtText), "Command Parser", "This will show the command issued by user")
                                    .dimColor(android.R.color.black)
                                    .outerCircleColor(R.color.target)
                                    .targetCircleColor(android.R.color.black)
                                    .transparentTarget(true)
                                    .textColor(android.R.color.black)
                                    .id(3)
                                    .targetRadius(100)
                    )
                    .listener(new TapTargetSequence.Listener() {
                        // This listener will tell us when interesting(tm) events happen in regards
                        // to the sequence
                        @Override
                        public void onSequenceFinish() {
                            editor.putBoolean("finished",true);
                            editor.commit();

                            Alerter.create(MainActivity.this)
                                    .setText("Yay! your personal assistant is ready for you.")
                                    .setIcon(R.drawable.ic_face)
                                    .setBackgroundColor(R.color.alert)
                                    .setDuration(2000)
                                    .show();
                        }

                        @Override
                        public void onSequenceStep(TapTarget lastTarget) {
                            Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                        }

                        @Override
                        public void onSequenceCanceled(TapTarget lastTarget) {
                            editor.putBoolean("finished",true);
                            editor.commit();
                            final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Uh oh")
                                    .setMessage("You canceled the sequence")
                                    .setPositiveButton("Oops", null).show();
                            TapTargetView.showFor(dialog,
                                    TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
                                            .cancelable(false)
                                            .tintTarget(false), new TapTargetView.Listener() {
                                        @Override
                                        public void onTargetClick(TapTargetView view) {
                                            super.onTargetClick(view);
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
            boolean isSequenceFinished = sharedPref.getBoolean("finished",false);
            if(!isSequenceFinished) {
                // You don't always need a sequence, and for that there's a single time tap target
                final SpannableString spannedDesc = new SpannableString("Use this button to issue voice command");
                spannedDesc.setSpan(new UnderlineSpan(), spannedDesc.length() - "Use this button to issue voice command".length(), spannedDesc.length(), 0);
                TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.btnSpeak), "Hello, world!", spannedDesc)
                        .dimColor(android.R.color.black)
                        .outerCircleColor(R.color.target)
                        .targetCircleColor(android.R.color.black)
                        .transparentTarget(true)
                        .textColor(android.R.color.black)
                        .cancelable(false)
                        .drawShadow(true)
                        .tintTarget(false), new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        // .. which evidently starts the sequence we defined earlier
                        sequence.start();
                    }

                    @Override
                    public void onOuterCircleClick(TapTargetView view) {
                        super.onOuterCircleClick(view);
                        Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                        Log.d("TapTargetViewSample", "You dismissed me :(");
                    }
                });
            }
            if ((ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.RECORD_AUDIO}, 1);
            }

            contexts.put("Disclosure","null");
            contexts.put("Major Lazor","null");
            contexts.put("Chainsmokers","null");
            contexts.put("2SYL","null");
            contexts.put("Charlie Puth","null");

            txtText.setHint("Hello! I am Bella.");
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    txtText.setText("How may I help you?");
                }
            }, 3000);

            Intent resultIntent = new Intent(this, MainActivity.class);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);

            btnSpeak.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if ((ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED)) {

                        Toast.makeText(MainActivity.this, "Please allow permission of recording audio.", Toast.LENGTH_SHORT).show();

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                Manifest.permission.RECORD_AUDIO}, 1);

                    }
                    else {
                        queryStatus = true;
                        txtText.setVisibility(View.INVISIBLE);
                        myVib.vibrate(50);
                        btnSpeak.setImageResource(R.drawable.ic_listen);
                        progressBar.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                        speech.startListening(intent);
                        try {

                            txtText.setText("");
                        } catch (ActivityNotFoundException a) {
                            Alerter.create(MainActivity.this)
                                    .setText("Oops! Your device doesn't support Speech to Text")
                                    .setBackgroundColor(R.color.alert)
                                    .show();
                        }
                    }

                }
            });
        }

        public void checkForUpdate() {
            //Check for update
            new AppUpdater(this)
                    .setUpdateFrom(UpdateFrom.XML)
                    .setUpdateXML("https://raw.githubusercontent.com/Bella-Assistant/Bella-Android/alpha/update-changelog.xml")
                    .setTitleOnUpdateNotAvailable("Update not available")
                    .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                    .setDisplay(com.github.javiersantos.appupdater.enums.Display.DIALOG)
                    .start();
        }


        //runs when MainActivity opens
        public void init(){
            progressBar.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.INVISIBLE);
            btnSpeak.setImageResource(R.drawable.ic_action_listen);
            txtText.setText(null);
            txtText.setHint("How may I help You?");
        }

        //runs when MainActivity is closed
        @Override
        protected void onDestroy() {
            if(tts != null) {

                tts.stop();
                tts.shutdown();
                speech.destroy();

            }
            super.onDestroy();
            Disconnect();
        }

        //duh!
        @Override
        public void onBackPressed() {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

        //When stt starts recognising
        @Override
        public void onBeginningOfSpeech()
        {
            btnSpeak.setImageResource(R.drawable.ic_listen);
        }

        //When stt recognises words
        @Override
        public void onBufferReceived(byte[] arg0)
        {
            btnSpeak.setImageResource(R.drawable.ic_listen);
        }

        //Duh!
        @Override
        public void onEndOfSpeech()
        {
            btnSpeak.setImageResource(R.drawable.ic_action_listen);
            progressBar.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onError(int e)
        {
            Toast.makeText(MainActivity.this, "You did not say anything!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEvent(int arg0, Bundle arg1)
        {
        }

        @Override
        public void onPartialResults(Bundle arg0)
        {
            Toast.makeText(MainActivity.this, "You did not say anything!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReadyForSpeech(Bundle arg0)
        {

        }

        //Animation stuff, better don't mess here
        void revealEffect(View v) {
            if(Build.VERSION.SDK_INT > 20) {
                int cx = v.getMeasuredWidth()/2;
                int cy = v.getMeasuredHeight()/2;
                int finalRadius = Math.max(v.getWidth(),v.getHeight());
                Animator a = ViewAnimationUtils.createCircularReveal(v,cx,cy,0,finalRadius);
                a.setDuration(1000);
                v.setVisibility(View.VISIBLE);
                a.start();
            }
        }

        //Runs when tts is initialised, don't touch
        @Override
        public void onInit(int status) {
            if(status == TextToSpeech.SUCCESS) {
                Locale English = tts.getLanguage();
                int result = tts.setLanguage(English);
                if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS","this language is not supported");
                }
            } else {
                Log.e("TTS","Initialization failed!!");
            }


        }

        //Checks if permission is granted or not
        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case 1: {

                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Alerter.create(MainActivity.this)
                                .setText("Permission granted to record audio")
                                .setIcon(R.drawable.ic_face)
                                .setBackgroundColor(R.color.alert)
                                .setDuration(2000)
                                .show();
                        // permission was granted
                    } else {
                        // permission denied, Disable the
                        // functionality that depends on this permission.
                        Alerter.create(MainActivity.this)
                                .setText("Permission denied to record audio")
                                .setBackgroundColor(R.color.alert)
                                .setIcon(R.drawable.ic_notifications)
                                .setDuration(2000)
                                .show();
                    }
                }

                // other 'case' lines to check for other
                // permissions this app might request
            }
        }


        //Heart and soul of Bella's mind
        @Override
        public void onResults(Bundle data)
        {
            btnSpeak.setImageResource(R.drawable.ic_action_done);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    btnSpeak.setImageResource(R.drawable.ic_action_listen);
                }
            }, 1000);
            ArrayList<String> text = data.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION);
            progressBar.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.INVISIBLE);
            txtText.setVisibility(View.VISIBLE);
            txtText.setText(text.get(0));

            String txt = txtText.getText().toString();
            if(txt.isEmpty()) {
                Toast.makeText(MainActivity.this, "You did not say anything!", Toast.LENGTH_SHORT).show();
            } else if (txt.contains("connect")) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                Intent i = new Intent(MainActivity.this, DeviceList.class);
                startActivity(i,compat.toBundle());
            } else if (txt.contains("machine learning")) {
                tts.speak("I am still learning, check out my neural network",TextToSpeech.QUEUE_FLUSH,null);
                Intent i = new Intent(MainActivity.this, MachineLearning.class);
                startActivity(i);
            } else if(txt.contains("room") || txt.contains("Room")) {
                if(!hardware) {
                    tts.speak("Please, connect to hardware first.", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    headText.setText("Room");
                    if (txt.contains(" on ") && (txt.contains("light 1") || txt.contains("light one"))) {
                        //turn on lights
                        command(000);
                    } else if (txt.contains(" off") && (txt.contains("light 1") || txt.contains("light one"))) {
                        //turn off lights
                        command(001);
                    } else if (txt.contains(" on ") && (txt.contains("light 2") || txt.contains("light two") || txt.contains("light to"))) {
                        //turn off lights
                        command(010);
                    } else if (txt.contains(" off") && (txt.contains("light 2") || txt.contains("light two") || txt.contains("light to"))) {
                        //turn off lights
                        command(011);
                    } else if (txt.contains(" on") && (txt.contains("fan") || txt.contains("fans"))) {
                        //turn on fan
                        command(1000);
                    } else if (txt.contains(" off") && (txt.contains("fan") || txt.contains("fans"))) {
                        //turn off fan
                        command(1001);
                    } else {
                        tts.speak("Sorry, Incorrect information!", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            } else if (txt.contains("kitchen") || txt.contains("Kitchen")) {
                if(!hardware) {
                    tts.speak("Please, connect to hardware first.", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    headText.setText("Kitchen");
                if (txt.contains("status") || txt.contains("enough food") || txt.contains("supplies")) {
                    //kitchen status
                    command(100);
                } else {
                    tts.speak("Sorry, I can only tell the status of container.", TextToSpeech.QUEUE_FLUSH, null);
                }
                }
            } else if (txt.contains("garden") || txt.contains("Garden")) {
                if(!hardware) {
                    tts.speak("Please, connect to hardware first.", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    headText.setText("Garden");
                    if (txt.contains(" on ")) {
                        //turn on sprinklers
                        command(101);
                    } else if (txt.contains("off")) {
                        //turn off sprinklers
                        command(110);
                    } else if (txt.contains("moisture")) {
                        //moisture
                        command(111);
                    } else {
                        tts.speak("Sorry, Incorrect information.", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }  else if (txt.contains("status")&&txt.contains("home")) {
                if(!hardware) {
                    tts.speak("Please, connect to hardware first.", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    command(1111);
                }
            } else if (txt.contains("turn off everything")) {
                if(!hardware) {
                    tts.speak("Please, connect to hardware first.", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    command(1011);
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
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                        Intent i = new Intent (MainActivity.this,AboutActivity.class);
                        startActivity(i,compat.toBundle());
                    }
                }, 2500);
            } else if (txt.contains("weather")) {
                if(isNetworkAvailable()==0) {
                    tts.speak("It seems like internet connection is unavailable so I am unable to fetch weather report", TextToSpeech.QUEUE_FLUSH, null);
                    init();
                } else if(txt.contains("forecast") && (txt.contains("of") || txt.contains("in"))) {
                    if(txt.contains("of")) {
                        c = txt.substring(txt.lastIndexOf(" of ") + 4, txt.length());
                    } else {
                        c = txt.substring(txt.lastIndexOf(" in ") + 4, txt.length());
                    }
                    tts.speak("Fetching weather information.", TextToSpeech.QUEUE_FLUSH, null);
                    Intent im = new Intent(MainActivity.this, WeatherActivity.class);
                    im.putExtra("city",c);
                    startActivity(im);
                } else if(txt.contains("of") || txt.contains("in")) {
                    if(txt.contains("of")) {
                        c = txt.substring(txt.lastIndexOf(" of ") + 4, txt.length());
                    } else {
                        c = txt.substring(txt.lastIndexOf(" in ") + 4, txt.length());
                    }
                    String t="http://api.openweathermap.org/data/2.5/forecast/daily?q={"+c+"}&mode=json&units=metric&cnt=7&appid=8ef3e7bbb7fc9e9bb6f0ab01cba793fa";
                    fetchWeatherDetails(t);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String k = "Today's forecast for"+ c +" is, " + max + " degrees";
                            tts.speak(k, TextToSpeech.QUEUE_FLUSH, null);
                            Intent im = new Intent(MainActivity.this, MaterialWeatherActivity.class);
                            im.putExtra("value",max);
                            startActivity(im);
                        }
                    }, 1000);

                } else {
                    String t="http://api.openweathermap.org/data/2.5/forecast/daily?q={bangalore}&mode=json&units=metric&cnt=7&appid=8ef3e7bbb7fc9e9bb6f0ab01cba793fa";
                    fetchWeatherDetails(t);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String k = "Today's forecast for Bangalore" +" is, " + max + " degrees";
                            tts.speak(k, TextToSpeech.QUEUE_FLUSH, null);
                            Intent im = new Intent(MainActivity.this, MaterialWeatherActivity.class);
                            im.putExtra("value",max);
                            startActivity(im);
                        }
                    }, 1000);


                }
            } else if(txt.contains("alarm")){
                tts.speak("Creating an alarm ", TextToSpeech.QUEUE_FLUSH, null);
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.deskclock");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
                else //in case the app is not found
                {   Intent app_intent;
                    app_intent = new Intent(Intent.ACTION_VIEW);
                    app_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app_intent.setData(Uri.parse("https://play.google.com/store/details?id=" + "com.google.android.deskclock"));
                    startActivity(app_intent);

                }
            }else if(txt.contains("remind")||txt.contains("reminder")){
                tts.speak("Creating a reminder ", TextToSpeech.QUEUE_FLUSH, null);
                if(txt.contains("to")) {
                    Calendar cal = Calendar.getInstance();
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    String s = txt.substring(txt.lastIndexOf(" to ") + 4, txt.length());
                    intent.putExtra("title", s);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", cal.getTimeInMillis());
                    intent.putExtra("allDay", false);
                    intent.putExtra("rrule", "FREQ=DAILY");
                    intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                    startActivity(intent);
                }
                else
                {
                    Calendar cal = Calendar.getInstance();
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.putExtra("title", "Reminder");
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", cal.getTimeInMillis());
                    intent.putExtra("allDay", false);
                    intent.putExtra("rrule", "FREQ=DAILY");
                    intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                    startActivity(intent);
                }
            } else if (txt.contains("watch")) {
                String s = txt.substring(txt.lastIndexOf("watch")+5,txt.length());
                s=s.replace(" ","+");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query="+s)));

            } else if (txt.contains("photo")||txt.contains("capture")||txt.contains("snap")||txt.contains("pic")||txt.contains("picture")) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES));
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(intent, 1);

            } else if((txt.contains("navigate") || txt.contains("take me")) && txt.contains("to")) {
                if(txt.substring(txt.length()-3,txt.length()).equals(" to")) {
                    tts.speak("Please specify a destination address.", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    Log.d("to: ",Integer.toString(txt.lastIndexOf("to+1")));
                    Log.d("to: ",Integer.toString(txt.length()));
                    tts.speak("Starting navigation ", TextToSpeech.QUEUE_FLUSH, null);
                    String s = txt.substring(txt.lastIndexOf(" to ") + 4, txt.length());
                    s = s.replace(" ", "+");
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=" + s);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                }
            } else if (txt.contains("mail") || (txt.contains("email"))) {
                String address = txt.substring(txt.lastIndexOf(" to ")+4,txt.length());
                address = address.replaceAll(" ","");
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.setPackage("com.google.android.gm");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{address});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Body");
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            } else if(txt.contains("add") && txt.contains("list") && txt.contains("to")) {
                tts.speak("Adding to shopping list ", TextToSpeech.QUEUE_FLUSH, null);
                String list = txt.substring(txt.lastIndexOf("add")+4, txt.lastIndexOf("to"));
                String listName = txt.substring(txt.lastIndexOf(" to ")+4,txt.length());
                list = list.replaceAll(" ","\n");
                Intent keepIntent = new Intent(Intent.ACTION_SEND);
                keepIntent.setType("text/plain");
                keepIntent.setPackage("com.google.android.keep");
                keepIntent.putExtra(Intent.EXTRA_SUBJECT, listName);
                keepIntent.putExtra(Intent.EXTRA_TEXT, list);
                try {
                    startActivity(keepIntent);
                } catch (Exception e) {
                    Intent app_intent;
                    app_intent = new Intent(Intent.ACTION_VIEW);
                    app_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app_intent.setData(Uri.parse("https://play.google.com/store/details?id=" + "com.google.android.keep"));
                    startActivity(app_intent);
                }

            } else if (txt.contains("message") || txt.contains("text")) {
                String num = txt.substring(txt.lastIndexOf(" to ")+4,txt.length());
                num = num.replaceAll(" ","");
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address",num);
                smsIntent.putExtra("sms_body","your desired message");
                startActivity(smsIntent);
            } else if (txt.contains("date") && txt.contains("time")) {
                String datetime = DateFormat.getDateTimeInstance().format(new Date());
                tts.speak("It is"+datetime, TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("date")) {
                String date = DateFormat.getDateInstance().format(new Date());
                tts.speak("It is"+date, TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("time ")) {
                String time = DateFormat.getTimeInstance().format(new Date());
                tts.speak("It is"+time, TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("what all can you play") || txt.contains("what all songs can you play") || txt.contains("supported songs")) {
                tts.speak("I can play. help me lose my mind by Disclosure. Lean On by Major Lazor. Closer by Chainsmokers. Ongoing things by 2SYL. We don't talk anymore by Charlie Puth. Soon I will support more songs.", TextToSpeech.QUEUE_FLUSH, null);
                stop();
            } else if(txt.contains("math") || txt.contains("maths") || txt.contains("mathematics")) {
                tts.speak("Pretty good, but I am still trying to learn Bodmas, please bear with me", TextToSpeech.QUEUE_FLUSH, null);
                stop();
            } else if(txt.contains("+") || txt.contains("-") || txt.contains(" x ") || txt.contains("X") || txt.contains("/")) {
                if(txt.contains(".")) {
                    tts.speak("I am still learning to work with decimal numbers.", TextToSpeech.QUEUE_FLUSH, null);
                    stop();
                } else {
                    String input = txt.replaceAll("[^xX+-/0-9]", "");
                    input = input.replace(" ", "");
                    String parsedInteger = "";
                    String operator = "";
                    float aggregate = 0;
                    int flag = 0;


                    for (int i = 0; i < input.length(); i++) {
                        char c = input.charAt(i);
                        if (Character.isDigit(c)) {
                            parsedInteger += c;
                            Log.e("Text", "Ans=" + parsedInteger);
                        }

                        if (!Character.isDigit(c) || i == input.length() - 1) {
                            int parsed = Integer.parseInt(parsedInteger);
                            if (operator.equals("")) {
                                aggregate = parsed;
                                Log.e("Text", "Ans1=" + aggregate);
                            } else {
                                if (operator.equals("+")) {
                                    aggregate += parsed;
                                    Log.e("Text", "Ans2=" + aggregate);
                                } else if (operator.equals("-")) {
                                    aggregate -= parsed;
                                } else if (operator.equals("x") || operator.equals("X")) {
                                    aggregate *= parsed;

                                } else if (operator.equals("/")) {
                                    if (Integer.valueOf(parsed) == 0) {
                                        flag = 1;
                                    } else {
                                        aggregate /= parsed;
                                    }
                                }
                            }
                            parsedInteger = "";
                            operator = "" + c;
                        }
                    }
                    if (flag == 0) {
                        tts.speak("It's " + aggregate, TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        tts.speak("That sounds like a trick question.", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            } else if (txt.contains("play") || txt.contains("song")) {
                if(isNetworkAvailable()==0) {
                    tts.speak("It seems like internet connection is unavailable so I am unable to play songs", TextToSpeech.QUEUE_FLUSH, null);
                }
                else {
                    txt = txt.toLowerCase();
                    if((txt.contains("is this") || txt.contains("is it")) && txt.contains("good")) {
                        if(contexts.get(song).equals("null")) {
                            tts.speak("I don't know if it is a good song", TextToSpeech.QUEUE_FLUSH, null);
                        } else if(contexts.get(song).equals("false")) {
                            tts.speak("I don't think it is a good song", TextToSpeech.QUEUE_FLUSH, null);
                        } else if (contexts.get(song).equals("true")) {
                            tts.speak("I like this song", TextToSpeech.QUEUE_FLUSH, null);
                        } else {
                            tts.speak("You have said something that I did not understand, I will try to learn as I grow up!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    } else if(txt.contains("this is") || txt.contains("it is")) {
                        if(txt.contains("good")) {
                            contexts.put(song,"true");
                            tts.speak("I did feel the same!", TextToSpeech.QUEUE_FLUSH, null);
                        } else if(txt.contains("bad")) {
                            contexts.put(song,"false");
                            tts.speak("Alright, noted!", TextToSpeech.QUEUE_FLUSH, null);
                        } else {
                            tts.speak("You have said something that I did not understand, I will try to learn as I grow up!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    } else if (txt.contains("good song")) {
                        if(txt.contains("major") || txt.contains("Major")) {
                            if(contexts.get("Major Lazor")=="true") {
                                tts.speak("Playing Lean On by Major Lazor", TextToSpeech.QUEUE_FLUSH, null);
                                String path = "https://www.dropbox.com/s/cquqiauh204ml7x/LeanOn.mp3?dl=1";
                                play(path);
                            } else {
                                tts.speak("There are no good songs by Major Lazor", TextToSpeech.QUEUE_FLUSH, null);
                            }

                        } else if(txt.contains("disclosure")) {
                            if(contexts.get("Disclosure")=="true") {
                                tts.speak("Playing help me lose my mind by Disclosure", TextToSpeech.QUEUE_FLUSH, null);
                                String path = "https://www.dropbox.com/s/bbdy28pwg6lwxp5/HelpMeLoseMyMind.mp3?dl=1";
                                play(path);
                            } else {
                                tts.speak("There are no good songs by Major Lazor", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else if(txt.contains("chainsmokers")) {
                            if(contexts.get("Chainsmokers")=="true") {
                                song = "Chainsmokers";
                                tts.speak("Playing Closer by Chainsmokers", TextToSpeech.QUEUE_FLUSH, null);
                                String path = "https://www.dropbox.com/s/x428bu4lv3wd1qj/Closer.mp3?dl=1";
                                play(path);
                            } else {
                            tts.speak("There are no good songs by Chainsmokers", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else if(txt.contains("2SYL") || txt.contains("to s y l")) {
                            if(contexts.get("2SYL")=="true") {
                                song="2SYL";
                                tts.speak("Playing Ongoing things by 2SYL", TextToSpeech.QUEUE_FLUSH, null);
                                String path = "https://www.dropbox.com/s/r7hmbxxlw13nlsf/OngoingThings.mp3?dl=1";
                                play(path);
                            } else {
                                tts.speak("There are no good songs by 2SYL", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else if(txt.contains("Charlie Puth") || txt.contains("charlie puth")) {
                            if(contexts.get("Charlie Puth")=="true") {
                                song="Charlie Puth";
                                tts.speak("Playing We don't talk anymore by Charlie Puth", TextToSpeech.QUEUE_FLUSH, null);
                                String path = "https://www.dropbox.com/s/a1ahxsid403ebj3/lkAnyMore.mp3?dl=1";
                                play(path);
                            } else {
                                tts.speak("There are no good songs by Charlie Puth", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }

                    } else if(txt.contains("help me lose my mind")) {
                        song = "Disclosure";
                        tts.speak("Playing help me lose my mind by Disclosure", TextToSpeech.QUEUE_FLUSH, null);
                        String path = "https://www.dropbox.com/s/bbdy28pwg6lwxp5/HelpMeLoseMyMind.mp3?dl=1";
                        play(path);
                    } else if(txt.contains("lean on")) {
                        song = "Major Lazor";
                        tts.speak("Playing Lean On by Major Lazor", TextToSpeech.QUEUE_FLUSH, null);
                        String path = "https://www.dropbox.com/s/cquqiauh204ml7x/LeanOn.mp3?dl=1";
                        play(path);
                    } else if(txt.contains("closer")) {
                        song = "Chainsmokers";
                        tts.speak("Playing Closer by Chainsmokers", TextToSpeech.QUEUE_FLUSH, null);
                        String path = "https://www.dropbox.com/s/x428bu4lv3wd1qj/Closer.mp3?dl=1";
                        play(path);
                    } else if(txt.contains("on going things") || txt.contains("ongoing things")) {
                        song="2SYL";
                        tts.speak("Playing Ongoing things by 2SYL", TextToSpeech.QUEUE_FLUSH, null);
                        String path = "https://www.dropbox.com/s/r7hmbxxlw13nlsf/OngoingThings.mp3?dl=1";
                        play(path);
                    } else if(txt.contains("we don't talk anymore")) {
                        song="Charlie Puth";
                        tts.speak("Playing We don't talk anymore by Charlie Puth", TextToSpeech.QUEUE_FLUSH, null);
                        String path = "https://www.dropbox.com/s/a1ahxsid403ebj3/lkAnyMore.mp3?dl=1";
                        play(path);
                    } else if(txt.contains("play") && (txt.contains("random")||txt.contains("any")||txt.contains("some"))) {
                        String[] songs = { "Disclosure", "Major Lazor", "Chainsmokers", "2SYL","Charlie Puth"};
                        song = songs[(int) (Math.random() * songs.length)];
                        if(song.equals("Disclosure")) {
                            tts.speak("Playing help me lose my mind by Disclosure", TextToSpeech.QUEUE_FLUSH, null);
                            String path = "https://www.dropbox.com/s/bbdy28pwg6lwxp5/HelpMeLoseMyMind.mp3?dl=1";
                            play(path);
                        } else if(song.equals("Major Lazor")) {
                            tts.speak("Playing Lean On by Major Lazor", TextToSpeech.QUEUE_FLUSH, null);
                            String path = "https://www.dropbox.com/s/cquqiauh204ml7x/LeanOn.mp3?dl=1";
                            play(path);
                        } else if(song.equals("Chainsmokers")) {
                            tts.speak("Playing Closer by Chainsmokers", TextToSpeech.QUEUE_FLUSH, null);
                            String path = "https://www.dropbox.com/s/x428bu4lv3wd1qj/Closer.mp3?dl=1";
                            play(path);
                        } else if(song.equals("2SYL")) {
                            tts.speak("Playing Ongoing things by 2SYL", TextToSpeech.QUEUE_FLUSH, null);
                            String path = "https://www.dropbox.com/s/r7hmbxxlw13nlsf/OngoingThings.mp3?dl=1";
                            play(path);
                        } else if(song.equals("Charlie Puth")) {
                            tts.speak("Playing We don't talk anymore by Charlie Puth", TextToSpeech.QUEUE_FLUSH, null);
                            String path = "https://www.dropbox.com/s/a1ahxsid403ebj3/lkAnyMore.mp3?dl=1";
                            play(path);
                        }
                    } else {
                            if(!(txt.length()==4)) {
                                String s = txt.substring(txt.lastIndexOf("song") + 4, txt.length());
                                String c = "Searching for the song " + s;
                                tts.speak(c, TextToSpeech.QUEUE_FLUSH, null);
                                s = s.replace(" ", "+");
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query="+s)));
                            } else {
                                tts.speak("Please, specify the song name", TextToSpeech.QUEUE_FLUSH, null);
                                stop();
                            }
                    }
                }
            } else if (txt.contains("feeling") && txt.contains("you")) {
                tts.speak("Never been better!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("your") && txt.contains("food")) {
                 tts.speak("I don't eat much, but when I do, I take megabytes!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("your") && txt.contains("car")) {
                 tts.speak("I like fast ones!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("your") && (txt.contains("color") || txt.contains("colour"))) {
                 tts.speak("The primary color of this app!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("you") && (txt.contains("boy")||txt.contains("girl")||txt.contains("male")||txt.contains("female"))) {
                 tts.speak("I'm all inclusive.", TextToSpeech.QUEUE_FLUSH, null);
            } else if (((txt.contains("I'm") || txt.contains("I am")) && txt.contains("bored")) || txt.contains("joke")) {
                 String[] jokes = { "Why do fish live in salt water? Because, pepper water makes them sneeze."
                         , "Did you hear about the new anti-gravity book? Apparently, you can't put it down."
                         , "What do you call a bear with no teeth? A gummy bear"
                         , "Why can't you trust an atom? Because they make up literally everything."
                         , "What do you call a can opener that doesn't work? A can't opener."};
                 String joke = jokes[(int) (Math.random() * jokes.length)];
                 tts.speak(joke, TextToSpeech.QUEUE_FLUSH, null);
            } else if ((txt.contains("you") || txt.contains("your")) && (txt.contains("born") || txt.contains("birthday"))) {
                tts.speak("I came to life on 5th of February 2017. I had a great day!", TextToSpeech.QUEUE_FLUSH, null);
                stop();
            } else if (txt.contains("Bella") || txt.contains("bella") || txt.contentEquals("who are you")) {
                tts.speak("Greetings! human, I am Bella! An assistant powered by Artificial Intelligence and machine learning.", TextToSpeech.QUEUE_FLUSH, null);
                stop();
            } else if(txt.contains("energy")) {
                String e = "Energy used is "+energyUsed+" units.";
                tts.speak(e, TextToSpeech.QUEUE_FLUSH, null);
            } else if(txt.contains("garbage")) {
                if(txt.contains("status")) {
                    tts.speak("Garbage status received.",TextToSpeech.QUEUE_FLUSH,null) ;
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent i = new Intent(MainActivity.this, GarbageActivity.class);
                    startActivity(i,compat.toBundle());
                } else if(txt.contains("collection") || txt.contains("collect")){
                    try {
                        Intent i = new Intent("android.intent.action.MAIN");
                        i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                        i.addCategory("android.intent.category.LAUNCHER");
                        i.setData(Uri.parse("https://bella-assistant.github.io/Bella-Garbage"));
                        startActivity(i);
                    }
                    catch(ActivityNotFoundException e) {
                        // Chrome is not installed
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bella-assistant.github.io/Bella-Garbage"));
                        startActivity(i);
                    }
                }
            } else if((txt.contains("Power Grid") || txt.contains("power grid")) && txt.contains("status")) {
                if(txt.contains("house")) {
                    link = 1;
                    tts.speak("Power grid status received.", TextToSpeech.QUEUE_FLUSH,null);
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent i = new Intent(MainActivity.this, PowergridActivity.class);
                    i.putExtra("link",link);
                    startActivity(i,compat.toBundle());
                } else if(txt.contains("industry")) {
                    link = 2;
                    tts.speak("Power grid status received.", TextToSpeech.QUEUE_FLUSH,null);
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent i = new Intent(MainActivity.this, PowergridActivity.class);
                    i.putExtra("link",link);
                    startActivity(i,compat.toBundle());
                }
                else if (txt.contains("farmer")){
                    link = 3;
                    tts.speak("Power grid status received.", TextToSpeech.QUEUE_FLUSH,null);
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent i = new Intent(MainActivity.this, PowergridActivity.class);
                    i.putExtra("link",link);
                    startActivity(i,compat.toBundle());
                }
                else
                    tts.speak("Power grid status not available.", TextToSpeech.QUEUE_FLUSH,null);

            } else if(txt.contains("grow") || txt.contains("crop") || txt.contains("crops")) {
                    city=txt.substring(txt.lastIndexOf(" in ")+4, txt.length());
                    info = "https://raw.githubusercontent.com/Bella-Assistant/Bella-Jsons/master/soil.json";
                    fetchSoilDetails(info);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tts.speak("I may suggest you to grow, "+temp_crop+","+temp_crop2+" according to present soil status", TextToSpeech.QUEUE_FLUSH,null);
                        }
                    }, 1000);
                    final Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                            Intent i = new Intent(MainActivity.this, SoilActivity.class);
                            startActivity(i,compat.toBundle());
                        }
                    }, 3000);



            } else if((txt.contains("status") || txt.contains("condition")) && (txt.contains("North Carolina") || txt.contains("Miami")) || txt.contains("Hawaii") || txt.contains("California") || txt.contains("location")) {
                int l=5;
                if(txt.contains("North Carolina")) {
                    l = 3;
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent i = new Intent(MainActivity.this, DisasterActivity.class);
                    i.putExtra("l",l);
                    startActivity(i,compat.toBundle());
                    tts.speak("Warning! Flood is approaching, please move to a safer location",TextToSpeech.QUEUE_FLUSH, null);
                } else if (txt.contains("Hawaii")) {
                    l = 4;
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent i = new Intent(MainActivity.this, DisasterActivity.class);
                    i.putExtra("l",l);
                    startActivity(i,compat.toBundle());
                    tts.speak("Warning! Seismic activities detected, please move to a safer location",TextToSpeech.QUEUE_FLUSH, null);
                } else if (txt.contains("Miami")) {
                    l = 2;
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent i = new Intent(MainActivity.this, DisasterActivity.class);
                    i.putExtra("l",l);
                    startActivity(i,compat.toBundle());
                    tts.speak("Warning! Heavy rain and fast moving winds approaching, brace yourself",TextToSpeech.QUEUE_FLUSH, null);
                } else if (txt.contains("California")) {
                    l = 1;
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent i = new Intent(MainActivity.this, DisasterActivity.class);
                    i.putExtra("l",l);
                    startActivity(i,compat.toBundle());
                    tts.speak("Warning! High tides are expected, please move to a safer location",TextToSpeech.QUEUE_FLUSH, null);
                }
                else {
                    tts.speak("Sorry !! Data is not available for this city",TextToSpeech.QUEUE_FLUSH, null);
                }

            } else if (txt.contains("exit") && txt.contains("app")){
                tts.speak("Have a good day", TextToSpeech.QUEUE_FLUSH, null);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        System.exit(0);
                    }
                }, 1000000);
            } else{
                if (txt.contains("news")) {
                    if (isNetworkAvailable() == 0) {

                        tts.speak("Internet Connection not available. Please Connect to Internet", TextToSpeech.QUEUE_FLUSH, null);
                        init();
                    } else {
                        fetchNewsDetails();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            fetchBusinessDetails();
                            }
                        },1000);
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fetchSportsDetails();
                            }
                        },2000);
                        tts.speak("Fetching News, please wait!", TextToSpeech.QUEUE_FLUSH, null);
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                                Intent i = new Intent(MainActivity.this, NewsActivity.class);
                                startActivity(i,compat.toBundle());
                                stop();
                            }
                        },3000);
                    }
                } else if (txt.contentEquals("hey") || txt.contentEquals("hi") || txt.contentEquals("hello")) {
                    tts.speak("Greetings! Human, I am Bella! An assistant powered by Artificial Intelligence and machine learning.", TextToSpeech.QUEUE_FLUSH, null);
                    stop();
                } else if ((txt.contains("hey") || txt.contains("hi") || txt.contains("hello")) && ((!txt.contains("bella") || !txt.contains("Bella")))) {
                    tts.speak("Sorry, Were you talking to me? You can call me bella", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    //tts.speak("You have said something that I did not understand, I will try to learn as I grow up!", TextToSpeech.QUEUE_FLUSH, null);
                    if(txt.contains("search")){
                        String s = txt.substring(txt.lastIndexOf("search")+6, txt.length());
                        s=s.replace(" ", "+");
                        s="https://www.google.co.in/search?q="+s;
                        tts.speak("The web has returned following result", TextToSpeech.QUEUE_FLUSH, null);
                        new FinestWebView.Builder(MainActivity.this).titleDefault("Search")
                                .toolbarScrollFlags(0)
                                .titleColorRes(R.color.finestWhite)
                                .statusBarColorRes(R.color.colorPrimaryDark)
                                .toolbarColorRes(R.color.colorPrimary)
                                .iconDefaultColorRes(R.color.finestWhite)
                                .progressBarColorRes(R.color.finestWhite)
                                .menuSelector(R.drawable.selector_light_theme)
                                .dividerHeight(0)
                                .webViewBuiltInZoomControls(true)
                                .webViewDisplayZoomControls(true)
                                .webViewJavaScriptEnabled(true)
                                .showSwipeRefreshLayout(true)
                                .gradientDivider(false)
                                .setCustomAnimations(R.anim.fade_in_medium, R.anim.fade_out_medium, R.anim.fade_in_medium, R.anim.fade_out_medium)
                                .disableIconBack(false)
                                .disableIconClose(false)
                                .disableIconForward(false)
                                .disableIconMenu(false)
                                .show(s);
                    } else{
                        String s = txt;
                        s=s.replace(" ", "+");
                        s="https://www.google.co.in/search?q="+s;
                        tts.speak("The web has returned following result", TextToSpeech.QUEUE_FLUSH, null);
                        new FinestWebView.Builder(MainActivity.this).titleDefault("Search")
                                .toolbarScrollFlags(0)
                                .titleColorRes(R.color.finestWhite)
                                .statusBarColorRes(R.color.colorPrimaryDark)
                                .toolbarColorRes(R.color.colorPrimary)
                                .iconDefaultColorRes(R.color.finestWhite)
                                .progressBarColorRes(R.color.finestWhite)
                                .menuSelector(R.drawable.selector_light_theme)
                                .dividerHeight(0)
                                .webViewBuiltInZoomControls(true)
                                .webViewDisplayZoomControls(true)
                                .webViewJavaScriptEnabled(true)
                                .showSwipeRefreshLayout(true)
                                .gradientDivider(false)
                                //                    .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                                .setCustomAnimations(R.anim.fade_in_medium, R.anim.fade_out_medium, R.anim.fade_in_medium, R.anim.fade_out_medium)
                                .disableIconBack(false)
                                .disableIconClose(false)
                                .disableIconForward(false)
                                .disableIconMenu(false)
                                .show(s);
                    }
                }
            }
        }

        //I don't know what this does, who cares!
        @Override
        public void onRmsChanged(float arg0)
        {
        }

        //This plays song using mediaplayer class
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



        //This handles menu
        public void showPopup(View v) {
            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_main, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                @Override
                public boolean onMenuItemClick(MenuItem item){
                    int id = item.getItemId();
                if (id == R.id.action_settings) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                    startActivity(intent,compat.toBundle());
                    return true;
                } else if (id == R.id.setting_settings) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                    startActivity(intent,compat.toBundle());
                    return true;
                } else if (id == R.id.help_settings) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent intent = new Intent(MainActivity.this,SuggestionActivity.class);
                    startActivity(intent,compat.toBundle());
                    return true;
                } else if (id == R.id.connect_settings) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent intent = new Intent(MainActivity.this,DeviceList.class);
                    startActivity(intent,compat.toBundle());
                    return true;
                } else if (id == R.id.update_settings) {
                    checkForUpdate();
                    return true;
                }
                    return true;
            }
        });
            popup.show();
        }

        //This part of code checks if network is available
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

        //This part of code stops tts on long press
        public void stop() {
            btnSpeak.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    tts.stop();
                    return true;
                }
            });
        }

        private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
        {
            private boolean ConnectSuccess = true; //if it's here, it's almost connected

            @Override
            protected void onPreExecute()
            {
                Alerter.create(MainActivity.this)
                        .setText("Connecting...")
                        .setIcon(R.drawable.ic_notifications)
                        .setBackgroundColor(R.color.alert)
                        .show();
                //txtText.setText("Connecting...");  //show a progress dialog
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
                        mmInputStream = btSocket.getInputStream();
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
                    Alerter.create(MainActivity.this)
                            .setTitle("Connection Failed")
                            .setText("Is it a SPP Bluetooth? Try again.")
                            .setIcon(R.drawable.ic_notifications)
                            .setBackgroundColor(R.color.alert)
                            .show();
                    txtText.setText("");
                }
                else
                {
                    Alerter.create(MainActivity.this)
                            .setTitle("Connected")
                            .setIcon(R.drawable.ic_face)
                            .setBackgroundColor(R.color.alert)
                            .show();
                    txtText.setText("");
                    isBtConnected = true;
                }
            }
        }

        //This part of code prints a string using a material alerter
        private void msg(String s)
        {
            Alerter.create(MainActivity.this)
                    .setText(s)
                    .setIcon(R.drawable.ic_notifications)
                    .setBackgroundColor(R.color.alert)
                    .show();
        }

        //This part of code receives stream of data from hardware using InputStream
        void beginListenForData()
        {
            final Handler handler = new Handler();
            final byte delimiter = 58; //This is the ASCII code for a colon character

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            workerThread = new Thread(new Runnable()
            {
                public void run()
                {
                    while(!Thread.currentThread().isInterrupted() && !stopWorker)
                    {
                        try
                        {
                            int bytesAvailable = mmInputStream.available();
                            if(bytesAvailable > 0)
                            {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for(int i=0;i<bytesAvailable;i++)
                                {
                                    byte b = packetBytes[i];
                                    if(b == delimiter)
                                    {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                       // Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();

                                        handler.post(new Runnable()
                                        {
                                            public void run()
                                            {
                                               //do your action based on recieved data
                                               // Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                                                // tts.speak("Status recieved", TextToSpeech.QUEUE_FLUSH, null);
                                                statuscheck(data);
                                            }
                                        });
                                    }
                                    else
                                    {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        }
                        catch (IOException ex)
                        {   ex.printStackTrace();
                            stopWorker = true;
                        }
                    }
                }
            });

            workerThread.start();
        }


        //This part of code sends the respective command to hardware via bluetooth socket
        private void command(int i)
        {
            if (btSocket!=null)
            {
                if (i == 000) {
                    try
                    {
                        btSocket.getOutputStream().write("RL1O:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if (i == 001) {
                    try
                    {
                        btSocket.getOutputStream().write("RL1F:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if (i == 010) {
                    try
                    {
                        btSocket.getOutputStream().write("RL2O:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if (i == 011) {
                    try
                    {
                        btSocket.getOutputStream().write("RL2F:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if (i == 100) {
                    try
                    {
                        btSocket.getOutputStream().write("KS:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if (i == 101) {
                    try
                    {
                        btSocket.getOutputStream().write("GSO:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if (i == 110) {
                    try
                    {
                        btSocket.getOutputStream().write("GSF:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if (i == 111) {
                    try
                    {
                        btSocket.getOutputStream().write("GSS:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if(i == 1000) {
                    try
                    {
                        btSocket.getOutputStream().write("FT:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if(i == 1001) {
                    try
                    {
                        btSocket.getOutputStream().write("FF:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if (i == 1111) {
                    try
                    {
                        btSocket.getOutputStream().write("X:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                } else if (i == 1011) {
                    try
                    {
                        btSocket.getOutputStream().write("Z:".toString().getBytes());
                        beginListenForData();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }
        }
        private void Disconnect()
        {
            if (btSocket!=null) //If the btSocket is busy
            {
                try
                {   stopWorker=true;
                    btSocket.close(); //close connection
                    mmInputStream.close();

                }
                catch (IOException e)
                { msg("Error");}
            }
            finish(); //return to the first layout
        }

        public void statuscheck(String s) {
            s = s.replaceAll("[\u0000-\u001f]", "");

            if (s.contains("T1")) {
                Log.d("s: ",s);
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("Turned On light 1 in room", TextToSpeech.QUEUE_FLUSH, null);
                t1 =  new CountDownTimer(30000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        energyUsed+=1;
                        Log.d("timer: ",Integer.toString(energyUsed));
                        if(energyUsed>=30)
                            notif("High Energy Usage","Please Switch off light 1 to save energy");
                    }

                    @Override
                    public void onFinish() {
                            notif("High Energy Usage","Please Switch off light 1 to save energy");
                    }
                }.start();
            } else if (s.contains("T2")) {
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("Turned On light 2 in room", TextToSpeech.QUEUE_FLUSH, null);
                t2 =  new CountDownTimer(30000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        energyUsed+=1;
                        Log.d("timer: ",Integer.toString(energyUsed));
                        if(energyUsed>=30)
                            notif("High Energy Usage","Please Switch off light 2 to save energy");
                    }

                    @Override
                    public void onFinish() {
                            notif("High Energy Usage","Please Switch off light 2 to save energy");
                    }
                }.start();
            } else if (s.contains("T3")) {
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("Turned On sprinklers in garden", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("T4")) {
                try {
                    t1.cancel();
                } catch (NullPointerException ne) {
                    Log.e("NE","fuck this shit!");
                }
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("Turned Off light 1 in room", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("T5")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("Turned Off light 2 in room", TextToSpeech.QUEUE_FLUSH, null);
                try {
                    t2.cancel();
                } catch (NullPointerException ne) {
                    Log.e("NE","fuck this shit!");
                }
            } else if (s.contains("F1")) {
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("light 1 already turned ON in room", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("F2")) {
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("light 2 already turned ON in room", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("F3")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("Warning! the soil is too wet to be watered", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("F4")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("light 1 already turned OFF in room", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("F5")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("light 2 already turned OFF in room", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("F6")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("Please! allow me to water your plants", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("F7")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("turned off sprinkler", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("S1")) {
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("Turned ON fan!", TextToSpeech.QUEUE_FLUSH, null);
                t3 =  new CountDownTimer(30000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        energyUsed+=1;
                        Log.d("timer: ",Integer.toString(energyUsed));
                        if(energyUsed>=30)
                            notif("High Energy Usage","Please Switch off light 2 to save energy");
                    }

                    @Override
                    public void onFinish() {
                            notif("High Energy Usage","Please Switch off fan to save energy");
                    }
                }.start();
            } else if (s.contains("S2")) {
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("fan already turned ON!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("S3")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("Turned off fan!", TextToSpeech.QUEUE_FLUSH, null);
                try {
                    t3.cancel();
                } catch (NullPointerException ne) {
                    Log.e("NE","fuck this shit!");
                }
            } else if (s.contains("S4")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("fan already turned off!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("ZT")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("Everything in home turned off!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("ZF")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("There is nothing to be turned off!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.startsWith("C1")) {
                Log.d("s:",s);
                String c = s.substring(2,4);
                String e = s.substring(4,5);
                String f = s.substring(7,9);
                String g = s.substring(9,10);
                if(e.equals("F") && g.equals("F")) {
                    String d = "Container 1 and Container 2 is low on surplus with just, " + c + "and" + f + " percent filled respectively";
                    status.setSwitchColor(getResources().getColor(R.color.red));
                    status.setDirection(StickySwitch.Direction.LEFT);
                    tts.speak(d, TextToSpeech.QUEUE_FLUSH, null);
                } else if(e.equals("F") && g.equals("T")) {
                    String d = "Container 1 is low on surplus with, " + c + " percent filled and container 2 is "+f+" percent filled";
                    status.setSwitchColor(getResources().getColor(R.color.red));
                    status.setDirection(StickySwitch.Direction.LEFT);
                    tts.speak(d, TextToSpeech.QUEUE_FLUSH, null);
                } else if(e.equals("T") && g.equals("F")) {
                    String d = "Container 1 is "+c+"percent filled and Container 2 is low on surplus with just, " + f + " percent filled";
                    status.setSwitchColor(getResources().getColor(R.color.red));
                    status.setDirection(StickySwitch.Direction.LEFT);
                    tts.speak(d, TextToSpeech.QUEUE_FLUSH, null);
                } else if(e.equals("T") && g.equals("T")) {
                    String d = "Container 1 and Container 2  are, " + c + "and"+ f +" percent filled";
                    status.setSwitchColor(getResources().getColor(R.color.red));
                    status.setDirection(StickySwitch.Direction.LEFT);
                    tts.speak(d, TextToSpeech.QUEUE_FLUSH, null);
                }
            } else if (s.startsWith("C2")) {

            } else if (s.startsWith("M1")) {
                String c = s.substring(2,4);
                String d = "The garden contains "+c+" percent moisture";
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak(d, TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.length()>10){
                Log.d("String: ",s);


            } else  if (s.length()>5){
                Log.d("string: ",s);
                tts.speak("Status Received!", TextToSpeech.QUEUE_FLUSH, null);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                Intent intent = new Intent(MainActivity.this,StatusActivity.class);
                intent.putExtra("status", s);
                startActivity(intent,compat.toBundle());

            }
        }

        private void notif(String title, String notif) {
            int notifId=1;
            Intent resultIntent = new Intent(this, MainActivity.class);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.mipmap.bella_launcher);
            mBuilder.setContentTitle("Warning! "+title);
            mBuilder.setContentText(notif);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            NotificationManager mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setContentIntent(resultPendingIntent);
            mNM.notify(notifId,mBuilder.build());
        }

        public void fetchSoilDetails(String s) {
            final String TAG = SoilActivity.class.getSimpleName();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, s, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {
                        // Parsing json object response
                        // response will be a json object
                        /*cityName = response.getString("c_name");
                        Log.d("City: ", cityName);
                        type = response.getString("s_type");
                        farm = response.getString("s_farm");
                        crop = response.getString("s_crop1");
                        cover = response.getString("s_cover");
                        ssnS = response.getString("ssn_s");
                        ssnE = response.getString("ssn_e");*/
                        JSONArray type = response.getJSONArray(city);
                        soil_type = type.getString(0);


                        JSONObject soil = response.getJSONObject(soil_type).getJSONObject("crop");
                        JSONArray name = soil.getJSONArray("type");
                        JSONObject obj = name.getJSONObject(0);
                        type_name = obj.getString("name");
                        JSONArray tmp = obj.getJSONArray("seasons");
                        start_month = tmp.getString(0);
                        end_month = tmp.getString(1);
                        JSONArray tmp2 = obj.getJSONArray("crops");
                        temp_crop= tmp2.getString(0);
                        temp_crop2=tmp2.getString(1);
                        String soil_type2 = type.getString(1);
                        if(!soil_type2.equals(" "))
                        {
                            temp_crop2= response.getJSONObject(soil_type2).getJSONObject("crop").getJSONArray("type").getJSONObject(0).getJSONArray("crops").getString(0);
                        }
                        Log.e(type_name,start_month);
                        Log.e(end_month,temp_crop);
                        Log.e(temp_crop2,"ashish");





                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error", e.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e("error", e.getMessage());
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


        public void fetchWeatherDetails(String s) {
            final String TAG = SoilActivity.class.getSimpleName();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, s, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {
                        // Parsing json object response
                        // response will be a json object
                        list= response.getJSONArray("list");

                        JSONObject temp= list.getJSONObject(0);
                        JSONObject temp1= temp.getJSONObject("temp");
                        max= temp1.getString("max");
                        min= temp1.getString("min");
                        Log.e(max,min);


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
                        for(int i=0;i<3;i++) {
                            obj[i] = new MainActivity.news();
                            JSONObject temp = articles.getJSONObject(i);
                            obj[i].title=temp.getString("title");
                            obj[i].url=temp.getString("url");
                            obj[i].url_img=temp.getString("urlToImage");
                            obj[i].desc=temp.getString("description");
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

        public void fetchSportsDetails() {
            final String TAG = NewsActivity.class.getSimpleName();
            String s = "https://newsapi.org/v1/articles?source=bbc-sport&sortBy=top&apiKey=f27d729ed4ea4d4e8b17db1bb5df031a";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, s, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {
                        // Parsing json object response
                        // response will be a json object
                        articles= response.getJSONArray("articles");
                        for(int i=3,j=0;i<6;i++,j++) {
                            obj[i] = new MainActivity.news();
                            JSONObject temp = articles.getJSONObject(j);
                            obj[i].title=temp.getString("title");
                            obj[i].url=temp.getString("url");
                            obj[i].url_img=temp.getString("urlToImage");
                            obj[i].desc=temp.getString("description");
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

        public void fetchBusinessDetails() {
            final String TAG = NewsActivity.class.getSimpleName();
            String s = "https://newsapi.org/v1/articles?source=business-insider&sortBy=top&apiKey=f27d729ed4ea4d4e8b17db1bb5df031a";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, s, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {
                        // Parsing json object response
                        // response will be a json object
                        articles= response.getJSONArray("articles");
                        for(int i=6,j=0;i<9;i++,j++) {
                            obj[i] = new MainActivity.news();
                            JSONObject temp = articles.getJSONObject(j);
                            obj[i].title=temp.getString("title");
                            obj[i].url=temp.getString("url");
                            obj[i].url_img=temp.getString("urlToImage");
                            obj[i].desc=temp.getString("description");
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
