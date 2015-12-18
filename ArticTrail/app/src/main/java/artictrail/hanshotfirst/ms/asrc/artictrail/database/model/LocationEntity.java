package artictrail.hanshotfirst.ms.asrc.artictrail.database.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Owner on 12/18/2015.
 */
public class LocationEntity extends Entity {

    @DatabaseField(columnName = "latitude")
    private double mLatitude;

    @DatabaseField(columnName = "longitude")
    private double mLongitude;

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
