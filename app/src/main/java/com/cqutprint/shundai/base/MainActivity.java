package com.cqutprint.shundai.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.RecyclerViewFragment;
import com.cqutprint.shundai.mvc.me.MeFragment;
import com.cqutprint.shundai.mvc.message.MessageFragment;
import com.cqutprint.shundai.mvc.publish.PublishFragment;
import com.cqutprint.shundai.mvc.task.TaskFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    Fragment fragments[];
    View tabIcons[];
    private ViewPager pager;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_main;
    }

    @Override
        public void initView() {
        pager=bind(R.id.viewPager);
        tabIcons=new View[]{bind(R.id.tab1),bind(R.id.tab2),bind(R.id.tab3),bind(R.id.tab4)};
        initListener(this,tabIcons);

        fragments= new Fragment[]{TaskFragment.newInstance("im first","im second"),
                PublishFragment.newInstance("9999","77777"),
                MessageFragment.newInstance(),
                MeFragment.newInstance("0000","+++++"),new RecyclerViewFragment()};

        tabIcons[0].setSelected(true);
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<tabIcons.length;i++){
                    tabIcons[i].setSelected(i==position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab1:
                pager.setCurrentItem(0);
                break;
            case R.id.tab2:
                pager.setCurrentItem(1);
                break;
            case R.id.tab3:
                pager.setCurrentItem(2);
                break;
            case R.id.tab4:
                pager.setCurrentItem(3);
                break;
            default:
        }
    }
}