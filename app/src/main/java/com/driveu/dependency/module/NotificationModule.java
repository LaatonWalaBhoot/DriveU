package com.driveu.dependency.module;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.driveu.R;
import com.driveu.activity.MapsActivity;
import com.driveu.dependency.scope.DriveUApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/23/2018.
 */
@Module (includes = ContextModule.class)
public class NotificationModule {

    @Provides
    @DriveUApplicationScope
    public Notification notification(Context context) {
        Intent notificationIntent = new Intent(context, MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        return new NotificationCompat.Builder(context,"Channel ID")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("Working in Background")
                .setContentTitle("DriveUApplication")
                .setContentIntent(pendingIntent).build();
    }
}
