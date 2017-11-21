package nl.vdbrandtmedia.sedentairapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {

    private static SharedPreferences myPreferences;
    private static String prefValue = "prefValues";

    static void writeSharedPreferences(Context context, String targetString, String newString) {
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = myPreferences.edit();
        edit.clear();
        edit.putString(targetString, newString);
        edit.commit();
    }

    public static String readSharedPreferences(Context context, String targetString) {
        myPreferences = context.getSharedPreferences(prefValue, Context.MODE_PRIVATE);
        return myPreferences.getString(targetString, "");
    }

    /* Example of getting and writing data

        Config.writeSharedPreferences(this, "value1", "shit");

        TextView textview = (TextView) findViewById(R.id.prefData1);
        textview.setText(Config.readSharedPreferences(this, "value1"));

    */

}
