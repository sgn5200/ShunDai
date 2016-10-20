package com.cqutprint.shundai.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseFragment;
import com.cqutprint.shundai.base.RecyclerAdapter;
import com.cqutprint.shundai.adapter.PublishAdapter;
import com.cqutprint.shundai.ui.PublishMessageActivity;
import com.cqutprint.shundai.ui.TaskDetailsActivity;
import com.cqutprint.shundai.utils.Log;
import com.cqutprint.shundai.utils.ScreenUtil;
import com.cqutprint.shundai.widget.CustomTitle;
import com.cqutprint.shundai.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class PublishFragment extends BaseFragment implements PullRefreshLayout.OnRefreshListener, View.OnClickListener {
    private PullRefreshLayout pullLayout;
    private CustomTitle customTitle;

    private TextView tvSchool;

    private RecyclerAdapter adapter;

    public static PublishFragment newInstance() {
        PublishFragment fragment = new PublishFragment();
        return fragment;
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_publish;
    }

    @Override
    protected void initView() {
        pullLayout = bind(R.id.recyclerPublish);
        tvSchool = bind(R.id.tvSchool);
        customTitle = bind(R.id.publishTitle);
        customTitle.setText("所有", "我的");
        customTitle.addChangeListener(new CustomTitle.TitleChangeListener() {
            @Override
            public void changed(boolean isLeftSelected) {
                showToast("你选了-->" + (isLeftSelected ? "左边" : "右边"));
            }
        });

        initListener(this, R.id.titleIvRight, R.id.ivSpiner);

        List<String> listData = initData("初始化");
        adapter = new PublishAdapter(listData, null);
        pullLayout.setOnRefreshListener(this);
        pullLayout.setFinishRefreshToPauseDuration(800);

        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.recyler_footer, pullLayout, false);
        adapter.setFooterView(footer);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object holder) {
                Bundle bundle = new Bundle();
                bundle.putString("key", (String) adapter.getItem((RecyclerAdapter.BaseHolder) holder));
                lunchActivity(TaskDetailsActivity.class, bundle);
            }
        });
        pullLayout.setAdapter(adapter);
        pullLayout.setItemDecoration(10);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSpiner:
                openPopWindow(v);
                break;
            case R.id.titleIvRight:
                lunchActivity(PublishMessageActivity.class);
                break;
        }
    }

    private List<String> initData(String msg) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String item = "这是-publish " + i + "  " + msg;
            list.add(item);
        }
        return list;
    }


    @Override
    public void onRefresh() {
        showToast("刷新数据");
        pullLayout.setRefreshing(true);
    }

    @Override
    public void onLoadMore() {
        Observable.just(initData("新消息")).compose(this.<List<String>>bindToLifecycle())
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        adapter.dataAppend(strings);
                        pullLayout.onLoadFinish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage());
                    }
                });

    }


    PopupWindow popupWindow;

    private void openPopWindow(View v) {
        if (popupWindow == null) {
            View popView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_school, null, false);
            popView.findViewById(R.id.tvPopSchool).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvSchool.setText("我的学校");
                    popupWindow.dismiss();
                }
            });

            popView.findViewById(R.id.tvPopOther).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvSchool.setText("其他学校");
                    popupWindow.dismiss();
                }
            });
            popupWindow = new PopupWindow(popView, ScreenUtil.dip2px(200), ScreenUtil.dip2px(100));
            popupWindow.setTouchable(true);
            popupWindow.showAtLocation(v, Gravity.LEFT | Gravity.TOP, 0, ScreenUtil.dip2px(51) + ScreenUtil.getStatusBarHeight(getActivity()));
        } else {
            popupWindow.showAtLocation(v, Gravity.LEFT | Gravity.TOP, 0, ScreenUtil.dip2px(51) + ScreenUtil.getStatusBarHeight(getActivity()));

        }
    }

}
