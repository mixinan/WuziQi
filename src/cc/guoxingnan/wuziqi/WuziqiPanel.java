package cc.guoxingnan.wuziqi;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class WuziqiPanel extends View{
	/*
	 * ���̵Ŀ��
	 */
	private int mPanelWidth;
	/*
	 * ÿ�����ӵĸ߶�
	 */
	private float mLineHeight;
	/*
	 * ������
	 */
	private int MAX_LINE = 10;
	/*
	 * ÿһ���������������ͬ��ɫ������
	 */
	private int MAX_COUNT_IN_LINE = 5;
	/*
	 * ��ʼ�������ʡ��ڰ�����
	 */
	private Paint mPaint = new Paint();
	private Bitmap mWhitePiece;
	private Bitmap mBlackPiece;
	/*
	 * ���ӳߴ����о�ı���
	 */
	private float ratioPieceOfLineHeight = 3*1.0f/4;
	/*
	 * �Ƿ��ֵ�����
	 */
	private boolean mIsWhite = true;
	/*
	 * ���弯�ϡ����弯��
	 */
	private ArrayList<Point> mWhiteArray = new ArrayList<Point>();
	private ArrayList<Point> mBlackArray = new ArrayList<Point>();
	/*
	 * ��Ϸ�Ƿ����
	 */
	private boolean mIsGameOver;
	/*
	 * �Ƿ�����ʤ
	 */
	private boolean mIsWhiteWinner;
	
	/**
	 * ���������Ĺ��췽��
	 * @param context
	 * @param attrs
	 */
	public WuziqiPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	/**
	 * ��ʼ�����ʡ�����
	 */
	private void init() {
		mPaint.setColor(0x88000000);
		mPaint.setAntiAlias(true); //�������
		mPaint.setDither(true); //�Ƿ�ʹ��ͼ�񶶶�������ʹ���Ƴ�����ͼƬ��ɫ����ƽ���ͱ�����ͼ��������� 
		mPaint.setStyle(Paint.Style.STROKE);
		
		mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
		mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
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
		// ���̵Ŀ��
		mPanelWidth = w;
		// �о�=���̿�ȡ�����
		mLineHeight = mPanelWidth *1.0f / MAX_LINE;            
		// ���ӵĿ��=�о������
		int pieceWidth = (int) (mLineHeight*ratioPieceOfLineHeight);
		// �����������������ӵĳߴ�
		mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
		mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// �����Ϸ�Ѿ���������ִ�к���Ĵ��룬�����κβ���
		if (mIsGameOver) return false;
		
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			int y = (int) event.getY();

			Point p = getValidPoint(x, y);
			// ������֮���������ӣ���ִ�к���Ĵ��룬�����κβ���
			if (mWhiteArray.contains(p) || mBlackArray.contains(p)) return false;
			// ����ֵ����壬����ð����ڴˣ�����ź���
			if (mIsWhite) mWhiteArray.add(p);
			else mBlackArray.add(p);
			
			// ���»��ƽ���
			invalidate();
			// ���ֵ�������
			mIsWhite = !mIsWhite;
		}
		
		return true;
	}
	
	
	private Point getValidPoint(int x,int y){
		return new Point((int)(x/mLineHeight), (int)(y/mLineHeight));
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBoard(canvas);
		drawPieces(canvas);
		checkGameOver();
	}


	private void checkGameOver() {
		boolean whiteWin = checkFiveInLine(mWhiteArray);
		boolean blackWin = checkFiveInLine(mBlackArray);
		
		if (whiteWin || blackWin) {
			mIsGameOver = true;
			mIsWhiteWinner = whiteWin;
			String text = mIsWhiteWinner?"����ʤ��":"����ʤ��";
			Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
		}
	}


	private boolean checkFiveInLine(List<Point> points) {
		for (Point p : points) {
			int x = p.x;
			int y = p.y;
			
			boolean win = checkHorizontal(x,y,points);
			if (win) return true;
			win = checkVertical(x,y,points);
			if (win) return true;
			win = checkLeftDiagonal(x, y, points);
			if (win) return true;
			win = checkRightDiagonal(x, y, points);
			if (win) return true;
		}
		return false;
	}

	/**
	 * �ж�ˮƽ�����Ƿ��ѳɹ�
	 * @param x
	 * @param y
	 * @param points
	 * @return
	 */
	private boolean checkHorizontal(int x, int y, List<Point> points) {
		int count = 1;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x-i, y))) {
				count++;
			}else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE) return true;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x+i, y))) {
				count++;
			}else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE) return true;
		return false;
	}
	
	/**
	 * �жϴ�ֱ�����Ƿ��ѳɹ�
	 * @param x
	 * @param y
	 * @param points
	 * @return
	 */
	private boolean checkVertical(int x, int y, List<Point> points) {
		int count = 1;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x, y-i))) {
				count++;
			}else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE) return true;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x, y+i))) {
				count++;
			}else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE) return true;
		return false;
	}
	
	/**
	 * �ж���б�����Ƿ��ѳɹ�
	 * @param x
	 * @param y
	 * @param points
	 * @return
	 */
	private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
		int count = 1;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x-i, y+i))) {
				count++;
			}else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE) return true;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x+i, y-i))) {
				count++;
			}else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE) return true;
		return false;
	}
	
	/**
	 * �ж���б�����Ƿ��ѳɹ�
	 * @param x
	 * @param y
	 * @param points
	 * @return
	 */
	private boolean checkRightDiagonal(int x, int y, List<Point> points) {
		int count = 1;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x-i, y-i))) {
				count++;
			}else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE) return true;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x+i, y+i))) {
				count++;
			}else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE) return true;
		return false;
	}

	/**
	 * ������
	 * @param canvas
	 */
	private void drawPieces(Canvas canvas) {
		for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
			Point whitePoint = mWhiteArray.get(i);
			canvas.drawBitmap(mWhitePiece, 
					(whitePoint.x + (1-ratioPieceOfLineHeight)/2)*mLineHeight, 
					(whitePoint.y + (1-ratioPieceOfLineHeight)/2)*mLineHeight, 
					null);
		}
		for (int i = 0, n = mBlackArray.size(); i < n; i++) {
			Point blackPoint = mBlackArray.get(i);
			canvas.drawBitmap(mBlackPiece, 
					(blackPoint.x + (1-ratioPieceOfLineHeight)/2)*mLineHeight, 
					(blackPoint.y + (1-ratioPieceOfLineHeight)/2)*mLineHeight, 
					null);
		}
	}

	/**
	 * ������
	 * @param canvas
	 */
	private void drawBoard(Canvas canvas) {
		int w = mPanelWidth;
		float lineHeight = mLineHeight;
		for (int i = 0; i < MAX_LINE; i++) {
			// ��߾�=�иߡ�2
			int startX = (int)(lineHeight/2);
			// �ұ߾�
			int endX = (int)(w-lineHeight/2);
			
			int y = (int) ((0.5+i)*lineHeight);
			canvas.drawLine(startX, y, endX, y, mPaint);
			canvas.drawLine(y, startX, y, endX, mPaint);
		}
	}
	
	private static final String INSTANCE = "instance";
	private static final String INSTANCE_GAME_OVER = "instance_game_over";
	private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
	private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";
	
	/**
	 * ���浱ǰ״̬
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
		bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
		bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
		bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);
		return bundle;
	}
	
	/**
	 * �ָ���ǰ״̬
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
			mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
			mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
			return;
		}
		super.onRestoreInstanceState(state);
	}
	
	/**
	 * ����һ��
	 */
	public void start(){
		mWhiteArray.clear();
		mBlackArray.clear();
		mIsGameOver = false;
		mIsWhiteWinner = false;
		invalidate();
	}
}
