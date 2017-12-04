package nl.vdbrandtmedia.sedentairapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //remove top bar from activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);

        remoteViews.setImageViewResource(R.id.notif_icon, R.mipmap.ic_launcher2);
        remoteViews.setTextViewText(R.id.notif_title, "App name");
        remoteViews.setTextViewText(R.id.notif_text, "How are you feeling today?");

        notification_id = (int) System.currentTimeMillis();
        Intent button_intent = new Intent("button_clicked");
        button_intent.putExtra("id", notification_id);

        PendingIntent p_button_intent = PendingIntent.getBroadcast(context, 123, button_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notif_btn, p_button_intent);

        findViewById(R.id.notification_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notification_intent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent
                        .getActivity(context, 0, notification_intent, 0);

                builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setCustomContentView(remoteViews)
                        .setContentIntent(pendingIntent);

                notificationManager.notify(notification_id, builder.build());
            }
        });

        Config.writeSharedPreferences(this, "scheduleName1", "test1");
        Log.d("test: ", "value: " + Config.readSharedPreferences(this, "scheduleName1"));

        activeFragment = "HOME";
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
