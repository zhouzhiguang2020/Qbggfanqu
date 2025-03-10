package com.fanqu;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fanqu.qbgg.fanqu.framework.Constants;
import com.fanqu.qbgg.fanqu.framework.LogUtil;
import com.fanqu.qbgg.fanqu.framework.activities.BaseActivity;
import com.fanqu.qbgg.fanqu.framework.autolayout.AutoUtils;
import com.fanqu.qbgg.fanqu.framework.utills.ToastUtils;

import org.kymjs.push.core.KJPushManager;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import cn.sharesdk.framework.ShareSDK;


public class PhoneGap2Activity extends BaseActivity {
    /**
     * Called when the activity is first created.
     */
    String str;
    private String HTTP_URL = Constants.LOADING_CURRENT_URL;
    private WxbroadcastReceiver broadcastreceiver;
    private PluginMethod pluginMethod;
    private WebView appView;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption;
    // 微信支付成功 succeed
    private final int WEIXIN_PAY_RESULT_CODE_SUCCESSED = 0;
    // 微信支付取消 cancel
    private final int WEIXIN_PAY_RESULT_CODE_CANCEL = -2;
    // 微信支付 error
    private final int WEIXIN_PAY_RESULT_CODE_ERROR = -1;
    private long mFirstClickTime;
    //	private String startURL = "http://cengfan7.cn/wap.php";
//	protected String url1 = "http://cengfan7.cn/wap.php?m=Wap&c=Index&a=login";
//	protected String url2 = "http://cengfan7.cn/wap.php?m=Wap&c=User&a=index";
//	private String url3 = "http://cengfan7.cn/wap.php?m=Wap&c=Set&a=index";
//	private String url4 = "http://cengfan7.cn/wap.php?m=Wap&c=Index&a=index";
//	private String url5 = "http://cengfan7.cn/wap.php?m=Wap&c=index&a=login&wx_auto_login=0";
//	//测试地址
    private String startURL = "http://test.cengfan7.cn/wap.php";
    protected String url1 = "http://test.cengfan7.cn/wap.php?m=Wap&c=Index&a=login";
    protected String url2 = "http://test.cengfan7.cn/wap.php?m=Wap&c=User&a=index";
    private String url3 = "http://test.cengfan7.cn/wap.php?m=Wap&c=Set&a=index";
    private String url4 = "http://test.cengfan7.cn/wap.php?m=Wap&c=Index&a=index";
    private String url5 = "http://test.cengfan7.cn/wap.php?m=Wap&c=index&a=login&wx_auto_login=0";
    private AMapLocation mAMapLocation;
    private MapLocation maplocation;
    private AMapLocationListener locationListener = new AMapLocationListener() {


        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            String address = aMapLocation.getAddress();
            maplocation = new MapLocation();
            maplocation.setLatitude(aMapLocation.getLatitude());
            maplocation.setLongitude(aMapLocation.getLongitude());
            ToastUtils.showCenterToast(PhoneGap2Activity.this, aMapLocation.toString());
            LogUtil.e("测试的", aMapLocation.toString());
            PhoneGap2Activity.this.mAMapLocation = aMapLocation;
        }
    };

    @SuppressLint({
            "JavascriptInterface", "SetJavaScriptEnabled"
    })
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 解决黑屏问题
        // super.init();
        setContentView(R.layout.main);
        appView = (WebView) findViewById(R.id.web);
        AutoUtils.auto(this);
        ShareSDK.initSDK(this);
        // CookieSyncManager.createInstance(this);
        // CookieManager cookieManager = CookieManager.getInstance();
        // cookieManager.setAcceptCookie(true);
        // String CookieStr = cookieManager.getCookie(url2);
        initWebView();
        pluginMethod = new PluginMethod(this, appView);
        this.appView.addJavascriptInterface(pluginMethod, "iOSLoginModel"); // 注意这里
        this.appView.addJavascriptInterface(pluginMethod, "iOSPayModel"); // 注意这里
        this.appView.addJavascriptInterface(pluginMethod, "iOSFXModel");
        this.appView.addJavascriptInterface(pluginMethod, "phoneModel");
        // if (TextUtils.isEmpty(CookieStr)) {
        // appView.loadUrl(url2);
        // } else {
        //正式
        appView.loadUrl(startURL);
        // 测试
        //appView.loadUrl(Constants.TEST_CURRENT_URL);
        // }
        initDate();
        initbroadcastReceiver();
        initListener();

    }

    private void initDate() {
        locationOption = new AMapLocationClientOption();
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        // 设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 启动定位
        locationClient.startLocation();
        // 设置定位监听
        locationClient.setLocationListener(locationListener);

    }


    //    /**
