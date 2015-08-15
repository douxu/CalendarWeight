package database;

import java.util.ArrayList;

import vo.PayDateTag;
import vo.PayVO;
import vo.ScheduleDateTag;
import vo.ScheduleVO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ScheduleOperation {
	/**
	 * 对日程和支付的操作
	 * @author dou_xu
	 *
	 */
		private DBOpenHelper dbOpenHelper = null;
		//private Context context = null;
		
		public ScheduleOperation(Context context){
			//this.context = context;
			dbOpenHelper = new DBOpenHelper(context, "schedules.db");
		}
		
		/**
		 * 保存日程信息
		 * @param scheduleVO
		 */
		public int save(ScheduleVO scheduleVO){
			//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("scheduleTypeID", scheduleVO.getScheduleTypeID());
			values.put("remindID", scheduleVO.getRemindID());
			values.put("scheduleContent", scheduleVO.getScheduleContent());
			values.put("scheduleDate", scheduleVO.getScheduleDate());
			db.beginTransaction();
			int scheduleID = -1;
			try{
				db.insert("schedule", null, values);
			    Cursor cursor = db.rawQuery("select max(scheduleID) from schedule", null);
			    //rawQuery语句用于执行数据库的select语句
			    if(cursor.moveToFirst()){
			    	scheduleID = (int) cursor.getLong(0);//获取第一列的值，以long类型返回
			    }
			    cursor.close();
			    db.setTransactionSuccessful();
			}finally{
				db.endTransaction();
			}
		    return scheduleID;
		}
		/**
		 * 保存支付信息
		 * @param payvo
		 * @return
		 */
		public int savePay(PayVO payvo){
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("use", payvo.getuse());
			values.put("cost", payvo.getcost());
			db.beginTransaction();
			int payID = -1;
			try{
				db.insert("pay",null,values);
				Cursor cursor = db.rawQuery("select max(payID) from pay", null);
				if(cursor.moveToFirst()){
					payID = (int)cursor.getLong(0);
				}
				cursor.close();
				db.setTransactionSuccessful();
			}finally{
				db.endTransaction();
			}
			return payID;
		}
		
		/**
		 * 查询某一条日程信息
		 * @param scheduleID
		 * @return
		 */
		public ScheduleVO getScheduleByID(int scheduleID){
			//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			Cursor cursor = db.query("schedule", new String[]{"scheduleID","scheduleTypeID","remindID","scheduleContent","scheduleDate"}, "scheduleID=?", new String[]{String.valueOf(scheduleID)}, null, null, null);
			if(cursor.moveToFirst()){
				int schID = cursor.getInt(cursor.getColumnIndex("scheduleID"));
				int scheduleTypeID = cursor.getInt(cursor.getColumnIndex("scheduleTypeID"));
				int remindID = cursor.getInt(cursor.getColumnIndex("remindID"));
				String scheduleContent = cursor.getString(cursor.getColumnIndex("scheduleContent"));
				String scheduleDate = cursor.getString(cursor.getColumnIndex("scheduleDate"));
				cursor.close();
				return new ScheduleVO(schID,scheduleTypeID,remindID,scheduleContent,scheduleDate);
			}
			cursor.close();
			return null;
			
		}
		
		/**
		 * 查询所有的日程信息
		 * @return
		 */
		public ArrayList<ScheduleVO> getAllSchedule(){
			ArrayList<ScheduleVO> list = new ArrayList<ScheduleVO>();
			//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
			SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("schedule", new String[]{"scheduleID","scheduleTypeID","remindID","scheduleContent","scheduleDate"}, null, null, null, null, "scheduleID desc");
			while(cursor.moveToNext()){
				int scheduleID = cursor.getInt(cursor.getColumnIndex("scheduleID")); 
				int scheduleTypeID = cursor.getInt(cursor.getColumnIndex("scheduleTypeID"));
				int remindID = cursor.getInt(cursor.getColumnIndex("remindID"));
				String scheduleContent = cursor.getString(cursor.getColumnIndex("scheduleContent"));
				String scheduleDate = cursor.getString(cursor.getColumnIndex("scheduleDate"));
				ScheduleVO vo = new ScheduleVO(scheduleID,scheduleTypeID,remindID,scheduleContent,scheduleDate);
				list.add(vo);
			}
			cursor.close();
			if(list != null && list.size() > 0){
				return list;
			}
			return null;
			
		}
		/**
		 * 查询指定日期的所有日程
		 * @param year
		 * @param month
		 * @param day
		 * @return
		 */
		public ArrayList<ScheduleVO>getDateSchedule(int year, int month, int day){
			ArrayList<ScheduleVO> list = new ArrayList<ScheduleVO>();
			SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("schedule",new String[]{"scheduleID","scheduleTypeID","remindID","scheduleContent","scheduleDate"},
			 "year=? and month=? and day=?",new String[]{String.valueOf(year),String.valueOf(month),String.valueOf(day)}, null, null, null);
			while(cursor.moveToNext()){
				int scheduleID = cursor.getInt(cursor.getColumnIndex("scheduleID")); 
				int scheduleTypeID = cursor.getInt(cursor.getColumnIndex("scheduleTypeID"));
				int remindID = cursor.getInt(cursor.getColumnIndex("remindID"));
				String scheduleContent = cursor.getString(cursor.getColumnIndex("scheduleContent"));
				String scheduleDate = cursor.getString(cursor.getColumnIndex("scheduleDate"));
				ScheduleVO vo = new ScheduleVO(scheduleID,scheduleTypeID,remindID,scheduleContent,scheduleDate);
				list.add(vo);
			}
			cursor.close();
			if(list!=null &&list.size() > 0){
			return list;
			}
			return null;
		}
		
		
		
		/**
		 * 用于检查被点击日期是否有支付信息
		 * @param year
		 * @param month
		 * @param day
		 * @return
		 */
		
		public String[] checkexit(int year, int month, int day){
			String[] payIDs = null;
			int i = 0;
			SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("paytagdate", new String[]{"payID"}, "year=? and month=? and day=?",
					new String[]{String.valueOf(year),String.valueOf(month),String.valueOf(day)}, null, null, null);
			payIDs =new String[cursor.getCount()];
			while(cursor.moveToNext()){
				String payID = cursor.getString(cursor.getColumnIndex("payID"));
				payIDs[i] = payID;
				i++;
			}
			cursor.close();
			return payIDs;
		}
		/**
		 * 查询当前月的所有消费
		 * @param year
		 * @param month
		 * @return
		 */
		public int checkMonthCost(int year, int month){
			SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("paytagdate", new String[]{"payID"}, "year=? and month=?",
					new String[]{String.valueOf(year),String.valueOf(month)}, null, null, null);
			int i = 0;
			int totalcost = 0;
//			String[] payIDs = null;
//			payIDs = new String[cursor.getCount()];
			while(cursor.moveToNext()){
				String payID = cursor.getString(cursor.getColumnIndex("payID"));
				//payIDs[i] =payID; 
				//i++;
				Cursor cursor1= db.query("pay", new String[]{"cost"}, "payID=?", new String[]{payID}, null, null, null);
				while(cursor1.moveToNext()){
					totalcost += Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("cost")));
				}
			    cursor1.close();
			}
			cursor.close();
