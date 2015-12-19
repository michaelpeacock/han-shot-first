package artictrail.hanshotfirst.ms.asrc.artictrail.notifications;

import android.content.Intent;

import android.app.NotificationManager;
import android.app.Service;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;

import com.google.android.gms.maps.model.Marker;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.LocationType;
//import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

import artictrail.hanshotfirst.ms.asrc.artictrail.R;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.DatabaseManager;
import artictrail.hanshotfirst.ms.asrc.artictrail.map.MapAccessor;

public class CollisionNotificationService extends Service {

    public class DbMarkerIdPair {
        public int mDatabaseId;
        public int mMarkerId;

        public DbMarkerIdPair(int databaseId, int markerId){
            mDatabaseId = databaseId;
            mMarkerId = markerId;
        }
    }
    private Handler handler = new Handler();
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private final int DETECTION_FREQUENCY = 10000;
    private DatabaseManager mDatabaseManager;
    private MapAccessor mMapAccessor;
    private ArrayList<DbMarkerIdPair> pairs;

    public CollisionNotificationService() {
        super();
        mDatabaseManager = new DatabaseManager(this);
        pairs = new ArrayList<DbMarkerIdPair>();
    }

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

                List<artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Location> locationList = mDatabaseManager.getLocationTable().queryForAll();

                android.location.Location myLocation = (android.location.Location)MapAccessor.getInstance().getCurrentLocation();

                for (artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Location temp : locationList) {
                    //check if location is within X miles of user
                    Log.d("GARRY", "" + temp.getLatitude() + ", " + temp.getLongitude());
                    Log.d("GARRY", "" + temp.getId());

                    boolean isFound = false;
                    for (int i = 0; i < pairs.size(); i++) {
                        if (pairs.get(i).mDatabaseId == temp.getId()) {
                            MapAccessor.getInstance().removePointFromMap(pairs.get(i).mMarkerId);
                        }
                    }

                    android.location.Location androidLocation = new android.location.Location("temp");
                    androidLocation.setLatitude(temp.getLatitude());
                    androidLocation.setLongitude(temp.getLongitude());
                    int pointId = MapAccessor.getInstance().addPointToMap(androidLocation, "Hunter", LocationType.HUNTER);

                    DbMarkerIdPair dbPair = new DbMarkerIdPair(temp.getId(), pointId);
                    pairs.add(dbPair);

                    Toast.makeText(getApplicationContext(),
                            ""+myLocation.distanceTo(androidLocation), Toast.LENGTH_LONG).show();

                    if (myLocation.distanceTo(androidLocation) < 6) {
                        mNotificationBuilder.setSmallIcon(R.drawable.ic_menu_share);
                        mNotificationBuilder.setContentTitle("WARNING - Hunter Nearby");
                        mNotificationBuilder.setContentText("Use caution");
                        mNotificationBuilder.setVibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500});
                        mNotificationBuilder.setLights(Color.RED, 3000, 3000);
                        Uri alarmSound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.imperial_march);
                        mNotificationBuilder.setSound(alarmSound);

                        mNotificationManager.notify(42, mNotificationBuilder.build());
                    }
                }

                handler.postDelayed(this, DETECTION_FREQUENCY);

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

