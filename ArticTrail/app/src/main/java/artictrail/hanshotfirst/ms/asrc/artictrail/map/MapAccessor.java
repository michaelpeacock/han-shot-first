package artictrail.hanshotfirst.ms.asrc.artictrail.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.LocationType;

public class MapAccessor {

    private GoogleMap mGoogleMap;
    public GoogleApiClient mClient;
    private Context mContext;
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;

    public void addMap(GoogleMap map) {
        mGoogleMap = map;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TODO do permission processing here
        }
        else {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    public void initialize(Context context, GoogleApiClient.ConnectionCallbacks callback, GoogleApiClient.OnConnectionFailedListener listener) {
        if (checkGooglePlayServices(context)) {
            buildGoogleApiClient(context, callback, listener);
        }
    }

    public void addPointToMap(Location location, String title, LocationType locationType) {
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(title);

        switch(locationType)
        {
            case ME:
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                break;
            case DOCK:
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                break;
            case FAV:
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                break;
            default:
                break;
        }

        // adding marker
        mGoogleMap.addMarker(marker);
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
}
