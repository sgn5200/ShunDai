package com.cqutprint.shundai.mvc.publish;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.RecyclerAdapter;
import com.cqutprint.shundai.base.BaseFragment;
import com.cqutprint.shundai.widget.SpaceItemDecoration;
import com.cqutprint.shundai.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;


public class PublishFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;

    private ImageView ivChooiseSchool,ivEidt;

    public static PublishFragment newInstance(String param1, String param2) {
        PublishFragment fragment = new PublishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_publish;
    }

    @Override
    protected void initView() {
        recyclerView=bind(R.id.rvPublish);
        ivChooiseSchool=bind(R.id.ivSpiner);
        ivEidt=bind(R.id.ivEdit);

        ivChooiseSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopWindow(v);
            }
        });

        ((TextView)bind(R.id.titleMsg)).setText("我的学校");

        // 创建布局管理器
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        //设置方向垂直
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        List<String> listData=initData();

        RecyclerAdapter adapter=new PublishAdapter(listData,null);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
    }

    PopupWindow popupWindow;
    private void openPopWindow(View v) {

        if(popupWindow==null){
            View popView=LayoutInflater.from(getActivity()).inflate(R.layout.pop_school,null,false);
            popView.findViewById(R.id.tvPopSchool).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "我的学校", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            });

            popView.findViewById(R.id.tvPopOther).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "其他的学校", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            });
            popupWindow=new PopupWindow(popView, ScreenUtil.dip2px(200), ScreenUtil.dip2px(100));
            popupWindow.setTouchable(true);
            popupWindow.showAtLocation(v,Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,ScreenUtil.dip2px(51)+ScreenUtil.getStatusBarHeight(getActivity()));
        }else{
            popupWindow.showAtLocation(v,Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,ScreenUtil.dip2px(51)+ScreenUtil.getStatusBarHeight(getActivity()));
        }
    }

    private List<String> initData() {
        List<String> list=new ArrayList<>();
        for(int i=0;i<20;i++){
            String item="这是-name"+ i ;
            list.add(item);
        }
        return list;
    }
}
