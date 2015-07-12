package com.m1905.mobile.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.m1905.mobile.common.HistoryContent;
import com.m1905.mobile.helper.DBHelper;
import com.m1905.mobile.util.LogUtils;

public class HistoryService {
	private DBHelper dbHelper;

	public HistoryService(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 获得所有的观看记录
	 * 
	 * @return
	 */
	public List<HistoryContent> getAllHistory() {
		List<HistoryContent> histories = new ArrayList<HistoryContent>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.beginTransaction();
		try {
			String sql = "select * from " + DBHelper.TABLE_HISTORY;
			Cursor cr = db.rawQuery(sql, null);
			while (cr.moveToNext()) {
				histories.add(new HistoryContent(cr.getString(cr
						.getColumnIndex(DBHelper.FIELD_ID)), cr.getInt(cr
						.getColumnIndex(DBHelper.FIELD_TYPE)), cr.getString(cr
						.getColumnIndex(DBHelper.FIELD_TITLE)), cr.getInt(cr
						.getColumnIndex(DBHelper.FIELD_WATCH_TIME)),
						cr.getString(cr
								.getColumnIndex(DBHelper.FIELD_LOCAL_PAHT))));
			}
			cr.close();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		LogUtils.i("观看记录共" + histories.size() + "条");
		return histories;
	}

	/**
	 * 根据视频id,删除观看记录
	 * 
	 * @param id
	 * @return 成功条数
	 */
	public int deleteHistoryByID(String id) {
		return deleteHistoriesByIDs(new String[] { id });
	}

	/**
	 * 根据视频id删除观看记录
	 * 
	 * @param ids
	 * @return 成功条数
	 */
	public int deleteHistoriesByIDs(String[] ids) {
		int iRlt = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (String id : ids) {
				iRlt += db.delete(DBHelper.TABLE_HISTORY, DBHelper.FIELD_ID
						+ "=? ", new String[] { id });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		LogUtils.i("删除观看记录共" + iRlt + "条");
		return iRlt;
	}

	/**
	 * 是否观看过，根据id
	 * 
	 * @param id
	 * @return
	 */
	public boolean isHistoryByID(String id) {
		return isHistoryByCondition(DBHelper.FIELD_ID, id);
	}

	/**
	 * 是否观看过，根据播放地址
	 * 
	 * @param path
	 * @return
	 */
	public boolean isHistoryByPath(String path) {
		return isHistoryByCondition(DBHelper.FIELD_LOCAL_PAHT, path);
	}

	/**
	 * 是否观看，根据条件
	 * 
	 * @param condition
	 * @param value
	 * @return
	 */
	private boolean isHistoryByCondition(String condition, String value) {
		int count = 0;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.beginTransaction();
		try {
			String sql = "select count(*) from " + DBHelper.TABLE_HISTORY
					+ " where " + condition + "=?";
			Cursor cr = db.rawQuery(sql, new String[] { value });
			cr.moveToFirst();
			count = cr.getInt(0);
			cr.close();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		LogUtils.i("是否观看过：" + (count != 0));
		return count != 0;
	}

	/**
	 * 添加观看记录
	 * 
	 * @param history
	 */
	public void addHistory(HistoryContent history) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor c = db.query(DBHelper.TABLE_HISTORY, new String[]{"id"}, "id=?", new String[]{history.getId()}, null,null,null);
		if(c.moveToNext()){
			c.close();
			db.close();
			updateWatchTimeByCondition("id", history.getId(), history.getWatchTime());
		}else{
			db.beginTransaction();
			try {
				StringBuffer sql = new StringBuffer("insert into ");
				sql.append(DBHelper.TABLE_HISTORY);
				sql.append("(");
				sql.append(DBHelper.FIELD_ID);
				sql.append(",");
				sql.append(DBHelper.FIELD_TYPE);
				sql.append(",");
				sql.append(DBHelper.FIELD_TITLE); 
				sql.append(",");
				sql.append(DBHelper.FIELD_LOCAL_PAHT);
				sql.append(",");
				sql.append(DBHelper.FIELD_WATCH_TIME);
				sql.append(")values(?,?,?,?,?)");
				db.execSQL(
						sql.toString(),
						new Object[] { history.getId(), history.getType(),
								history.getTitle(), history.getLocalPath(),
								history.getWatchTime() });

				db.setTransactionSuccessful();
			} finally {
				c.close();
				db.endTransaction();
				db.close();
			}
		}
		
	}

	/**
	 * 更新观看记录,根据条件
	 * 
	 * @param condition
	 * @param conditionValue
	 * @param watchTime
	 * @return
	 */
	private int updateWatchTimeByCondition(String condition,
			String conditionValue, int watchTime) {
		int iRlt = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			values.put(DBHelper.FIELD_WATCH_TIME, watchTime);
			iRlt += db.update(DBHelper.TABLE_HISTORY, values, condition + "=?",
					new String[] { conditionValue });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		LogUtils.i("观看记录更新" + iRlt + "条");
		return iRlt;
	}

	/**
	 * 更新观看记录,根据视频id
	 * 
	 * @param id
	 * @param watchTime
	 * @return
	 */
	public int updateWatchTimeByID(String id, int watchTime) {
		return updateWatchTimeByCondition(DBHelper.FIELD_ID, id, watchTime);
	}

	/**
	 * 更新观看记录,根据视频地址
	 * 
	 * @param path
	 * @param watchTime
	 * @return
	 */
	public int updateWatchTimeByPath(String path, int watchTime) {
		return updateWatchTimeByCondition(DBHelper.FIELD_ID, path, watchTime);
	}

}
