package com.example.admin.beerandmusic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String name = null;

        if(intent.getExtras().getString("name") != null) {
            name = intent.getExtras().getString("name");
        }

        //Assign inbox style notification
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        inboxStyle.setBigContentTitle("Reminder from BeerMusic");
        inboxStyle.addLine("You have set reminder for " + name);
        inboxStyle.setSummaryText("Click to get more info");


        long[] pattern = {0, 300, 0};
        PendingIntent pi = PendingIntent.getActivity(context, 01234, new Intent(context, MapActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
//                .setContentTitle("Reminder from BeerMusic")
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("You have set reminder for " + name + "." +"\nClick to view map!"))
//                .setContentText("You have set reminder for " + name + "." +"\nClick to view map!")
//                .setVibrate(pattern)
//                .setAutoCancel(true);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.instagram_icon_small)
                .setContentTitle("Reminder from BeerMusic")
                .setStyle(inboxStyle)
                .setContentText("You have set reminder for " + name + "." +"\nClick to view map!")
                .setVibrate(pattern)
                .setAutoCancel(true);

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01234, mBuilder.build());
    }
}
