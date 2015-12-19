package artictrail.hanshotfirst.ms.asrc.artictrail.notifications;

import android.content.Intent;

import android.app.NotificationManager;
import android.app.Service;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import artictrail.hanshotfirst.ms.asrc.artictrail.R;

public class CollisionNotificationService extends Service {

    private Handler handler = new Handler();
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private final int DETECTION_FREQUENCY = 20000;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Collision Detection Started", Toast.LENGTH_LONG).show();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, DETECTION_FREQUENCY);

                mNotificationBuilder.setSmallIcon(R.drawable.ic_menu_share);
                mNotificationBuilder.setContentTitle("WARNING - Hunter Nearby");
                mNotificationBuilder.setContentText("Use caution");
                mNotificationBuilder.setVibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500});
                mNotificationBuilder.setLights(Color.RED, 3000, 3000);
                Uri alarmSound = Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/"+R.raw.imperial_march);
                mNotificationBuilder.setSound(alarmSound);

                mNotificationManager.notify(42, mNotificationBuilder.build());
            }
        };

        handler.postDelayed(runnable, DETECTION_FREQUENCY);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Collision Detection Stopped", Toast.LENGTH_LONG).show();
    }
}

