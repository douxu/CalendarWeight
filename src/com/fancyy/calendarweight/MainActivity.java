package com.fancyy.calendarweight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import borderText.BorderTextView;

import com.fancyy.calendarweight.KCalendar.OnCalendarClickListener;
import com.fancyy.calendarweight.KCalendar.OnCalendarDateChangedListener;
import com.fancyy.calendarweight.R;
import com.fancyy.calendarweight.R.drawable;
import com.fancyy.calendarweight.R.id;
import com.fancyy.calendarweight.R.layout;

import constant.Calendarconstant;
import activity.ModificationStatement;
import activity.SavePayInfo;
import activity.ScheduleAll;
import activity.ScheduleInfoView;
import activity.ScheduleView;
import database.ScheduleOperation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import database.DBOpenHelper;
import database.ScheduleOperation;

public class MainActivity extends Activity {

	String date = null;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式
	ScheduleOperation op = new ScheduleOperation(MainActivity.this);
	Calendar thisday = Calendar.getInstance(Calendarconstant.tz1);
	int tadayYear = thisday.get(Calendar.YEAR);
	int tadayMonth = thisday.get(Calendar.MONTH) + 1;
	int tadayDay = thisday.get(Calendar.DATE);
	int payYear = thisday.get(Calendar.YEAR);
	int payMonth = thisday.get(Calendar.MONTH) + 1;
	int payDay = thisday.get(Calendar.DATE);
	boolean commit = false;
	ImageView imageview;
	ListView payview;
	ArrayAdapter<String> adapter;
	List<String> paylist = new ArrayList<String>();
	boolean exitPay;
	String week;
	String getmessage = null;
	String[] payIDs = new String[100];
	String[] IDs = new String[100];
	private BorderTextView message;
	private DBOpenHelper dbhelper;
	private Button bt1;
	private Button bt2;

