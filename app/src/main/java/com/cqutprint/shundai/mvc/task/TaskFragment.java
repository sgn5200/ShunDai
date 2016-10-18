package com.cqutprint.shundai.mvc.task;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseFragment;
import com.cqutprint.shundai.base.RecyclerAdapter;
import com.cqutprint.shundai.login.SplashAdapter;
import com.cqutprint.shundai.widget.PullRefreshLayout;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class TaskFragment extends BaseFragment implements PullRefreshLayout.OnRefreshListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ViewPager pager;
    private View[] ivIndicator;
    private ArrayList<View> views;

    RecyclerAdapter adapter;

    PullRefreshLayout pullLayout;


    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TaskFragment(){
        TAG=getClass().getSimpleName();
    }


    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_task;
    }
    @Override
    protected void initView() {
        pullLayout=bind(R.id.pullLayout);
        pullLayout.setOnRefreshListener(this);
        pullLayout.setFinishRefreshToPauseDuration(800);

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header, null);
        setHeader(header);
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.recyler_footer, pullLayout, false);
        adapter = new TaskAdapter(setList(),null);
        adapter.setHeaderView(header);
        adapter.setFooterView(footer);
        pullLayout.setAdapter(adapter);

    }

    //  设置布局中的头
    private void setHeader(View header) {
        pager = (ViewPager) header.findViewById(R.id.viewPager);
        ivIndicator = new View[]{ header.findViewById(R.id.ivIndicator1),header.findViewById(R.id.ivIndicator2),header.findViewById(R.id.ivIndicator3)};
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

        timerInterval();

    }

    /**
     * 广告轮询播放，时间间隔2秒
     */
    private void timerInterval(){
        Observable.interval(0,2, TimeUnit.SECONDS)
                .compose(this.<Long>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                if (pager != null) {
                    pager.setCurrentItem((int)(aLong% 3));
                }
            }
        });

    }


    private int mCount = 1;

    private List<String> setList() {
        List<String> dataList = new ArrayList<>();
        int start = 20 * (mCount - 1);
        for (int i = start; i < 20 * mCount; i++) {
            dataList.add("Frist" + i);
        }
        return dataList;
    }

    @Override
    public void onRefresh() {
        if (flagIsOnLoadMore){
            flagIsOnLoadMore=false;
            return;
        }
        Toast.makeText(getContext(), "刷新数据", Toast.LENGTH_SHORT).show();
        pullLayout.setRefreshing(true);
    }

    @Override
    public void onLoadMore() {
        flagIsOnLoadMore=true;
        Toast.makeText(getContext(), "加载", Toast.LENGTH_SHORT).show();
        pullLayout.setRefreshing(true,true);

    }

    boolean flagIsOnLoadMore=false;
}
