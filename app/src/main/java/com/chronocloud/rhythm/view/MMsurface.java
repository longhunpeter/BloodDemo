package com.chronocloud.rhythm.view;

import java.util.LinkedList;
import java.util.List;

import android.R.integer;
import android.R.plurals;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MMsurface extends SurfaceView{
	private int height,width;
	private SurfaceHolder holder;
	private boolean isFirstCreat=true;
//	private Paint paint;
	private LinkedList<Point> pList;
	private int X_INTERVAL=40;
	//默认在控件中间
	private int DefaultCenterVertivalPoint=width/2;
	//设置多少数据量为中心点
	private int Number2Zero=0;

	public MMsurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public MMsurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public MMsurface(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	private void init(){
		holder = getHolder();
		pList = new LinkedList<Point>(); 
	}
	//一起加载的心电图
	public void showAll(float[] arr){
		Canvas lock = holder.lockCanvas(null);
		lock.drawColor(Color.BLACK);
		holder.unlockCanvasAndPost(lock);
		Canvas mCanvas=holder.lockCanvas(new Rect(0,0, arr.length*20+1, height));
		
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		for(int i = 0; i<pList.size()-1; i++){
			if (isFirstCreat) {
				mCanvas.drawLine(pList.get(i).x, pList.get(i).y, pList.get(i+1).x,height/2, paint);
				isFirstCreat=false;
			}else
				mCanvas.drawLine(pList.get(i).x, pList.get(i).y, pList.get(i+1).x, pList.get(i+1).y, paint);
		}
		
	}
	//实时画心电图
	public void showSingle(int data){
		System.out.println("data= "+data);
		Canvas lock = holder.lockCanvas(null);
		lock.drawColor(Color.BLACK);
		holder.unlockCanvasAndPost(lock);
		Canvas mCanvas=holder.lockCanvas(new Rect(0,0, width, height));
		prepareLine(data*3);
		drawCurve(mCanvas);
		holder.unlockCanvasAndPost(mCanvas);
	}
	private void drawCurve(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		//canvas.drawLines(line, paint);
		
		if(pList.size() >= 3){
			for(int i = 0; i<pList.size()-1; i++){
				if (isFirstCreat) {
					canvas.drawLine(pList.get(i).x, height/2, pList.get(i+1).x,height/2, paint);
					isFirstCreat=false;
				}else
				canvas.drawLine(pList.get(i).x, pList.get(i).y, pList.get(i+1).x, pList.get(i+1).y, paint);
			}
		}
	}
	
	private void prepareLine(int y){
		System.out.println(y+"y");
		int py = height/2-y+Number2Zero;
		Point p;
		if (isFirstCreat) {
			 p = new Point(0 , height/2 );
		}else{
			 p=new Point(width,py);
		}
		if(pList.size() > width/20){
			pList.remove(0);
			for(int i = 0; i<width/20-1; i++){
				if(i == 0) pList.get(i).x -= (X_INTERVAL );
				else
					pList.get(i).x -= X_INTERVAL;
			}
			pList.addLast(p);
		}
		else{
			for(int i = 0; i<pList.size()-1; i++){
				if (i==0) {
					
				}else
				pList.get(i).x -= X_INTERVAL;
			}
			pList.addLast(p);
		}

	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		height=getMeasuredHeight();
		width=getMeasuredWidth();
	}

	public int getNumber2Zero() {
		return Number2Zero;
	}

	public void setNumber2Zero(int number2Zero) {
		Number2Zero = number2Zero;
	}

	public int getDefaultCenterVertivalPoint() {
		return DefaultCenterVertivalPoint;
	}

	public void setDefaultCenterVertivalPoint(int defaultCenterVertivalPoint) {
		DefaultCenterVertivalPoint = defaultCenterVertivalPoint;
	}

}
