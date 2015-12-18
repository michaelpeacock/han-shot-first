package artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Prey;

/**
 * Created by Owner on 12/18/2015.
 */
public class PreyTable extends BaseTable<Prey> {

    public PreyTable(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Prey.class);
    }

}
