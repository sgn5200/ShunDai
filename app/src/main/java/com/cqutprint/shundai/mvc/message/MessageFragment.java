package com.cqutprint.shundai.mvc.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseFragment;
import com.cqutprint.shundai.base.RecyclerAdapter;
import com.cqutprint.shundai.mvc.publish.PublishAdapter;
import com.cqutprint.shundai.widget.CustomTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */
public class MessageFragment extends BaseFragment {

    CustomTitle title;
    View noMessageInfo;

    RecyclerView rv;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_message;
    }


    @Override
    protected void initView() {

        title=bind(R.id.titleSwitch);
        title.addChangeListener(new CustomTitle.TitleChangeListener() {
            @Override
            public void changed(boolean isLeftSelected) {
                //noMessageInfo.setVisibility(isLeftSelected?View.VISIBLE:View.GONE);

            }
        });

        //noMessageInfo=bind(R.id.noMessage);

        rv=bind(R.id.rv);

        // 创建布局管理器
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        //设置方向垂直
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置布局管理器
        rv.setLayoutManager(layoutManager);

        List<String> listData=initData();

        rv.setLayoutManager(layoutManager);

        RecyclerAdapter adapter=new PublishAdapter(listData,null);

        rv.setAdapter(adapter);

    }


    private List<String> initData() {
        List<String> list=new ArrayList<>();
        for(int i=0;i<20;i++){
            String item="这是我的 "+ i+" 屁孩" ;
            list.add(item);
        }
        return list;
    }


}

