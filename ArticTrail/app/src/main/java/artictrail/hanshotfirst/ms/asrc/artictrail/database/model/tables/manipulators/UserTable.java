package artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.User;

/**
 * Created by Owner on 12/18/2015.
 */
public class UserTable extends BaseTable<User> {

    public UserTable(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, User.class);
    }
}
