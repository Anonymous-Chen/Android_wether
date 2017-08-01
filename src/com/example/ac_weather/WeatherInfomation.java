package com.example.ac_weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherInfomation extends Fragment {

	public static final int SHOW_RESPONSE = 0;
	public static final int SHOW_RESPONSE1 = 1;
	public static final int SHOW_RESPONSE2 = 2;
	private Spinner provinceSpinner = null; // 省（省、直辖市）
	private Spinner citySpinner = null; // 市
	private Button b_share;
	private ArrayAdapter<String> provinceAdapter = null; // 省
	private ArrayAdapter<String> cityAdapter = null; // 市
	private int provincePosition = 0;
	private int cityPosition = 0;
	private int flag = 0;
	private String s_url1;
	private String s_url2;
	private String s_url3;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String response = "";
			Editor editor;
			SharedPreferences sharedPreferences = getActivity()
					.getSharedPreferences("information", Context.MODE_PRIVATE);

			switch (msg.what) {
			case SHOW_RESPONSE:
				flag++;
				response = (String) msg.obj;
				editor = sharedPreferences.edit();
				editor.putString("now", response);
				editor.commit();
				break;
			case SHOW_RESPONSE1:
				flag++;
				response = (String) msg.obj;
				editor = sharedPreferences.edit();
				editor.putString("daily", response);
				editor.commit();

				break;
			case SHOW_RESPONSE2:
				flag++;
				response = (String) msg.obj;
				editor = sharedPreferences.edit();
				editor.putString("suggestion", response);
				editor.commit();
				break;

			}
			if (flag == 3) {
				Toast.makeText(getActivity(), "数据已更新", Toast.LENGTH_SHORT)
						.show();
				Update();
				flag = 0;
			}
		}
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_weatherinformation,
				container, false);

		return view;
	}

	public void onStart() {
		super.onStart();

		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("information", Context.MODE_PRIVATE);

		provincePosition = sharedPreferences.getInt("provincePosition", 1);
		cityPosition = sharedPreferences.getInt("cityPosition", 1);
		Log.d("pc", "aa1   " + provincePosition + "   " + cityPosition);

		provinceSpinner = (Spinner) getActivity().findViewById(R.id.s_province);
		citySpinner = (Spinner) getActivity().findViewById(R.id.s_city);

		Log.d("jo", "aa1");
		ReadExcel("list.xls");

		Log.d("jo", "aa12");
		provinceAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, province);
		provinceSpinner.setAdapter(provinceAdapter);
		provinceSpinner.setSelection(provincePosition, true);

		cityAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, city3[provincePosition]);
		citySpinner.setAdapter(cityAdapter);
		citySpinner.setSelection(cityPosition, true);

		provinceSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						cityAdapter = new ArrayAdapter<String>(getActivity()
								.getApplication(),
								android.R.layout.simple_spinner_item,
								city3[position]);
						citySpinner.setAdapter(cityAdapter);

						provincePosition = position; // 记录当前省级序号，留给下面修改县级适配器时用
						cityPosition = 0;
						SharedPreferences sharedPreferences = getActivity()
								.getSharedPreferences("information",
										Context.MODE_PRIVATE);
						Editor editor = sharedPreferences.edit();
						editor.putInt("provincePosition", provincePosition);
						editor.putInt("cityPosition", cityPosition);
						editor.commit();
						UpdateData();
						Update();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});
		citySpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						cityPosition = position;
						SharedPreferences sharedPreferences = getActivity()
								.getSharedPreferences("information",
										Context.MODE_PRIVATE);
						Editor editor = sharedPreferences.edit();
						editor.putInt("provincePosition", provincePosition);
						editor.putInt("cityPosition", cityPosition);
						editor.commit();
						UpdateData();
						Update();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		Log.d("pc", "aa1" + provincePosition + "   " + cityPosition);

		Log.d("jo", "aa123");

		UpdateData();

		Log.d("jo", s_url1);

		Update();

		Log.d("jo", s_url2);

		b_share = (Button) getActivity().findViewById(R.id.b_share);
		b_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("smsto:");            
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);            
				it.putExtra("sms_body", "102");            
				getActivity().startActivity(it);  
			}
		});
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private void UpdateData() {

		s_url1 = "https://api.seniverse.com/v3/weather/now.json?key=rpk0pgm6z7upjbdr&location="
				+ city2[provincePosition][cityPosition]
				+ "&language=zh-Hans&unit=c";
		s_url2 = "https://api.seniverse.com/v3/weather/daily.json?key=rpk0pgm6z7upjbdr&location="
				+ city2[provincePosition][cityPosition]
				+ "&language=zh-Hans&unit=c&start=0&days=5";
		s_url3 = "https://api.seniverse.com/v3/life/suggestion.json?key=rpk0pgm6z7upjbdr&location="
				+ city2[provincePosition][cityPosition] + "&language=zh-Hans";
		sendRequestWithHttpURLConnection(s_url1, SHOW_RESPONSE);
		sendRequestWithHttpURLConnection(s_url2, SHOW_RESPONSE1);
		sendRequestWithHttpURLConnection(s_url3, SHOW_RESPONSE2);
	}

	private void Update() {
		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("information", Context.MODE_PRIVATE);
		String now = sharedPreferences.getString("now", "0");
		String daily = sharedPreferences.getString("daily", "0");
		String suggestion = sharedPreferences.getString("suggestion", "0");

		if (now.equals("0") || daily.equals("0") || suggestion.equals("0")) {
			Log.d("jo", now);
			Log.d("jo", daily);
			Log.d("jo", suggestion);
		} else {
			Map<String, String> map = new HashMap<String, String>();

			String x = "";
			int pid = 0;

			try {
				// 解析now
				JSONObject jo = new JSONObject(now);
				String s_results = jo.optString("results");
				JSONArray ja = new JSONArray(s_results);
				JSONObject jo1 = ja.getJSONObject(0);
				String s_location = jo1.optString("location");
				String s_now = jo1.optString("now");
				JSONObject jo_location = new JSONObject(s_location);
				JSONObject jo_now = new JSONObject(s_now);

				Log.d("jo", "as1");

				map.put("s_name", jo_location.optString("name"));
				map.put("s_code", jo_now.optString("code"));
				map.put("s_text", jo_now.optString("text"));
				map.put("s_temperature", jo_now.optString("temperature"));
				map.put("s_wind_direction", jo_now.optString("wind_direction"));

				Log.d("jo", "as12");

				// 解析daily
				jo = new JSONObject(daily);
				s_results = jo.optString("results");

				Log.d("jo", s_results);
				ja = new JSONArray(s_results);
				jo1 = ja.getJSONObject(0);
				s_location = jo1.optString("location");
				String s_daily = jo1.optString("daily");
				ja = new JSONArray(s_daily);

				Log.d("jo", "as123");

				for (int i = 0; i < 3; i++) {
					JSONObject jsonObject = ja.getJSONObject(i);

					map.put("date" + i, jsonObject.optString("date"));
					map.put("text_day" + i, jsonObject.optString("text_day"));
					map.put("code_day" + i, jsonObject.optString("code_day"));
					map.put("high" + i, jsonObject.optString("high"));
					map.put("low" + i, jsonObject.optString("low"));

				}

				Log.d("jo", "as134");

				// 解析suggestion

				jo = new JSONObject(suggestion);
				s_results = jo.optString("results");
				ja = new JSONArray(s_results);
				jo1 = ja.getJSONObject(0);
				String s_suggestion = jo1.optString("suggestion");
				jo1 = new JSONObject(s_suggestion);

				s_results = jo1.optString("dressing");
				jo = new JSONObject(s_results);
				map.put("s_dressing_brief", jo.optString("brief"));
				map.put("s_dressing_details", jo.optString("details"));

				s_results = jo1.optString("uv");
				jo = new JSONObject(s_results);
				map.put("s_uv_brief", jo.optString("brief"));
				map.put("s_uv_details", jo.optString("details"));

				s_results = jo1.optString("car_washing");
				jo = new JSONObject(s_results);
				map.put("s_car_washing_brief", jo.optString("brief"));
				map.put("s_car_washing_details", jo.optString("details"));

				s_results = jo1.optString("travel");
				jo = new JSONObject(s_results);
				map.put("s_travel_brief", jo.optString("brief"));
				map.put("s_travel_details", jo.optString("details"));

				s_results = jo1.optString("flu");
				jo = new JSONObject(s_results);
				map.put("s_flu_brief", jo.optString("brief"));
				map.put("s_flu_details", jo.optString("details"));

				s_results = jo1.optString("sport");
				jo = new JSONObject(s_results);
				map.put("s_sport_brief", jo.optString("brief"));
				map.put("s_sport_details", jo.optString("details"));

				Log.d("jo", "as12345");

				// dressing uv car_washing travel flu sport

				TextView tv_tp = (TextView) getActivity().findViewById(
						R.id.tv_ntxet);
				tv_tp.setText(map.get("s_temperature") + "℃");
				TextView tv_nt = (TextView) getActivity().findViewById(
						R.id.tv_nt);
				tv_nt.setText(map.get("s_text"));
				TextView tv_nw = (TextView) getActivity().findViewById(
						R.id.tv_nw);
				tv_nw.setText(map.get("s_wind_direction"));
				TextView tv_ss = (TextView) getActivity().findViewById(
						R.id.tv_ss);
				tv_ss.setText(map.get("s_uv_brief"));

				ImageView iv_now = (ImageView) getActivity().findViewById(
						R.id.iv_now);

				Field field;
				R.drawable drawables;

				try {
					field = R.drawable.class.getField("p" + map.get("s_code"));
					drawables = new R.drawable();
					// 取值
					pid = (Integer) field.get(drawables);
					iv_now.setImageResource(pid);

				} catch (Exception e) {
					e.printStackTrace();
				}

				TextView tv_a1 = (TextView) getActivity().findViewById(
						R.id.tv_a1);
				TextView tv_a2 = (TextView) getActivity().findViewById(
						R.id.tv_a2);
				TextView tv_b1 = (TextView) getActivity().findViewById(
						R.id.tv_b1);
				TextView tv_b2 = (TextView) getActivity().findViewById(
						R.id.tv_b2);
				TextView tv_c1 = (TextView) getActivity().findViewById(
						R.id.tv_c1);
				TextView tv_c2 = (TextView) getActivity().findViewById(
						R.id.tv_c2);

				tv_a1.setText(map.get("text_day" + 0));
				tv_a2.setText(map.get("low" + 0) + "/" + map.get("high" + 0)
						+ "℃");
				tv_b1.setText(map.get("text_day" + 1));
				tv_b2.setText(map.get("low" + 1) + "/" + map.get("high" + 1)
						+ "℃");
				tv_c1.setText(map.get("text_day" + 2));
				tv_c2.setText(map.get("low" + 2) + "/" + map.get("high" + 2)
						+ "℃");

				ImageView iv_a = (ImageView) getActivity().findViewById(
						R.id.iv_a);
				ImageView iv_b = (ImageView) getActivity().findViewById(
						R.id.iv_b);
				ImageView iv_c = (ImageView) getActivity().findViewById(
						R.id.iv_c);

				try {
					field = R.drawable.class.getField("p"
							+ map.get("code_day0"));
					drawables = new R.drawable();
					pid = (Integer) field.get(drawables);
					iv_a.setImageResource(pid);

					field = R.drawable.class.getField("p"
							+ map.get("code_day1"));
					drawables = new R.drawable();
					pid = (Integer) field.get(drawables);
					iv_b.setImageResource(pid);

					field = R.drawable.class.getField("p"
							+ map.get("code_day2"));
					drawables = new R.drawable();
					pid = (Integer) field.get(drawables);
					iv_c.setImageResource(pid);

				} catch (Exception e) {
					e.printStackTrace();
				}

				// dressing uv car_washing travel flu sport s_sport_brief
				// s_sport_details

				TextView tv_d1 = (TextView) getActivity().findViewById(
						R.id.tv_d1);
				TextView tv_d2 = (TextView) getActivity().findViewById(
						R.id.tv_d2);
				TextView tv_u1 = (TextView) getActivity().findViewById(
						R.id.tv_u1);
				TextView tv_u2 = (TextView) getActivity().findViewById(
						R.id.tv_u2);
				TextView tv_cw1 = (TextView) getActivity().findViewById(
						R.id.tv_cw1);
				TextView tv_cw2 = (TextView) getActivity().findViewById(
						R.id.tv_cw2);
				TextView tv_t1 = (TextView) getActivity().findViewById(
						R.id.tv_t1);
				TextView tv_t2 = (TextView) getActivity().findViewById(
						R.id.tv_t2);
				TextView tv_f1 = (TextView) getActivity().findViewById(
						R.id.tv_f1);
				TextView tv_f2 = (TextView) getActivity().findViewById(
						R.id.tv_f2);
				TextView tv_s1 = (TextView) getActivity().findViewById(
						R.id.tv_s1);
				TextView tv_s2 = (TextView) getActivity().findViewById(
						R.id.tv_s2);

				tv_d1.setText("穿衣：" + map.get("s_dressing_brief"));
				tv_d2.setText("" + map.get("s_dressing_details"));
				tv_u1.setText("紫外线：" + map.get("s_uv_brief"));
				tv_u2.setText("" + map.get("s_uv_details"));
				tv_cw1.setText("洗车：" + map.get("s_car_washing_brief"));
				tv_cw2.setText("" + map.get("s_car_washing_details"));
				tv_t1.setText("旅行：" + map.get("s_travel_brief"));
				tv_t2.setText("" + map.get("s_travel_details"));
				tv_f1.setText("感冒：" + map.get("s_flu_brief"));
				tv_f2.setText("" + map.get("s_flu_details"));
				tv_s1.setText("运动：" + map.get("s_sport_brief"));
				tv_s2.setText("" + map.get("s_sport_details"));

				// dressing uv car_washing travel flu sport s_sport_brief
				// s_sport_details

				// Toast.makeText(getActivity(), x, Toast.LENGTH_SHORT).show();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("jo", "4");
				e.printStackTrace();

			}
		}

	}

	private void sendRequestWithHttpURLConnection(final String s_url,
			final int flag) {
		// 开启线程来发起网络请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(s_url);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					// 下面对获取到的输入流进行读取
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					Message message = new Message();
					message.what = flag;
					// 将服务器返回的结果存放到Message中
					message.obj = response.toString();
					handler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	private String[] province = new String[31];
	private String[][] city1 = new String[31][25];
	private String[][] city2 = new String[31][25];
	private String[][] city3 = new String[31][];
	private int[] city4 = new int[31];

	private void ReadExcel(String fileName) {
		try {
			InputStream mInputStream = getResources().getAssets()
					.open(fileName);
			Workbook wb = Workbook.getWorkbook(mInputStream);
			Sheet mSheet = wb.getSheet(0);
			int row = mSheet.getRows();
			int columns = mSheet.getColumns();
			Log.d("WWWW", "Total Row: " + row + ", Total Columns: " + columns);
			int flag = 0;
			for (int i = 0; i < row; i++) {
				Cell temp = mSheet.getCell(0, i);
				String content = temp.getContents();

				Log.d("WWWW", "..error4" + content);

				if (Arrays.asList(province).contains(content)) {
					for (int k = 0; k < flag; k++) {
						if (province[k].equals(content)) {
							Cell temp1 = mSheet.getCell(2, i);
							String content1 = temp1.getContents();
							city1[k][city4[k]] = content1;
							temp1 = mSheet.getCell(3, i);
							content1 = temp1.getContents();
							city2[k][city4[k]] = content1;
							city4[k] += 1;
						}
					}
				} else {
					province[flag] = content;
					city4[flag] = 0;
					Cell temp1 = mSheet.getCell(2, i);
					String content1 = temp1.getContents();
					Log.d("WWWW", "..error5" + content1);
					city1[flag][city4[flag]] = content1;
					temp1 = mSheet.getCell(3, i);
					content1 = temp1.getContents();
					city2[flag][city4[flag]] = content1;
					city4[flag] += 1;
					flag++;
				}
			}

			for (int i = 0; i < 31; i++) {

				city3[i] = new String[city4[i]];
				for (int j = 0; j < city4[i]; j++) {
					Log.d("WWWW", "..qqeror1  " + i + "  " + j + "   "
							+ city1[i][j] + "  " + city4[i]);

					city3[i][j] = city1[i][j];
				}
			}

			wb.close();
			mInputStream.close();
		} catch (BiffException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {

			e.printStackTrace();
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}