package artictrail.hanshotfirst.ms.asrc.artictrail.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Location;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Prey;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.User;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.LocationTable;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.PreyTable;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.UserTable;

/**
 * Base class for database helpers.
 */
public abstract class SqlLiteOpenHelper extends OrmLiteSqliteOpenHelper {
	private static final String TAG = SqlLiteOpenHelper.class.getSimpleName();

	/**
	 * Default constructor.
	 * @param context Context.
	 * @param databaseName Database name.
	 * @param factory null.
	 * @param databaseVersion Database version.
	 */
	public SqlLiteOpenHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}

	/**
	 * Return a list of supported classes for this database.
	 * @return List<Class>s supported by this database.
	 */
	public abstract List<Class> getSupportedClasses();

	/**
	 * Retrieve a list of classes/tables that can be forced dropped.
	 * @return List<Class> that can be forced dropped.
	 */
	public abstract List<Class> getForceDropTables();

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			for (Class clazz : getSupportedClasses()) {
				TableUtils.createTable(connectionSource, clazz);
			}
		} catch (SQLException e) {
			Log.e(TAG, "Cannot create database.", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			for (Class clazz : getSupportedClasses()) {
				TableUtils.dropTable(connectionSource, clazz, true);
			}
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(TAG, "Cannot drop database.", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the Dao for an object from the database.
	 * @param <T> The type of object to retrieve from the database.
	 * @param clazz The class representing the object in the database.
	 * @return A Dao object for the requested class if it exists.
	 */
	public <T> Dao<T, Integer> getObjectDao(final Class<T> clazz) {
		Dao<T, Integer> objectDao = null;
		try {
			objectDao = getDao(clazz);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return objectDao;
	}

	/**
	 * Force clear some database tables.
	 */
	public void forceClearTables() {
		for (Class clazz : getForceDropTables()) {
			try {
				TableUtils.clearTable(getConnectionSource(), clazz);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private UserTable mUserTable;
	private PreyTable mPreyTable;
	private LocationTable mLocationTable;

	/**
	 * Getter.
	 * @return Instance of ReportTable.
	 */
	public UserTable getUserTable() {
		if (mUserTable == null) {
			mUserTable = (UserTable) getObjectDao(User.class);
		}
		return mUserTable;
	}

	/**
	 * Getter.
	 * @return Instance of SupplementTable.
	 */
	public PreyTable getPreyTable() {
		if (mPreyTable == null) {
			mPreyTable = (PreyTable) getObjectDao(Prey.class);
		}
		return mPreyTable;
	}

	/**
	 * Getter.
	 * @return Instance of KestrelWeather.
	 */
	public LocationTable getLocationTable() {
		if (mLocationTable == null) {
			mLocationTable = (LocationTable) getObjectDao(Location.class);
		}
		return mLocationTable;
	}
}
