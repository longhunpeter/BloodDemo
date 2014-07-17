package com.chronocloud.rhythm.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 全局方法
 * 
 * @author Administrator
 * @author ljw
 */
public class GlobalMethod {
	public static final int MIN = 0;
	public static final int NORMAL = 1;
	public static final int MAX = 2;

	/**
	 * 得到屏幕属性
	 * 
	 * @param context
	 * @return
	 */
	public static Display getDisplay(Context context) {

		return ((Activity) context).getWindowManager().getDefaultDisplay();
	}

	/**
	 * 获取屏幕属性的宽
	 * 
	 * @param context
	 * @return
	 */
	public static int getDisplayWidth(Context context) {
		return ((Activity) context).getWindowManager().getDefaultDisplay()
				.getWidth();
	}

	/**
	 * 获取屏幕属性的高
	 * 
	 * @param context
	 * @return
	 */
	public static int getDisplayHeight(Context context) {
		return ((Activity) context).getWindowManager().getDefaultDisplay()
				.getHeight();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px 的单位 转成为 dp
	 * 
	 * @param context
	 * @return
	 */
	public static float px2dip(Context context, int pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 判断是否连接网络
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean CheckNetWork(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			return false;
		} else {
			return true;
		}
	}

	/***
	 * 
	 * @description 把图片的Uri地址转换成Bitmap
	 * @author ljw 2014-3-5 下午2:04:10
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap getBitmapFromUri(Context context, Uri uri) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					context.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			Log.e("[MainActivity]", e.getMessage());
			Log.e("[MainActivity]", "目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 得到string资源
	 * 
	 * @param context
	 * @param id
	 *            资源id
	 * @return
	 */
	public static String getString(Context context, int id) {
		return context.getResources().getString(id);
	}

	/**
	 * Toast提示
	 * 
	 * @param context
	 * @param content
	 *            内容
	 */
	public static void ShowContent(Context context, String content) {
		if (isNullString(content)) {
			Toast.makeText(context, content, 500).show();
		}
	}

	/**
	 * 
	 * @description Method Toast提示
	 * @author ljw 2014-1-20 上午10:18:13
	 * @param context
	 * @param content
	 *            要提示的类容
	 * @param duration
	 *            Toast显示的时间
	 */
	public static void ShowContent(Context context, String content, int duration) {
		if (isNullString(content)) {
			Toast.makeText(context, content, duration).show();
		}
	}

	/**
	 * 弹性计算
	 * 
	 * @param totalLenght
	 *            总长度
	 * @param currentLength
	 *            当前长度
	 * @param coefficient
	 *            弹性系数
	 * @return
	 */
	public static int calculationOfElasticity(int totalLenght,
			int currentLength, int coefficient) {
		coefficient = coefficient <= 0 ? 4 : coefficient;
		return (totalLenght - currentLength) / coefficient;
	}

	/**
	 * 判断屏幕适配
	 * 
	 * @param type
	 *            屏幕大小类型
	 * @param context
	 * @return 是否适配
	 */
	public static boolean isAdaptation(int type, Context context) {
		boolean flag = false;
		Display display = getDisplay(context);
		switch (type) {
		case MIN:
			flag = display.getWidth() == 480;
			break;
		case NORMAL:
			flag = display.getWidth() == 720;
			break;
		case MAX:
			flag = display.getWidth() == 1080;
			break;
		}
		return flag;
	}

	/**
	 * 
	 * @description 获取系统版本号
	 * @author Mars 2013-12-2 上午11:24:44
	 * @return
	 */
	public static String getSystemVrision() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 
	 * @description 是否是2.3以上的版本
	 * @author Mars 2013-12-2 下午1:36:25
	 * @return
	 */
	public static boolean isGreaterSystem() {
		if (Integer.parseInt(getSystemVrision().substring(0, 1)) > 2) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @description 毫秒数转日期
	 * @author Mars 2013-12-9 下午02:38:41
	 * @param currentTimeMillis
	 *            毫秒数 （默认当前毫秒数）
	 * @param dateFormat
	 *            日期格式（yyyy-MM-dd HH:mm:ss）
	 * @return
	 */
	public static String getDate(String currentTimeMillis, String dateFormat) {
		String re_StrTime = null;
		if (currentTimeMillis == null) {
			currentTimeMillis = System.currentTimeMillis() + "";
		}
		if (dateFormat == null || dateFormat.equals("")) {
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		if (currentTimeMillis.length() == 10) { // 单位 秒
			currentTimeMillis += "000";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		long lcc_time = Long.valueOf(currentTimeMillis);
		re_StrTime = sdf.format(new Date(lcc_time));

		return re_StrTime;

	}

	/**
	 * 得到请求时间
	 */
	public static String getReqTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * 
	 * @description Method 得到当前时间， 格式为 1122
	 * @author ljw 2014-1-15 下午4:24:51
	 * @return
	 */
	public static String getTime() {
		return new SimpleDateFormat("HHmm").format(new Date());
	}

	/**
	 * 
	 * @description Method 获取当前时间
	 * @author ljw 2013-12-26 下午4:33:14
	 * @return 时间的格式：yyyy-MM-dd
	 */
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 
	 * @description Method 获取昨天的时间
	 * @author ljw 2013-12-26 下午4:35:18
	 * @return 时间的格式：yyyyMMdd
	 */
	public static String getYesterdayTime() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, -1); // 得到前一天
		return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
	}

	/**
	 * 解决ScrollView嵌套ListView不能正常显示高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 
	 * @description measure view
	 * @author Mars 2013-10-11 下午02:51:49
	 * @param view
	 */
	public static void measureView(View view) {
		ViewGroup.LayoutParams viewParams = view.getLayoutParams();
		if (viewParams == null) {
			viewParams = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int widthMeasureSpec = ViewGroup.getChildMeasureSpec(0, 0,
				viewParams.width);
		int height = viewParams.height;
		int heightMeasureSpec;
		if (height > 0) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		view.measure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 
	 * @description Method description
	 * @author Mars 2013-12-20 上午11:51:58
	 * @param pattern
	 * @param object
	 * @return
	 */
	public static String getDToSDeci(String pattern, Object object) {
		return new DecimalFormat(pattern).format(object);
	}

	/**
	 * 
	 * @description is null
	 * @author Mars 2013-12-27 上午11:16:22
	 * @param object
	 * @return
	 */
	public static boolean isNull(Object object) {
		if (object == null || object.toString().equals("")) {
			return true;
		}
		return false;
	}

	/***
	 * 获取系统时间
	 * 
	 * @return 格式：1405130933（14年5月13日9点33分）
	 * 
	 */
	public static String getSystemTime() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		int Y = c.get(Calendar.YEAR);
		int M = c.get(Calendar.MONTH) + 1;
		int D = c.get(Calendar.DAY_OF_MONTH);
		int H = c.get(Calendar.HOUR_OF_DAY);
		int MI = c.get(Calendar.MINUTE);
		String y = getLastString(Y).substring(2);
		String m = getLastString(M);
		String d = getLastString(D);
		String h = getLastString(H);
		String mi = getLastString(MI);

		// return m + "-" + d + " " + h + ":" + mi;
		return y + m + d + h + mi;
	}

	public static String getLastString(int i) {
		String s = String.valueOf(i);
		s = s.length() == 1 ? "0" + s : s;
		return s;
	}

	/**
	 * 
	 * @description Method 判断字符串是否为空
	 * @author ljw 2014-1-14 下午2:59:15
	 * @param str
	 * @return 不为空返回true
	 */
	public static boolean isNullString(String str) {

		return str != null && !"".equals(str);
	}

	/**
	 * 
	 * @description Method 判断gson里面的集合是否为空
	 * @author ljw 2014-1-14 下午2:59:15
	 * @param str
	 * @return 不为空返回true
	 */
	public static boolean isNullGsonList(String str) {

		return str != null && !"".equals(str) && !"[]".equals(str);
	}

	/**
	 * 
	 * @description Method 判断email格式是否正确
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 判断api是否大于2.2
	 */
	public static boolean isCompatible() {
		return android.os.Build.VERSION.SDK_INT > 8;
	}

	/***
	 * 
	 * @description 判别手机是否为正确手机号码；
	 * @author ljw 2014-2-27 下午2:12:31
	 * @param mobiles
	 * @return 是的话，返回true
	 */
	public static boolean isMobileNum(String mobiles) {

		Pattern p = Pattern
				.compile("^13[0-9]{1}[0-9]{8}$|15[0-9]{1}[0-9]{8}$|18[0-9]{1}[0-9]{8}$|14[57]{1}[0-9]{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/***
	 * 
	 * @description 判断是否为电话号码
	 * @author ljw 2014-2-27 下午2:09:02
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		/*
		 * 可接受的电话格式有: ^\\(? : 可以使用 "(" 作为开头 (\\d{3}): 紧接着三个数字 \\)? : 可以使用")"接续
		 * [- ]? : 在上述格式后可以使用具选择性的 "-". (\\d{3}) : 再紧接着三个数字 [- ]? : 可以使用具选择性的
		 * "-" 接续. (\\d{5})$: 以五个数字结束. 可以比较下列数字格式: (123)456-7890, 123-456-7890,
		 * 1234567890, (123)-456-7890
		 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		/*
		 * 可接受的电话格式有: ^\\(? : 可以使用 "(" 作为开头 (\\d{3}): 紧接着三个数字 \\)? : 可以使用")"接续
		 * [- ]? : 在上述格式后可以使用具选择性的 "-". (\\d{4}) : 再紧接着四个数字 [- ]? : 可以使用具选择性的
		 * "-" 接续. (\\d{4})$: 以四个数字结束. 可以比较下列数字格式: (02)3456-7890, 02-3456-7890,
		 * 0234567890, (02)-3456-7890
		 */
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber; /* 创建Pattern */
		Pattern pattern = Pattern.compile(expression);
		/* 将Pattern 以参数传入Matcher作Regular expression */
		Matcher matcher = pattern.matcher(inputStr);
		/* 创建Pattern2 */
		Pattern pattern2 = Pattern.compile(expression2);
		/* 将Pattern2 以参数传入Matcher2作Regular expression */
		Matcher matcher2 = pattern2.matcher(inputStr);
		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 按照指定大小拆分数组
	 * 
	 * @param ary
	 *            数组
	 * @param subSize
	 *            按照多大的来分割
	 * @return
	 */
	public static Object[] splitAry(byte[] ary, int subSize) {
		int count = ary.length % subSize == 0 ? ary.length / subSize
				: ary.length / subSize + 1;

		List<List<Byte>> subAryList = new ArrayList<List<Byte>>();

		for (int i = 0; i < count; i++) {
			int index = i * subSize;

			List<Byte> list = new ArrayList<Byte>();
			int j = 0;
			while (j < subSize && index < ary.length) {
				list.add(ary[index++]);
				j++;
			}

			subAryList.add(list);
		}

		Object[] subAry = new Object[subAryList.size()];

		for (int i = 0; i < subAryList.size(); i++) {
			List<Byte> subList = subAryList.get(i);

			byte[] subAryItem = new byte[subList.size()];
			for (int j = 0; j < subList.size(); j++) {
				subAryItem[j] = subList.get(j).byteValue();
			}

			subAry[i] = subAryItem;
		}

		return subAry;
	}

	/**
	 * 把包头字节数组跟内容字节数组，合并成一个数组
	 * 
	 * @param head 包头
	 * @param content	内容
	 * @return
	 */
	public static byte[] mergerArray(byte[] head, byte[] content) {

		byte[] byteValue = new byte[head.length + content.length];

		for (int i = 0; i < byteValue.length; i++) {
			if (i <= head.length - 1) {
				byteValue[i] = head[i];
			} else {
				byteValue[i] = content[i - head.length];

			}
		}

		return byteValue;
	}

	/**
	 * 把字节数组里面的包头拆分出来不要
	 * 
	 * @param byteValue
	 * @return
	 */
	public static byte[] splitArray(byte[] byteValue) {

		byte[] b = new byte[byteValue.length - 8];

		for (int i = 0; i < b.length; i++) {
			b[i] = byteValue[i + 8];
		}

		return b;
	}

}
