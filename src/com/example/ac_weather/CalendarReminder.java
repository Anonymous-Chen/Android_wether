package com.example.ac_weather;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class CalendarReminder extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_calendarreminder,
				container, false);
		return view;
	}
	
	private CalendarView cv;
	private final static int DATE_DIALOG = 4;
	private int cyear;
	private int cmonth;
	private int cdayOfMonth;
	private String[] items;
	private int[] items1;
	public void onStart() {
		super.onStart();
		cv=(CalendarView)getActivity().findViewById(R.id.cv);
        //为CalendarView组件的日期改变事件添加事件监听器
        cv.setOnDateChangeListener(new OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                    int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                //使用Toast显示用户选择的日期
            	cyear = year;
            	cmonth = month;
            	cdayOfMonth = dayOfMonth;
        		
        		TPDatabaseHelper dbHelper = new TPDatabaseHelper(getActivity(),
        				"travelplan.db", null, 1);
        		SQLiteDatabase db = dbHelper.getWritableDatabase();

        		String x = "data1 = "+cyear+" and data2 = "+cmonth+" and data3 = "+cdayOfMonth;
        		Cursor cursor = db.query("travelplan", null, null, null, null,
        				null, null);
        		int flag = 0;
        		String date1="";
				String date2="";
				String date3="";
        		if (cursor.moveToFirst()) {
        			do {
        				 date1 = cursor.getString(cursor.getColumnIndex("date1"));
        				 date2 = cursor.getString(cursor.getColumnIndex("date2"));
        				 date3 = cursor.getString(cursor.getColumnIndex("date3"));
        				if( date1.equals(cyear+"") && date2.equals((cmonth+1)+"") && date3.equals(cdayOfMonth+"") )
        				{
        					flag++;
        				}
        				Log.d("joo",date1+"   "+date2+"   "+date3+"   "+cyear+"   "+(cmonth+1)+"   "+cdayOfMonth+"   " +flag);
        				
        			} while (cursor.moveToNext());
        		}
        		if (flag!=0){
        			items = new String[flag];
        			items1 = new int[flag];
        			flag = 0;
        			if (cursor.moveToFirst()) {
        				do {
        					date1 = cursor.getString(cursor.getColumnIndex("date1"));
           				 	date2 = cursor.getString(cursor.getColumnIndex("date2"));
           				 	date3 = cursor.getString(cursor.getColumnIndex("date3"));
        					if( date1.equals(cyear+"") && date2.equals((cmonth+1)+"") && date3.equals(cdayOfMonth+"") )
            				{
        						int id = cursor.getInt(cursor.getColumnIndex("id"));
            					String tag = cursor.getString(cursor.getColumnIndex("tag"));
            					items[flag] = tag;
            					items1[flag] = id;
            					flag++;
            				}
        					
        				} while (cursor.moveToNext());
        			}
        			cursor.close();
        			showListDialog();
        		}else{
        			cursor.close();
        			showNormalDialog();
        		}

                
            }
            
           
        });
        
	}
	
	private void  showListDialog(){
		AlertDialog.Builder listDialog = 
		        new AlertDialog.Builder(getActivity());
		    listDialog.setTitle("当天有如下活动，点击查看详情");
		    listDialog.setItems(items, new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            // which 下标从0开始
		            // ...To-do
		        	SharedPreferences sharedPreferences = getActivity()
							.getSharedPreferences("information",
									Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					editor.putInt("id", items1[which]);
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
		    listDialog.show();
	}
	private void showNormalDialog(){
		
		final AlertDialog.Builder normalDialog = 
	            new AlertDialog.Builder(getActivity());
	        
	        normalDialog.setTitle("提示");
	        normalDialog.setMessage("当天没有活动，是否添加");
	        normalDialog.setPositiveButton("添加", 
	            new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	AddActivity fragment_tp = new AddActivity();
					FragmentManager fragmentManager_tp = getFragmentManager();
					FragmentTransaction transaction_tp = fragmentManager_tp
							.beginTransaction();
					transaction_tp.replace(R.id.fl_fun, fragment_tp);
					transaction_tp.addToBackStack(null);
					transaction_tp.commit();
	            }
	        });
	        normalDialog.setNegativeButton("关闭", 
	            new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                
	            }
	        });
	        // 显示
	        normalDialog.show();
		
	}
        
		
}