package artictrail.hanshotfirst.ms.asrc.artictrail.database;

import org.joda.time.DateTime;

import java.util.List;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.LocationTable;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.PreyTable;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators.UserTable;

/**
 * Accessor functions to the database.
 */
public interface DatabaseAccessor {
	/**
	 * Getter.
	 * @return Instance of ReportTable.
	 */
	LocationTable getLocationTable();

	/**
	 * Getter.
	 * @return Instance of SupplementTable.
	 */
	PreyTable getPreyTable();

	/**
	 * Getter.
	 * @return Instance of KestrelWeather.
	 */
	UserTable getUserTable();

	/**
	 * Getter.
	 * @return Get the users current name.
	 */
	String getUserName();

	/**
	 * Force drop all tables from the database.
	 */
	void clearDataTables();
}
