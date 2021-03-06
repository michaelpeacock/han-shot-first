package artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.manipulators;

import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.Entity;

/**
 * Base Class for manipulators.
 * @param <T> The Entity type that the table
 */
public class BaseTable<T extends Entity> extends BaseDaoImpl<T, Integer> implements Dao<T, Integer> {
	private static final String TAG = BaseTable.class.getSimpleName();
	private static final int INVALID_DB_ID = -1;

	protected BaseTable(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
		super(connectionSource, clazz);
	}

	@Override
	public List<T> queryForAll() {
		try {
			return super.queryForAll();
		} catch (SQLException exp) {
			Log.e(TAG, "Unable to query for all reports.", exp);
			return Collections.emptyList();
		}
	}

	@Override
	public int create(T data) {
		try {
			return super.create(data);
		} catch (SQLException exp) {
			Log.e(TAG, "Unable to create report in database.", exp);
			return INVALID_DB_ID;
		}
	}

	@Override
	public int update(T data) {
		try {
			data.setUpdateAt(DateTime.now());
			return super.update(data);
		} catch (SQLException exp) {
			Log.e(TAG, "Unable to update report in database.", exp);
			return INVALID_DB_ID;
		}
	}

	@Override
	public int delete(T data) {
		try {
			return super.delete(data);
		} catch (SQLException exp) {
			Log.e(TAG, "Unable to delete report in database.", exp);
			return INVALID_DB_ID;
		}
	}

	@Override
	public T queryForId(Integer integer) {
		try {
			return super.queryForId(integer);
		} catch (SQLException e) {
			Log.e(TAG, "Unable to query for ID.", e);
			return null;
		}
	}

	@Override
	public int deleteById(Integer integer) {
		try {
			return super.deleteById(integer);
		} catch (SQLException e) {
			Log.e(TAG, "Unable to deleteById.", e);
			return INVALID_DB_ID;
		}
	}

	@Override
	public List<T> queryForEq(String fieldName, Object value) {
		try {
			return super.queryForEq(fieldName, value);
		} catch (SQLException e) {
			Log.e(TAG, "Unable to queryForEq", e);
			return Collections.emptyList();
		}
	}

	@Override
	public long countOf() {
		try {
			return super.countOf();
		} catch (SQLException e) {
			Log.e(TAG, "Unable to countOf", e);
			return 0;
		}
	}
}
