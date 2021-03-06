package nl.vdbrandtmedia.sedentairapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Ergonomics_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergonomics_home);

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
            case R.id.Sitting:
                newIntent(new Intent(this, ergonomics_sittingpose.class));
                break;
            case R.id.Equipment:
                newIntent(new Intent(this, ergonomics_equipment.class));
                break;
            case R.id.Standing:
                newIntent(new Intent(this, ergonomics_standingpose.class));
                break;
            case R.id.Workspace:
                newIntent(new Intent(this, ergonomics_organiseworkspace.class));
                break;
            case R.id.Breaks:
                newIntent(new Intent(this, ergonomics_breaks.class));
                break;
        }
    }

    public void newIntent(Intent intent){
        startActivity(intent);
    }
}
