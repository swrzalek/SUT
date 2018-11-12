package pl.edu.ug.sut;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.os.Build;

public class MainActivity extends AppCompatActivity {

    private Button imgBtnGraph, imgBtnDb;
    private TextView unlockNumberTextView,longestOn,longestOff;
    private TextView sumOn,sumOff;
    private ProgressBar unlockCounterProgressBar, progressOnTime, progressOffTime, progressLongestOff, progressLongestOn ;
    public long startTime;
    public long endTime;
    private DatabaseHelper databaseHelper;
    DatabaseHelper db = new DatabaseHelper( this );
    private String format(double insert) {
        if(insert > 3600.0){
            // zamień sekundy na godziny z dwoma liczbami po przecinku
            //bedzie taki blad ze moze wyswietlac sie np. 1.9 godziny :/
            DecimalFormat df = new DecimalFormat("##.#");
            df.setRoundingMode(RoundingMode.CEILING);
            double i = insert/3600;
            return ( df.format( i ) + " godziny");

        }
        else if (insert > 60.0 ) {
            //zamień na minuty
            DecimalFormat df = new DecimalFormat("##");
            df.setRoundingMode(RoundingMode.CEILING);
            double i = insert/60;
            return ( df.format( i ) + " minut");
        }
        else {
            //zostaw sekundy ale zaokrąglij
            DecimalFormat df = new DecimalFormat("##");
            df.setRoundingMode(RoundingMode.CEILING);
            double i = insert;
           return ( df.format( i ) + " sekund");
        }
    }
    // ustaw TextView na wartosci db.dataCounter(),db.longestOnCounter(),db.longestOffCounter(),db.sumOn(),db.sumOff()
    private void setText(int num,double timeOn,double timeOff, double totalOn, double totalOff){
            unlockNumberTextView.setText(String.valueOf( num) );
            longestOn.setText( format( timeOn ) );
            longestOff.setText( format( timeOff ) );
            sumOn.setText( format( totalOn ) );
             sumOff.setText( format( totalOff ) );
        //

    }
    private int formatDayProgress(double dbouble){
        DecimalFormat df = new DecimalFormat("##");
        df.setRoundingMode(RoundingMode.CEILING);
        double i = dbouble/1800;
        return  ( Integer.valueOf( df.format(i ) ));

    }
    //metoda formatująca
    private int formatLongestProgress(double londoub){
        DecimalFormat df = new DecimalFormat("##");
        df.setRoundingMode(RoundingMode.CEILING);
        double i = londoub/200;
        return  ( Integer.valueOf( df.format(i ) ));
    }
    //metoda ustawiajaca progressbary db.dataCounter(),db.longestOnCounter(),db.longestOffCounter(),db.sumOn(),db.sumOff()
    private void setProg(int unCoPr,double timeOnProg ,double timeOffProg, double totalOnProg, double totalOffProg){
        unlockCounterProgressBar.setProgress( unCoPr );
        progressLongestOn.setProgress(formatLongestProgress( timeOnProg ));
        progressLongestOff.setProgress(formatLongestProgress(  timeOffProg));
        progressOnTime.setProgress(formatDayProgress(  totalOnProg));
        progressOffTime.setProgress(formatDayProgress( totalOffProg ));




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), OwnReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);

        if (Build.VERSION.SDK_INT >= 23) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 5), pi);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 5), pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 5), pi);
        }
        ////
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_main);
        //
            unlockCounterProgressBar = findViewById( R.id.unlockCounterProgressBar );
            imgBtnDb = findViewById( R.id.imgBtnDb );
            imgBtnGraph = findViewById( R.id.imgBtnGraph );
            unlockNumberTextView = findViewById( R.id.unlockNumberTextView );
            longestOn = findViewById( R.id.textViewLongestOn );
            longestOff = findViewById( R.id.textViewLongestOff );
            sumOff = findViewById(R.id.sumOffTime);
            sumOn = findViewById(R.id.sumOnTime);
            progressOnTime = findViewById(R.id.progressOnTime );
            progressOffTime = findViewById(R.id.progressOffTime );
            progressLongestOff = findViewById(R.id.progressLongestOff );
            progressLongestOn = findViewById(R.id.progressLongestOn );
        //Button :Baza: i :Wykres:
        TextView intro = findViewById( R.id.introTextView );
        intro.startAnimation(AnimationUtils.loadAnimation(this , android.R.anim.fade_in));
        //
        imgBtnGraph.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        } );

        imgBtnDb.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DatabaseActivity.class);
                startActivity(intent);
            }
        } );

        BroadcastReceiver myReceiverOff = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                //obsługa zdarzeń podpięcia telefonu do ładowarki bądź odłączenia telefonu od ładowarki
                Toast.makeText(context, "Ekran wyłączony", Toast.LENGTH_LONG).show();
                    startTime = System.currentTimeMillis();
                    double ONduration = ( startTime - endTime );
                    double hms = ((ONduration/1000));
                    Log.d("TAG", "ONduration: " + ONduration);
                java.util.Date date = new java.util.Date(System.currentTimeMillis());
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String dateAsISOString = df.format(date);
                    if (endTime == 0 || startTime == 0) {
                        Log.d("TAG", "endTime: " + endTime + " startTime: " + startTime);
                    }
                    else  {
    //                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    //                    Calendar cal = Calendar.getInstance();
    //                    dateFormat.format( cal )
                        DataModel model = new DataModel();
                        model.setState("ON");
                        model.setDate(dateAsISOString);
                        model.setDuration( Double.toString( hms ) );
                        db.addPeriod(model);
                    }
            }
        };
        BroadcastReceiver myReceiverOn = new BroadcastReceiver()
        {
            public void onReceive(Context context, Intent intent) {
                //obsługa zdarzeń podpięcia telefonu do ładowarki bądź odłączenia telefonu od ładowarki
                Toast.makeText(context, "Ekran włączony", Toast.LENGTH_LONG).show();
                    endTime = System.currentTimeMillis();
                    double OFFduration = (endTime - startTime);
                     Log.d("TAG", "Offduration: " + OFFduration);
                    double hms = (OFFduration/1000);
                java.util.Date date = new java.util.Date(System.currentTimeMillis());
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String dateAsISOString = df.format(date);

                    if (endTime == 0 || startTime == 0) {
                        Log.d("TAG", "endTime: " + endTime + " startTime: " + startTime);
                    }
                    else  {
                        DataModel model = new DataModel();
                        model.setState("OFF");
                        model.setDate(dateAsISOString);
                        model.setDuration( Double.toString( hms ) );
                        db.addPeriod(model);
                    }
            }
        };


    // drugi argument to obiekt IntentFilter z informacją, że zdarzenie które chcemy obsłużyć to ACTION_POWER_CONNECTED
    registerReceiver(myReceiverOn, new IntentFilter(Intent.ACTION_SCREEN_ON));
    registerReceiver(myReceiverOff, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    final DatabaseHelper db = new DatabaseHelper(this);

        //odświeżanie co 5 sekund
        Thread t = new Thread(  ) {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep( 5000 );
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                Log.e("CHECK" , "db.dataCounter(),db.longestOnCounter(),db.longestOffCounter(),db.sumOn(),db.sumOff() " + " | " + db.dataCounter()+ " | " +db.longestOnCounter()+ " | " +db.longestOffCounter()+ " | " +db.sumOn()+ " | " +db.sumOff() );
                                setText( db.dataCounter(),db.longestOnCounter(),db.longestOffCounter(),db.sumOn(),db.sumOff() );
                                setProg(db.dataCounter(),db.longestOnCounter(),db.longestOffCounter(),db.sumOn(),db.sumOff());
                                FrameLayout rlayout1 = findViewById(R.id.readerBottomLayout);
                                rlayout1.setVisibility(View.GONE);
                            }
                        } );
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }


}
