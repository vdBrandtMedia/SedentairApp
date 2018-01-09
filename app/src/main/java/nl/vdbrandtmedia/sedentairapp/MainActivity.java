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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    int todayStep, startingStep;
    TextView tv_steps, tv_steps_average;
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
        tv_steps_average = (TextView) findViewById(R.id.tv_steps_average);
        todayStep = -1;

        //set the startingStep
        if (Config.readSharedPreferencesInt(this, today() + "start") < 1) {
            //        Log.i("Day", "dayvalue tday = " + Config.readSharedPreferencesInt(this, today()));
            //        Log.i("Day", "dayvalue yday = " + Config.readSharedPreferencesInt(this, yesterday(1)));
            startingStep = Config.readSharedPreferencesInt(this, yesterday(1));
            if (startingStep < 1) {
                startingStep = 1;
            }
            Config.writeSharedPreferencesInt(this, today() + "start", startingStep);
            Log.i("Tag", "startingStep = " + startingStep);
        } else {
            startingStep = Config.readSharedPreferencesInt(this, today() + "start");
            Log.i("Tag", "startingStep = " + startingStep);
        }
        tv_steps.setText("" + Config.readSharedPreferencesInt(this, today()));

//        if (Config.readSharedPreferencesInt(this, today()) < 1) {
//            todayStep = 0;
//            Log.i("startupValue", "todayStep = " + Config.readSharedPreferences(this, today()));
//            tv_steps.setText("0");
//        } else {
//            todayStep = Config.readSharedPreferencesInt(this, today());
//            Log.i("startupValue", "todayStep = " + todayStep);
//            tv_steps.setText(Integer.toString(todayStep));
//        }

        //set average steps according to last week
        List<Integer> averageStepList = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++) {
            int averageStep = Config.readSharedPreferencesInt(this, yesterday(i));
            if (averageStep > 0) {
                averageStepList.add(averageStep);
            }
            Log.i("Average", "Dayvalue = " + averageStep + " " + yesterday(i));
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        tv_steps_average.setText(decimalFormat.format(calculateAverage(averageStepList)));
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
            Config.writeSharedPreferencesInt(this, today(), Math.round(totalStepSinceReboot - startingStep));
            tv_steps.setText(String.valueOf(Math.round(totalStepSinceReboot - startingStep)));
            Log.i("TAG", "Your today step now is " + Config.readSharedPreferencesInt(this, today()));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }

    private Date getDateYesterday(int days) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    private String yesterday(int days) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(getDateYesterday(days));
    }

    private double calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if (!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return Math.round(sum);
    }

}
