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

    private static void initConfig(Context context){
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        myPreferences.contains("");

        for (int i = 1; i < 21; i++){
            if(myPreferences.contains("scheduleName"+i)){
                String mystring = "scheduleName" + i;
                scheduleList.add(mystring); //this adds an element to the list.
            } else {
            }
        }

        for (String object: scheduleList) {
            Log.d("set param list ","value: " + object);
        }

        initConfig = true;
    }

    static void writeSharedPreferences(Context context, String targetString, String newString) {
        if (!initConfig){
            initConfig(context);
        }
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = myPreferences.edit();
        edit.clear();
        edit.putString(targetString, newString);
        edit.commit();
    }

    public static String readSharedPreferences(Context context, String targetString) {
        if (!initConfig){
            initConfig(context);
        }
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        return myPreferences.getString(targetString, "");
    }

    /* Example of getting and writing data

        Config.writeSharedPreferences(this, "value1", "shit");

        TextView textview = (TextView) findViewById(R.id.prefData1);
        textview.setText(Config.readSharedPreferences(this, "value1"));

    */

}
