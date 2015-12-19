package artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.Entity;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.LocationEntity;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.LocationType;

/**
 * Created by Owner on 12/18/2015.
 */
@DatabaseTable(daoClass = artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.LocationTable.class)
public class Location extends LocationEntity {

    @DatabaseField(columnName = "type", dataType = DataType.ENUM_INTEGER)
    private LocationType locationType;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "user_id")
    private User user;

    public Location() {

    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
