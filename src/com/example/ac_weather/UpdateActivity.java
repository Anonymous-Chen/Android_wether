package com.example.ac_weather;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends Fragment {

	private int id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.update_activity, container, false);
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
				String date1 = cursor.getString(cursor.getColumnIndex("date1"));
				String date2 = cursor.getString(cursor.getColumnIndex("date2"));
				String date3 = cursor.getString(cursor.getColumnIndex("date3"));
				String time1 = cursor.getString(cursor.getColumnIndex("time1"));
				String time2 = cursor.getString(cursor.getColumnIndex("time2"));
				String content = cursor.getString(cursor
						.getColumnIndex("content"));
				String tag = cursor.getString(cursor.getColumnIndex("tag"));
				int clockflag = cursor.getInt(cursor
						.getColumnIndex("clockflag"));

				TextView tv_date = (TextView) getActivity().findViewById(
						R.id.tv_date);
				TextView tv_time = (TextView) getActivity().findViewById(
						R.id.tv_time);
				TextView tv_at = (TextView) getActivity().findViewById(
						R.id.tv_at);
				TextView tv_ac = (TextView) getActivity().findViewById(
						R.id.tv_ac);

				tv_date.setText(date1 + "-" + date2 + "-" + date3 + "");
				tv_time.setText(time1 + ":" + time2);
				tv_at.setText(tag);
				tv_ac.setText(content);

			} while (cursor.moveToNext());
		}
		cursor.close();

		Button b_update = (Button) getActivity().findViewById(R.id.b_update);
		Button b_delete = (Button) getActivity().findViewById(R.id.b_delete);
		Button b_back = (Button) getActivity().findViewById(R.id.b_back);
		b_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				TPDatabaseHelper dbHelper = new TPDatabaseHelper(getActivity(),
						"travelplan.db", null, 1);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.delete("travelplan", "id = ?", new String[] { id + "" });

				TravelPlan fragment_tp = new TravelPlan();
				FragmentManager fragmentManager_tp = getFragmentManager();
				FragmentTransaction transaction_tp = fragmentManager_tp
						.beginTransaction();
				transaction_tp.replace(R.id.fl_fun, fragment_tp);
				transaction_tp.addToBackStack(null);
				transaction_tp.commit();

			}
		});
		b_update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Update2 fragment_tp = new Update2();
				FragmentManager fragmentManager_tp = getFragmentManager();
				FragmentTransaction transaction_tp = fragmentManager_tp
						.beginTransaction();
				transaction_tp.replace(R.id.fl_fun, fragment_tp);
				transaction_tp.addToBackStack(null);
				transaction_tp.commit();

			}
		});
		b_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				TravelPlan fragment_tp = new TravelPlan();
				FragmentManager fragmentManager_tp = getFragmentManager();
				FragmentTransaction transaction_tp = fragmentManager_tp
						.beginTransaction();
				transaction_tp.replace(R.id.fl_fun, fragment_tp);
				transaction_tp.addToBackStack(null);
				transaction_tp.commit();
			}
		});

	}
}