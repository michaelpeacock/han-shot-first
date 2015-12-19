package artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.Entity;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.LocationEntity;

/**
 * Created by Owner on 12/18/2015.
 */
@DatabaseTable(daoClass = artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.PreyTable.class)
public class Prey extends LocationEntity {

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "image_path")
    private String imagePath;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "user_id")
    private User user;

    public Prey() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
