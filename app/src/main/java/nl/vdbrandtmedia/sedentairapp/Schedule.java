package nl.vdbrandtmedia.sedentairapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Schedule extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button button;
    ArrayList<String> targetArray = new ArrayList<>();
    ArrayList<String> valueArray = new ArrayList<>();


    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    private String scheduleName;
    RelativeLayout scheduleInput;
    EditText newScheduleName;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        scrollView = (ScrollView) findViewById(R.id.scheduleScrollView);
        newScheduleName = (EditText) findViewById(R.id.newScheduleName);
        scheduleInput = (RelativeLayout) findViewById(R.id.scheduleInputBlock);
        scheduleInput.setVisibility(View.GONE);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        setTitle("Schedule");

        for (int i = 0; i < 3; i++) {
            targetArray.add("scheduleName" + i);
            valueArray.add("Meeting");
            targetArray.add("scheduleTime" + i);
            valueArray.add(i + ":00");
            targetArray.add("scheduleDay" + i);
            valueArray.add("Monday");
            targetArray.add("scheduleBool" + i);
            valueArray.add("true");
        }

        Config.writeMultiplePreferences(this, targetArray, valueArray);

        ScheduleAdapter ca = new ScheduleAdapter(createList(getScheduleList()));
        recList.setAdapter(ca);
    }


    private List createList(String[][] scheduleList) {
        List result = new ArrayList();

        for (int i = 0; i < scheduleList.length; i++) {
            if (!Objects.equals(scheduleList[i][0], "") &&
                    !Objects.equals(scheduleList[i][1], "") &&
                    !Objects.equals(scheduleList[i][2], "") &&
                    !Objects.equals(scheduleList[i][3], "")) {

                ScheduleAdapter.ScheduleInfo ci = new ScheduleAdapter.ScheduleInfo();
                ci.day = scheduleList[i][0];
                ci.time = scheduleList[i][1];
                ci.timerDay = scheduleList[i][2];
                ci.timerBool = Boolean.valueOf(scheduleList[i][3]);
                result.add(ci);
            }
        }

//        for (int i = 1; i <= size; i++) {
//            ScheduleAdapter.ScheduleInfo ci = new ScheduleAdapter.ScheduleInfo();
//            ci.day = ScheduleAdapter.ScheduleInfo.DAY_PREFIX;
//            ci.time = ScheduleAdapter.ScheduleInfo.TIME_PREFIX;
//            ci.timerDay = ScheduleAdapter.ScheduleInfo.TIMERDAY_PREFIX + i;
//            ci.timerBool = ScheduleAdapter.ScheduleInfo.TIMERBOOL_PREFIX;
//            result.add(ci);
//        }

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


    public String[][] getScheduleList() {

        //ArrayList<String> arrayList = new ArrayList<String>();
        String[][] arrayList = new String[valueArray.size() / 4][4];
        int h = 0;
        int j = 0;
        for (int i = 0; i < 20 / 4; i++) {

            //set variable for implementation
            String scheduleName = Config.readSharedPreferences(this, "scheduleName" + i);
            Log.i("test Read", "val: " + scheduleName);
            String scheduleTime = Config.readSharedPreferences(this, "scheduleTime" + i);
            Log.i("test Read", "val: " + scheduleTime);
            String scheduleDay = Config.readSharedPreferences(this, "scheduleDay" + i);
            Log.i("test Read", "val: " + scheduleDay);
            String scheduleBool = Config.readSharedPreferences(this, "scheduleBool" + i);
            Log.i("test Read", "val: " + scheduleBool);

            //if (scheduleName != null && scheduleTime != null && scheduleDay != null && scheduleBool != null) {
            //this adds an element to the String[][][]
            if (h < valueArray.size()) {
                arrayList[i][j] = valueArray.get(h);
                j++;
                h++;
                arrayList[i][j] = valueArray.get(h);
                j++;
                h++;
                arrayList[i][j] = valueArray.get(h);
                j++;
                h++;
                arrayList[i][j] = valueArray.get(h);
                j = 0;
                h++;
            }
//            arrayList[i][j] = scheduleName;
//            j++;
//            arrayList[i][j] = scheduleTime;
//            j++;
//            arrayList[i][j] = scheduleDay;
//            j++;
//            arrayList[i][j] = scheduleBool;


        }
//        Log.i("getScheduleList", "Data: " + Arrays.deepToString(arrayList));
//        Log.i("getScheduleList", "Value: " + Config.readSharedPreferences(this, "scheduleTime0"));

        return arrayList;
    }

    public void setAlarmManager(int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        Intent intent = new Intent(this, NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void addScheduleItem(View v) {
        RelativeLayout scheduleInput = (RelativeLayout) findViewById(R.id.scheduleInputBlock);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scheduleScrollView);
        scrollView.setVisibility(View.GONE);
        scheduleInput.setVisibility(View.VISIBLE);
    }

    public void continueScheduleAdd(View v){
        addScheduleName(newScheduleName);
    }

    public void closeScheduleItem(View v) {
        scrollView.setVisibility(View.VISIBLE);
        scheduleInput.setVisibility(View.GONE);
    }

    public void addScheduleName(EditText newScheduleName) {
        String editTextValue = newScheduleName.getText().toString();
        if (!editTextValue.matches("")) {

            scheduleName = editTextValue;
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
            datePickerDialog.show();
        } else {
            //Toast.makeText(this, "Enter a schedule name", Toast.LENGTH_SHORT).show();
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.activity_schedule),
                    "Enter a schedule name to continue \r\n", Snackbar.LENGTH_SHORT);

            //mySnackbar.setAction("Text", new MyUndoListener());
            mySnackbar.show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i1;
        monthFinal = i1 + 1;
        dayFinal = i2;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinal = i;
        minuteFinal = i1;
        scheduleInput.setVisibility(View.GONE);
        newScheduleName.setText("");
        scrollView.setVisibility(View.VISIBLE);

        Log.i("Time set", " day: " + dayFinal + " hour: " + hourFinal + " minte: " + minuteFinal + " scheduleName: " + scheduleName);
    }
}
