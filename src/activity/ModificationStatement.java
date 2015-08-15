package activity;

import java.util.Calendar;

import borderText.BorderEditText;
import android.widget.EditText;
import android.widget.Toast;

import com.fancyy.calendarweight.R;

import android.view.View;
import android.view.View.OnClickListener;
import constant.Calendarconstant;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class ModificationStatement extends Activity {
	private BorderEditText btx = null;
	String week = null;
	String message = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modificationstatement);
		btx = (BorderEditText)findViewById(R.id.edit);
		final Button determine = (Button) findViewById(R.id.button1);
		final Button cancel = (Button) findViewById(R.id.button2);
		SharedPreferences mySharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
		final SharedPreferences.Editor editor = mySharedPreferences.edit();
		Calendar calendar = Calendar.getInstance(Calendarconstant.tz1);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		Toast.makeText(ModificationStatement.this,String.valueOf(weekday), Toast.LENGTH_SHORT).show();
		//String week = null;
		switch (weekday) {
		case 1:
			week = "sunday";
			break;
		case 2:
			week = "monday";
			break;
		case 3:
			week = "tuesday";
			break;
		case 4:
			week = "wednesday";
			break;
		case 5:
			week = "thursday";
			break;
		case 6:
			week = "friday";
			break;
		case 7:
			week = "saturday";
			break;
		}

		determine.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				new AlertDialog.Builder(ModificationStatement.this).setTitle("修改语句").setMessage("是否确认修改？").setPositiveButton("确认", new 
						android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						message = btx.getText().toString();
						editor.putString(week,message);
						editor.commit();
						Toast.makeText(ModificationStatement.this,"修改成功", Toast.LENGTH_SHORT).show();
						ModificationStatement.this.finish();
					}
				}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
					}
				}).show();
			}
		});
		cancel.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				new AlertDialog.Builder(ModificationStatement.this).setTitle("取消修改").setMessage("是否取消修改？").setPositiveButton("确认", new 
						android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						ModificationStatement.this.finish();
					}
				}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
					}
				}).show();
			}
		});
    }
}