	protected void onCreate(Bundle savedInstanceState) {
		notification();
		super.onCreate(savedInstanceState);
		SharedPreferences mySharedPreferences = getSharedPreferences("data",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		int flag = mySharedPreferences.getInt("commit", 0);
		if(flag == 0){
			commit =  false;
		}else{
			commit = true;
		}
		//Toast.makeText(MainActivity.this, String.valueOf(commit), Toast.LENGTH_LONG).show();
		if(commit == false){
			editor.putString("monday", "今天是星期一，请努力学习吧");
			editor.putString("tuesday", "今天是星期二，请努力学习吧");
			editor.putString("wednesday", "今天是星期三，请努力学习吧");
			editor.putString("thursday", "今天是星期四，请努力学习吧");
			editor.putString("friday", "今天是星期五，请努力学习吧");
			editor.putString("saturday", "今天是星期六，可以休息一下了");
			editor.putString("sunday", "今天是星期日，可以休息一下了");
			editor.putInt("commit", 1);
			editor.commit();
		}

		/*
		 * AnimationSet animationset = new AnimationSet(true)
		 * view.startAnimation
		 * (AnimationUtils.loadAnimation(getApplicationContext(),
		 * R.anim.fade_in));
		 * ll_popup.startAnimation(AnimationUtils.loadAnimation
		 * (getApplicationContext(), R.anim.push_bottom_in_1));
		 * 
		 * View view = View.inflate(MainActivity.this,
		 * R.layout.popupwindow_calendar, null);
		 */
		setContentView(R.layout.popupwindow_calendar);
		final TextView popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
		final KCalendar calendar = (KCalendar) findViewById(R.id.popupwindow_calendar);
		payview = (ListView) findViewById(R.id.listview);
		imageview = (ImageView) findViewById(R.id.imageview);
		message = (BorderTextView) findViewById(R.id.message);
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);

		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
				+ calendar.getCalendarMonth() + "月");
		int weekday = thisday.get(Calendar.DAY_OF_WEEK);
		// Toast.makeText(MainActivity.this, String.valueOf(weekday),
		// Toast.LENGTH_LONG).show();
		switch (weekday) {
		case 1:
			getmessage = mySharedPreferences.getString("sunday", "1");
			getInfo(getmessage);
			break;
		case 2:
			getmessage = mySharedPreferences.getString("monday", "1");
			getInfo(getmessage);
			break;
		case 3:
			getmessage = mySharedPreferences.getString("tuesday", "1");
			getInfo(getmessage);
			break;
		case 4:
			getmessage = mySharedPreferences.getString("wednesday", "1");
			getInfo(getmessage);
			break;
		case 5:
			getmessage = mySharedPreferences.getString("thursday", "1");
			getInfo(getmessage);
			break;
		case 6:
			getmessage = mySharedPreferences.getString("friday", "1");
			getInfo(getmessage);
			break;
		case 7:
			getmessage = mySharedPreferences.getString("saturday", "1");
			getInfo(getmessage);
			break;
		}
//		Toast.makeText(
//				MainActivity.this,
//				String.valueOf(tadayYear) + String.valueOf(tadayMonth)
//						+ String.valueOf(tadayDay), Toast.LENGTH_LONG).show();
		payIDs = op.checkexit(tadayYear, tadayMonth, tadayDay);
		if (payIDs != null && payIDs.length > 0) {
			IDs = readlist(tadayYear, tadayMonth, tadayDay);// 在没有点击日期前显示但前天列表
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, paylist);
			payview.setAdapter(adapter);
		} else {
			// Toast.makeText(MainActivity.this,"a",Toast.LENGTH_LONG).show();
			payview.setVisibility(View.GONE);
			imageview.setVisibility(View.VISIBLE);
		}
		if (null != date) {

			int years = Integer.parseInt(date.substring(0, date.indexOf("-"))); // 从头开始截取直到找到第一个-为止
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,
					date.lastIndexOf("-")));
			// 截取从第一个-处后一位开始直到下一个-处结束
			popupwindow_calendar_month.setText(years + "年" + month + "月");
			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date,
					R.drawable.calendar_date_focused);
		}

		List<String> list = new ArrayList<String>(); // 设置标记列表
		list.add("2014-04-01");
		list.add("2014-04-02");
		calendar.addMarks(list, 0);

		// 监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				paylist.clear();
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));

				if (calendar.getCalendarMonth() - month == 1// 跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();
					// month指的是要跳转的月份，calendar指的是当前的月份
				} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {
					calendar.removeAllBgColor();
					calendar.setCalendarDayBgColor(dateFormat,
							R.drawable.calendar_date_focused);
					date = dateFormat;// 最后返回给全局 date
					// Toast.makeText(MainActivity.this
					// ,dateFormat,Toast.LENGTH_LONG).show(); 日期抓取成功
				}
				int clickYear = Integer.parseInt(dateFormat.substring(0,
						dateFormat.indexOf("-"))); // 从头开始截取直到找到第一个-为止
				int clickMonth = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));
				// 截取从第一个-处后一位开始直到下一个-处结束
				int clickDay = Integer.parseInt(dateFormat.substring(
						date.lastIndexOf("-") + 1, dateFormat.length()));
				payYear = clickYear;
				payMonth = clickMonth;
				payDay = clickDay;
				// if(oldclickYear!=clickYear&&oldclickMonth!=clickMonth&&oldclickDay!=clickDay){
				// oldclickYear = clickYear;
				// oldclickMonth = clickMonth;
				// oldclickDay = clickDay;
				// }
				// Toast.makeText(
				// MainActivity.this,
				// String.valueOf(clickYear) + "-"
				// + String.valueOf(clickMonth) + "-"
				// + String.valueOf(clickDay), Toast.LENGTH_LONG)
				// .show();
				// Toast.makeText(MainActivity.this,
				// String.valueOf(oldclickYear)+"-"+String.valueOf(oldclickMonth)+
				// "-"+String.valueOf(oldclickDay), Toast.LENGTH_LONG).show();
				payIDs = op.checkexit(clickYear, clickMonth, clickDay);
				if (payIDs != null && payIDs.length > 0) {
					payview.setVisibility(View.VISIBLE);
					imageview.setVisibility(View.GONE);
					IDs = readlist(clickYear, clickMonth, clickDay);
					adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, paylist);
					payview.setAdapter(adapter);
				} else {
					payview.setVisibility(View.GONE);
					imageview.setVisibility(View.VISIBLE);
				}
				String[] scheduleIDs = op.getScheduleByTagDate(clickYear,
						clickMonth, clickDay);
				// 通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
				if (scheduleIDs != null && scheduleIDs.length > 0) {
					// 跳转到显示这一天的所有日程信息界面
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ScheduleInfoView.class);
					intent.putExtra("scheduleID", scheduleIDs);
					intent.putExtra("clickYear", String.valueOf(clickYear));
					intent.putExtra("clickMonth", String.valueOf(clickMonth));
					intent.putExtra("clickDay", String.valueOf(clickDay));
					startActivity(intent);
				} else {
					// Calendar day =
					// Calendar.getInstance(Calendarconstant.tz1);
					thisday.set(clickYear, clickMonth, clickDay);
					// int out = thisday.getFirstDayOfWeek();
					// Toast.makeText(MainActivity.this,String.valueOf(out),
					// Toast.LENGTH_LONG).show();
					// thisday.add(Calendar.YEAR, clickYear);
					// thisday.add(Calendar.MONTH, clickMonth);
					// thisday.add(Calendar.DATE, clickDay);
					int weekday = thisday.get(Calendar.DAY_OF_WEEK) - 1;// 获取星期出错误
					//Toast.makeText(MainActivity.this, String.valueOf(weekday),
							//Toast.LENGTH_LONG).show();
					switch (weekday) {
					case 1:
						week = "星期五";
						break;
					case 2:
						week = "星期六";
						break;
					case 3:
						week = "星期日";
						break;
					case 4:
						week = "星期一";
						break;
					case 5:
						week = "星期二";
						break;
					case 6:
						week = "星期三";
						break;
					case 7:
						week = "星期四";
						break;
					}
					ArrayList<String> scheduleDate = new ArrayList<String>();
					scheduleDate.add(String.valueOf(clickYear));
					scheduleDate.add(String.valueOf(clickMonth));
					scheduleDate.add(String.valueOf(clickDay));
					scheduleDate.add(week);

					Intent intent = new Intent();
					intent.putStringArrayListExtra("scheduleDate", scheduleDate);
					intent.setClass(MainActivity.this, ScheduleView.class);
					startActivity(intent);
				}

			}
		});

		// 监听当前月份
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "年" + month + "月");
			}
		});

		// 上月监听按钮
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.lastMonth();
					}

				});

		// 下月监听按钮
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.nextMonth();
					}
				});
		message.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// 创建onclickListener时要注意是否有多个不同类型的监听事件，如果有，导入全路径名
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("修改语句")
						.setMessage("是否确认修改？")
						.setPositiveButton(
								"确认",
								new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent();
										intent.setClass(MainActivity.this,
												ModificationStatement.class);
										startActivity(intent);
									}
								})
						.setNegativeButton(
								"取消",
								new android.content.DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).show();

				return true;
			}

		});
		/**
		 * 插入信息按钮监听事件
		 */
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("payYear", payYear);
				intent.putExtra("payMonth", payMonth);
				intent.putExtra("payDay", payDay);
				intent.setClass(MainActivity.this, SavePayInfo.class);
				startActivity(intent);
			}
		});
		/**
		 * 显示详情按钮
		 */
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int daycost = op.checkDayCost(payYear, payMonth, payDay);
				int monthcost = op.checkMonthCost(payYear, payMonth);
				if (monthcost == 0 || daycost == 0) {
					if (daycost == 0) {
						new AlertDialog.Builder(MainActivity.this)
								.setTitle("消费详情")
								.setMessage("今天的消费金额是0")
								.setPositiveButton(
										"确认",
										new android.content.DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										}).show();
					} else {
						new AlertDialog.Builder(MainActivity.this)
								.setTitle("消费详情")
								.setMessage("当月并无消费")
								.setPositiveButton(
										"确认",
										new android.content.DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										}).show();
					}
				} else {
					float percent = (daycost/(float)monthcost) * 100;
					//Toast.makeText(MainActivity.this,String.valueOf(percent),Toast.LENGTH_LONG).show();
					new AlertDialog.Builder(MainActivity.this)
							.setTitle("消费详情")
							.setMessage(
									"今天的消费金额是" + daycost + "\n" + "当月总消费是"
											+ monthcost + "\n" + "占当月所有花费的"
											+ percent+"%")
							.setPositiveButton(
									"确认",
									new android.content.DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
				}
			}
		});
	}

	private String[] readlist(int year, int month, int day) {

		dbhelper = new DBOpenHelper(this, "schedules.db");
		int i = 0;
		String[] IDs = null;
		Cursor cursor = null;
		Cursor cursor1 = null;
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		try {
			cursor = db.query("paytagdate", new String[] { "payID" },
					"year=? and month=? and day=?",
					new String[] { String.valueOf(year), String.valueOf(month),
							String.valueOf(day) }, null, null, null);
			IDs = new String[cursor.getCount()];
			while (cursor.moveToNext()) {
				String ID = cursor.getString(cursor.getColumnIndex("payID"));
				IDs[i] = ID;
				i++;
				cursor1 = db.query("pay",
						new String[] { "payID", "use", "cost" }, "payID=?",
						new String[] { ID }, null, null, null);
				while (cursor1.moveToNext()) {
					String use = cursor1.getString(cursor1.getColumnIndex("use"));
					String cost = cursor1.getString(cursor1
							.getColumnIndex("cost"));
					paylist.add("在" + use + "上花费了" + cost);
				}
				cursor1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return IDs;
	}

	private void getInfo(final String s) {
		new AsyncTask<Void, Void, String>() {
			protected String doInBackground(Void... arg0) {
				return s;
			}

			protected void onPostExecute(String result) {
				message.setText(result);
			}
		}.execute((Void[]) null);
	}
	private void notification(){
		int number;
		String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        
        int icon = R.drawable.icon;
        CharSequence tickerText = "日历消息通知";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
		String[] scheduleIDs = op.getScheduleByTagDate(tadayYear,
				tadayMonth, tadayDay);
		CharSequence contentText = null;
		if (scheduleIDs != null && scheduleIDs.length > 0) {
			number = scheduleIDs.length;
			contentText = "今天你有"+number+"条日程安排";
		}else
			contentText = "今天你没有日程安排";
		Context context = getApplicationContext();
        CharSequence contentTitle = "消息提醒";
        Intent notificationIntent = new Intent(this, ScheduleAll.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentIntent);     
        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);
	}

}

