package cc.guoxingnan.wuziqi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WuziqiPanel extends View{
	private int mPanelWidth;
	private float mLineHeight;
	private int MAX_LINE = 10;
	private Paint mPaint = new Paint();
	
	public WuziqiPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(0x44ff0000);
		init();
	}
	
	
	private void init() {
		mPaint.setColor(0x88000000);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int width = Math.min(widthSize, heightSize);
		
		if (widthMode == MeasureSpec.UNSPECIFIED) {
			width = heightSize;
		}else if (heightMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
		}
		
		setMeasuredDimension(width, width);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mPanelWidth = w;
		mLineHeight = mPanelWidth *1.0f / MAX_LINE;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBoard(canvas);
	}


	private void drawBoard(Canvas canvas) {
		int w = mPanelWidth;
		float lineHeight = mLineHeight;
		for (int i = 0; i < MAX_LINE; i++) {
			int startX = (int)(lineHeight/2);
			int endX = (int)(w-lineHeight/2);
			
			int y = (int) ((0.5+i)*lineHeight);
			canvas.drawLine(startX, y, endX, y, mPaint);
			canvas.drawLine(y, startX, y, endX, mPaint);
		}
	}

}