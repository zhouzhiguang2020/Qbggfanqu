package com.fanqu.personcentre.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.fanqu.R;
import com.fanqu.framework.activities.BaseActivity;
import com.fanqu.framework.autolayout.AutoUtils;
import com.fanqu.framework.main.util.CountDownTimerUtils;
import com.fanqu.framework.main.util.ThemUtils;
import com.fanqu.framework.model.ToolBarOptions;
import com.fanqu.main.widget.PayEditPasswordText;

/**
 * 设置交易密码
 */
public class SetTradersPasswordActivity extends BaseActivity implements View.OnClickListener {
    private TextView send_code;
    private PayEditPasswordText traders_password_PayEditText_pay, traders_password_ensure_PayEditText_pay;
    private AppCompatEditText set_traders_password_phone_number, send_code_user_pass;
    private TextView confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemUtils.initthem(this, R.color.white);
       // AutoUtils.setSize(this, false, 1080, 1812);// 没有状态栏,设计尺寸的宽高1.6875倍
        setContentView(R.layout.activity_set_traders_password_layout);
        AutoUtils.auto(this);
        ToolBarOptions options = new ToolBarOptions();
        options.isNeedNavigate = true;
        options.navigateId = R.mipmap.ic_left_arrows;
        options.titleString = "";
        setToolBar(R.id.toolbar, options);
        Toolbar toolbar = getToolBar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        initDate();
        initListener();
    }

    private void initView() {
        //手机号码

        set_traders_password_phone_number = findView(R.id.set_traders_password_phone_number);
        //验证码
        send_code_user_pass = findView(R.id.send_code_user_pass);
        send_code = findView(R.id.send_code);
        traders_password_PayEditText_pay = findView(R.id.traders_password_PayEditText_pay);
        traders_password_ensure_PayEditText_pay = findView(R.id.traders_password_ensure_PayEditText_pay);
        confirm = findView(R.id.confirm);
    }

    private void initDate() {


    }

    private void initListener() {
        send_code.setOnClickListener(this);
        confirm.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                //确定交易密码
                break;
            case R.id.send_code:
                getauthcode();
                break;
            default:
                break;
        }
    }

    private void getauthcode() {
        CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(send_code, 30000, 1000);
        countDownTimerUtils.start();

    }
}
