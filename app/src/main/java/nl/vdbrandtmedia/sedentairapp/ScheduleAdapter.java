package nl.vdbrandtmedia.sedentairapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lex van den Brandt on 14-11-2017.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{

    private List<ScheduleInfo> contactList;

    public ScheduleAdapter(List<ScheduleInfo> scheduleList) {
        this.contactList = scheduleList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder contactViewHolder, int i) {
        ScheduleInfo ci = contactList.get(i);
        contactViewHolder.vDay.setText(ci.day);
        contactViewHolder.vTime.setText(ci.time);
        contactViewHolder.vTimerName.setText(ci.timerName);
        contactViewHolder.vTimerBool.setChecked(ci.timerBool);
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ScheduleViewHolder(itemView);
    }

    public static class ScheduleInfo {
        protected String day;
        protected String time;
        protected String timerName;
        protected Boolean timerBool;
        protected static final String DAY_PREFIX = "Name_";
        protected static final String TIME_PREFIX = "Surname_";
        protected static final String TIMERNAME_PREFIX = "email_";
        protected static final Boolean TIMERBOOL_PREFIX = true;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        protected TextView vDay;
        protected TextView vTime;
        protected TextView vTimerName;
        protected Switch vTimerBool;

        public ScheduleViewHolder(View v) {
            super(v);
            vDay =  (TextView) v.findViewById(R.id.day);
            vTime = (TextView)  v.findViewById(R.id.time);
            vTimerName = (TextView)  v.findViewById(R.id.timerName);
            vTimerBool = (Switch) v.findViewById(R.id.scheduleBool);
        }
    }

}
