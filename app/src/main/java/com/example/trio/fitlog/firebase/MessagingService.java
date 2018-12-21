package com.example.trio.fitlog.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.trio.fitlog.MainActivity;
import com.example.trio.fitlog.R;
import com.example.trio.fitlog.utils.PreferencesHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    PreferencesHelper preferencesHelper;
    @Override
    public void onNewToken(String s) {
        Log.d("TOKEN", s);
        preferencesHelper= new PreferencesHelper(getApplicationContext());
        preferencesHelper.setFcm_token(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String msg = remoteMessage.getNotification().getBody();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "my_channel")
                .setContentText(msg)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(sound)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification.build());
    }
}
