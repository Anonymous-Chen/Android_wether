package com.example.ac_weather;

import java.util.List;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TPitemAdapter extends ArrayAdapter<TPitem> {
	private int resourceId;

	public TPitemAdapter(Context context, int textViewResourceId,
			List<TPitem> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TPitem tpitem = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
		TextView tv_tag = (TextView) view.findViewById(R.id.tv_tag);
		tv_date.setText(tpitem.getDate1() + "-" + tpitem.getDate2() + "-"
				+ tpitem.getDate3() + "   " + tpitem.getTime1() + ":"
				+ tpitem.getTime2());
		tv_tag.setText(tpitem.getTag());
		return view;
	}
}
