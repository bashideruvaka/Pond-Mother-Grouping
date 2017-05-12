package eruvaka.pmgp.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.android.gms.gcm.GcmListenerService;


/**
 * Created by eruvaka on 24-08-2016.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String LOG_TAG = MyGcmListenerService.class.getSimpleName();
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(LOG_TAG, "onMessageReceived/from:" + from);
        Log.d(LOG_TAG, "onMessageReceived/msg:" + data.get("message"));
        String msg = data.getString("subject");
        String message = data.getString("message");
        String total_message = msg + ":" + message;
        System.out.println("mygcm"+total_message);
        Context context = getBaseContext();
        generateNotification(context, total_message);

    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
       /* try {
            int icon = R.drawable.pg;
            long when = System.currentTimeMillis();
            int mNotificationId = 001;
            Intent notificationIntent = new Intent(context, NotificationActivity.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT
                    );

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            String[] separated = message.split(":");
            String str1 = separated[0];
            String str2 = separated[1];
            Notification notification = mBuilder.setSmallIcon(icon).setTicker("Pond Guard").setWhen(when)
                    .setAutoCancel(true)
                    .setContentTitle("Pond Guard")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(str1 + "\n" + str2))
                    .setContentIntent(resultPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.pg))
                    .setContentText(str1 + "\n" + str2).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(mNotificationId, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }*/



    }
}
