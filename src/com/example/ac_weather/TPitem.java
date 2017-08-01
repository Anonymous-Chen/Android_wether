package com.example.ac_weather;

public class TPitem {

	private String date1;
	private String date2;
	private String date3;
	private String time1;
	private String time2;
	private String tag;
	private String content;
	private int clock_flag;
	private int id;

	public TPitem(int id, String date1, String date2, String date3,
			String time1, String time2, String tag, String content,
			int clock_flag) {
		this.id = id;
		this.date1 = date1;
		this.date2 = date2;
		this.date3 = date3;
		this.time1 = time1;
		this.time2 = time2;
		this.tag = tag;
		this.content = content;
		this.clock_flag = clock_flag;
	}

	public int getID() {
		return id;
	}

	public String getDate1() {
		return date1;
	}

	public String getDate2() {
		return date2;
	}

	public String getDate3() {
		return date3;
	}

	public String getTime1() {
		return time1;
	}

	public String getTime2() {
		return time2;
	}

	public String getTag() {
		return tag;
	}

	public String getContent() {
		return content;
	}

	public int getClock_flag() {
		return clock_flag;
	}
}