package com.m1905.mobile.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.m1905.mobile.common.FavouriteContent;
import com.m1905.mobile.helper.DBHelper;
import com.m1905.mobile.util.LogUtils;

public class FavouriteService {
	private DBHelper dbHelper;

	public FavouriteService(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 获得所有的收藏记录
	 * 
	 * @return
	 */
	public List<FavouriteContent> getAllFavourite() {
		List<FavouriteContent> favorites = new ArrayList<FavouriteContent>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.beginTransaction();
		try {
			String sql = "select * from " + DBHelper.TABLE_FAVOURITE;
			Cursor cr = db.rawQuery(sql, null);
			while (cr.moveToNext()) {
				favorites.add(new FavouriteContent(cr.getString(cr
						.getColumnIndex(DBHelper.FIELD_ID)), cr.getInt(cr
						.getColumnIndex(DBHelper.FIELD_TYPE)), cr.getString(cr
						.getColumnIndex(DBHelper.FIELD_TITLE))));
			}
			cr.close();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		LogUtils.i("收藏记录共" + favorites.size() + "条");
		return favorites;
	}

	/**
	 * 根据视频id删除收藏记录
	 * 
	 * @param id
	 * @return 成功条数
	 */
	public int deleteFavouriteByID(String id) {
		return deleteFavouritesByIDs(new String[] { id });
	}

	/**
	 * 根据视频id删除收藏记录
	 * 
	 * @param ids
	 * @return 成功条数
	 */
	public int deleteFavouritesByIDs(String[] ids) {
		int iRlt = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (String id : ids) {
				iRlt += db.delete(DBHelper.TABLE_FAVOURITE, DBHelper.FIELD_ID
						+ "=? ", new String[] { id });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		LogUtils.i("删除收藏记录共" + iRlt + "条");
		return iRlt;
	}

	/**
	 * 是否收藏
	 * 
	 * @param id
	 * @return
	 */
	public boolean isFavourite(String id) {
		int count = 0;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.beginTransaction();
		try {
			String sql = "select count(*) from " + DBHelper.TABLE_FAVOURITE
					+ " where " + DBHelper.FIELD_ID + "=?";
			Cursor cr = db.rawQuery(sql, new String[] { id });
			cr.moveToFirst();
			count = cr.getInt(0);
			cr.close();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		LogUtils.i("是否收藏过：" + (count != 0));
		return count != 0;
	}

	/**
	 * 添加收藏
	 * 
	 * @param favourite
	 */
	public boolean addFavourite(FavouriteContent favourite) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor c = db.query(DBHelper.TABLE_FAVOURITE,new String[]{"id"},"id=?",new String[]{favourite.getId()},null,null,null);
		if(c.moveToNext()){
			c.close();
			db.close();
			return true;
		}else{
			db.beginTransaction();
			try {
				StringBuffer sql = new StringBuffer("insert into ");
				sql.append(DBHelper.TABLE_FAVOURITE);
				sql.append("(");
				sql.append(DBHelper.FIELD_ID);
				sql.append(",");
				sql.append(DBHelper.FIELD_TYPE);
				sql.append(",");
				sql.append(DBHelper.FIELD_TITLE);
				sql.append(")values(?,?,?)");
				db.execSQL(sql.toString(), new Object[] { favourite.getId(),
						favourite.getType(), favourite.getTitle() });
				db.setTransactionSuccessful();
			} finally {
				c.close();
				db.endTransaction();
				db.close();
				return false;
			}
		}
	}

}
