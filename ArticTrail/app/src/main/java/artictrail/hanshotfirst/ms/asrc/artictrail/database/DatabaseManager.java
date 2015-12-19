package artictrail.hanshotfirst.ms.asrc.artictrail.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.LocationTable;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.PreyTable;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.UserTable;

/**
 * Manages the connections to the databases for all databases in this
 * application.
 */
public class DatabaseManager implements DatabaseAccessor, DatabaseLifeCycle {
	private LocalDatabaseHelper mLocalDatabaseHelper;
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The android context associated with this
	 * application.
	 */
	public DatabaseManager(Context context) {
		mContext = context;
		mLocalDatabaseHelper = new LocalDatabaseHelper(context);
	}

	@Override
	public String getUserName() {
		return createUserNameIfNeeded();
	}

	private String createUserNameIfNeeded() {
		String testUuid = UUID.randomUUID().toString();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		String testUserName = prefs.getString("uuid", testUuid);
		if (testUuid.equals(testUserName)) {
			//new user needed
			SharedPreferences.Editor prefEdit = prefs.edit();
			String newUuid = UUID.randomUUID().toString();
			prefEdit.putString("uuid", newUuid).commit();
			return newUuid;
		} else {
			return testUserName;
		}
	}

	@Override
	public void clearDataTables() {
		mLocalDatabaseHelper.forceClearTables();
	}

	@Override
	public void onDestroy() {
		mLocalDatabaseHelper = null;
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}

	@Override
	public LocationTable getLocationTable() {
		return mLocalDatabaseHelper.getLocationTable();
	}

	@Override
	public PreyTable getPreyTable() {
		return mLocalDatabaseHelper.getPreyTable();
	}

	@Override
	public UserTable getUserTable() {
		return mLocalDatabaseHelper.getUserTable();
	}
}
