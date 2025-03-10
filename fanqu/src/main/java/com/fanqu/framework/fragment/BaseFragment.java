package com.fanqu.framework.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanqu.framework.CustomApplication;
import com.fanqu.framework.activities.BaseActivity;
import com.fanqu.framework.autolayout.AutoUtils;
import com.fanqu.framework.main.util.ToastUtils;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;
import com.trello.rxlifecycle.RxLifecycle;

import java.lang.reflect.Field;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 *
 */

public abstract class BaseFragment extends Fragment implements FragmentLifecycleProvider {
    //是否可见状态
    private boolean isVisible;
    //View已经初始化完成
    private boolean isPrepared;
    //是否第一次加载完
    private boolean isFirstLoad = true;
    private View mContentView;
    protected String TAG;
    private BaseActivity mActivity;
    private CustomApplication mApp;
    private int containerId;
    protected LayoutInflater inflater;

    public BaseFragment() {

    }

    public int getContainerId() {

        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = this.getClass().getSimpleName();

        mActivity = (BaseActivity) activity;
        mApp = (CustomApplication) activity.getApplication();
        inflater = mActivity.getLayoutInflater();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onUserVisible();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        onCreateView(savedInstanceState);
        if (mContentView == null) {
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
           // return super.onCreateView(inflater, container, savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }

        }
        return mContentView;
    }

    protected void onCreateView(Bundle savedInstanceState) {

    }

    public void setContentView(View view) {
        mContentView = view;
        AutoUtils.auto(mContentView);
    }


    protected void setContentView(@LayoutRes int layoutResID) {
        mContentView = inflater.inflate(layoutResID, null);
        AutoUtils.auto(mContentView);
    }
    public View getContentView() {

        return mContentView;
    }
    /**
     * 初始化View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 当fragment对用户可见时，会调用该方法，可在该方法中懒加载网络数据
     */
    protected abstract void onUserVisible();

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT findView(@IdRes int id) {
        return (VT) mContentView.findViewById(id);
    }

    protected void showToast(String text) {
        ToastUtils.showToast(mActivity, text);
    }


    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    public final Observable<FragmentEvent> lifecycle() {

        return lifecycleSubject.asObservable();
    }

    @Override
    public final <T> Observable.Transformer<T, T> bindUntilEvent(FragmentEvent event) {
        return RxLifecycle.bindUntilFragmentEvent(lifecycleSubject, event);
    }

    @Override
    public final <T> Observable.Transformer<T, T> bindToLifecycle() {
        return RxLifecycle.bindFragment(lifecycleSubject);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }


    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
