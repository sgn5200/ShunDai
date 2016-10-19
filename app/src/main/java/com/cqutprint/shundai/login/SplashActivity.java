package com.cqutprint.shundai.login;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseActivity;
import com.cqutprint.shundai.base.MainActivity;
import com.cqutprint.shundai.utils.Log;
import com.cqutprint.shundai.utils.ShareUtils;

import java.util.ArrayList;

public class SplashActivity extends BaseActivity {

    private int[] pageID =new int[]{R.mipmap.l1,R.mipmap.l2,R.mipmap.l3,R.mipmap.l4};//引导页
    private ArrayList<View> pageViews;//引导页数组
    private ArrayList<ImageView> indicator;//下标圆点数组
    private ViewPager viewPager;
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_splush;
    }

    @Override
    public void initView() {
        initPageView();
        viewPager.setAdapter(SplashAdapter.getPageAdapter(pageViews));

        viewPager.setCurrentItem(0);
        indicator.get(0).setSelected(true);

        pageViews.get(indicator.size()-1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchActivity( MainActivity.class);
                finish();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG,"po  "+position);
                viewPager.setCurrentItem(position);
                for(int i=0;i<indicator.size();i++){
                    indicator.get(i).setSelected(i==position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initPageView() {
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams wrar=new FrameLayout.LayoutParams(50,50);
        viewPager=bind(R.id.viewPager);
        LinearLayout indicatorView=bind(R.id.indicator);
        pageViews=new ArrayList<>();
        indicator=new ArrayList<>();
        for(int i = 0; i< pageID.length; i++){
            ImageView imageView=new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(pageID[i]);
            pageViews.add(i,imageView);
            ImageView pointView=new ImageView(this);
            pointView.setLayoutParams(wrar);
            pointView.setSelected(false);
            indicator.add(i,pointView);
            indicatorView.addView(pointView,wrar);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShareUtils.setIsFirstLunch(false);
    }
}


