package com.cqutprint.shundai.mvc.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseFragment;
import com.cqutprint.shundai.base.RecyclerAdapter;
import com.cqutprint.shundai.login.SplashAdapter;
import com.cqutprint.shundai.utils.Log;
import com.cqutprint.shundai.utils.ThreadTimer;
import com.cqutprint.shundai.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class TaskFragment extends BaseFragment implements PullRefreshLayout.OnRefreshListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ViewPager pager;
    private View[] ivIndicator;
    private ArrayList<View> views;

    RecyclerAdapter adapter;

    RecyclerView recyclerView;
    PullRefreshLayout pullLayout;
    private ProgressBar progressBar;
    private TextView tvLoad;
    private ImageView ivLoad;
    private ImageView ivLoadOk;

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
        recyclerView = bind(R.id.rvTask);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRefreshHeader();

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header, null);
        setHeader(header);
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.recyler_footer, recyclerView, false);
        adapter = new TaskAdapter(setList(),null);
        adapter.setHeaderView(header);
        adapter.setFooterView(footer);
        recyclerView.setAdapter(adapter);

    }

    //设置下拉的头部
    private void initRefreshHeader() {
        pullLayout=bind(R.id.pullLayout);
        pullLayout.setOnRefreshListener(this);
        pullLayout.setFinishRefreshToPauseDuration(800);

        //设置下拉头
        View pullRefresh=pullLayout.setRefreshView(R.layout.layout_pull_refresh);
        progressBar = (ProgressBar) pullRefresh.findViewById(R.id.pb_view);
        tvLoad = (TextView) pullRefresh.findViewById(R.id.text_view);
        tvLoad.setText("下拉刷新");
        ivLoad = (ImageView) pullRefresh.findViewById(R.id.image_view);
        ivLoad.setVisibility(View.VISIBLE);
        ivLoad.setImageResource(R.mipmap.v2female);
        ivLoadOk=(ImageView)pullRefresh.findViewById(R.id.img_done);
        ivLoadOk.setImageResource(R.mipmap.v2type1);
        ivLoadOk.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
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

        timer = new ThreadTimer(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i(TAG, msg.what + "   ----------");
                if (pager != null) {
                    pager.setCurrentItem(msg.what % 3);
                }
            }
        }, 5000);

    }

    @Override
    public void onPause() {
        super.onPause();
        timer.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer.start();
    }

    private ThreadTimer timer;

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
        tvLoad.setText("正在刷新");
        android.util.Log.d("TAG", "onRefresh() called with: " + "");
        ivLoadOk.setVisibility(View.GONE);
        ivLoad.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        pullLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onDragDistanceChange(float distance, float percent, float offset) {
        if (percent >= 1.0f) {
            tvLoad.setText("松开刷新");
            ivLoadOk.setVisibility(View.GONE);
            ivLoad.setVisibility(View.VISIBLE);
            ivLoad.setRotation(180);
        } else {
            tvLoad.setText("下拉刷新");
            ivLoadOk.setVisibility(View.GONE);
            ivLoad.setVisibility(View.VISIBLE);
            ivLoad.setRotation(0);
        }
    }

    @Override
    public void onFinish() {
        tvLoad.setText("刷新成功");
        ivLoad.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        ivLoadOk.setVisibility(View.VISIBLE);
    }
}
