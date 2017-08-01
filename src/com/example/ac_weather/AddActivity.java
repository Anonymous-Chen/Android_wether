package com.example.ac_weather;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddActivity extends Fragment {

	private Button dateBtn = null;
	private Button timeBtn = null;

	private final static int DATE_DIALOG = 0;
	private final static int TIME_DIALOG = 1;
	private Calendar c = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_activity, container, false);
		return view;
	}

	public void onStart() {
		super.onStart();

		c = Calendar.getInstance();
		EditText et_date = (EditText) getActivity().findViewById(R.id.et_date);
		et_date.setText(c.get(Calendar.YEAR) + "-"
				+ (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH) + "");
		EditText et_time = (EditText) getActivity().findViewById(R.id.et_time);
		et_time.setText(c.get(Calendar.HOUR_OF_DAY) + ":"
				+ c.get(Calendar.MINUTE) + "");
		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("information", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt("date1", c.get(Calendar.YEAR));
		editor.putInt("date2", (c.get(Calendar.MONTH) + 1));
		editor.putInt("date3", c.get(Calendar.DAY_OF_MONTH));
		editor.putInt("time1", c.get(Calendar.HOUR_OF_DAY));
		editor.putInt("time2", c.get(Calendar.MINUTE));
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

				EditText et_date = (EditText) getActivity().findViewById(
						R.id.et_date);
				EditText et_time = (EditText) getActivity().findViewById(
						R.id.et_time);
				EditText et_at = (EditText) getActivity().findViewById(
						R.id.et_at);
				EditText et_ac = (EditText) getActivity().findViewById(
						R.id.et_ac);

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
				db1.insert("travelplan", null, values1);
				values1.clear();

				TravelPlan fragment_tp = new TravelPlan();
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
				EditText et_date = (EditText) getActivity().findViewById(
						R.id.et_date);
				EditText et_time = (EditText) getActivity().findViewById(
						R.id.et_time);
				EditText et_at = (EditText) getActivity().findViewById(
						R.id.et_at);
				EditText et_ac = (EditText) getActivity().findViewById(
						R.id.et_ac);

				c = Calendar.getInstance();

				et_date.setText(c.get(Calendar.YEAR) + "-"
						+ (c.get(Calendar.MONTH) + 1) + "-"
						+ c.get(Calendar.DAY_OF_MONTH) + "");
				et_time.setText(c.get(Calendar.HOUR_OF_DAY) + ":"
						+ c.get(Calendar.MINUTE));
				et_at.setText("活动概述");
				et_ac.setText("活动详情");

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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

}