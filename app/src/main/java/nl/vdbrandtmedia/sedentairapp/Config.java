package nl.vdbrandtmedia.sedentairapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

public class Config {
    private static SharedPreferences myPreferences;
    private static String prefValue = "prefValues";
    public static boolean initConfig = false;
    public static ArrayList<String> scheduleList = new ArrayList<String>();

    private static void initConfig(Context context) {
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        myPreferences.contains("");

        for (int i = 1; i < 21; i++) {
            if (myPreferences.contains("scheduleName" + i)) {
                String mystring = "scheduleName" + i;
                scheduleList.add(mystring); //this adds an element to the list.
            }
        }

        for (String object : scheduleList) {
            Log.d("set param list ", "value: " + object);
            //todo: remove log
        }

        initConfig = true;
    }

    static void writeSharedPreferences(Context context, String targetString, String newString) {
        if (!initConfig) {
            initConfig(context);
        }
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = myPreferences.edit();
        edit.clear();
        edit.putString(targetString, newString);
        edit.commit();
    }

    static String readSharedPreferences(Context context, String targetString) {
        if (!initConfig) {
            initConfig(context);
        }
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        return myPreferences.getString(targetString, "");
    }

    static ArrayList<String> getScheduleList(Context context) {
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        myPreferences.contains("");

        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 1; i < 21; i++) {
            if (myPreferences.contains("scheduleName" + i)) {
                //set variable for implementation
                String myName  = myPreferences.getString("scheduleName" + i, "");
                String myTime1 = myPreferences.getString("scheduleTime" + i, "");
                String myTime2 = myPreferences.getString("scheduleDay" + i, "");
                String myBool  = myPreferences.getString("scheduleBool" + i, "");

                //this adds an element to the list.
                arrayList.add(myName);
                arrayList.add(myTime1);
                arrayList.add(myTime2);
                arrayList.add(myBool);

                //check variable
                Log.d("getScheduleList", "Value: " + myName);
            }
        }

        return arrayList;
    }

    /* Example of getting and writing data

        Config.writeSharedPreferences(this, "value1", "shit");

        TextView textview = (TextView) findViewById(R.id.prefData1);
        textview.setText(Config.readSharedPreferences(this, "value1"));

    */

}
