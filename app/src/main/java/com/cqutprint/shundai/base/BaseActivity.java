package com.cqutprint.shundai.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.utils.Log;

/**
 * Created by Administrator on 2016/9/26.
 */
public abstract class BaseActivity extends FragmentActivity {

    /**
     * 日子打印TAG  <--GuangNeng-->
     */
    protected String TAG;

    /**
     * 界面组件
     */
    private SparseArray<View> mViews = new SparseArray<>();

    /**
     * 子类实现，传布局id
     * @return root 设置root视图ID =xml layout id
     */
    public abstract int getRootLayoutId();

    /**
     * 初始化视图组件
     */
    public abstract void initView();

    /**
     * 绑定ID获取视图
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T bind(int id){
        T view = (T) mViews.get(id);
        if (view == null) {
            view = (T) this.findViewById(id);
            mViews.put(id, view);
        }
        return view;
    }

    /**
     * 给指定ID的视图添加点击事件
     * @param listener
     * @param ids
     */
    public void initListener(View.OnClickListener listener,int... ids){
        if(ids==null){
            return;
        }
        for (int id : ids){
            bind(id).setOnClickListener(listener);
        }
    }

    /**
     * 给指定ID的视图添加点击事件
     * @param listener
     */
    public void initListener(View.OnClickListener listener,View... views){
        if(views==null){
            return;
        }
        for (View v : views){
            v.setOnClickListener(listener);
        }
    }


    /**
     * 显示一个提示信息
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示一个提示信息
     *
     * @param strId 显示信息在XML中ID
     */
    protected void showToast(int strId) {
        Toast.makeText(this, strId, Toast.LENGTH_SHORT).show();
    }


    /**
     * 启动Activity 不带参数
     *
     * @param className
     */
    protected void lunchActivity(Class<?> className) {
        startActivity(new Intent(this, className));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 生命周期 ：创建
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=getLocalClassName();
        View root=LayoutInflater.from(this).inflate(getRootLayoutId(),null);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(root);
        initView();

        Log.i(TAG);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_right_out);
    }
}
