package borderText;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 带边框的EditText
 * @author jack_peng
 *
 */
public class BorderEditText extends EditText{

	public BorderEditText(Context context,AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// 实例化一支画笔
		Paint paint = new Paint();
		paint.setStrokeWidth(1);
		paint.setStyle(Style.STROKE);
		paint.setColor(android.graphics.Color.GRAY);
		paint.setAntiAlias(true);
		RectF rectF = new RectF(2,0,this.getWidth()-2,this.getHeight()-2);
		canvas.drawRoundRect(rectF, 8, 8, paint);
		//绘制圆角矩形drawRoundRect(rect, rx, ry, paint); rectF类型对象，rx x方向上的圆角半径，		
		//ry y方向上的圆角半径paint：绘制时所使用的画笔
	}
}
