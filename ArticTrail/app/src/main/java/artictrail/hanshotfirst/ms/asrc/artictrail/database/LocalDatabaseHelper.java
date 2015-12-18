package artictrail.hanshotfirst.ms.asrc.artictrail.database;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Location;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Prey;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.User;

/**
 * Local database helper.
 */
public class LocalDatabaseHelper extends SqlLiteOpenHelper {
	private static final String DATABASE_NAME = "local.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 * Default constructor.
	 * @param context The context associated with this application.
	 */
	public LocalDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public List<Class> getSupportedClasses() {
		List<Class> ret = new LinkedList<Class>();
		ret.add(Location.class);
		ret.add(Prey.class);
		ret.add(User.class);
		return ret;
	}

	@Override
	public List<Class> getForceDropTables() {
		List<Class> ret = new LinkedList<Class>();
		ret.add(Location.class);
		ret.add(Prey.class);
		ret.add(User.class);
		return ret;
	}
}
