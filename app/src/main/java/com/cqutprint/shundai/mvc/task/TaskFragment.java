package com.cqutprint.shundai.mvc.task;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseFragment;
import com.cqutprint.shundai.base.RecyclerAdapter;
import com.cqutprint.shundai.login.SplashAdapter;
import com.cqutprint.shundai.mvc.TaskDetailsActivity;
import com.cqutprint.shundai.utils.Log;
import com.cqutprint.shundai.widget.PullRefreshLayout;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class TaskFragment extends BaseFragment implements PullRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ViewPager pager;
    private View[] ivIndicator;
    private ArrayList<View> views;
    private TextView titleCenter;
    private ImageView titleRight;

    RecyclerAdapter adapter;
    PullRefreshLayout pullLayout;

    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }

    public TaskFragment() {
        TAG = getClass().getSimpleName();
    }


    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void initView() {
        titleCenter = bind(R.id.titleTvCenter);
        titleCenter.setText("任务");


        titleRight = bind(R.id.titleIvRight);
        titleRight.setVisibility(View.VISIBLE);
        titleRight.setImageResource(R.mipmap.fold);
        titleRight.setOnClickListener(this);

        pullLayout = bind(R.id.pullLayout);
        pullLayout.setOnRefreshListener(this);
        pullLayout.setFinishRefreshToPauseDuration(800);

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header, null);
        setHeader(header);
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.recyler_footer, pullLayout, false);
        adapter = new TaskAdapter(setList("短消息"), null);
        adapter.setHeaderView(header);
        adapter.setFooterView(footer);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object holder) {
                String s;
                if(holder instanceof RecyclerAdapter.BaseHolder){
                    s= (String) adapter.getItem((RecyclerAdapter.BaseHolder) holder);
                    Bundle bundle=new Bundle();
                    bundle.putString("key",s);
                    lunchActivity(TaskDetailsActivity.class,bundle);
                }
            }
        });
        pullLayout.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleIvRight:
                showToast("发布任务没有做");
                break;
            case R.id.allTask:
                showToast("任务列表没有做");
                break;

            case R.id.myPublish:
                showToast("我的发布没有做");
                break;

            case R.id.myTask:
                showToast("我的任务没有做");

                break;
        }
    }

    //  设置布局中的头
    private void setHeader(View header) {
        pager = (ViewPager) header.findViewById(R.id.viewPager);
        ivIndicator = new View[]{header.findViewById(R.id.ivIndicator1), header.findViewById(R.id.ivIndicator2), header.findViewById(R.id.ivIndicator3)};
        ImageView iv1 = new ImageView(getActivity());
        ImageView iv2 = new ImageView(getActivity());
        ImageView iv3 = new ImageView(getActivity());
        iv1.setImageResource(R.mipmap.secondhand);
        iv2.setImageResource(R.mipmap.redpackage_share);
        iv3.setImageResource(R.mipmap.rewardpunish);
        views = new ArrayList<>();
        views.add(iv1);
        views.add(iv2);
        views.add(iv3);
        ivIndicator[0].setSelected(true);
        pager.setAdapter(SplashAdapter.getPageAdapter(views));
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < views.size(); i++) {
                    ivIndicator[i].setSelected(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        initListener(this, header.findViewById(R.id.myTask), header.findViewById(R.id.myPublish), header.findViewById(R.id.allTask));

        timerInterval();

    }

    /**
     * 广告轮询播放，时间间隔2秒
     */
    private void timerInterval() {
        Observable.interval(0, 2, TimeUnit.SECONDS)
                .compose(this.<Long>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (pager != null) {
                            pager.setCurrentItem((int) (aLong % 3));
                        }
                    }
                });

    }


    private List<String> setList(String msg) {
        List<String> dataList = new ArrayList<>();
        for (int i =0; i <5; i++) {
            dataList.add(String.format("收到 %s 条新消息,%s", i, msg));
        }
        return dataList;
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "刷新数据", Toast.LENGTH_SHORT).show();


        pullLayout.setRefreshing(true);
    }

    @Override
    public void onLoadMore() {
        Observable.just(setList("新消息")).compose(this.<List<String>>bindToLifecycle())
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
}
