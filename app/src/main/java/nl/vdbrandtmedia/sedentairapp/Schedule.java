package nl.vdbrandtmedia.sedentairapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Schedule extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    String scheduleItemString;

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    private String scheduleName;
    RelativeLayout scheduleInput;
    EditText newScheduleName;
    ScrollView scrollView;
    RecyclerView recList;

    DatabaseHelper myDb;
    EditText editName, editSurname, editMarks, editTextId;
    Button btnAddData, btnviewAll, btnDelete;

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

        myDb = new DatabaseHelper(this);
        btnviewAll = (Button) findViewById(R.id.button_viewAll);
        viewAll();

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        setTitle("Schedule");

        //scheduleItemString = "Meeting,13:00,Monday,true,Meeting 2,10:00,Tuesday,true,Meeting 3,18:00,Saturday,true";
        //Config.writeSharedPreferences(this, "scheduleItemString", scheduleItemString);

        ScheduleAdapter ca = new ScheduleAdapter(createList(getScheduleList()));
        recList.setAdapter(ca);
    }

    public void DeleteData(String Name, String Time, String Day) {
        Integer deletedRows = myDb.deleteData(Name, Time, Day);
        if (deletedRows > 0)
            Toast.makeText(Schedule.this, "Data Deleted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(Schedule.this, "Data not Deleted", Toast.LENGTH_LONG).show();
    }

    public void AddData(final String Name, final String Time, final String Day) {
        boolean isInserted = myDb.insertData(Name, Time, Day);
        if (isInserted)
            Toast.makeText(Schedule.this, "Data Inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(Schedule.this, "Data not Inserted", Toast.LENGTH_LONG).show();
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if (res.getCount() == 0) {
                            // show message
                            showMessage("Error", "Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :" + res.getString(0) + "\n");
                            buffer.append("Name :" + res.getString(1) + "\n");
                            buffer.append("Day :" + res.getString(2) + "\n");
                            buffer.append("Time :" + res.getString(3) + "\n");
                            buffer.append("Bool :" + res.getString(4) + "\n\n");
                        }

                        // Show all data
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    private List createList(String[][] scheduleList) {
        List result = new ArrayList();

//        for (int i = 0; i < scheduleList.length; i++) {
//            if (!Objects.equals(scheduleList[i][0], "") &&
//                    !Objects.equals(scheduleList[i][1], "") &&
//                    !Objects.equals(scheduleList[i][2], "") &&
//                    !Objects.equals(scheduleList[i][3], "")) {
//
//                ScheduleAdapter.ScheduleInfo ci = new ScheduleAdapter.ScheduleInfo();
//                ci.day = scheduleList[i][0];
//                ci.time = scheduleList[i][1];
//                ci.timerDay = scheduleList[i][2];
//                ci.timerBool = Boolean.valueOf(scheduleList[i][3]);
//                result.add(ci);
//            }
//        }

        Cursor res = myDb.getAllData();

        while (res.moveToNext()) {
            ScheduleAdapter.ScheduleInfo ci = new ScheduleAdapter.ScheduleInfo();
            ci.name =  res.getString(1);
            ci.day = res.getString(2);
            ci.time = res.getString(3);
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


    public String[][] getScheduleList() {

        String getScheduleItemString = Config.readSharedPreferences(this, "scheduleItemString");
        Log.i("scheduleItemString", "value: " + getScheduleItemString);
        String[] scheduleValueArray = getScheduleItemString.split(",");


        //ArrayList<String> arrayList = new ArrayList<String>();
        String[][] arrayList = new String[scheduleValueArray.length / 4][4];
        int h = 1;
        int j = 0;
        for (int i = 0; i < 20; i++) {

            if (scheduleValueArray.length > 2) {
                if (h < scheduleValueArray.length) {
                    arrayList[i][j] = scheduleValueArray[h];
                    j++;
                    h++;
                    arrayList[i][j] = scheduleValueArray[h];
                    j++;
                    h++;
                    arrayList[i][j] = scheduleValueArray[h];
                    j++;
                    h++;
                    arrayList[i][j] = scheduleValueArray[h];
                    j = 0;
                    h++;
                }
            }
        }
//        Log.i("getScheduleList", "Data: " + Arrays.deepToString(arrayList));

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

    public void continueScheduleAdd(View v) {
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

        int yearValue = Calendar.getInstance().get(Calendar.YEAR);

        Log.i("Time set", "year1: " + yearFinal + " month: " + monthFinal + " day: " + dayFinal + " hour: " + hourFinal + " minute: " + minuteFinal + " scheduleName: " + scheduleName);

//        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayFinal);
        calendar.set(Calendar.MONTH, monthFinal);
        calendar.set(Calendar.YEAR, yearValue);
        String weekDay = dayFormat.format(calendar.getTime());
        Log.i("DayOfWeek", "string value: " + weekDay);
        Log.i("DayOfWeek", "int    value: " + Calendar.DAY_OF_MONTH);

        //String getScheduleItemString = Config.readSharedPreferences(this, "scheduleItemString");
        //getScheduleItemString += "," + scheduleName + "," + hourFinal + ":" + minuteFinal + "," + weekDay + ",true";
        //Config.writeSharedPreferences(this, "scheduleItemString", getScheduleItemString);

        AddData(scheduleName, Integer.toString(hourFinal) + ":" + Integer.toString(minuteFinal), weekDay);

        ScheduleAdapter ca = new ScheduleAdapter(createList(getScheduleList()));
        recList.setAdapter(ca);


    }
}
