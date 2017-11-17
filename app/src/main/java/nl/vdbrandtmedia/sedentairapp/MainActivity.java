package nl.vdbrandtmedia.sedentairapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import nl.vdbrandtmedia.sedentairapp.fragments.ErgonomicsFragment;
import nl.vdbrandtmedia.sedentairapp.fragments.HomeFragment;
import nl.vdbrandtmedia.sedentairapp.fragments.SettingsFragment;

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

     /*
     void menuButtonClick(View v){
        switch (v.getId()){
            case R.id.homeBtn:
                HomeFragment homeFrag = new HomeFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myFragment,  homeFrag);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                activeFragment = "Home";
                break;

            case R.id.settingBtn:
                SettingsFragment settingFrag = new SettingsFragment();

                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.myFragment,  settingFrag);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction1.commit();

                activeFragment = "Settings";
                break;


            case R.id.ErgonomicsBtn:
                ErgonomicsFragment ergoFrag = new ErgonomicsFragment();

                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.myFragment, ergoFrag);
                //fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();

                activeFragment = "Ergonomics";
                break;
        }
    }
    */

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
