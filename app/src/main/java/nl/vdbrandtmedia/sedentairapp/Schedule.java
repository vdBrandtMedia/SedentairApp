package nl.vdbrandtmedia.sedentairapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Schedule extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        setTitle("Schedule");

        Log.d("sp test",Config.readSharedPreferences(this,"scheduleName"));

        for (int i = 0; i < 3; i++) {
            Config.writeSharedPreferences(this, "scheduleName" + i, "Meeting");
            Config.writeSharedPreferences(this, "scheduleTime" + i, "12;00");
            Config.writeSharedPreferences(this, "scheduleDay" + i, "Monday");
            Config.writeSharedPreferences(this, "scheduleBool" + i, "true");

        }

        ScheduleAdapter ca = new ScheduleAdapter(createList(20, Config.getScheduleList(this)));
        recList.setAdapter(ca);
    }



    private List createList(int size, ArrayList<String> arrayList) {
        List result = new ArrayList();

        /*for (String object: arrayList) {
            ScheduleAdapter.ScheduleInfo ci = new ScheduleAdapter.ScheduleInfo();
            ci.day =        object;
            ci.time =       object;
            ci.timerDay =   object;
            ci.timerBool =  Boolean.valueOf(object);

            result.add(ci);
        }*/

        for (int i = 1; i <= size; i++) {
            ScheduleAdapter.ScheduleInfo ci = new ScheduleAdapter.ScheduleInfo();
            ci.day = ScheduleAdapter.ScheduleInfo.DAY_PREFIX;
            ci.time = ScheduleAdapter.ScheduleInfo.TIME_PREFIX;
            ci.timerDay = ScheduleAdapter.ScheduleInfo.TIMERDAY_PREFIX + i;
            ci.timerBool = ScheduleAdapter.ScheduleInfo.TIMERBOOL_PREFIX;
            result.add(ci);
        }

        return result;
    }

    public void menuButtonClick(View v) {
        switch (v.getId()) {
            case R.id.homeButton:
                newIntent(new Intent(this, MainActivity.class));
                break;
        }
    }

    public void newIntent(Intent intent) {
        startActivity(intent);
    }
}
