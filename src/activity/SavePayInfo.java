package activity;

import java.util.ArrayList;
import java.util.Calendar;

import vo.PayDateTag;
import vo.PayVO;
import vo.ScheduleDateTag;
import borderText.BorderEditText;

import com.fancyy.calendarweight.R;

import database.ScheduleOperation;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SavePayInfo extends Activity {
	
	int payYear,payMonth,payDay;
	String use = null;
	String cost = null;
	private EditText edittext;//花费金额
	private BorderEditText bordertextview;//花费内容
	private Button determine;
	private Button cancel;
	private ArrayList<PayDateTag> dateTagList = new ArrayList<PayDateTag>();
	private ScheduleOperation dao = null;
	
	
	public SavePayInfo(){
		dao = new ScheduleOperation(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.savepayinfo);
		edittext = (EditText) findViewById(R.id.edittext);
		bordertextview = (BorderEditText) findViewById(R.id.borderEditText1);
		determine = (Button) findViewById(R.id.button1);
		cancel = (Button) findViewById(R.id.button2);
		
		Intent intent = getIntent();
		payYear = intent.getIntExtra("payYear",2015);
		payMonth = intent.getIntExtra("payMonth",5);
		payDay = intent.getIntExtra("payDay",14);
		
		determine.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(TextUtils.isEmpty(bordertextview.getText().toString()) || 
						TextUtils.isEmpty(edittext.getText().toString())){					
					if(TextUtils.isEmpty(edittext.getText().toString())){
						new AlertDialog.Builder(SavePayInfo.this).setTitle("输入金额").setMessage
						("输入的金额不能为空").setPositiveButton("确定",null).show();
					}else{
						new AlertDialog.Builder(SavePayInfo.this).setTitle("花费内容").setMessage
						("输入的内容不能为空").setPositiveButton("确定",null).show();
					}
				}else{
					use = bordertextview.getText().toString();
					cost = edittext.getText().toString();
					PayVO payvo = new PayVO();
					payvo.setuse(use);
					payvo.setcost(cost);
					int payID = dao.savePay(payvo);
					handledata(payYear,payMonth,payDay,payID);
					dao.savaPayTagDate(dateTagList);
					Toast.makeText(SavePayInfo.this, "插入数据成功", Toast.LENGTH_SHORT).show();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				new AlertDialog.Builder(SavePayInfo.this).setTitle("关闭插入信息").setMessage
				("是否要关闭当前的插入信息").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					SavePayInfo.this.finish();
					}
				}).setNegativeButton("取消",new android.content.DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
					}
				}).show();
			}
		});
	}
	public void handledata(int year,int month,int day,int id){
		PayDateTag dateTag = new PayDateTag();
		dateTag.setYear(year);
		dateTag.setMonth(month);
		dateTag.setDay(day);
		dateTag.setPayID(id);
		dateTagList.add(dateTag);
	}
}
