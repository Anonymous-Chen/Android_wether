package com.example.ac_weather;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class TravelPlan extends Fragment {

	private TPDatabaseHelper dbHelper;
	private List<TPitem> TPitemList = new ArrayList<TPitem>();
	private ListView lv_tp;
	private TPitemAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_travelplan, container,
				false);
		return view;
	}

	public void onStart() {
		super.onStart();

		dbHelper = new TPDatabaseHelper(getActivity(), "travelplan.db", null, 1);

		initTPitems();

		adapter = new TPitemAdapter(getActivity(), R.layout.activity_item,
				TPitemList);
		lv_tp = (ListView) getActivity().findViewById(R.id.lv_tp);
		lv_tp.setAdapter(adapter);
		lv_tp.setFocusable(true);

		lv_tp.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				TPitem NewTP = TPitemList.get(position);
				Toast.makeText(getActivity(), "短按详情，长按删除", Toast.LENGTH_SHORT)
						.show();
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.delete("travelplan", "id = ?", new String[] { NewTP.getID()
						+ "" });
				TPitemList.remove(position);
				adapter.notifyDataSetChanged();
				return true;
			}
		});

		lv_tp.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TPitem NewTP = TPitemList.get(position);
				Toast.makeText(getActivity(), "短按详情，长按删除" + NewTP.getID(),
						Toast.LENGTH_SHORT).show();
				SharedPreferences sharedPreferences = getActivity()
						.getSharedPreferences("information",
								Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.putInt("id", NewTP.getID());
				editor.commit();
				UpdateActivity fragment_tp = new UpdateActivity();
				FragmentManager fragmentManager_tp = getFragmentManager();
				FragmentTransaction transaction_tp = fragmentManager_tp
						.beginTransaction();
				transaction_tp.replace(R.id.fl_fun, fragment_tp);
				transaction_tp.addToBackStack(null);
				transaction_tp.commit();

			}

		});

		Button b_add = (Button) getActivity().findViewById(R.id.b_add);

		b_add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AddActivity fragment_tp = new AddActivity();
				FragmentManager fragmentManager_tp = getFragmentManager();
				FragmentTransaction transaction_tp = fragmentManager_tp
						.beginTransaction();
				transaction_tp.replace(R.id.fl_fun, fragment_tp);
				transaction_tp.addToBackStack(null);
				transaction_tp.commit();

			}
		});

	}

	private void initTPitems() {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String orderBy = "date1,date2,date3,time1,time2";
		Cursor cursor = db.query("travelplan", null, null, null, null, null,
				orderBy);

		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
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

				TPitem content1 = new TPitem(id, date1, date2, date3, time1,
						time2, tag, content, clockflag);
				TPitemList.add(content1);

			} while (cursor.moveToNext());
		}
		cursor.close();

	}
}