package com.fanqu.qbgg.fanqu.framework;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.amap.api.navi.AMapNavi;

public class CustomApplication extends Application {
	/** 屏幕分辨率：屏幕宽度 */
	public static int SCREEN_WIDTH = 0;

	/** _屏幕分辨率：屏幕高度 */
	public static int SCREEN_HEIGHT = 0;

	

	/** 状态栏的高 */
	public static int STATUSBAR_HEIGHT = 25;

	/** 屏幕密度 */
	public static float DENSITY;

	public static float SCALE_X;

	/** 只和像素相关的拉伸 **/
	public static float SCALE_X_ALL;

	public static float SCALE_Y;

	/** 用" 点/英-per-inch"屏幕密度 */
	public static int DENSITY_DPI;

	private static float xdpi;

	private static float ydpi;

	public static int DEFAULT_PAGE_SIZE = 10;

	public static boolean isPad = false;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		init();
		AMapNavi.setApiKey(this, "f5bf910cb1934d7b6e93bc252952b63c");
		LogUtil.isDebug=true;
	}

	private void init() {
		// TODO Auto-generated method stub
		Context context = CustomApplication.this.getApplicationContext();
		DisplayMetrics display = context
				.getResources().getDisplayMetrics();
		SCREEN_WIDTH = display.widthPixels;
		SCREEN_HEIGHT = display.heightPixels;
		// 屏幕的宽大于高宽高到转过来
		if (SCREEN_WIDTH > SCREEN_HEIGHT) {
			int flag = SCREEN_WIDTH;
			SCREEN_WIDTH = SCREEN_HEIGHT;
			SCREEN_HEIGHT = flag;
		}
		// 屏幕密度 density
		DENSITY = context.getResources().getDisplayMetrics().density;
		DENSITY_DPI =0;//context.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);  ((Resources) context).getDisplayMetrics().densityDpi;
		xdpi =context.getResources().getDisplayMetrics().xdpi;
		ydpi = context.getResources().getDisplayMetrics().ydpi;
		double length = Math.sqrt(CustomApplication.SCREEN_WIDTH
				* CustomApplication.SCREEN_WIDTH
				/ (CustomApplication.xdpi * CustomApplication.xdpi)
				+ CustomApplication.SCREEN_HEIGHT
				* CustomApplication.SCREEN_HEIGHT
				/ (CustomApplication.ydpi * CustomApplication.ydpi));
		// 判断是否是平板
		if (length > 6.0f) {
			isPad = true;
		} else {
			isPad = false;
		}

		if (isPad) {
			SCALE_X = 1;
		} else {
			SCALE_X = SCREEN_WIDTH / 480f;
		}
		// scale x all
		SCALE_X_ALL = SCREEN_WIDTH / 480f;
		SCALE_Y = SCREEN_HEIGHT / 800f;

	}

}