//				Cursor cursor1= db.query("pay", new String[]{"cost"}, "payID=?", payIDs, null, null, null);
//				while(cursor1.moveToNext()){
//					totalcost += Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("cost")));
//				}
//			cursor1.close();
			return totalcost;			
		}
		/**
		 * 查询当前天的所有消费
		 * @param year
		 * @param month
		 * @param day
		 * @return
		 */
		public int checkDayCost(int year, int month,int day){
			SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("paytagdate", new String[]{"payID"}, "year=? and month=? and day=?",
					new String[]{String.valueOf(year),String.valueOf(month),String.valueOf(day)}, null, null, null);
			int i = 0;
			int totalcost = 0;
//			String[] payIDs = null;
//			payIDs = new String[cursor.getCount()];
			while(cursor.moveToNext()){
				String payID = cursor.getString(cursor.getColumnIndex("payID"));
//				payIDs[i] =payID; 
//				i++;
				Cursor cursor1= db.query("pay", new String[]{"cost"}, "payID=?", new String[]{payID}, null, null, null);
				while(cursor1.moveToNext()){
					totalcost += Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("cost")));
				}
			    cursor1.close();
			}
			cursor.close();
//				Cursor cursor1= db.query("pay", new String[]{"cost"}, "payID=?", payIDs, null, null, null);
//				while(cursor1.moveToNext()){
//					totalcost += Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("cost")));
//				}
//			cursor1.close();
			return totalcost;			
		}
	
		
		/**
		 * 删除日程
		 * @param scheduleID
		 */
		public void delete(int scheduleID){
			//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			db.beginTransaction();
			try{
				db.delete("schedule", "scheduleID=?", new String[]{String.valueOf(scheduleID)});
				db.delete("scheduletagdate", "scheduleID=?", new String[]{String.valueOf(scheduleID)});
				db.setTransactionSuccessful();
			}finally{
				db.endTransaction();
			}
		}
		/**
		 * 删除支付信息
		 * @param PayID
		 */
		public void deletePay(int PayID){
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			db.beginTransaction();
			try{
				db.delete("pay","payID=?",new String[]{String.valueOf(PayID)});
				db.delete(" paytagdate", "payID=?", new String[]{String.valueOf(PayID)});
				db.setTransactionSuccessful();
			}finally{
				db.endTransaction();
			}
		}
		
		/**
		 * 更新日程
		 * @param vo
		 */
		public void update(ScheduleVO vo){
			//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("scheduleTypeID", vo.getScheduleTypeID());
			values.put("remindID", vo.getRemindID());
			values.put("scheduleContent", vo.getScheduleContent());
			values.put("scheduleDate", vo.getScheduleDate());
			db.update("schedule", values, "scheduleID=?", new String[]{String.valueOf(vo.getScheduleID())});
		}
		
		/**
		 * 将日程标志日期保存到数据库中
		 * @param dateTagList
		 */
		public void saveTagDate(ArrayList<ScheduleDateTag> dateTagList){
			//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			ScheduleDateTag dateTag = new ScheduleDateTag();
			for(int i = 0; i < dateTagList.size(); i++){
				dateTag = dateTagList.get(i);
				ContentValues values = new ContentValues();
				values.put("year", dateTag.getYear());
				values.put("month", dateTag.getMonth());
				values.put("day", dateTag.getDay());
				values.put("scheduleID", dateTag.getScheduleID());
				db.insert("scheduletagdate", null, values);
			}
		}
		/**
		 * 将支付标志日期保存到数据库中
		 * @param paytaglist
		 */
		public void savaPayTagDate(ArrayList<PayDateTag> paytaglist){
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			PayDateTag pdatetag = new PayDateTag();
			for(int i = 0;i<paytaglist.size();i++){
				pdatetag = paytaglist.get(i);
				ContentValues values = new ContentValues();
				values.put("year", pdatetag.getYear());
				values.put("month", pdatetag.getMonth());
				values.put("day", pdatetag.getDay());
				values.put("payID",pdatetag.getPayID());
				db.insert("paytagdate", null, values);
			}
		}
		
		/**
		 * 只查询出当前月的日程日期
		 * @param currentYear
		 * @param currentMonth
		 * @return
		 */
		public ArrayList<ScheduleDateTag> getTagDate(int currentYear, int currentMonth){
			ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
			//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
			SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("scheduletagdate", new String[]{"tagID","year","month","day","scheduleID"}, "year=? and month=?", new String[]{String.valueOf(currentYear),String.valueOf(currentMonth)}, null, null, null);
			while(cursor.moveToNext()){
				int tagID = cursor.getInt(cursor.getColumnIndex("tagID"));
				int year = cursor.getInt(cursor.getColumnIndex("year"));
				int month = cursor.getInt(cursor.getColumnIndex("month"));
				int day = cursor.getInt(cursor.getColumnIndex("day"));
				int scheduleID = cursor.getInt(cursor.getColumnIndex("scheduleID"));
				ScheduleDateTag dateTag = new ScheduleDateTag(tagID,year,month,day,scheduleID);
				dateTagList.add(dateTag);
				}
			cursor.close();
			if(dateTagList != null && dateTagList.size() > 0){
				return dateTagList;
			}
			return null;
		}
		
		/**
		 * 当点击每一个gridview中item时,查询出此日期上所有的日程标记(scheduleID)
		 * @param year
		 * @param month
		 * @param day
		 * @return
		 */
		public String[] getScheduleByTagDate(int year, int month, int day){
			//ArrayList<ScheduleVO> scheduleList = new ArrayList<ScheduleVO>();
			SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
			//根据时间查询出日程ID（scheduleID），一个日期可能对应多个日程ID
			Cursor cursor = db.query("scheduletagdate", new String[]{"scheduleID"}, "year=? and month=? and day=?", new String[]{String.valueOf(year),String.valueOf(month),String.valueOf(day)}, null, null, null);
			String scheduleIDs[] = null;
			scheduleIDs = new String[cursor.getCount()];
			int i = 0;
			while(cursor.moveToNext()){
				String scheduleID = cursor.getString(cursor.getColumnIndex("scheduleID"));
				scheduleIDs[i] = scheduleID;
				i++;
			}
			cursor.close();
			
			return scheduleIDs;  	
		}
		
		/**
		 *关闭DB
		 */
		public void destoryDB(){
			if(dbOpenHelper != null){
				dbOpenHelper.close();
			}
		}
		
}
