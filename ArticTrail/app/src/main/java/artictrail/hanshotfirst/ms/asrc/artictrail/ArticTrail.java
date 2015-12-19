package artictrail.hanshotfirst.ms.asrc.artictrail;

import android.*;
import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.LocationManager;
import android.location.Criteria;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.UUID;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.DatabaseManager;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.LocationType;
import artictrail.hanshotfirst.ms.asrc.artictrail.map.MapAccessor;
import artictrail.hanshotfirst.ms.asrc.artictrail.dialogs.HunterKillDialog;
import artictrail.hanshotfirst.ms.asrc.artictrail.notifications.CollisionNotificationService;
import eu.hgross.blaubot.android.BlaubotAndroid;
import eu.hgross.blaubot.android.BlaubotAndroidFactory;
import eu.hgross.blaubot.core.IBlaubotDevice;
import eu.hgross.blaubot.core.ILifecycleListener;
import eu.hgross.blaubot.messaging.BlaubotMessage;
import eu.hgross.blaubot.messaging.IBlaubotChannel;
import eu.hgross.blaubot.messaging.IBlaubotMessageListener;

import static android.view.View.VISIBLE;

public class ArticTrail extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = ArticTrail.class.getSimpleName();

    private MapAccessor mMapAccessor;

    private BlaubotAndroid blaubot ;
    private IBlaubotChannel channel;
    private boolean mConnected = false;

    private DatabaseManager mDatabaseManager;

    public ArticTrail() {
        mDatabaseManager = new DatabaseManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artic_trail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMapAccessor = new MapAccessor();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HunterKillDialog hkd = new HunterKillDialog(ArticTrail.this);
                hkd.show();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapAccessor.initialize(this, this, this);

        //Notification Stuff
        startService(new Intent(getBaseContext(), CollisionNotificationService.class));
        initBluetooth();


        initDatabase();
    }

    private void initDatabase() {

    }


    private void initBluetooth() {
        final UUID APP_UUID = UUID.fromString("ec127529-2e9c-4046-a5a5-144feb30465f");
        blaubot = BlaubotAndroidFactory.createBluetoothBlaubot(APP_UUID);

        channel = blaubot.createChannel((short) 1);

        blaubot.addLifecycleListener(new ILifecycleListener() {
            @Override
            public void onDisconnected() {
                // THIS device disconnected from the network
                Log.d(TAG, "BLUETOOTH DISCONNNECTED");
                mConnected = false;
            }

            @Override
            public void onDeviceLeft(IBlaubotDevice blaubotDevice) {
                // ANOTHER device disconnected from the network
                Log.d(TAG, "BLUETOOTH DEVICE LEFT " + blaubotDevice.getReadableName());
            }

            @Override
            public void onDeviceJoined(IBlaubotDevice blaubotDevice) {
                // ANOTHER device connected to the network THIS device is on
                Log.d(TAG, "BLUETOOTH DEVICE JOINED " + blaubotDevice.getReadableName());
            }

            @Override
            public void onConnected() {
                Log.d(TAG, "BLUETOOTH CONNECTED");
                mConnected = true;
                // THIS device connected to a network
                // you can now subscribe to channels and use them:
                channel.subscribe(new IBlaubotMessageListener() {
                    @Override
                    public void onMessage(BlaubotMessage message) {
                        // we got a message - our payload is a byte array
                        // deserialize


                        String msg = new String(message.getPayload());
                        String[] msgParts = msg.split(";");
                        double lat = Double.valueOf(msgParts[0]);
                        double lon = Double.valueOf(msgParts[1]);

                        artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Location dbLoc = new artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Location();

                        dbLoc.setLatitude(lat);
                        dbLoc.setLongitude(lon);
                        dbLoc.setLocationType(LocationType.HUNTER);
                        mDatabaseManager.getLocationTable().create(dbLoc);

                        Log.i("BLUETOOTH_MESSAGE", "Got a location: " + lat + ", " + lon);
                    }
                });

                Location location = getCurrentLocation();
                if(location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    StringBuilder loc = new StringBuilder();
                    loc.append(String.valueOf(lat) + ";" + String.valueOf(lon));

                    channel.publish(loc.toString().getBytes(), true);
                }
                // onDeviceJoined(...) calls will follow for each OTHER device that was already connected
            }

            @Override
            public void onPrinceDeviceChanged(IBlaubotDevice oldPrince, IBlaubotDevice newPrince) {
                // if the network's king goes down, the prince will rule over the remaining peasants
            }

            @Override
            public void onKingDeviceChanged(IBlaubotDevice oldKing, IBlaubotDevice newKing) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMapAccessor.addMap(map);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.artic_trail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.boat_mode:
                showBoatMode();
                return true;
            case R.id.hunt_mode:
                showHuntMode();
                return true;
            case R.id.save_current_location:
                //do some cool stuff
                return true;
            case R.id.sos:
                //help somebody
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mMapAccessor.mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ArticTrail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://artictrail.hanshotfirst.ms.asrc.artictrail/http/host/path")
        );

        if (mMapAccessor.mClient != null) {
            mMapAccessor.mClient.connect();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ArticTrail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://artictrail.hanshotfirst.ms.asrc.artictrail/http/host/path")
        );

        mMapAccessor.mClient.disconnect();
        stopService(new Intent(getBaseContext(), CollisionNotificationService.class));

        blaubot.stopBlaubot();

    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = getCurrentLocation();
        if(location != null) {
            mMapAccessor.centerMapOnCurrentLocation(location);
            mMapAccessor.addPointToMap(location, "Me", LocationType.ME);
        }

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        } else {
//            Location location = LocationServices.FusedLocationApi.getLastLocation(
//                    mMapAccessor.mClient);
//
//            if (location != null) {
//                mMapAccessor.centerMapOnCurrentLocation(location);
//
//                mMapAccessor.addPointToMap(location, "Me", LocationType.ME);
//            }
//        }
    }

    private Location getCurrentLocation() {
        Location ret = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            ret = LocationServices.FusedLocationApi.getLastLocation(mMapAccessor.mClient);
        }
        return ret;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("ARCTIC_TRAIL", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("ARCTIC_TRAIL", "onConnectionFailed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        blaubot.unregisterReceivers(this);
        blaubot.onPause(this); // if activity
        mDatabaseManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        blaubot.startBlaubot();
        blaubot.registerReceivers(this);
        blaubot.setContext(this);
        blaubot.onResume(this);
        mDatabaseManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseManager.onDestroy();
    }

    private void showBoatMode()
    {
        LinearLayout huntlinearLayout = (LinearLayout)findViewById(R.id.hunt_view);
        LinearLayout boatlinearLayout = (LinearLayout)findViewById(R.id.boat_view);
        if(boatlinearLayout.getVisibility() == VISIBLE) {
            boatlinearLayout.setVisibility(View.INVISIBLE);
            huntlinearLayout.setVisibility(View.INVISIBLE);
        }

        else {
            huntlinearLayout.setVisibility(View.INVISIBLE);
            boatlinearLayout.setVisibility(VISIBLE);
        }

    }
    private void showHuntMode()
    {
        LinearLayout huntlinearLayout = (LinearLayout)findViewById(R.id.hunt_view);
        LinearLayout boatlinearLayout = (LinearLayout)findViewById(R.id.boat_view);
        if(huntlinearLayout.getVisibility() == VISIBLE) {
            boatlinearLayout.setVisibility(View.INVISIBLE);
            huntlinearLayout.setVisibility(View.INVISIBLE);

        }
        else {
            boatlinearLayout.setVisibility(View.INVISIBLE);
            huntlinearLayout.setVisibility(VISIBLE);
        }
    }

}
