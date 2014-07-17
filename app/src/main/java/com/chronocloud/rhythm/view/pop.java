package com.chronocloud.rhythm.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.ActionBar.LayoutParams;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.chronocloud.rhythm.R;

public class pop implements OnClickListener,OnItemClickListener, OnDismissListener{
	View view;
	PopupWindow pop;
	private View layout;
	private ListView devList;
	private ImageView progressImage;
	private List<String> devs;
	private OnClickInMain mOnClick;
	private boolean isPlay=true;
	
	private Handler mHandler = new Handler();
	public pop(View view){
		this.view=view;
		layout = View.inflate(view.getContext(), R.layout.activity_main_popup, null);
		pop = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOnDismissListener(this);
		
		initPopContent();
	}
	private void initPopContent() {
		progressImage = (ImageView) layout.findViewById(R.id.pop_img);
		devList = (ListView) layout.findViewById(R.id.pop_list);
		
		progressImage.setOnClickListener(this);
		devList.setOnItemClickListener(this);
		replaceImg();
		
	}
	private void replaceImg() {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>() {

			private Bitmap decodeResource;
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				while(isPlay){
				try {
					if (false) {
						return null;
					}
					Random random = new Random();
					String eq = "t"+random.nextInt(40);
					
					//用反射机制来获取资源中的图片ID和尺寸  
					Field[] fields = R.drawable.class.getDeclaredFields();  
					for (Field field : fields)  
					{  
						if (eq.equals(field.getName())) {
							decodeResource = BitmapFactory.decodeResource(view.getContext().getResources(), field.getInt(R.drawable.class));
						} 
					}  
					
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressImage.setImageBitmap(decodeResource);
							progressImage.invalidate();
						}
					});
					Thread.sleep(750);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				return null;
			}
			protected void onPostExecute(Void result) {
				progressImage.setImageBitmap(decodeResource);
				progressImage.invalidate();
				
			};
			
		}.execute();
//		new Thread(){
//			public void run() {
//				try {
//					sleep(1500);
//					Random random = new Random();
//					String eq = "t"+random.nextInt(40);
//					
//					//用反射机制来获取资源中的图片ID和尺寸  
//					Field[] fields = R.drawable.class.getDeclaredFields();  
//					for (Field field : fields)  
//					{  
//						if (eq.equals(field.getName())) {
//							System.out.println(eq +"::::"+field.getInt(R.drawable.class));
//							Bitmap decodeResource = BitmapFactory.decodeResource(view.getContext().getResources(), field.getInt(R.drawable.class));
//							progressImage.setImageBitmap(decodeResource);
//							progressImage.invalidate();
//						} 
//					}  
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			};
//		}.start();
	}
	/**
	 * 设置pop内容
	 * @param list List<BluetoothDevice>
	 */
	public void setPopContent(List<BluetoothDevice> list){
		List<String> mlist = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			mlist.add(list.get(i).getName());
		}
		
		ArrayAdapter<String> mArrayAdapter=new ArrayAdapter<String>(view.getContext(),  R.layout.text, mlist);
		
		devList.setAdapter(mArrayAdapter);
		devList.invalidate();
	}
	//show
	public void show(){
		pop.showAsDropDown(view);
		
	}
	
	
	
	public void setClickInMain(OnClickInMain click){
		this.mOnClick=click;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		mOnClick.click(position);
		pop.dismiss();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub  图片点击事件
		mOnClick.click(-1);
	}
	
	
	
	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		isPlay=false;
		mOnClick.click(-2);
	}
	
	
	
	
	
	//把事件抛出去 让其在主activity中执行
	public interface OnClickInMain{
		//如果i==-1 则执行刷新操作   else 则是点击操作 要求传回position
		abstract void click(int i);
	}






	
	
}