//     * 默认的定位参数
//     * @since 2.8.0
//     * @author hongming.wang
//     *
//     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(3000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiActiveScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        return mOption;
    }

    private void initListener() {
        // TODO Auto-generated method stub
        this.appView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    @SuppressLint({
            "JavascriptInterface", "SetJavaScriptEnabled"
    })
    private void initWebView() {
        // TODO Auto-generated method stub
        WebSettings webSettings = appView.getSettings();
        webSettings
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 优先使用缓存
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);

        webSettings.setLoadWithOverviewMode(true);

        webSettings.setJavaScriptEnabled(true);

        webSettings.setGeolocationEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        appView.requestFocus();

        appView.setScrollBarStyle(0);

        appView.setWebViewClient(new MyWebViewClient());
        appView.setWebChromeClient(new MyWebChromeClient());
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            String url = appView.getUrl();
            if (!TextUtils.isEmpty(url)) {
                if (url.equals(Constants.LOADING_CURRENT_URL)
                        || url.equals(url1) || url.equals(url2)
                        || url.equals(url3) || url.equals(url4)
                        || url.equals(startURL) || url.equals(url5)) {
                    long currentTime = System.currentTimeMillis();
                    if (mFirstClickTime == 0
                            || currentTime - mFirstClickTime > 2000) {
                        mFirstClickTime = currentTime;
                        Toast.makeText(PhoneGap2Activity.this,
                                R.string.tip_exist_application,
                                Toast.LENGTH_SHORT).show();

                    } else {
                        appView.destroy();
                        // android.os.Process.killProcess(android.os.Process.myPid());
                        finishAffinity();
                        System.exit(0);// 退出程序的代码
                        return true;
                    }
                } else {
                    appView.goBack();
                    return true;

                }

            }
            return true;
        }
        return false;

    }

    public MapLocation getMineMapLocation() {
        return maplocation;
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       Callback callback) {
            // TODO Auto-generated method stub
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            if (newProgress > 90) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        initupdate();
                        KJPushManager.create().startWork(PhoneGap2Activity.this, MyReceiver.class);
                    }
                }, 5000);

            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            boolean flag = super.shouldOverrideUrlLoading(view, url);

            return flag;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            appView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        }
    }

    /**
     * 初始化自动更新
     */
    private void initupdate() {
        // TODO Auto-generated method stub
        UpdateKey.API_TOKEN = Constants.UPDATEKEY_API_TOKEN;
        UpdateKey.APP_ID = Constants.UPDATEKEY_API_ID;
        // 如果你想通过Dialog来进行下载，可以如下设置
        // UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;
        UpdateFunGO.init(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        UpdateFunGO.onStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
    }

    /**
     * 初始化微信支付成功后的回调
     */
    private void initbroadcastReceiver() {
        // TODO Auto-generated method stub
        IntentFilter filter = new IntentFilter(Constants.WXPAY_SUCCESS_RESULT);
        broadcastreceiver = new WxbroadcastReceiver();
        registerReceiver(broadcastreceiver, filter);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(broadcastreceiver);

    }

    private class WxbroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int result_code = intent.getIntExtra(
                    Constants.WXPAY_SUCCESS_RESULT_CODE, 6);
            switch (result_code) {
                case WEIXIN_PAY_RESULT_CODE_SUCCESSED:
                    ToastUtils.showToast(PhoneGap2Activity.this,
                            R.string.weixin_pay_succeed);
                    pluginMethod.payresultnotify();
                    break;
                case WEIXIN_PAY_RESULT_CODE_ERROR:
                    ToastUtils.showToast(PhoneGap2Activity.this, "错误");
                    break;
                case WEIXIN_PAY_RESULT_CODE_CANCEL:
                    ToastUtils.showToast(PhoneGap2Activity.this,
                            R.string.weixin_pay_cancel);
                    break;

                default:
                    break;
            }
        }

    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return 0;
    }

}