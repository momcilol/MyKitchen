package com.example.mykitchen.fragments;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.mykitchen.R;
import com.example.mykitchen.utils.NotificationReceiver;

import java.util.Calendar;

public class NotificationFragment extends Fragment {


    static TextView textViewTime;

    Button buttonTime;
    Button buttonCancel;
    Button buttonSet;

    public static int notificationHour;
    public static int notificationMinute;

    boolean alarmCanceled;

    AlarmManager alarmManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public NotificationFragment() {

    }

    public interface OnSaveNotificationTime {
        void onSaveNotificationTime();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor = sharedPreferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        textViewTime = view.findViewById(R.id.textViewTime);
        buttonTime = view.findViewById(R.id.buttonTime);
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonSet = view.findViewById(R.id.button_set);


        buttonTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                androidx.fragment.app.DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNotificationChannel();
                setAlarm();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notificationHour = sharedPreferences.getInt("hour", 9);
        notificationMinute = sharedPreferences.getInt("minute", 0);
        textViewTime.setText(notificationHour + ":" + notificationMinute);
    }

    public static class TimePickerFragment extends androidx.fragment.app.DialogFragment
            implements TimePickerDialog.OnTimeSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            textViewTime.setText(hourOfDay + ":" + minute);
            notificationHour = hourOfDay;
            notificationMinute = minute;
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NotificationReceiver.channelId,
                    "JokeNotificationChannel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel for Alarm Manager");

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setAlarm() {
        if (alarmManager == null) {
            alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        }

        Intent intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, notificationHour);
        calendar.set(Calendar.MINUTE, notificationMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(getActivity(), "Alarm set", Toast.LENGTH_LONG).show();
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, 0);

        if (alarmManager == null) {
            alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(getActivity(), "Alarm cancelled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        editor.putInt("hour", notificationHour);
        editor.putInt("minute", notificationMinute);
        editor.apply();
    }
}