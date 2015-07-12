package com.m1905.mobile.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper {
	private final static int VERSION = 1;
	private final static String DB_NAME = "m1905.db";
	public final static String TABLE_HISTORY = "tab_history";// 表名
	public final static String TABLE_FAVOURITE = "tab_favourite";// 表名
	public final static String FIELD_ID = "id";
	public final static String FIELD_TITLE = "title";
	public final static String FIELD_TYPE = "type";
	public final static String FIELD_LOCAL_PAHT = "local_path";
	public final static String FIELD_WATCH_TIME = "watch_time";

	public DBHelper(Context context) {
		this(context, DB_NAME, null, VERSION);
	}

	public DBHelper(Context context, String name) {
		this(context, name, null, VERSION);
	}

	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_HISTORY
				+ "(_id integer primary key autoincrement , " + FIELD_ID
				+ " varchar(50) , " + FIELD_TYPE + " integer , " + FIELD_TITLE
				+ " varchar(50) , " + FIELD_LOCAL_PAHT + " varchar(50) , "
				+ FIELD_WATCH_TIME + " integer )");
		db.execSQL("create table " + TABLE_FAVOURITE
				+ "(_id integer primary key autoincrement , " + FIELD_ID
				+ " varchar(50) , " + FIELD_TYPE + " integer , " + FIELD_TITLE
				+ " varchar(50) )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_HISTORY);
		db.execSQL("drop table IF exists " + TABLE_FAVOURITE);
	}

}
