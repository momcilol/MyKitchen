package com.example.mykitchen.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mykitchen.R;
import com.example.mykitchen.activities.JokeActivity;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String channelId = "notifyJoke";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent jokeIntent = new Intent(context, JokeActivity.class);
        jokeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, jokeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.cooking)
                .setContentTitle("MyKitchen")
                .setContentText("See todays food joke...")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}
