package com.cqutprint.shundai.login;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

//下标圆点 adapter
public class SplashAdapter extends PagerAdapter {
    private ArrayList<View> views;

    private SplashAdapter(ArrayList<View> views){this.views=views;}

    public static SplashAdapter getPageAdapter(ArrayList<View> views){
        SplashAdapter adapter=new SplashAdapter(views);
        return adapter;
    }


    //viewpager中的组件数量
    @Override
    public int getCount() {
        return views==null?-1:views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    //每次滑动的时候生成的组件
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
         container.addView(views.get(position));
        return views.get(position);
    }

    //滑动切换的时候销毁当前的组件
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}


