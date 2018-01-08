package nl.vdbrandtmedia.sedentairapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public String activeFragment;
    public boolean doubleBackToExitPressedOnce;
    public LinearLayout homeBtn;

    private android.support.v7.app.NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;

    SensorManager sensorManager;
    private int milestoneStep;
    int todayStep, startingStep;
    TextView tv_steps;
    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //remove top bar from activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

//        Config.writeSharedPreferences(this, "scheduleName1", "test1");
//        Log.d("test: ", "value: " + Config.readSharedPreferences(this, "scheduleName1"));

        activeFragment = "HOME";

//        button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setAlarmManager(1,13,6);
//            }
//        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tv_steps = (TextView) findViewById(R.id.tv_steps);
        todayStep = -1;
        startingStep = 0;
        if (Config.readSharedPreferences(this, today()) == "") {
            todayStep = 0;
            Log.i("startupValue", "todayStep = 0 / value: " + Config.readSharedPreferences(this, today()));
            tv_steps.setText("0");
        } else {
            todayStep = Integer.parseInt(Config.readSharedPreferences(this, today()));
            Log.i("startupValue", "todayStep = " + todayStep);
            tv_steps.setText(Integer.toString(todayStep));
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onPause();
            finish();
        }

        if (Objects.equals(activeFragment, "Home")) {
            menuButtonClick(homeBtn);
        } else {
            this.doubleBackToExitPressedOnce = true;
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.activity_main),
                    "Please click BACK again to exit the app \r\n", Snackbar.LENGTH_SHORT);

            //mySnackbar.setAction("Text", new MyUndoListener());
            mySnackbar.show();
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void menuButtonClick(View v) {
        switch (v.getId()) {
//            case R.id.settingBtn:
//                newIntent(new Intent(this, Settings.class));
//                break;
            case R.id.scheduleBtn:
                newIntent(new Intent(this, Schedule.class));
                break;
            case R.id.ErgonomicsBtn:
                newIntent(new Intent(this, Ergonomics_home.class));
                break;
        }
    }

    public void newIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        //if you unregister the hardware will stop detecting steps
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running) {

//            tv_steps.setText(String.valueOf(Math.round(sensorEvent.values[0])));
//            Config.writeSharedPreferences(this, "stepsToday", String.valueOf(Math.round(sensorEvent.values[0])));
//            Log.d("Stepstoday: ", "Value; " + Config.readSharedPreferences(this, "stepsToday"));

            int totalStepSinceReboot = (int) sensorEvent.values[0];
            Log.i("TotalSinceReboot", "TotalSinceReboot = " + totalStepSinceReboot);

            //set the startingStep
            if (startingStep == 0) {
                if (Objects.equals(Config.readSharedPreferences(this, today() + "start"), "")) {
                    startingStep = 1;
                    Log.i("Tag", "todayStep = 0 / valuez: " + Config.readSharedPreferences(this, today() + "start"));
                } else {
                    startingStep = Integer.parseInt(Config.readSharedPreferences(this, today() + "start"));
                }
            }

            if (todayStep == -1) {

                //set the todayStep
                if (Objects.equals(Config.readSharedPreferences(this, today()), "")) {
                    todayStep = 0;
                    Log.i("Tag", "todayStep = 0 / valuez: " + Config.readSharedPreferences(this, today()));
                } else {
                    todayStep = Integer.parseInt(Config.readSharedPreferences(this, today()));
                }

            } else {
                if (todayStep > 0) {
                    todayStep = Integer.parseInt(Config.readSharedPreferences(this, today()));
                }
            }

            if (todayStep == 0) {
                milestoneStep = totalStepSinceReboot;
                Config.writeSharedPreferences(this, today() + "start", String.valueOf(totalStepSinceReboot));
            } else {
                //int additionStep = totalStepSinceReboot - milestoneStep;
                Config.writeSharedPreferences(this, today(), String.valueOf(startingStep - todayStep));
                milestoneStep = totalStepSinceReboot;

                tv_steps.setText(String.valueOf(Math.round(startingStep - todayStep)));
                Log.i("TAG", "Your today step now is " + Config.readSharedPreferences(this, today()));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }

    public void setAlarmManager(int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        Intent intent = new Intent(this, NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
