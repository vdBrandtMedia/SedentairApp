package nl.vdbrandtmedia.sedentairapp;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity {


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

        ScheduleAdapter ca = new ScheduleAdapter(createList(50));
        recList.setAdapter(ca);
    }


    private List createList(int size) {

        List result = new ArrayList();
        for (int i=1; i <= size; i++) {
            ScheduleAdapter.ScheduleInfo ci = new ScheduleAdapter.ScheduleInfo();
            ci.day = ScheduleAdapter.ScheduleInfo.DAY_PREFIX + i;
            ci.time = ScheduleAdapter.ScheduleInfo.TIME_PREFIX + i;
            ci.timerName = ScheduleAdapter.ScheduleInfo.TIMERNAME_PREFIX + i;
            ci.timerBool = ScheduleAdapter.ScheduleInfo.TIMERBOOL_PREFIX;

            result.add(ci);

        }

        return result;
    }

    //implement fragments for multiple time events
}
