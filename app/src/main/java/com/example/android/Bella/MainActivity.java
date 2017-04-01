    package com.example.android.Bella;

    import android.Manifest;
    import android.animation.Animator;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.bluetooth.BluetoothAdapter;
    import android.bluetooth.BluetoothDevice;
    import android.bluetooth.BluetoothSocket;
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

    import java.io.File;
    import java.io.IOException;
    import java.io.InputStream;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.HashMap;
    import java.util.Locale;
    import android.media.MediaPlayer;
    import java.text.DateFormat;
    import java.util.Date;
    import java.util.Timer;
    import java.util.TimerTask;
    import java.util.UUID;

    import android.speech.RecognizerIntent;
    import android.speech.tts.TextToSpeech;
    import android.content.ActivityNotFoundException;
    import android.content.Intent;
    import android.support.v7.widget.PopupMenu;
    import android.text.SpannableString;
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

    import com.getkeepsafe.taptargetview.TapTarget;
    import com.getkeepsafe.taptargetview.TapTargetSequence;
    import com.getkeepsafe.taptargetview.TapTargetView;
    import com.github.nisrulz.sensey.Sensey;
    import com.github.nisrulz.sensey.ShakeDetector;
    import com.tapadoo.alerter.Alerter;

    import org.jetbrains.annotations.NotNull;

    import io.ghyeok.stickyswitch.widget.StickySwitch;

    public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, RecognitionListener {

        protected static final int RESULT_SPEECH = 1;
        private ProgressBar progressBar;
        public ImageButton btnSpeak;
        private TextView txtText;
        public TextView headText;
        public StickySwitch status;
        private Button b1;
        TextToSpeech tts;
        boolean doubleBackToExitPressedOnce;
        TransitionInflater tf;

        //bluetooth receive initializer
        InputStream mmInputStream;
        volatile boolean stopWorker;
        Thread workerThread;

        byte[] readBuffer;
        int readBufferPosition;
        public String updateStatus;

        private SpeechRecognizer speech;
        public Vibrator myVib;
        private com.tuyenmonkey.mkloader.MKLoader loader;
        public static boolean hardware = false;
        String address = null;
        String song = null;
        String query = null;
        boolean queryStatus = false;
        boolean shakeStatus = false;

        private Timer timer;
        private TimerTask timerTask;

        BluetoothAdapter myBluetooth = null;
        BluetoothSocket btSocket = null;
        private boolean isBtConnected = false;
        //SPP UUID. Look for it
        static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        HashMap<String,String> contexts = new HashMap<>();
        File file = new File("song.txt");

        //Notification
        NotificationCompat.Builder mBuilder;
        CountDownTimer c1;

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Sensey.getInstance().init(this);

            /*try {
                tf = TransitionInflater.from(this);
                Transition t = tf.inflateTransition(R.transition.transactivity);
                getWindow().setExitTransition(t);
            } catch (Exception e) {
                Log.d("Exit transition", "fuck this shit");
            }*/

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

            updateStatus = "FFF";
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

            txtText.setHint("Shake to get suggestions!");
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    txtText.setText("How may I help you?");
                }
            }, 3000);

            final String[] queries = { "How's the weather today?", "Play me some song", "What is 15 + 20?", "Fetch me latest news","Connect to my home","Turn On light1","Turn On Sprinklers","Moisture status",
                    "What date is it today?","What time is it now?","Is this is a good song?","When is your birthday?","What is 50 * 12","Play the song Closer"};
            final boolean[] isSelected = new boolean[queries.length];
            for (int i = 0; i< isSelected.length;i++)
            {
                isSelected[i] = false;
            }
            final ShakeDetector.ShakeListener shakeListener=new ShakeDetector.ShakeListener() {
                @Override public void onShakeDetected() {
                    shakeStatus = true;
                }

                @Override public void onShakeStopped() {
                        txtText.setText(null);
                        txtText.setHint("You can ask me the following!");
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (!queryStatus) {
                                            int pos = (int) Math.random()*queries.length;
                                            if (!(isSelected[pos]))
                                            {
                                                query = queries[pos];
                                                isSelected[pos] = true;
                                                txtText.setHint(query);
                                            }
                                        }
                                    }
                                });
                            }
                        };
                        timer.schedule(timerTask, 2000, 3000);
                }
            };

            Sensey.getInstance().startShakeDetection(10, 2000, shakeListener);

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
                        Sensey.getInstance().stopShakeDetection(shakeListener);
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


        @Override
        protected void onDestroy() {
            if(tts != null) {

                tts.stop();
                tts.shutdown();
                speech.destroy();

            }
            super.onDestroy();
            Sensey.getInstance().stop();
            if(shakeStatus) {
                timer.purge();
            }
            Disconnect();
        }

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
        @Override
        public void onBeginningOfSpeech()
        {
            btnSpeak.setImageResource(R.drawable.ic_listen);
        }

        @Override
        public void onBufferReceived(byte[] arg0)
        {
            btnSpeak.setImageResource(R.drawable.ic_listen);
        }

        @Override
        public void onEndOfSpeech()
        {
            btnSpeak.setImageResource(R.drawable.ic_action_voice);
            progressBar.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.INVISIBLE);
        }

        public void init(){
            progressBar.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.INVISIBLE);
            btnSpeak.setImageResource(R.drawable.ic_action_voice);
            txtText.setText(null);
            txtText.setHint("How may I help You?");
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


        @Override
        public void onResults(Bundle data)
        {
            btnSpeak.setImageResource(R.drawable.ic_action_done);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    btnSpeak.setImageResource(R.drawable.ic_action_voice);
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
            } else if(txt.contains("room") || txt.contains("Room")) {
                if(!checkBT()) {
                    tts.speak("Please, connect to hardware first.", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    headText.setText("Room");
                    if (txt.contains(" on ") && (txt.contains("light 1") || txt.contains("light one"))) {
                        //turn on lights
                        command(000);
                    } else if (txt.contains(" off") && (txt.contains("light 1") || txt.contains("light one"))) {
                        //turn off lights
                        command(001);
                        status.setDirection(StickySwitch.Direction.LEFT);
                    } else if (txt.contains(" on ") && (txt.contains("light 2") || txt.contains("light two") || txt.contains("light to"))) {
                        //turn off lights
                        command(010);
                    } else if (txt.contains(" off") && (txt.contains("light 2") || txt.contains("light two") || txt.contains("light to"))) {
                        //turn off lights
                        command(011);
                    } else {
                        tts.speak("Sorry, Incorrect information!", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            } else if (txt.contains("kitchen") || txt.contains("Kitchen")) {
                if(!checkBT()) {
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
                if(!checkBT()) {
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
            }  else if (txt.contains("status")) {
                command(1111);
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
            } else if (txt.contains("feeling")) {
                tts.speak("Never been better!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("weather")) {
                if(isNetworkAvailable()==0) {
                    tts.speak("It seems like internet connection is unavailable so I am unable to fetch weather report", TextToSpeech.QUEUE_FLUSH, null);
                    init();
                }
                else {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    tts.speak(" ", TextToSpeech.QUEUE_FLUSH, null); // Initialize tts engine
                    Intent im = new Intent(MainActivity.this, WeatherActivity.class);
                    startActivity(im,compat.toBundle());
                    String s = getIntent().getStringExtra("EXTRA");
                    if(s==null) {
                        tts.speak("Fetching weather information.", TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        tts.speak("Today's forecast for Bengaluru is " + s + " degrees", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    Intent in = new Intent(MainActivity.this, WeatherActivity2.class);
                    startActivity(in);
                }
            } else if(txt.contains("alarm")){
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

            } else if(txt.contains("navigate")) {
                String s = txt.substring(txt.lastIndexOf(" to ")+4, txt.length());
                s=s.replace(" ","+");
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+s);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);

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
            } else if(txt.contains("+") || txt.contains("-") || txt.contains("x") || txt.contains("X") || txt.contains("/")) {
                String input = txt.replaceAll("[^xX+-/0-9]","");
                input = input.replace(" ","");
                String parsedInteger = "";
                String operator = "";
                int aggregate=0;
                int flag =0;


                for (int i = 0; i< input.length(); i++) {
                    char c = input.charAt(i);
                    if(Character.isDigit(c)) {
                        parsedInteger += c;
                        Log.e("Text","Ans="+parsedInteger);
                    }

                    if(!Character.isDigit(c) || i == input.length()-1) {
                        int parsed = Integer.parseInt(parsedInteger);
                        if(operator.equals("")) {
                            aggregate = parsed;
                            Log.e("Text","Ans1="+aggregate);
                        } else {
                            if(operator.equals("+")) {
                                aggregate += parsed;
                                Log.e("Text","Ans2="+aggregate);
                            } else if(operator.equals("-")) {
                                aggregate -= parsed;
                            } else if(operator.equals("x") || operator.equals("X")) {
                                aggregate *= parsed;

                            } else if(operator.equals("/")) {
                                if (Integer.valueOf(parsed)==0) {
                                    flag = 1;
                                } else {
                                    aggregate /= parsed;
                                }
                            }
                        }
                        parsedInteger = "";
                        operator = ""+c;
                    }
                }
                if(flag==0) {
                    Log.e("Text","Ans="+aggregate);
                    tts.speak("It's " + aggregate, TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    tts.speak("That sounds like a trick question." , TextToSpeech.QUEUE_FLUSH, null);
                }


            } else if (txt.contains("play") || txt.contains("song")) {
                if(isNetworkAvailable()==0) {
                    tts.speak("It seems like internet connection is unavailable so I am unable to play songs", TextToSpeech.QUEUE_FLUSH, null);
                }
                else {
                    txt = txt.toLowerCase();
                    if((txt.contains("is this") || txt.contains("is it")) && txt.contains("good")) {
                        if(contexts.get(song)=="null") {
                            tts.speak("I don't know if it is a good song", TextToSpeech.QUEUE_FLUSH, null);
                        } else if(contexts.get(song)=="false") {
                            tts.speak("I don't think it is a good song", TextToSpeech.QUEUE_FLUSH, null);
                        } else if (contexts.get(song)=="true") {
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
                    } else if(txt.contains("play") && (txt.contains("random")||txt.contains("any"))) {
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
                            String s = txt.substring(txt.lastIndexOf("song")+4,txt.length());
                            s=s.replace(" ","+");
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query="+s)));
                    }
                }
            } else if (txt.contains("your") && txt.contains("food")) {
                 tts.speak("I don't eat much, but when I do, I take megabytes!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("your") && txt.contains("car")) {
                 tts.speak("I like fast ones!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (txt.contains("your") && txt.contains("color")) {
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
            }  else if (txt.contains("exit") && txt.contains("app")){
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
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                        tts.speak("News for Today", TextToSpeech.QUEUE_FLUSH, null);
                        Intent i = new Intent(MainActivity.this, NewsActivity.class);
                        startActivity(i,compat.toBundle());
                        stop();
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
                        tts.speak("The web has returned following result", TextToSpeech.QUEUE_FLUSH, null);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/search?q="+s));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else{
                    String s = txt;
                    s=s.replace(" ", "+");
                    tts.speak("The web has returned following result", TextToSpeech.QUEUE_FLUSH, null);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/search?q="+s));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    }
                }
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
                } else if (id == R.id.status_settings) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                    Intent intent = new Intent(MainActivity.this,StatusActivity.class);
                    intent.putExtra("status", updateStatus);
                    startActivity(intent,compat.toBundle());
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


        private void msg(String s)
        {
            Alerter.create(MainActivity.this)
                    .setText(s)
                    .setIcon(R.drawable.ic_notifications)
                    .setBackgroundColor(R.color.alert)
                    .show();
        }


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
        private boolean checkBT() {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            if (!myBluetooth.isEnabled()) {
                return false;
            }
            else {
                return true;
            }
        }

        private void statuscheck(String s) {
            s = s.replaceAll("[\u0000-\u001f]", "");
            if (s.contains("T1")) {
                 c1 = new CountDownTimer(20000,1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        notif(1);
                    }
                }.start();
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("Turned On light 1 in room", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("T2")) {
                new CountDownTimer(10000,1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        notif(2);
                    }
                }.start();
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("Turned On light 2 in room", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("T3")) {
                status.setSwitchColor(getResources().getColor(R.color.green));
                status.setDirection(StickySwitch.Direction.RIGHT);
                tts.speak("Turned On sprinklers in garden", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("T4")) {
                c1.cancel();
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("Turned Off light 1 in room", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.contains("T5")) {
                status.setSwitchColor(getResources().getColor(R.color.red));
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak("Turned Off light 2 in room", TextToSpeech.QUEUE_FLUSH, null);
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
                tts.speak("Sprinkler already turned off!", TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.startsWith("C1")) {
                String c = s.substring(2,4);
                String d = "Container 1 is low on surplus with just, "+c+" percent filled";
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak(d, TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.startsWith("C2")) {
                String c = s.substring(2,4);
                String d = "Container 2 is low on surplus with just, "+c+" percent filled";
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak(d, TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.startsWith("C3")) {
                String c = s.substring(2,4);
                String d = "Container 3 is low on surplus with just, "+c+" percent filled";
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak(d, TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.startsWith("M1")) {
                String c = s.substring(2,4);
                String d = "The garden contains "+c+" percent moisture";
                status.setDirection(StickySwitch.Direction.LEFT);
                tts.speak(d, TextToSpeech.QUEUE_FLUSH, null);
            } else if (s.length()>2){
                Log.d("string: ",s);
                tts.speak("Status Received!", TextToSpeech.QUEUE_FLUSH, null);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,null);
                Intent intent = new Intent(MainActivity.this,StatusActivity.class);
                intent.putExtra("status", s);
                startActivity(intent,compat.toBundle());
            } else {
                Log.d("String: ",s);
                tts.speak("Unknown value received!",TextToSpeech.QUEUE_FLUSH,null);
            }
        }

        private void notif(int b) {
            if(b==1) {
                    int notifID = 001;
                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(notifID, mBuilder.build());
            } else if(b==2) {
                    int notifID = 002;
                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(notifID, mBuilder.build());
            }
        }
    }
