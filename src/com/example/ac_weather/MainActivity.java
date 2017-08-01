package com.example.ac_weather;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends Activity implements OnClickListener {

	private WebView webView;
	private TPDatabaseHelper dbHelper;
	private List<TPitem> TPitemList = new ArrayList<TPitem>();

	private Button dateBtn = null;
	private Button timeBtn = null;
	private EditText et_date = null;
	private EditText et_time = null;
	private final static int DATE_DIALOG = 0;
	private final static int TIME_DIALOG = 1;
	private final static int DATE_DIALOG1 = 2;
	private final static int TIME_DIALOG1 = 3;
	private final static int TIME_DIALOG2 = 4;
	private Calendar c = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Log.d("11", "111");
		Button b_wi = (Button) findViewById(R.id.b_wi);
		Button b_tp = (Button) findViewById(R.id.b_tp);
		Button b_cr = (Button) findViewById(R.id.b_cr);

		b_wi.setOnClickListener(this);
		b_tp.setOnClickListener(this);
		b_cr.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_wi:
			WeatherInfomation fragment_wi = new WeatherInfomation();
			FragmentManager fragmentManager_wi = getFragmentManager();
			FragmentTransaction transaction_wi = fragmentManager_wi
					.beginTransaction();
			transaction_wi.replace(R.id.fl_fun, fragment_wi);
			transaction_wi.addToBackStack(null);
			transaction_wi.commit();
			break;
		case R.id.b_tp:
			TravelPlan fragment_tp = new TravelPlan();
			FragmentManager fragmentManager_tp = getFragmentManager();
			FragmentTransaction transaction_tp = fragmentManager_tp
					.beginTransaction();
			transaction_tp.replace(R.id.fl_fun, fragment_tp);
			transaction_tp.addToBackStack(null);
			transaction_tp.commit();
			dbHelper = new TPDatabaseHelper(MainActivity.this, "travelplan.db",
					null, 1);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			break;
		case R.id.b_cr:
			CalendarReminder fragment_cr = new CalendarReminder();
			FragmentManager fragmentManager_cr = getFragmentManager();
			FragmentTransaction transaction_cr = fragmentManager_cr
					.beginTransaction();
			transaction_cr.replace(R.id.fl_fun, fragment_cr);
			transaction_cr.addToBackStack(null);
			transaction_cr.commit();
			break;

		default:
			break;
		}
	}

	protected Dialog onCreateDialog(int id) {
		Log.d("jo", "aa31");
		Dialog dialog = null;
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"information", Context.MODE_PRIVATE);
		int date1 = sharedPreferences.getInt("date1", 1);
		int date2 = sharedPreferences.getInt("date2", 1);
		int date3 = sharedPreferences.getInt("date3", 1);
		int time1 = sharedPreferences.getInt("time1", 1);
		int time2 = sharedPreferences.getInt("time2", 1);
		switch (id) {

		case DATE_DIALOG:
			c = Calendar.getInstance();
			dialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker dp, int year,
								int month, int dayOfMonth) {
							et_date = (EditText) findViewById(R.id.et_date);
							et_date.setText(year + "-" + (month + 1) + "-"
									+ dayOfMonth + "");
							SharedPreferences sharedPreferences = getSharedPreferences(
									"information", Context.MODE_PRIVATE);
							Editor editor = sharedPreferences.edit();
							editor.putInt("date1", year);
							editor.putInt("date2", month + 1);
							editor.putInt("date3", dayOfMonth);
							editor.commit();
						}
					}, c.get(Calendar.YEAR), // 传入年份
					c.get(Calendar.MONTH), // 传入月份
					c.get(Calendar.DAY_OF_MONTH) // 传入天数
			);

			break;
		case TIME_DIALOG:
			c = Calendar.getInstance();
			dialog = new TimePickerDialog(
					this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							et_time = (EditText) findViewById(R.id.et_time);
							et_time.setText(hourOfDay + ":" + minute + "");
							SharedPreferences sharedPreferences = getSharedPreferences(
									"information", Context.MODE_PRIVATE);
							Editor editor = sharedPreferences.edit();
							editor.putInt("time1", hourOfDay);
							editor.putInt("time2", minute);
							editor.commit();
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					false);
			break;
		case DATE_DIALOG1:

			c = Calendar.getInstance();
			dialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker dp, int year,
								int month, int dayOfMonth) {
							et_date = (EditText) findViewById(R.id.et_date);
							et_date.setText(year + "-" + (month + 1) + "-"
									+ dayOfMonth + "");
							SharedPreferences sharedPreferences = getSharedPreferences(
									"information", Context.MODE_PRIVATE);
							Editor editor = sharedPreferences.edit();
							editor.putInt("date1", year);
							editor.putInt("date2", month + 1);
							editor.putInt("date3", dayOfMonth);
							editor.commit();
						}
					}, date1, // 传入年份
					date2, // 传入月份
					date3 // 传入天数
			);

			break;
		case TIME_DIALOG1:

			c = Calendar.getInstance();
			dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							et_time = (EditText) findViewById(R.id.et_time);
							et_time.setText(hourOfDay + ":" + minute + "");
							SharedPreferences sharedPreferences = getSharedPreferences(
									"information", Context.MODE_PRIVATE);
							Editor editor = sharedPreferences.edit();
							editor.putInt("time1", hourOfDay);
							editor.putInt("time2", minute);
							editor.commit();
						}
					}, time1, time2, false);
			break;
			
		case TIME_DIALOG2:
			final AlertDialog.Builder	normalDialog = 
            new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.tishi);
        normalDialog.setTitle("我是一个普通Dialog");
        normalDialog.setMessage("你要点击哪一个按钮呢?");
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
        normalDialog.setNegativeButton("关闭", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
			break;
		}
		return dialog;
	}
}
