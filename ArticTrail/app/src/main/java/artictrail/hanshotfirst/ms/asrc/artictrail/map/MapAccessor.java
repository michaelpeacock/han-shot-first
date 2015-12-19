package artictrail.hanshotfirst.ms.asrc.artictrail.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

import artictrail.hanshotfirst.ms.asrc.artictrail.R;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.LocationType;

public class MapAccessor implements Serializable {

    private static MapAccessor instance;
    private GoogleMap mGoogleMap;
    public GoogleApiClient mClient;
    private Context mContext;
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private Marker[] markers;
    private int top = 0;
    private int destinationPointId;
    private boolean destinationEnabled = false;

    private MapAccessor() {
        super();

        markers = new Marker[10000];
        destinationPointId = 0;
    }

    public static synchronized MapAccessor getInstance() {
        if(instance == null)
            instance = new MapAccessor();
        return instance;
    }

    public void addMap(GoogleMap map) {
        mGoogleMap = map;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TODO do permission processing here
        }
        else {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (destinationEnabled) {
                        Location l = new Location("Destination");
                        l.setLatitude(latLng.latitude);
                        l.setLongitude(latLng.longitude);
                        if (destinationPointId > 0) {
                            removePointFromMap(destinationPointId);
                        }
                        destinationPointId = addPointToMap(l, "Destination", LocationType.DESTINATION);
                        destinationEnabled = false;
                    }
                }
            });
        }
    }

    public void initialize(Context context, GoogleApiClient.ConnectionCallbacks callback, GoogleApiClient.OnConnectionFailedListener listener) {
        if (checkGooglePlayServices(context)) {
            buildGoogleApiClient(context, callback, listener);
        }
    }

    public int addPointToMap(Location location, String title, LocationType locationType) {
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(title);

        switch(locationType)
        {
            case ME:
                marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.darth_vader));
                break;
            case DOCK:
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                break;
            case FAV:
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                break;
            case DESTINATION:
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                break;
            case HUNTER:
                marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.boba_fett));
                break;
            default:
                break;
        }

        // adding marker
        Marker m = mGoogleMap.addMarker(marker);
        markers[top] = m;
        top++;

        return(top - 1);
    }

    public void removePointFromMap(int markerId) {
        markers[markerId].remove();
    }

    public void centerMapOnCurrentLocation(Location location) {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                .zoom(18)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
    }

    public boolean checkGooglePlayServices(Context context) {

        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);

        return true;
    }

    protected synchronized void buildGoogleApiClient(
            Context context, GoogleApiClient.ConnectionCallbacks callback, GoogleApiClient.OnConnectionFailedListener listener) {
        mClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(callback)
                .addOnConnectionFailedListener(listener)
                .addApi(LocationServices.API)
                .build();

        mContext = context;
    }

    public Location getCurrentLocation() {
        Location ret = null;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            ret = LocationServices.FusedLocationApi.getLastLocation(MapAccessor.getInstance().mClient);
        }

        return ret;
    }

    public void setDestinationToggle(boolean isDestinationValid) {
        destinationEnabled = isDestinationValid;
    }
}
