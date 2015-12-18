package artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.Entity;

/**
 * Created by Owner on 12/18/2015.
 */
@DatabaseTable(daoClass = artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.UserTable.class)
public class User extends Entity {

    @DatabaseField(columnName = "device_name")
    private String mDeviceName;

    @DatabaseField(columnName = "name")
    private String mName;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Prey> preyList;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Location> locationList;

    public User() {
        mDeviceName = "";
        mName = "";
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }
}
