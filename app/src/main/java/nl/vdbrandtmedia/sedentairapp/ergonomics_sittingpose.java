package nl.vdbrandtmedia.sedentairapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ergonomics_sittingpose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergonomics_sittingpose);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

//        setTitle("Ergonomics");
    }

    public void menuButtonClick(View v) {
        switch (v.getId()) {
            case R.id.homeButton:
                newIntent(new Intent(this, MainActivity.class));
                break;
        }
    }

    public void newIntent(Intent intent){
        startActivity(intent);
    }
}
