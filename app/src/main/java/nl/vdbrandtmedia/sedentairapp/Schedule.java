package nl.vdbrandtmedia.sedentairapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Arrays;
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

        for (int i = 0; i < 3; i++) {
            Config.writeSharedPreferences(this, "scheduleName" + i, "Meeting");
//            Log.i("testPref", "scheduleName" + i + ": " + Config.readSharedPreferences(this, "scheduleName" + i));

            Config.writeSharedPreferences(this, "scheduleTime" + i, i + ":00");
//            Log.i("testPref", "scheduleTime" + i + ": " + Config.readSharedPreferences(this, "scheduleTime" + i));

            Config.writeSharedPreferences(this, "scheduleDay" + i, "Monday");
//            Log.i("testPref", "scheduleDay" + i + ": " + Config.readSharedPreferences(this, "scheduleDay" + i));

            Config.writeSharedPreferences(this, "scheduleBool" + i, "true");
//            Log.i("testPref", "scheduleBool" + i + ": " + Config.readSharedPreferences(this, "scheduleBool" + i));
        }

        ScheduleAdapter ca = new ScheduleAdapter(createList(10, getScheduleList(this)));
        recList.setAdapter(ca);
    }


    private List createList(int size, String[][] scheduleList) {
        List result = new ArrayList();

//        for(int i = 0; i < scheduleList.length; i++) {
//            String[] anon = new String[ /* your number here */];
//            // or String[] anon = new String[]{"I'm", "a", "new", "array"};
//            scheduleList[i] = anon;
//
//          ScheduleAdapter.ScheduleInfo ci = new ScheduleAdapter.ScheduleInfo();
//          ci.day = "";
//          ci.time = "";
//          ci.timerDay = "";
//          ci.timerBool = false;

//          result.add(ci);
//        }


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


    public String[][] getScheduleList(Context context) {

        //ArrayList<String> arrayList = new ArrayList<String>();
        String[][] arrayList = new String[20][4];
        int k = 0;
        int j = 0;
        for (int i = 0; i < 20; i++) {

            //set variable for implementation
            String scheduleName = Config.readSharedPreferences(this, "scheduleName" + i);
//            Log.i("test Read", "val: " + scheduleName);
            String scheduleTime = Config.readSharedPreferences(this, "scheduleTime" + i);
//            Log.i("test Read", "val: " + scheduleTime);
            String scheduleDay = Config.readSharedPreferences(this, "scheduleDay" + i);
//            Log.i("test Read", "val: " + scheduleDay);
            String scheduleBool = Config.readSharedPreferences(this, "scheduleBool" + i);
//            Log.i("test Read", "val: " + scheduleBool);

            //if (scheduleName != null && scheduleTime != null && scheduleDay != null && scheduleBool != null) {
            //this adds an element to the String[][][]
            arrayList[i][j] = scheduleName;
            j++;
            arrayList[i][j] = scheduleTime;
            j++;
            arrayList[i][j] = scheduleDay;
            j++;
            arrayList[i][j] = scheduleBool;
            j = 0;
            k++;
            //}

        }
        Log.i("getScheduleList", "Data: " + Arrays.deepToString(arrayList));
        Log.i("getScheduleList", "Value: " + Config.readSharedPreferences(this, "scheduleTime0"));

//        for(int i = 0; i < arrayList.length; i++) {
//            String[] anon = new String[1];
//            int l = 0
//            Log.i("getScheduleList","value = " + arrayList[i][l]);
//            // or String[] anon = new String[]{"I'm", "a", "new", "array"};
//            //arrayList[i] = anon;
//            l++;
//        }
        return arrayList;
    }
}
