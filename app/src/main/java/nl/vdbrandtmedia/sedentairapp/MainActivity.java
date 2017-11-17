package nl.vdbrandtmedia.sedentairapp;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public String activeFragment;
    public boolean doubleBackToExitPressedOnce;
    public LinearLayout homeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
 
        activeFragment = "HOME";
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onPause();
            finish();
        }

        if (Objects.equals(activeFragment, "Home")){
            menuButtonClick(homeBtn);
        } else
        {
            this.doubleBackToExitPressedOnce = true;
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.activity_main),
                    "Please click BACK again to exit the app \r\n", Snackbar.LENGTH_SHORT);

            //mySnackbar.setAction("Text", new MyUndoListener());
            mySnackbar.show();
        }


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void menuButtonClick(View v) {
        switch (v.getId()) {
            case R.id.settingBtn:
                newIntent(new Intent(this, Settings.class));
                break;
            case R.id.scheduleBtn:
                newIntent(new Intent(this, Schedule.class));
                break;
            case R.id.ErgonomicsBtn:
                newIntent(new Intent(this, Ergonomics_home.class));
                break;
        }
    }

    public void newIntent(Intent intent){
        startActivity(intent);
    }
}
