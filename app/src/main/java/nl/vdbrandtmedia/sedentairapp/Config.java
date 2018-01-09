package nl.vdbrandtmedia.sedentairapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

public class Config {
    private static SharedPreferences myPreferences;
    private static String prefValue = "prefValues";
    private static boolean initConfig = false;
    public static ArrayList<String> scheduleList = new ArrayList<String>();

    private static void initConfig(Context context) {
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        //myPreferences.contains("");
        initConfig = true;
    }

    static void writeSharedPreferences(Context context, String targetString, String newString) {
        if (!initConfig) {
            initConfig(context);
        }
        SharedPreferences.Editor edit = myPreferences.edit();
        edit.clear();
        edit.putString(targetString, newString);
        edit.apply();
    }

    static void writeSharedPreferencesInt(Context context, String targetString, int newString) {
        if (!initConfig) {
            initConfig(context);
        }
        SharedPreferences.Editor edit = myPreferences.edit();
        edit.clear();
        edit.putInt(targetString, newString);
        edit.apply();
    }

    static void writeMultiplePreferences(Context context, ArrayList<String> targetArray, ArrayList<String> valueArray) {
        if (!initConfig) {
            initConfig(context);
        }

        if (targetArray.size() == valueArray.size()){
            for (int i = 0; i < targetArray.size(); i++) {
                SharedPreferences.Editor edit = myPreferences.edit();
                edit.clear();
                edit.putString(targetArray.get(i), valueArray.get(i));
                edit.commit();
                Log.i("WriteMultiple sp","added row " + targetArray.get(i) + " " +  valueArray.get(i));
            }
        }
    }

    static String readSharedPreferences(Context context, String targetString) {
        if (!initConfig) {
            initConfig(context);
        }
        return myPreferences.getString(targetString, "");
    }

    static int readSharedPreferencesInt(Context context, String targetString) {
        if (!initConfig) {
            initConfig(context);
        }
        return myPreferences.getInt(targetString, 0);
    }


    static SharedPreferences getPreferences(Context context) {
        return myPreferences;
    }
    /* Example of getting and writing data

        Config.writeSharedPreferences(this, "value1", "shit");

        TextView textview = (TextView) findViewById(R.id.prefData1);
        textview.setText(Config.readSharedPreferences(this, "value1"));

    */

}
