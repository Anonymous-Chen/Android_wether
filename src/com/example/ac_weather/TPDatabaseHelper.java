package com.example.ac_weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class TPDatabaseHelper extends SQLiteOpenHelper {
	public static final String CREATE_travelplan = "create table travelplan ("
			+ "id integer primary key autoincrement, " + "time1 text, "
			+ "time2 text, " + "date1 text, " + "date2 text, " + "date3 text, "
			+ "clockflag integer, " + "content text, " + "tag text)";

	private Context mContext;

	public TPDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_travelplan);
		Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// switch (oldVersion) {
		// case 1:
		// db.execSQL(CREATE_CATEGORY);
		// case 2:
		// db.execSQL("alter table Book add column category_id integer");
		// default:
		// }
	}
}