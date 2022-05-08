package com.example.venyomasnaplo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "bp_notification_channel";
    private final int NOTIFICATION_ID = 0;
    private Context mContext;
    private NotificationManager mManager;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        CreateChannel();
    }

    private void CreateChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Vérnyomás notofocation", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.setDescription("Mérd meg a vérnyomsáod!");
        this.mManager.createNotificationChannel(channel);
    }

    public void Send(String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
        builder.setContentTitle("Vérnyomás monitorozó");
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.ic_baseline_add_24);
        this.mManager.notify(NOTIFICATION_ID, builder.build());
    }
}
