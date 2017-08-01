package com.example.ac_weather;

import java.util.Calendar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Update2 extends Fragment {

	private Button dateBtn = null;
	private Button timeBtn = null;

	private int id;
	private String date1;
	private String date2;
	private String date3;
	private String time1;
	private String time2;
	private String content;
	private String tag;
	private final static int DATE_DIALOG = 2;
	private final static int TIME_DIALOG = 3;
	private Calendar c = null;

	private EditText et_date;
	private EditText et_time;
	private EditText et_at;
	private EditText et_ac;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.update_activity2, container,
				false);
		return view;
	}

	public void onStart() {
		super.onStart();

		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("information", Context.MODE_PRIVATE);
		id = sharedPreferences.getInt("id", 0);

		TPDatabaseHelper dbHelper = new TPDatabaseHelper(getActivity(),
				"travelplan.db", null, 1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor cursor = db.query("travelplan", null, "id = " + id, null, null,
				null, null);

		if (cursor.moveToFirst()) {
			do {
				id = cursor.getInt(cursor.getColumnIndex("id"));
				date1 = cursor.getString(cursor.getColumnIndex("date1"));
				date2 = cursor.getString(cursor.getColumnIndex("date2"));
				date3 = cursor.getString(cursor.getColumnIndex("date3"));
				time1 = cursor.getString(cursor.getColumnIndex("time1"));
				time2 = cursor.getString(cursor.getColumnIndex("time2"));
				content = cursor.getString(cursor.getColumnIndex("content"));
				tag = cursor.getString(cursor.getColumnIndex("tag"));
				int clockflag = cursor.getInt(cursor
						.getColumnIndex("clockflag"));

				et_date = (EditText) getActivity().findViewById(R.id.et_date);
				et_time = (EditText) getActivity().findViewById(R.id.et_time);
				et_at = (EditText) getActivity().findViewById(R.id.et_at);
				et_ac = (EditText) getActivity().findViewById(R.id.et_ac);

				et_date.setText(date1 + "-" + date2 + "-" + date3 + "");
				et_time.setText(time1 + ":" + time2);
				et_at.setText(tag);
				et_ac.setText(content);

			} while (cursor.moveToNext());
		}
		cursor.close();

		Editor editor = sharedPreferences.edit();
		editor.putInt("date1", Integer.parseInt(date1));
		editor.putInt("date2", Integer.parseInt(date2));
		editor.putInt("date3", Integer.parseInt(date3));
		editor.putInt("time1", Integer.parseInt(time1));
		editor.putInt("time2", Integer.parseInt(time2));
		editor.commit();

		dateBtn = (Button) getActivity().findViewById(R.id.dateBtn);
		timeBtn = (Button) getActivity().findViewById(R.id.timeBtn);
		dateBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getActivity().showDialog(DATE_DIALOG);
			}
		});

		timeBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getActivity().showDialog(TIME_DIALOG);
			}
		});

		Button b_save = (Button) getActivity().findViewById(R.id.b_save);
		Button b_reset = (Button) getActivity().findViewById(R.id.b_reset);
		Button b_back = (Button) getActivity().findViewById(R.id.b_back);

		b_save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TextView tv_date =
				// (TextView)getActivity().findViewById(R.id.tv_date);

				et_at = (EditText) getActivity().findViewById(R.id.et_at);
				et_ac = (EditText) getActivity().findViewById(R.id.et_ac);

				SharedPreferences sharedPreferences = getActivity()
						.getSharedPreferences("information",
								Context.MODE_PRIVATE);
				int date1 = sharedPreferences.getInt("date1", 1);
				int date2 = sharedPreferences.getInt("date2", 1);
				int date3 = sharedPreferences.getInt("date3", 1);
				int time1 = sharedPreferences.getInt("time1", 1);
				int time2 = sharedPreferences.getInt("time2", 1);
				String s_at = et_at.getText().toString().trim();
				String s_ac = et_ac.getText().toString().trim();

				TPDatabaseHelper dbHelper = new TPDatabaseHelper(getActivity(),
						"travelplan.db", null, 1);
				SQLiteDatabase db1 = dbHelper.getWritableDatabase();
				ContentValues values1 = new ContentValues();

				values1.put("date1", date1);
				values1.put("date2", date2);
				values1.put("date3", date3);
				values1.put("time1", time1);
				values1.put("time2", time2);
				values1.put("content", s_ac);
				values1.put("tag", s_at);
				values1.put("clockflag", 1);

				db1.update("travelplan", values1, "id = ?", new String[] { id
						+ "" });

				values1.clear();

				UpdateActivity fragment_tp = new UpdateActivity();
				FragmentManager fragmentManager_tp = getFragmentManager();
				FragmentTransaction transaction_tp = fragmentManager_tp
						.beginTransaction();
				transaction_tp.replace(R.id.fl_fun, fragment_tp);
				transaction_tp.addToBackStack(null);
				transaction_tp.commit();
			}
		});
		b_reset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				et_date = (EditText) getActivity().findViewById(R.id.et_date);
				et_time = (EditText) getActivity().findViewById(R.id.et_time);
				et_at = (EditText) getActivity().findViewById(R.id.et_at);
				et_ac = (EditText) getActivity().findViewById(R.id.et_ac);

				et_date.setText(date1 + "-" + date2 + "-" + date3 + "");
				et_time.setText(time1 + ":" + time2);
				et_at.setText(tag);
				et_ac.setText(content);

			}
		});
		b_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				UpdateActivity fragment_tp = new UpdateActivity();
				FragmentManager fragmentManager_tp = getFragmentManager();
				FragmentTransaction transaction_tp = fragmentManager_tp
						.beginTransaction();
				transaction_tp.replace(R.id.fl_fun, fragment_tp);
				transaction_tp.addToBackStack(null);
				transaction_tp.commit();
			}
		});

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

}