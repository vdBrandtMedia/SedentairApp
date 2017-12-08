package nl.vdbrandtmedia.sedentairapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public String activeFragment;
    public boolean doubleBackToExitPressedOnce;
    public LinearLayout homeBtn;

    private android.support.v7.app.NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //remove top bar from activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        button = (Button) findViewById(R.id.button);
        context = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmManager(13,6);
            }
        });

        Config.writeSharedPreferences(this, "scheduleName1", "test1");
        Log.d("test: ", "value: " + Config.readSharedPreferences(this, "scheduleName1"));

        activeFragment = "HOME";

    }

    public void setAlarmManager(int hour, int min){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        Intent intent = new Intent(context,NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
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
            case R.id.settingBtn:
                newIntent(new Intent(context, Settings.class));
                break;
            case R.id.scheduleBtn:
                newIntent(new Intent(context, Schedule.class));
                break;
            case R.id.ErgonomicsBtn:
                newIntent(new Intent(context, Ergonomics_home.class));
                break;
        }
    }

    public void newIntent(Intent intent) {
        startActivity(intent);
    }
}
